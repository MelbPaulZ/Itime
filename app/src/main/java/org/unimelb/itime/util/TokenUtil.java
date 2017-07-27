package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.bean.SyncToken;
import org.unimelb.itime.bean.SyncTokenDao;
import org.unimelb.itime.manager.DBManager;

import java.util.List;

/**
 * Created by yuhaoliu on 20/01/2017.
 */

public class TokenUtil {
    private static TokenUtil instance;
    private DBManager dbManager;

    private TokenUtil(Context context) {
        this.dbManager = DBManager.getInstance(context);
    }

    public static TokenUtil getInstance(Context context){
        if (instance == null){
            instance = new TokenUtil(context);
        }

        return instance;
    }

    /**
     * for /calendar/list
     * @param userUid
     * @return
     */
    public String getCalendarToken(String userUid){
        String tokenKey = userUid + "_" + SyncToken.PREFIX_CAL;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        List<SyncToken> calendarToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.Name.eq(tokenKey)
                ).list();
        if (calendarToken.size() == 0){
            return "";
        }
        return calendarToken.get(0).getValue();
    }

    public void setCalendarToken(String userUid, String token){
        if(token==null||token.isEmpty()){
            return;
        }
        String tokenKey = userUid + "_" + SyncToken.PREFIX_CAL;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        SyncToken newToken = new SyncToken();
        newToken.setName(tokenKey);
        newToken.setUserUid(userUid);
        newToken.setValue(token);
        dao.insertOrReplace(newToken);
    }

    public String getEventToken(String userUid){
        String tokenKey = userUid + "_" + SyncToken.PREFIX_EVENT;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        List<SyncToken> calendarToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.Name.eq(tokenKey)).list();
        if (calendarToken.size() == 0){
            return "";
        }

        return calendarToken.get(0).getValue();
    }

    /**
     * for event/list/{-r or calendarUid}
     * @param userUid
     * @param token
     */
    public void setEventToken(String userUid, String token){
        if(token==null||token.isEmpty()){
            return;
        }
        String tokenKey = userUid + "_" + SyncToken.PREFIX_EVENT;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        SyncToken newToken = new SyncToken();
        newToken.setName(tokenKey);
        newToken.setUserUid(userUid);
        newToken.setValue(token);
        dao.insertOrReplace(newToken);
    }

    public String getMessageToken(String userUid){
        String tokenKey = userUid + "_" + SyncToken.PREFIX_MESSAGE;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        List<SyncToken> messageToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.Name.eq(tokenKey)
                ).list();
        if (messageToken.size() == 0){
            return "";
        }
        return messageToken.get(0).getValue();
    }

    public void setMessageToken(String userUid, String token){
        if(token==null||token.isEmpty()){
            return;
        }
        String tokenKey = userUid + "_" + SyncToken.PREFIX_MESSAGE;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        SyncToken newToken = new SyncToken();
        newToken.setName(tokenKey);
        newToken.setUserUid(userUid);
        newToken.setValue(token);
        dao.insertOrReplace(newToken);
    }

    public String getContactToken(String userUid){
        String tokenKey = userUid + "_" + SyncToken.PREFIX_CONTACT;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        List<SyncToken> contactToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.Name.eq(tokenKey)
        ).list();
        if (contactToken.size() == 0){
            return "";
        }
        return contactToken.get(0).getValue();
    }

    public void setContactToken(String userUid, String token){
        if(token==null||token.isEmpty()){
            return;
        }
        String tokenKey = userUid + "_" + SyncToken.PREFIX_CONTACT;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        SyncToken newToken = new SyncToken();
        newToken.setName(tokenKey);
        newToken.setUserUid(userUid);
        newToken.setValue(token);
        dao.insertOrReplace(newToken);
    }

    public String getAccountToken(String userUid){
        return getToken(userUid, SyncToken.PREFIX_ACCOUNT);
    }

    public void setAccountToken(String userUid, String token){
        setToken(userUid, token, SyncToken.PREFIX_ACCOUNT);
    }

    public String getFriendRequestToken(String userUid){
        return getToken(userUid, SyncToken.PREFIX_FRIEND_REQUEST);
    }

    public void setFriendRequestToken(String userUid, String token){
        setToken(userUid, token, SyncToken.PREFIX_FRIEND_REQUEST);
    }

    public String getMeetingToken(String userUid){
        return getToken(userUid, SyncToken.PREFIX_MEETING);
    }

    public void setMeetingToken(String userUid, String token){
        setToken(userUid, token, SyncToken.PREFIX_MEETING);
    }

    public String getBlockToken(String userUid){
        return getToken(userUid, SyncToken.PREFIX_BLOCK);
    }

    public void setBlockToken(String userUid, String token){
        setToken(userUid, token, SyncToken.PREFIX_BLOCK);
    }

    /**
     * Added by Qiushuo Huang
     * @param userUid
     * @param token
     * @param prefix
     */
    private void setToken(String userUid, String token, String prefix){
        if(token==null||token.isEmpty()){
            return;
        }
        String tokenKey = userUid + "_" + prefix;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        SyncToken newToken = new SyncToken();
        newToken.setName(tokenKey);
        newToken.setUserUid(userUid);
        newToken.setValue(token);
        dao.insertOrReplace(newToken);
    }

    private String getToken(String userUid, String prefix){
        String tokenKey = userUid + "_" + prefix;
        SyncTokenDao dao = dbManager.getNewSession().getSyncTokenDao();
        List<SyncToken> contactToken = dao.queryBuilder().where(
                SyncTokenDao.Properties.Name.eq(tokenKey)
        ).list();
        if (contactToken.size() == 0){
            return "";
        }
        return contactToken.get(0).getValue();
    }
}
