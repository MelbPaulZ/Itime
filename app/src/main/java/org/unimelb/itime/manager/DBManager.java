package org.unimelb.itime.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;
import org.unimelb.itime.bean.Block;
import org.unimelb.itime.bean.BlockDao;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.bean.CalendarDao;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.ContactDao;
import org.unimelb.itime.bean.DaoMaster;
import org.unimelb.itime.bean.DaoSession;
import org.unimelb.itime.bean.Domain;
import org.unimelb.itime.bean.DomainDao;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.EventDao;
import org.unimelb.itime.bean.FriendRequest;
import org.unimelb.itime.bean.FriendRequestDao;
import org.unimelb.itime.bean.User;
import org.unimelb.itime.bean.UserDao;
import org.unimelb.itime.util.UserUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 28/08/16.
 */
public class DBManager {
    private final static String dbName = "test_db";
    private static DBManager mInstance;
    private DaoMaster.DevOpenHelper openHelper;
    private Context context;
    private DaoMaster daoMaster;

    private DBManager(Context context) {
        this.context = context.getApplicationContext();
        openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        daoMaster = new DaoMaster(getWritableDatabase());
    }


    public static DBManager getInstance(Context context) {
        if (mInstance == null) {
            synchronized (DBManager.class) {
                if (mInstance == null) {
                    mInstance = new DBManager(context);
                }
            }
        }
        return mInstance;
    }

    public synchronized void clearDB(){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(
                context.getApplicationContext(), dbName, null);
        SQLiteDatabase db = devOpenHelper.getWritableDatabase();
        devOpenHelper.onUpgrade(db,0,0);
    }

    private SQLiteDatabase getWritableDatabase() {
        if (openHelper == null) {
            openHelper = new DaoMaster.DevOpenHelper(context, dbName, null);
        }
        SQLiteDatabase db = openHelper.getWritableDatabase();
        return db;
    }

    /*********************************** delete above *************************************/
    @SuppressWarnings("unchecked")
    public <T extends Object,V> List<T> find(Class<T> className, String name, V value){
        AbstractDao abd =  daoMaster.newSession().getDao(className);
        QueryBuilder<T> qb = abd.queryBuilder();
        Property[] ptys = abd.getProperties();
        Property attr = null;

        for (Property pty:ptys
             ) {
            if (pty.name.equals(name)){
                attr = pty;
                break;
            }
        }
        if (attr == null){
            return null;
        }

        List<T> list = qb.where(attr.eq(value)).list();
        return list;
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> AbstractDao getQueryDao(Class<T> className){
        return daoMaster.newSession().getDao(className);
    }

    @SuppressWarnings("unchecked")
    public <T extends Object> List<T> getAll(Class<T> className){
        return ((AbstractDao) daoMaster.newSession().getDao(className)).queryBuilder().list();
    }


    @SuppressWarnings("unchecked")
    public synchronized  <T extends Object> void deleteAll(Class<T> className){
        ((AbstractDao) (daoMaster.newSession()).getDao(className)).deleteAll();
    }

    @SuppressWarnings("unchecked")
    public synchronized  <T extends Object> void insertOrReplace(List<T> objs){
        if (objs == null || objs.isEmpty()) {
            return;
        }

        for (T obj:objs) {
            ((AbstractDao) (daoMaster.newSession()).getDao(obj.getClass())).insertOrReplace(obj);
        }
    }

    /************************ Customized DB method here **************************************/
    public synchronized User getUser(String userUid){
        DaoSession daoSession = daoMaster.newSession();
        UserDao userDao = daoSession.getUserDao();
        QueryBuilder<User> qb = userDao.queryBuilder();
        qb.where(
                UserDao.Properties.UserUid.eq(userUid));
        List<User> list = qb.list();
        if(list.isEmpty()){
            return null;
        }else {
            return list.get(0);
        }
    }

    /**
     *
     * @return all available calendars, exclude delete level = 1
     */
    public List<Calendar> getAllAvailableCalendarsForUser(){
        DaoSession daoSession = daoMaster.newSession();
        CalendarDao calendarDao = daoSession.getCalendarDao();
        List<Calendar> calendarList = calendarDao.queryBuilder().where(
                CalendarDao.Properties.UserUid.eq(UserUtil.getInstance(context).getUserUid()),
                CalendarDao.Properties.DeleteLevel.eq(0)
        ).orderAsc(CalendarDao.Properties.CreatedAt).list();
        return calendarList;
    }

    public Event getEvent(String uid) {

        DaoSession daoSession = daoMaster.newSession();
        EventDao eventDao = daoSession.getEventDao();
        QueryBuilder<Event> qb = eventDao.queryBuilder();
        qb.where(EventDao.Properties.EventUid.eq(uid));
        List<Event> list = qb.list();

        return list.size() > 0 ? list.get(0) : null;
    }

    public DaoSession getNewSession(){
        return daoMaster.newSession();
    }

    public synchronized List<Domain> getAllDomains() {
        DaoSession daoSession = daoMaster.newSession();
        DomainDao domainDao = daoSession.getDomainDao();
        return domainDao.loadAll();
    }

    public synchronized void insertFriendRequest(FriendRequest request) {
        if(request==null){
            return;
        }
        DaoSession daoSession = daoMaster.newSession();
        FriendRequestDao friendRequestDao = daoSession.getFriendRequestDao();
        friendRequestDao.insertOrReplace(request);

    }

    public List<Event> getAllAvailableEvents(List<org.unimelb.itime.bean.Calendar> calendars, String userUid){
        List<Event> events = new ArrayList<>();
        AbstractDao query = DBManager.getInstance(context).getQueryDao(Event.class);
        for (org.unimelb.itime.bean.Calendar cal:calendars
                ) {
            if (cal.getDeleteLevel() == 0 && cal.getVisibility() == 1){
                List<Event> dbEvents = query.queryBuilder().where(
                        EventDao.Properties.UserUid.eq(userUid),
                        EventDao.Properties.CalendarUid.eq(cal.getCalendarUid()),
                        EventDao.Properties.ShowLevel.gt(0)
                ).list();

                events.addAll(dbEvents);
            }
        }

        return events;
    }

    public synchronized List<Contact> getAllContact() {
        DaoSession daoSession = daoMaster.newSession();
        ContactDao contactDao = daoSession.getContactDao();
        QueryBuilder<Contact> qb = contactDao.queryBuilder();
        qb.where(qb.and(ContactDao.Properties.UserUid.eq(UserUtil.getInstance(context).getUserUid()),
                qb.and(ContactDao.Properties.Status.eq(Contact.ACTIVATED),
                        ContactDao.Properties.BlockLevel.eq(0)))).orderAsc(ContactDao.Properties.AliasName);
        List<Contact> list = qb.list();
        return list;
    }

    public synchronized List<Block> getBlockContacts() {
        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        QueryBuilder<Block> qb = blockDao.queryBuilder();
        qb.where(qb.and(BlockDao.Properties.UserUid.eq(UserUtil.getInstance(context).getUserUid()),
                BlockDao.Properties.BlockLevel.gt(0)));
        List<Block> list = qb.list();
        return list;
    }

    public synchronized void insertBlock(Block block) {
        if(block==null){
            return;
        }
        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        blockDao.insertOrReplace(block);
    }

    public synchronized void insertDomain(Domain domain) {
        if(domain==null){
            return;
        }
        DaoSession daoSession = daoMaster.newSession();
        DomainDao domainDao = daoSession.getDomainDao();
        domainDao.insertOrReplace(domain);
    }

    public synchronized void deleteBlock(Block block) {
        if(block==null){
            return;
        }

        DaoSession daoSession = daoMaster.newSession();
        BlockDao blockDao = daoSession.getBlockDao();
        blockDao.delete(block);
    }
}
