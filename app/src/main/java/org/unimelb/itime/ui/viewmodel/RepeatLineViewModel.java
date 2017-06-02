package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.Toast;

import org.unimelb.itime.BR;

/**
 * Created by Paul on 2/6/17.
 */

public class RepeatLineViewModel extends BaseObservable {

    private String repeatText;
    private int iconVisibility = View.GONE;
    private Drawable rightIcon;
    private boolean canHideIcon = true;

    private OnClickCallBack onClickCallBack;

    public RepeatLineViewModel(String repeatText, int iconVisibility, Drawable rightIcon, boolean canHideIcon) {
        this.repeatText = repeatText;
        this.iconVisibility = iconVisibility;
        this.rightIcon = rightIcon;
        this.canHideIcon = canHideIcon;
    }

    @Bindable
    public String getRepeatText() {
        return repeatText;
    }

    public void setRepeatText(String repeatText) {
        this.repeatText = repeatText;
        notifyPropertyChanged(BR.repeatText);
    }

    @Bindable
    public int getIconVisibility() {
        return iconVisibility;
    }

    @Bindable
    public Drawable getRightIcon() {
        return rightIcon;
    }

    public void setRightIcon(Drawable rightIcon) {
        this.rightIcon = rightIcon;
        notifyPropertyChanged(BR.rightIcon);
    }

    public void setIconVisibility(int iconVisibility) {
        this.iconVisibility = iconVisibility;
        notifyPropertyChanged(BR.iconVisibility);
    }

    public View.OnClickListener onItemClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canHideIcon){
                    // normal repeat
                    if (onClickCallBack!=null){
                        onClickCallBack.beforeOnClick(RepeatLineViewModel.this);
                    }
                }else{
                    Toast.makeText(v.getContext(), "custom", Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    public void setOnClickCallBack(OnClickCallBack onClickCallBack) {
        this.onClickCallBack = onClickCallBack;
    }

    public interface OnClickCallBack{
        void beforeOnClick(RepeatLineViewModel repeatLineViewModel);
    }

}
