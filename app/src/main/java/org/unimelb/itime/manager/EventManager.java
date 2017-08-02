package org.unimelb.itime.manager;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.LoginMvpView;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.UserUtil;
import org.unimelb.itime.util.rulefactory.RuleFactory;
import org.unimelb.itime.util.rulefactory.RuleModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import david.itimecalendar.calendar.listeners.ITimeEventInterface;
import david.itimecalendar.calendar.listeners.ITimeEventPackageInterface;
import david.itimecalendar.calendar.util.BaseUtil;

/**
 * Created by yuhaoliu on 29/08/16.
 */
public class EventManager {
    private final String TAG = "EventManager";
    private static EventManager instance;

    private Event currentEvent = new Event();

    /**
     *
     */
    private Map<String, ITimeEventInterface> eventFinder = new HashMap<>();
    private Map<String, List<ITimeEventInterface>> crossDayMap = new HashMap<>();

//    private List<ITimeEventInterface> allDayEventList = new ArrayList<>();
    private Map<Long, List<ITimeEventInterface>> regularEventMap = new HashMap<>();

    private ArrayList<Event> orgRepeatedEventList = new ArrayList<>();
    private Map<Long, List<ITimeEventInterface>> repeatedEventMap = new HashMap<>();

    //<UUID, List of tracer> : For tracking event on Day of repeated event map
    private Map<String,ArrayList<EventTracer>> uidTracerMap = new HashMap();

    //recurrence uid (origin event uid) : special events for origin event
    private Map<String, ArrayList<Event>> specialEvent = new HashMap<>();

    private EventsPackage eventsPackage = new EventsPackage();

    private final int defaultRepeatedRange = 100;
    // [0 - 1] percentage of frag for load more fur/pre event
    private final float refreshFlag = 0.9f;

    private Calendar nowRepeatedEndAt = Calendar.getInstance();
    private Calendar nowRepeatedStartAt = Calendar.getInstance();

    private Context context;

    private EventManager(Context context){
        this.context = context;
        this.init();
    }

    private void init(){
        currentEvent = new Event();
//        allDayEventList = new ArrayList<>();
        regularEventMap = new HashMap<>();
        orgRepeatedEventList = new ArrayList<>();
        repeatedEventMap = new HashMap<>();
        //<UUID, List of tracer> : For tracking event on Day of repeated event map
        uidTracerMap = new HashMap();
        //recurrence uid (origin event uid) : special events for origin event
        specialEvent = new HashMap<>();
        eventsPackage = new EventsPackage();

        nowRepeatedStartAt = EventUtil.getBeginOfDayCalendar(nowRepeatedStartAt);
        nowRepeatedEndAt = EventUtil.getBeginOfDayCalendar(nowRepeatedEndAt);

        nowRepeatedStartAt.add(Calendar.DATE, -defaultRepeatedRange);
        nowRepeatedEndAt.add(Calendar.DATE, defaultRepeatedRange);

        eventsPackage.setRepeatedEventMap(repeatedEventMap);
        eventsPackage.setRegularEventMap(regularEventMap);
    }

    public static EventManager getInstance(Context context){
        if (instance == null){
            instance = new EventManager(context);
        }

        return instance;
    }

//    public static EventManager getInstance(){
//        return instance;
//    }

    public EventsPackage getEventsPackage(){
        return eventsPackage;
    }

    public List<Event> getOrgRepeatedEventList(){
        return this.orgRepeatedEventList;
    }

    public Map<String, ArrayList<Event>> getSpecialEventMap(){
        return  this.specialEvent;
    }


    public Map<Long, List<ITimeEventInterface>> getRepeatedEventMap() {
        return repeatedEventMap;
    }

    public Map<Long, List<ITimeEventInterface>> getRegularEventMap() {
        return regularEventMap;
    }

    public List<Event> getAllEvents(){
        ArrayList<Event> allEvents = new ArrayList<>();
        for (Map.Entry<Long, List<ITimeEventInterface>> entry: regularEventMap.entrySet()){
            for (ITimeEventInterface event:entry.getValue()
                 ) {
                allEvents.add((Event) event);
            }
        }

        for (Map.Entry<Long, List<ITimeEventInterface>> entry: repeatedEventMap.entrySet()){
            for (ITimeEventInterface event:entry.getValue()
                    ) {
                allEvents.add((Event) event);
            }
        }

        return allEvents;
    }

    public Event findEventByUid(String eventUid){
        Event event = (Event) eventFinder.get(eventUid);

        if (event==null){
            return null;
        }

        Long key = EventUtil.getDayBeginMilliseconds(event.getStartTime());

        if (event.getRecurrence().length!=0){
            // repeat event
            for (Event ev : orgRepeatedEventList){
                if (ev.getEventUid().equals(eventUid)){
                    return ev;
                }
            }
        }else {
            // non-repeat event
            if (regularEventMap.containsKey(key)) {
                for (ITimeEventInterface iTimeEventInterface : regularEventMap.get(key)) {
                    if ((iTimeEventInterface).getEventUid().equals(eventUid)) {
                        return (Event) iTimeEventInterface;
                    }
                }
            }
        }
        return null;
    }

    public synchronized void syncRepeatedEvent(long currentDate){
        boolean reachPreFlg = currentDate < this.getLoadPreFlag();
        boolean reachFurFlg = currentDate > this.getLoadFurFlag();
        if (reachPreFlg || reachFurFlg){
            //load more pre
            if (reachPreFlg){
                Calendar tempStart = Calendar.getInstance();
                tempStart.setTimeInMillis(nowRepeatedStartAt.getTimeInMillis());
                tempStart.add(Calendar.DATE,-defaultRepeatedRange);
                for (Event event:orgRepeatedEventList
                        ) {
                    this.addRepeatedEvent(event, tempStart.getTimeInMillis(), nowRepeatedStartAt.getTimeInMillis());
                }
                nowRepeatedStartAt.setTimeInMillis(tempStart.getTimeInMillis());
            }
            //load more future
            if (reachFurFlg){
                Calendar tempEnd = Calendar.getInstance();
                tempEnd.setTimeInMillis(nowRepeatedEndAt.getTimeInMillis());
                tempEnd.add(Calendar.DATE,defaultRepeatedRange);
                for (Event event:orgRepeatedEventList
                        ) {
                    this.addRepeatedEvent(event, nowRepeatedEndAt.getTimeInMillis(), tempEnd.getTimeInMillis());
                }
                nowRepeatedEndAt.setTimeInMillis(tempEnd.getTimeInMillis());
            }
//            EventBus.getDefault().post(new MessageEvent(MessageEvent.RELOAD_EVENT));
        }
    }

    /**
     * reload all events
     */
    public synchronized void refreshEventManager(){
        init();
        loadDB();
    }

    private void loadDB(){
        List<org.unimelb.itime.bean.Calendar> calendars = CalendarUtil.getInstance(context).getCalendar();
        String userUid = UserUtil.getInstance(context).getUserUid();
        List<Event> events = DBManager.getInstance(context).getAllAvailableEvents(calendars,userUid);

        for (Event ev: events) {
            addEvent(ev);
        }
    }

    public synchronized void clear(){
        this.instance = null;
    }

    public synchronized void addEvent(Event event){
        //check if special event, handled it, then as regular event
        handleSpecialEvent(event);

        //if should show
        if (event.getShowLevel() <= 0) {
            return;
        }

        //syn event finder
        eventFinder.put(event.getEventUid(),event);

        if (event.getRecurrence().length == 0) {
            addRegularEvent(event);
        } else {
            if (!isIncludeRepeated(event)){
                orgRepeatedEventList.add(event);
                this.addRepeatedEvent(event, nowRepeatedStartAt.getTimeInMillis(), nowRepeatedEndAt.getTimeInMillis());
            }else{
                Log.i(TAG, "addEvent: duplicating adding repeated event, dropped.");
            }
        }
    }

    public synchronized void insertOrUpdate(Event event){
        Event oldEvent = findEventByUid(event.getEventUid());

        if (oldEvent != null){
            this.updateEvent(oldEvent,event);
        }else {
            this.addEvent(event);
        }

//        AlarmUtil.synchronizeSysAlarm(context);
    }

    private void updateEvent(Event oldEvent, Event newEvent){
//        if (EventUtil.isAllDayEvent(oldEvent)){
//            updateAllDayEvent(oldEvent, newEvent);
//            return;
//        }

        //if old not repeated
        if (oldEvent.getRecurrence().length == 0){
            this.updateRegularEvent(oldEvent, newEvent);
        }else{
            //if old is repeated
           this.updateRepeatedEvent(oldEvent, newEvent);
        }
    }

    private synchronized void updateRegularEvent(Event oldEvent, Event newEvent){
        long oldBeginTime = EventUtil.getDayBeginMilliseconds(oldEvent.getStartTime());
        long oldEndTime = EventUtil.getDayEndMilliseconds(oldEvent.getEndTime());

        while (oldBeginTime < oldEndTime){
            if (this.regularEventMap.containsKey(oldBeginTime)){
                Event old = null;

                for (ITimeEventInterface iTimeEventInterface : regularEventMap.get(oldBeginTime)){
                    if ((iTimeEventInterface).getEventUid().equals(oldEvent.getEventUid())){
                        old = (Event) iTimeEventInterface;
                        break;
                    }
                }

                if (old != null){
                    regularEventMap.get(oldBeginTime).remove(old);
                }
            }
            oldBeginTime += EventUtil.allDayMilliseconds;
        }
        //update deleted level
        //show -> hide
        if (oldEvent.getShowLevel() == 1 && newEvent.getShowLevel() != 1){
            //deleted
        }else{
            //not deleted
            this.addEvent(newEvent);
        }
    }

    private synchronized void updateRepeatedEvent(Event oldEvent, Event newEvent){
        this.removeRepeatedEvent(oldEvent);
        this.orgRepeatedEventList.remove(oldEvent);
        this.addEvent(newEvent);
    }

//    private synchronized void updateAllDayEvent(Event oldEvent, Event newEvent){
//        this.allDayEventList.remove(EventUtil.getItemInList(new ArrayList(this.allDayEventList), oldEvent));
//        this.addEvent(newEvent);
//    }

    private synchronized void handleSpecialEvent(Event event){
        String rEUID = event.getRecurringEventUid();

        if (!rEUID.equals("") && !rEUID.equals(event.getEventUid())){
            if (this.specialEvent.containsKey(rEUID)){
                ArrayList<Event> specialEvents = this.specialEvent.get(rEUID);
                EventUtil.removeWhileLooping(specialEvents,event);
                this.specialEvent.get(rEUID).add(event);
            }else{
                ArrayList<Event> specialList = new ArrayList<>();
                specialList.add(event);
                this.specialEvent.put(rEUID, specialList);
            }

            Event org = findRepeatedOrgByUUID(event.getRecurringEventUid());
            if (org != null){
                refreshRepeatedEvent(org);
            }
        }
    }

    private boolean isIncludeRepeated(Event repeater){
        for (Event event:this.orgRepeatedEventList
             ) {
            if (event.getEventUid().equals(repeater.getEventUid())){
                return true;
            }
        }

        return false;
    }

    private synchronized void addRegularEvent(Event event){
        Long startTime = event.getStartTime();
        Long endTime = event.getEndTime();
        Long dayBeginMilliseconds = EventUtil.getDayBeginMilliseconds(startTime);
        Long dayEndMilliseconds = EventUtil.getDayEndMilliseconds(endTime);

        while (dayBeginMilliseconds < dayEndMilliseconds ){
            if (regularEventMap.containsKey(dayBeginMilliseconds)) {
                regularEventMap.get(dayBeginMilliseconds).add(event);
            } else {
                regularEventMap.put(dayBeginMilliseconds, new ArrayList<ITimeEventInterface>());
                regularEventMap.get(dayBeginMilliseconds).add(event);
            }
            dayBeginMilliseconds += BaseUtil.getAllDayLong(dayBeginMilliseconds);
        }
    }

    private synchronized void addRepeatedEvent(Event event, long rangeStart, long rangeEnd){
        RuleModel rule = RuleFactory.getInstance().getRuleModel(event);
        event.setRule(rule);

        ArrayList<Event> specialList = null;
        //special event
        if (this.specialEvent.containsKey(event.getEventUid())){
            specialList = this.specialEvent.get(event.getEventUid());
        }

        //handle special event
        if (specialList != null){
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");

            for (Event spEvent:specialList) {
                try {
                    String dateStr =spEvent.getEventUid().split("_")[1];
                    //this date new to be convert from UTC to Local
                    Date localDate = EventUtil.untilConverter(event.getStartTime(),sf.parse(dateStr), TimeZone.getTimeZone("UTC"));
                    rule.addEXDate(localDate);
                }catch (Exception e){
                    Log.i(TAG, "Parse Special Event Date Error");
                }
            }
        }

        ArrayList<Long> repeatedEventsTimes = rule.getOccurenceDates(rangeStart,rangeEnd);

        for (Long time: repeatedEventsTimes
                ) {

            Event dup_event;
            dup_event = event.clone();

            if (dup_event == null){
                throw new RuntimeException("Clone error");
            }

            //dup time is right
            long duration = dup_event.getDurationMilliseconds();
            dup_event.setStartTime(time);
            dup_event.setEndTime(time + duration);

            //if should show
            Long startTime = dup_event.getStartTime();
            Long endTime = dup_event.getEndTime();
            Long dayBeginMilliseconds = EventUtil.getDayBeginMilliseconds(startTime);
            Long dayEndMilliseconds = EventUtil.getDayEndMilliseconds(endTime);

            while (dayBeginMilliseconds < dayEndMilliseconds ){
                EventTracer tracer = new EventTracer(this.repeatedEventMap, dup_event, dayBeginMilliseconds);

                //add event to uuid - tracer map for tracking back to delete on day map.
                if (uidTracerMap.containsKey(event.getEventUid())){
                    uidTracerMap.get(event.getEventUid()).add(tracer);
                }else {
                    uidTracerMap.put(event.getEventUid(), new ArrayList<>());
                    uidTracerMap.get(event.getEventUid()).add(tracer);
                }

                //add event to repeated map
                if (repeatedEventMap.containsKey(dayBeginMilliseconds)){
                    repeatedEventMap.get(dayBeginMilliseconds).add(dup_event);
                }else {
                    repeatedEventMap.put(dayBeginMilliseconds, new ArrayList<>());
                    repeatedEventMap.get(dayBeginMilliseconds).add(dup_event);
                }

                dayBeginMilliseconds += BaseUtil.getAllDayLong(dayBeginMilliseconds);
            }
        }
    }

    public void removeEvent(Event event){
        //if allday
//        if (EventUtil.isAllDayEvent(event)){
//            this.removeAlldayEvent(event);
//        }

        //if old not repeated
        if (event.getRecurrence().length == 0){
            this.removeRegularEvent(event);
        }else{
            //if old is repeated
            this.removeRepeatedEvent(event);
        }

        //update event finder
        if (eventFinder.containsKey(event.getEventUid())){
            eventFinder.remove(event.getEventUid());
        }
    }

//    private synchronized void removeAlldayEvent(Event event){
//        this.allDayEventList.remove(event);
//    }

    private synchronized void removeRegularEvent(Event event){
        ITimeEventInterface org = findRegularEvent(event);
        if (org != null){
            long dayOfBegin = EventUtil.getDayBeginMilliseconds(event.getStartTime());
            List<ITimeEventInterface> events = regularEventMap.get(dayOfBegin);
            events.remove(org);
        }
    }

    private Event findRegularEvent(Event event){
        long dayOfBegin = EventUtil.getDayBeginMilliseconds(event.getStartTime());
        if (!regularEventMap.containsKey(dayOfBegin)){
            return null;
        }
        List<ITimeEventInterface> events = regularEventMap.get(dayOfBegin);
        for (ITimeEventInterface temp :events
                ) {
            if (temp.getEventUid().equals(event.getEventUid())){
                return (Event) temp;
            }
        }

        return null;
    }

    private synchronized void removeRepeatedEvent(Event event){
        List<EventTracer> tracers = uidTracerMap.get(event.getEventUid());
        if (tracers != null){
            for (EventTracer tracer:tracers
                    ) {
                tracer.removeSelfFromRepeatedEventMap();
            }
            uidTracerMap.remove(event.getEventUid());
        }
    }

    private long getLoadPreFlag(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.nowRepeatedStartAt.getTimeInMillis());
        cal.add(Calendar.DATE, (int) (defaultRepeatedRange * (1 - refreshFlag)));
        return cal.getTimeInMillis();
    }

    private long getLoadFurFlag(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(this.nowRepeatedEndAt.getTimeInMillis());
        cal.add(Calendar.DATE, (int) (- defaultRepeatedRange * (1-refreshFlag)));
        return cal.getTimeInMillis();
    }

    private Event findRepeatedOrgByUUID(String UUID){
        for (Event orgE:orgRepeatedEventList
                ) {
            if (orgE.getEventUid().equals(UUID)){
                return orgE;
            }
        }
        return null;
    }

    /**
     * Paul Paul, your finding Repeated Events
     * @param dayStartTime
     * @param UUID
     * @return
     */
    public Event findRepeatedByUUUID(long dayStartTime, String UUID){
        if (repeatedEventMap.containsKey(dayStartTime)){
            List<ITimeEventInterface> repeats = repeatedEventMap.get(dayStartTime);
            for (ITimeEventInterface event:repeats
                 ) {
                if (event.getEventUid().equals(UUID)){
                    return (Event) event;
                }
            }
        }else {
            Log.i(TAG, "findRepeatedByUUUID:  Repeated Event not belong to input day");
        }

        return null;
    }

    private void refreshRepeatedEvent(Event event){
        this.removeRepeatedEvent(event);
        this.orgRepeatedEventList.remove(event);
        this.addEvent(event);
    }

    public class EventsPackage implements ITimeEventPackageInterface {
        private Map<Long, List<ITimeEventInterface>> regularEventMap;
        private Map<Long, List<ITimeEventInterface>> repeatedEventMap;

        void setRegularEventMap(Map<Long, List<ITimeEventInterface>> regularEventMap) {
            this.regularEventMap = regularEventMap;
        }

        void setRepeatedEventMap(Map<Long, List<ITimeEventInterface>> repeatedEventMap) {
            this.repeatedEventMap = repeatedEventMap;
        }

        public void clearPackage(){
            this.regularEventMap.clear();
            //** here need to handle the linked effect.
//            this.repeatedMap.clear();
        }

        @Override
        public Map<Long, List<ITimeEventInterface>> getRegularEventDayMap() {
            return regularEventMap;
        }

        @Override
        public Map<Long,List<ITimeEventInterface>> getRepeatedEventDayMap() {
            return repeatedEventMap;
        }
    }

    private class EventTracer{
        private Map<Long, List<ITimeEventInterface>> repeatedEventMap;
        private ITimeEventInterface event;
        private long belongToDayOfBegin;

        EventTracer(Map<Long, List<ITimeEventInterface>> repeatedEventMap
                , ITimeEventInterface event,long belongToDayOfBegin){
            this.repeatedEventMap = repeatedEventMap;
            this.event = event;
            this.belongToDayOfBegin = belongToDayOfBegin;
        }

        void removeSelfFromRepeatedEventMap(){
            repeatedEventMap.get(belongToDayOfBegin).remove(event);
        }
    }

    //for update EventManager
    public interface OnRefreshEventManager {
        void onTaskStart();
        void onTaskEnd();
    }

    /********************************** Paul Paul 改 *********************************************/



    public Event getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(Event currentEvent) {
        this.currentEvent = currentEvent;
    }

    public String getHostInviteeUid(Event event) {
//        for (Invitee invitee : event.getInvitee()) {
//            if (invitee.getIsHost() == 1) {
//                // 1 means is host
//                return invitee.getInviteeUid();
//            }
//        }
        return null;
    }

    public Context getContext() {
        return context;
    }

    public Event copyEvent(Event event){
        Gson gson = new Gson();
        String eventString = gson.toJson(event);
        return gson.fromJson(eventString, Event.class);
    }
}
