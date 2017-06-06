package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ToolbarInterface;

/**
 * Created by Paul on 2/6/17.
 */

public class ToolbarViewModel<V extends ToolbarInterface> extends BaseObservable{

    private String title;
    private String rightText;
    private Drawable leftIcon;
    private int rightTextColor;

    private V view;

    public ToolbarViewModel(V view) {
        this.view = view;
    }

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
    public int getRightTextColor() {
        return rightTextColor;
    }

    public void setRightTextColor(int rightTextColor) {
        this.rightTextColor = rightTextColor;
        notifyPropertyChanged(BR.rightTextColor);
    }

    @Bindable
    public Drawable getLeftIcon() {
        return leftIcon;
    }

    public void setLeftIcon(Drawable leftIcon) {
        this.leftIcon = leftIcon;
        notifyPropertyChanged(BR.leftIcon);
    }

    public View.OnClickListener onClickLeft(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.onBack();
            }
        };
    }

    public View.OnClickListener onCLickRight(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                view.onNext();
            }
        };
    }
}