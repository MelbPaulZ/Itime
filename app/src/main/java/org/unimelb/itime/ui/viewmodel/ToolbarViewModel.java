package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;

import com.android.databinding.library.baseAdapters.BR;

/**
 * Created by Paul on 2/6/17.
 */

public class ToolbarViewModel extends BaseObservable {

    private String title;
    private String rightText;
    private Drawable leftIcon;

    @Bindable
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        notifyPropertyChanged(BR.title);
    }

    @Bindable
    public String getRightText() {
        return rightText;
    }

    public void setRightText(String rightText) {
        this.rightText = rightText;
        notifyPropertyChanged(BR.rightText);
    }

    @Bindable
    public Drawable getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(Drawable leftIcon) {
        this.leftIcon = leftIcon;
        notifyPropertyChanged(BR.leftIcon);
    }
}