package org.unimelb.itime.bean;

/**
 * Created by Paul on 4/7/17.
 */

public class Message {
    private int messageUid;
    private int messageGroupUid;
    private int userUid;
    private String eventUid = "";
    private String photo = "";
    private String title = "";
    private String subtitle1 = "";
    private String subtitle2 = "";
    /** template -> {host_unconfirmed_normal, host_unconfirmed_normal, host_confirmed_normal, invitee_normal,
     *   invitee_deleted, host_unconfirmed_deleted, host_confirmed_deleted}**/
    private String template = "";
    private int type; // 0 -> default?
    private boolean isRead;
    private int deleteLevel;
    private String createAt = "";
    private String updateAt = "";

    public int getMessageUid() {
        return messageUid;
    }

    public void setMessageUid(int messageUid) {
        this.messageUid = messageUid;
    }

    public int getMessageGroupUid() {
        return messageGroupUid;
    }

    public void setMessageGroupUid(int messageGroupUid) {
        this.messageGroupUid = messageGroupUid;
    }

    public int getUserUid() {
        return userUid;
    }

    public void setUserUid(int userUid) {
        this.userUid = userUid;
    }

    public String getEventUid() {
        return eventUid;
    }

    public void setEventUid(String eventUid) {
        this.eventUid = eventUid;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle1() {
        return subtitle1;
    }

    public void setSubtitle1(String subtitle1) {
        this.subtitle1 = subtitle1;
    }

    public String getSubtitle2() {
        return subtitle2;
    }

    public void setSubtitle2(String subtitle2) {
        this.subtitle2 = subtitle2;
    }

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public int getDeleteLevel() {
        return deleteLevel;
    }

    public void setDeleteLevel(int deleteLevel) {
        this.deleteLevel = deleteLevel;
    }

    public String getCreateAt() {
        return createAt;
    }

    public void setCreateAt(String createAt) {
        this.createAt = createAt;
    }

    public String getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(String updateAt) {
        this.updateAt = updateAt;
    }
}
