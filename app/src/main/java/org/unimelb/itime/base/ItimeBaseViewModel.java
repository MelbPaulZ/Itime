package org.unimelb.itime.base;

import android.animation.ValueAnimator;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Switch;

import org.unimelb.itime.widget.BadgeView;

/**
 * Created by Paul on 2/6/17.
 */

public class ItimeBaseViewModel extends BaseObservable {


    @BindingAdapter("android:onFocusChange")
    public static void setOnFocusChangeListener(View view, View.OnFocusChangeListener onFocusChangeListener){
        view.setOnFocusChangeListener(onFocusChangeListener);
    }

    @BindingAdapter("android:onEditTextChange")
    public static void setOnEditTextCchangeListener(EditText editText, TextWatcher textWatcher){
        editText.addTextChangedListener(textWatcher);
    }

    @BindingAdapter("android:onSwitchChange")
    public static void setOnSwitchChangeListener(Switch s, Switch.OnCheckedChangeListener listener){
        s.setOnCheckedChangeListener(listener);
    }

    /**
     * Bind this method to an editText, this editText will automatically focus and put cursor on the end of the content
     * @param editText
     * @param isFocus
     */
    @BindingAdapter("android:onInitialFocus")
    public static void setInitialFocus(EditText editText, boolean isFocus){
        if (isFocus) {
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);
            editText.setSelection(editText.getText().length()); // move cursor to end of text, this has to be called after edit text has text
        }else{
            InputMethodManager imm = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }
    }

    @BindingAdapter("android:badgeNumber")
    public static void setBadgeNumber(BadgeView badgeNumber, int number){
        badgeNumber.setNumber(number + "");
        badgeNumber.invalidate();
    }



    public View.OnFocusChangeListener onInputEditFocusChange(){
        return new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if(hasFocus){
                    showKeyBoard(view);
                }else{
                    closeKeyBoard(view);
                }
            }
        };
    }

    public void showKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public void closeKeyBoard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }


}

