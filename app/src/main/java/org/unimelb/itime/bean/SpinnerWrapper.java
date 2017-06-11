package org.unimelb.itime.bean;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import org.unimelb.itime.BR;

/**
 * Created by Qiushuo Huang on 2017/3/11.
 */

public class SpinnerWrapper extends BaseObservable {
    private String name;
    private int isSelecte;

    public SpinnerWrapper(String name, int isSelecte) {
        this.name = name;
        this.isSelecte = isSelecte;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public int getIsSelecte() {
        return isSelecte;
    }

    public void setIsSelecte(int isSelecte) {
        this.isSelecte = isSelecte;
        notifyPropertyChanged(BR.isSelecte);
    }
}