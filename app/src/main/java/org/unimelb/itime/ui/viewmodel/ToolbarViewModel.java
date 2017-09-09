package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
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
    private boolean leftEnable;
    private boolean rightEnable = true;
    private Drawable rightIcon;
    private int rightIconVisibility = View.GONE;
    private int dividerVisibility = View.VISIBLE;

    V view;

    public ToolbarViewModel(V view) {
        this.view = view;
        rightTextColor = Color.parseColor("#222222");
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
    public boolean isLeftEnable() {
        return leftEnable;

    }

    public void setLeftEnable(boolean leftEnable) {
        this.leftEnable = leftEnable;
        notifyPropertyChanged(BR.leftEnable);
    }

    @Bindable
    public boolean isRightEnable() {
        return rightEnable;
    }

    public void setRightEnable(boolean rightEnable) {
        this.rightEnable = rightEnable;
        notifyPropertyChanged(BR.rightEnable);
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

    @Bindable
    public Drawable getRightIcon() {
        return rightIcon;
    }

    public void setRightIcon(Drawable rightIcon) {
        this.rightIcon = rightIcon;
        notifyPropertyChanged(BR.rightIcon);
    }

    @Bindable
    public int getRightIconVisibility() {
        return rightIconVisibility;
    }

    public void setRightIconVisibility(int rightIconVisibility) {
        this.rightIconVisibility = rightIconVisibility;
        notifyPropertyChanged(BR.rightIconVisibility);
    }

    @Bindable
    public int getDividerVisibility() {
        return dividerVisibility;
    }

    public void setDividerVisibility(int dividerVisibility) {
        this.dividerVisibility = dividerVisibility;
        notifyPropertyChanged(BR.dividerVisibility);
    }
}
