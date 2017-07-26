package org.unimelb.itime.manager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.QueryBuilder;
import org.unimelb.itime.bean.DaoMaster;

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


}
