package org.unimelb.itime.ui.viewmodel.event;

import android.content.Intent;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.alertview.AlertView;
import com.bigkoo.alertview.OnItemClickListener;
import com.developer.paul.closabledatabindingview.closableItem.ButtonItem;
import com.developer.paul.closabledatabindingview.closableItem.RowItem;

import com.android.databinding.library.baseAdapters.BR;
import com.developer.paul.closabledatabindingview.interfaces.ClosableItem;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.databinding.CellEventCreateRepeatMiddleBinding;
import org.unimelb.itime.ui.mvpview.event.EventCreateMvpView;
import org.unimelb.itime.ui.presenter.EventCreatePresenter;
import org.unimelb.itime.util.CalendarUtil;
import org.unimelb.itime.util.EventUtil;
import org.unimelb.itime.util.rulefactory.RuleFactory;
import org.unimelb.itime.util.rulefactory.RuleModel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Paul on 2/6/17.
 */

public class EventCreateViewModel extends ItimeBaseViewModel{

    protected EventCreatePresenter presenter;
    protected HashMap<String, Integer> orderHashMap = new HashMap<>();
    protected List<ButtonItem> buttonItems = new ArrayList<>();
    protected List<RowItem> rowItems = new ArrayList<>();
    protected Event event;
    protected EventCreateMvpView mvpView;
    private boolean hasChange;


    protected List<String> mockAvatorLists = new ArrayList<>();

    public EventCreateViewModel(EventCreatePresenter presenter) {
        this.presenter = presenter;
        mvpView = (EventCreateMvpView) presenter.getView();
        init();
    }

    private void init(){
        orderHashMap.put(getString(R.string.photos_toolbar_btn), 0);
        orderHashMap.put(getString(R.string.note_toolbar_btn), 1);
        orderHashMap.put(getString(R.string.url_toolbar_btn),2);
        orderHashMap.put(getString(R.string.repeat_toolbar_btn),3);
        notifyPropertyChanged(BR.orderHashMap);

        buttonItems.add(createButtonItem(getString(R.string.photos_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.note_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.url_toolbar_btn)));
        buttonItems.add(createButtonItem(getString(R.string.repeat_toolbar_btn)));
        notifyPropertyChanged(BR.buttonItems);
    }


    public CompoundButton.OnCheckedChangeListener onAlldayChangeListener(){
        return (buttonView, isChecked) -> {
            if (isChecked){
                EventUtil.notAllDayToAllDay(event);
            }else{
                EventUtil.allDayToNotAllDay(event);
            }
            setEvent(event);
        };
    }





    public int getAllDayVisibility(Event event){
        return event.getIsAllDay()? View.VISIBLE:View.GONE;
    }

    public int getNotAllDayVisibility(Event event){
        return event.getIsAllDay() ? View.GONE:View.VISIBLE;
    }

    public String getEventStartDate(Event event){

        Date d = EventUtil.parseTimeZoneToDate(event.getStart().getDateTime());
        if(d!=null) {
            return EventUtil.getFormatTimeString(d.getTime(), EventUtil.WEEK_DAY_MONTH);
        }else{
            return "";
        }
    }

    public String getEventEndDate(Event event){
        Date d = EventUtil.parseTimeZoneToDate(event.getEnd().getDateTime());
        if(d!=null) {
            return EventUtil.getFormatTimeString(d.getTime(), EventUtil.WEEK_DAY_MONTH);
        }else{
            return "";
        }
    }

    public String getEventStartTime(Event event){
        Date d = EventUtil.parseTimeZoneToDate(event.getStart().getDateTime());
        if(d!=null) {
            return EventUtil.getFormatTimeString(d.getTime(), EventUtil.HOUR_MIN);
        }else{
            return "";
        }
    }

    public String getEventEndTime(Event event){
        Date d = EventUtil.parseTimeZoneToDate(event.getEnd().getDateTime());
        if(d!=null) {
            return EventUtil.getFormatTimeString(d.getTime(), EventUtil.HOUR_MIN);
        }else{
            return "";
        }
    }

    @Bindable
    public List<String> getMockAvatorLists() {
        return mockAvatorLists;
    }


    public String getTitleString(Event event){
        if (event.getSummary().equals("")){
            return presenter.getContext().getString(R.string.event_title_hint);
        }
        return event.getSummary();
    }

    public int getTitleColor(Event event){
        if (event.getSummary().equals("")){
            return presenter.getContext().getResources().getColor(R.color.whiteTwo);
        }else{
            return presenter.getContext().getResources().getColor(R.color.black);
        }
    }

    public int getLocationColor(Event event){
        if (event.getLocation().equals("")){
            return presenter.getContext().getResources().getColor(R.color.whiteTwo);
        }else{
            return presenter.getContext().getResources().getColor(R.color.black);
        }
    }

    public int getLocationHintVisibility(Event event){
        if ("".equals(event.getLocation().getLocationString1()) &&
                "".equals(event.getLocation().getLocationString2())){
            return View.VISIBLE;
        }
        return View.GONE;
    }

    public int getLocationStringVisibility(Event event){
        return getLocationHintVisibility(event) == View.VISIBLE? View.GONE: View.VISIBLE;
    }

    public String getDurationString(Event event){
        return EventUtil.durationInt2String(presenter.getContext(), event.getDuration()==0? 60 : event.getDuration());
    }

    public int getAddTimeslotVisibility(Event event){
        return event.getTimeslot()==null || event.getTimeslot().size()==0? View.VISIBLE:View.GONE;
    }

    public int getTimeslotsVisibility(Event event){
        return getAddTimeslotVisibility(event) == View.VISIBLE? View.GONE : View.VISIBLE;
    }

    public String getTimeslotsString(Event event){
        if (event.getTimeslot()==null){
            return "";
        }
        return String.format(presenter.getContext().getString(R.string.event_candidate_timeslots), event.getTimeslot().size());
    }

    public String getDisplayCalendar(Event event){
        return CalendarUtil.getInstance(presenter.getContext()).getCalendarName(event.getCalendarUid());
    }


    /** Timeslot parts
     * **/


    private List<TimeslotLineViewModel> timeslot = new ArrayList<>();
    private ItemBinding<TimeslotLineViewModel> onItemBind = ItemBinding.of(BR.timeslotVM, R.layout.row_timeslot);

    @Bindable
    public List<TimeslotLineViewModel> getTimeslot() {
        return timeslot;
    }

    public void setTimeslot(List<TimeslotLineViewModel> timeslot) {
        Collections.sort(timeslot, new Comparator<TimeslotLineViewModel>() {
            @Override
            public int compare(TimeslotLineViewModel o1, TimeslotLineViewModel o2) {
                return (int) (o1.getTimeslot().getStartTime() - o2.getTimeslot().getStartTime());
            }
        });
        this.timeslot = timeslot;
        notifyPropertyChanged(BR.timeslot);
    }

    @Bindable
    public ItemBinding<TimeslotLineViewModel> getOnItemBind() {
        return onItemBind;
    }

    public void setOnItemBind(ItemBinding<TimeslotLineViewModel> onItemBind) {
        this.onItemBind = onItemBind;
        notifyPropertyChanged(BR.onItemBind);
    }


    private void resetTimeslots(){
        if (event.getTimeslot()==null||event.getTimeslot().size()==0){
            return;
        }
        List<TimeslotLineViewModel> timeslotLineViewModels = new ArrayList<>();
        for (TimeSlot timeSlot: event.getTimeslot().values()){
            timeslotLineViewModels.add(new TimeslotLineViewModel(presenter.getContext(), timeSlot));
        }
        setTimeslot(timeslotLineViewModels);

    }

    /** End
     * **/

    @Bindable
    public List<ButtonItem> getButtonItems() {
        return buttonItems;
    }

    public void setButtonItems(List<ButtonItem> buttonItems) {
        this.buttonItems = buttonItems;
        notifyPropertyChanged(BR.buttonItems);
    }

    @Bindable
    public List<RowItem> getRowItems() {
        return rowItems;
    }

    public void setRowItems(List<RowItem> rowItems) {
        this.rowItems = rowItems;
        notifyPropertyChanged(BR.rowItems);
    }


    /**
     * Every time event attributes change, this method will be called
     */
    protected void resetButtonsAndRows(){
        if (!event.getNote().equals("")){
            addNoteToRow(event.getNote());
            removeItem(buttonItems,getString(R.string.note_toolbar_btn));
        }else{
            addButton(getString(R.string.note_toolbar_btn));
            removeItem(rowItems,getString(R.string.note_toolbar_btn));
        }

        if (!event.getUrl().equals("")){
            addUrlToRow(event.getUrl());
            removeItem(buttonItems,getString(R.string.url_toolbar_btn));
        }else{
            addButton(getString(R.string.url_toolbar_btn));
            removeItem(rowItems, getString(R.string.url_toolbar_btn));
        }

        if (event.getRecurrence()!=null && event.getRecurrence().length>0){
            addRepeatToRow(EventUtil.getRepeatString(presenter.getContext(), event));
            removeItem(buttonItems, getString(R.string.repeat_toolbar_btn));
        }else{
            addButton(getString(R.string.repeat_toolbar_btn));
            removeItem(rowItems, getString(R.string.repeat_toolbar_btn));
        }
        notifyPropertyChanged(BR.rowItems);
        notifyPropertyChanged(BR.buttonItems);

    }

    /**
     * Use this method to remove Item from items.
     * @param items
     * @param name
     */
    protected void removeItem(List<? extends ClosableItem> items, String name){
        ClosableItem closableItem = null;
        for (ClosableItem item: items){
            if (item.getItemName().equals(name)){
                closableItem = item;
                break;
            }
        }
        if (closableItem==null){
            return;
        }
        items.remove(closableItem);
    }



    @Bindable
    public HashMap<String, Integer> getOrderHashMap() {
        return orderHashMap;
    }

    public void setOrderHashMap(HashMap<String, Integer> orderHashMap) {
        this.orderHashMap = orderHashMap;
        notifyPropertyChanged(BR.orderHashMap);
    }

    @Bindable
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
        resetButtonsAndRows();
        resetTimeslots();
        updateAvatars();
        notifyPropertyChanged(BR.event);
    }

    private void updateAvatars(){
        mockAvatorLists.clear();
        for (Invitee invitee: event.getInvitee().values()){
            mockAvatorLists.add(invitee.getAliasPhoto());
        }
        notifyPropertyChanged(BR.mockAvatorLists);

    }

    /**
     *
     * @return
     */
    public View.OnClickListener onClickInvitees(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toInvitee(event);
                }
            }
        };
    }


    public View.OnClickListener onClickDuration(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toDuration(event);
                }
            }
        };
    }

    public View.OnClickListener onClickTitle(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toTitle(event);
                }
            }
        };
    }


    public View.OnClickListener onClickTimeslot(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toTimeslot(event);
                }
            }
        };
    }

    public View.OnClickListener onClickLocation(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toLocation(event);
                }
            }
        };
    }

    public View.OnClickListener onClickCalendars(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toCalendars(event);
                }
            }
        };
    }

    public View.OnClickListener onClickGreetings(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toGreeting(event);
                }
            }
        };
    }


    public View.OnClickListener onClickRepeat(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null) {
                    mvpView.toRepeat(event);
                }
            }
        };
    }



    /*  update buttons */

    /**
     * Use this method to add Button
     * @param name
     */
    protected void addButton(String name){
        if (isContainBtn(name)){
            return;
        }
        buttonItems.add(createButtonItem(name));
        notifyPropertyChanged(BR.buttonItems);
    }


    /*  update rows */




    /**
     * This method update row text
     */
    protected void updateRow(String itemName, String text){
        RowItem item = findRowItem(itemName);
        if (item!=null){
            item.setText(text);
            notifyPropertyChanged(BR.rowItems);
        }
    }

    public class RepeatCellViewModel extends BaseObservable{

        private Event cellEvent;

        public RepeatCellViewModel(Event event) {
            this.cellEvent = event;
        }

        public int getTwoLinesVisibility(Event event){
            RuleModel ruleModel = RuleFactory.getInstance().getRuleModel(event);
            return ruleModel.getUntil() == null ? View.GONE : View.VISIBLE;
        }

        public int getOneLineVisiblitity(Event event){
            return getTwoLinesVisibility(event) == View.GONE ? View.VISIBLE : View.GONE;
        }

        @Bindable
        public Event getCellEvent() {
            return cellEvent;
        }

        public void setCellEvent(Event event) {
            this.cellEvent = event;
            notifyPropertyChanged(BR.cellEvent);
        }

        public String getRepeatPrimaryString(Event event){
            return EventUtil.getRepeatString(presenter.getContext(), event);
        }

        public String getRepeatSecondaryString(Event event){
            Calendar c = Calendar.getInstance();
            RuleModel ruleModel = RuleFactory.getInstance().getRuleModel(event);
            if (ruleModel == null || ruleModel.getUntil() == null) {
                return "";
            }
            c.setTime(ruleModel.getUntil());

            return "until " + EventUtil.getFormatTimeString(c.getTimeInMillis(), EventUtil.DAY_MONTH_YEAR);
        }
    }

    private void updateRepeatRow(){
        RowItem item = findRowItem(getString(R.string.repeat_toolbar_btn));
        item.getRowCreateInterface().updateClosableView(item);
    }

    private void addRepeatToRow(String text){
        if (containRow(getString(R.string.repeat_toolbar_btn))){
            updateRepeatRow();
//            updateRow(getString(R.string.repeat_toolbar_btn), text);
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickRepeat().onClick(v);

            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.repeat_toolbar_btn));
                event.setRecurrence(new String[]{});
                setEvent(event);
            }
        };

        final RepeatCellViewModel[] cellVM = new RepeatCellViewModel[1];
        addInList(getString(R.string.repeat_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_repeat),
                text, onClickListener, onDeleteListener, new RowItem.RowCreateInterface() {
                    @Override
                    public View onCreateMiddleView(RowItem rowItem) {
                        LayoutInflater inflater = LayoutInflater.from(presenter.getContext());
                        CellEventCreateRepeatMiddleBinding binding = DataBindingUtil.inflate(inflater, R.layout.cell_event_create_repeat_middle, null, false);
                        cellVM[0] = new RepeatCellViewModel(event);
                        binding.setVm(cellVM[0]);
                        return binding.getRoot();
                    }

                    @Override
                    public void updateClosableView(RowItem rowItem) {
                        cellVM[0].setCellEvent(event);
                    }
                });
        notifyPropertyChanged(BR.rowItems);
    }

    /**
     * when click note button, add note to row,
     * when click this view, goto note page
     * when click close icon, reset event note to ""
     * @param text
     */
    private void addNoteToRow(String text){
        if (containRow(getString(R.string.note_toolbar_btn))){
            updateRow(getString(R.string.note_toolbar_btn), text);
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toNote(event);
                }
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.note_toolbar_btn));
                event.setNote("");
                setEvent(event);
            }
        };
        addInList(getString(R.string.note_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_note),
                text, onClickListener, onDeleteListener);
    }


    private void addUrlToRow(String text){
        if (containRow(getString(R.string.url_toolbar_btn))){
            updateRow(getString(R.string.url_toolbar_btn), text);
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mvpView!=null){
                    mvpView.toUrl(event);
                }
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.url_toolbar_btn));
                event.setUrl("");
                setEvent(event);
            }
        };

        addInList(getString(R.string.url_toolbar_btn), presenter.getContext().getResources().getDrawable(R.drawable.icon_event_url), text, onClickListener, onDeleteListener);
    }

    private String[] getMessages(){
        return new String[]{getString(R.string.action_sheet_take_photo), getString(R.string.action_sheet_choose_from_photos)};
    }

    private void photoActionSheetPopup(){
        new AlertView.Builder().setContext(presenter.getContext())
                .setStyle(AlertView.Style.ActionSheet)
                .setCancelText(getString(R.string.action_sheet_cancel))
                .setOthers(getMessages())
                .setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(Object o, int position) {
                        Toast.makeText(presenter.getContext(), position + "", Toast.LENGTH_SHORT).show();
                        ((EventCreateMvpView)presenter.getView()).toPhoto();
                    }
                })
                .build()
                .setCancelable(true)
                .show();
    }

    private void addPhotoToRow(){
        if (containRow(getString(R.string.photos_toolbar_btn))){
            return;
        }
        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoActionSheetPopup();
            }
        };

        View.OnClickListener onDeleteListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addButton(getString(R.string.photos_toolbar_btn));
            }
        };
        addInList(getString(R.string.photos_toolbar_btn),
                presenter.getContext().getResources().getDrawable(R.drawable.icon_event_photo),
                getString(R.string.photos_toolbar_btn), onClickListener, onDeleteListener, new RowItem.RowCreateInterface() {
                    @Override
                    public View onCreateMiddleView(RowItem rowItem) {
                        return null; // TODO: 20/6/17 photo layout view
                    }

                    @Override
                    public void updateClosableView(RowItem rowItem) {
                        
                    }
                });

    }


    protected String getString(int stringId){
        return presenter.getContext().getString(stringId);
    }
//
    /**
     * update row to rowItems
     * @param rowName
     * @param icon
     * @param text
     * @param onClickListener
     * @param onDeleteListener
     */
    protected void addInList(String rowName, Drawable icon, String text,
                           View.OnClickListener onClickListener, View.OnClickListener onDeleteListener){
        addInList(rowName, icon, text, onClickListener, onDeleteListener, null);
    }

    protected void addInList(String rowName, Drawable icon, String text,
                             View.OnClickListener onClickListener, View.OnClickListener onDeleteListener,
                             RowItem.RowCreateInterface rowCreateInterface){
        RowItem rowItem = new RowItem();
        rowItem.setItemName(rowName);
        rowItem.setIcon(icon);
        rowItem.setText(text);
        rowItem.setClickListener(onClickListener);
        rowItem.setOnDeleteClickListener(onDeleteListener);
        if (rowCreateInterface!=null){
            rowItem.setRowCreateInterface(rowCreateInterface);
        }
        rowItems.add(rowItem);
    }



    protected boolean containRow(String rowName){
        return isIn(rowName, rowItems);
    }

    protected boolean isContainBtn(String buttonName){
        return isIn(buttonName, buttonItems);
    }

    private boolean isIn(String name, List<? extends ClosableItem> items){
        for (ClosableItem item: items){
            if (item.getItemName().equals(name)){
                return true;
            }
        }
        return false;
    }

    private RowItem findRowItem(String name){
        return (RowItem) findClosableItem(name, rowItems);
    }

    private ButtonItem findButtomItem(String name){
        return (ButtonItem) findClosableItem(name, buttonItems);
    }

    private ClosableItem findClosableItem(String name, List<? extends ClosableItem> items){
        for (ClosableItem item: items){
            if (item.getItemName().equals(name)){
                return item;
            }
        }
        return null;
    }


    /**
     * Use this function to create buttons
     * @param name
     * @return
     */
    protected ButtonItem createButtonItem(String name){
        if (name.equals(getString(R.string.photos_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_photo), new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    photoActionSheetPopup();
                }
            });
        }

        if (name.equals(getString(R.string.note_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_note),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mvpView!=null){
                        mvpView.toNote(event);
                    }
                }
            });
        }

        if (name.equals(getString(R.string.url_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_url),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mvpView!=null){
                        mvpView.toUrl(event);
                    }
                }
            });
        }

        if (name.equals(getString(R.string.repeat_toolbar_btn))){
            return createButtonItem(name, presenter.getContext().getResources().getDrawable(R.drawable.icon_event_toolbar_repeat),new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mvpView!=null){
                        mvpView.toRepeat(event);
                    }
                }
            });
        }
        return null;
    }

    protected ButtonItem createButtonItem(String name, Drawable drawable, View.OnClickListener onClickListener){
        ButtonItem btnItem = new ButtonItem();
        btnItem.setItemName(name);
        btnItem.setIcon(drawable);
        btnItem.setText(name);
        btnItem.setOnClickListener(onClickListener);
        return btnItem;
    }
}
