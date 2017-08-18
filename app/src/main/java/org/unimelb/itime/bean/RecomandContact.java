package org.unimelb.itime.bean;

/**
 * Created by Qiushuo Huang on 2017/8/17.
 */

public class RecomandContact implements ITimeUserInfoInterface{

    private String userUid ="";
    private int commonContact = 0;
    private User userDetail = new User();

    public RecomandContact(String userUid, int commonContact, User userDetail) {
        this.userUid = userUid;
        this.commonContact = commonContact;
        this.userDetail = userDetail;
    }

    public String getUserUid() {
        return userUid;
    }

    public void setUserUid(String userUid) {
        this.userUid = userUid;
    }

    public int getCommonContact() {
        return commonContact;
    }

    public void setCommonContact(int commonContact) {
        this.commonContact = commonContact;
    }

    public User getUserDetail() {
        return userDetail;
    }

    public void setUserDetail(User userDetail) {
        this.userDetail = userDetail;
    }

    @Override
    public String getShowPhoto() {
        return userDetail.getShowPhoto();
    }

    @Override
    public String getShowName() {
        return userDetail.getShowName();
    }

    @Override
    public String getSecondInfo() {
        return userDetail.getSecondInfo();
    }
}
