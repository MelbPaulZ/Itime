package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.base.ToolbarInterface;

/**
 * Created by Paul on 2/6/17.
 */

public class ToolbarBtnViewModel extends BaseObservable{

    private String text;
    private Drawable icon;
    private int textColor;
    private boolean isEnable;

    public ToolbarBtnViewModel() {
    }

    @Bindable
    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Bindable
    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Bindable
    public int getTextColor() {
        return textColor;
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    @Bindable
    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public View.OnClickListener onClick(){
        return v -> {
        };
    }


}