package org.unimelb.itime.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;

/**
 * Created by Qiushuo Huang on 2017/3/8.
 */

@Entity
public class Domain {
    private String name = "";
    private String type = "";
    private String rule = "";
    private String source = "";
    private int hasCalendar = 0;

    @Id
    private long domainUid;

    @Generated(hash = 388281671)
    public Domain(String name, String type, String rule, String source, int hasCalendar, long domainUid) {
        this.name = name;
        this.type = type;
        this.rule = rule;
        this.source = source;
        this.hasCalendar = hasCalendar;
        this.domainUid = domainUid;
    }

    @Generated(hash = 2097243279)
    public Domain() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public long getDomainUid() {
        return domainUid;
    }

    public void setDomainUid(long domainUid) {
        this.domainUid = domainUid;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getHasCalendar() {
        return hasCalendar;
    }

    public void setHasCalendar(int hasCalendar) {
        this.hasCalendar = hasCalendar;
    }

    public boolean hasCalendar(){
        return hasCalendar == 1;
    }
}
