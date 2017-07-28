package org.unimelb.itime.bean;

/**
 * Created by 37925 on 2016/12/9.
 */

public class FriendRequestWrapper extends BeanWrapper {
    private FriendRequest request;

    public FriendRequest getRequest() {
        return request;
    }

    public void setRequest(FriendRequest request) {
        this.request = request;
    }

    public FriendRequestWrapper(FriendRequest request){
        super(request.getUserDetail().getPersonalAlias());
        this.request = request;
    }

    public String getDisplayStatus() {
        return request.getDisplayStatus();
    }

    public void setDisplayStatus(String displayStatus) {
        this.request.setDisplayStatus(displayStatus);
    }

    @Override
    public String getName() {
        return request.getUserDetail().getPersonalAlias();
    }

    @Override
    public String getPhoto() {
        return request.getUserDetail().getPhoto();
    }

    @Override
    public String getUserId() {
        return request.getUserDetail().getUserId();
    }

    @Override
    public String getUserUid() {
        return request.getUserDetail().getUserUid();
    }
}
