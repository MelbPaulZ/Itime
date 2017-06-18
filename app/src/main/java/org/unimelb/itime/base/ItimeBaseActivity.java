package org.unimelb.itime.base;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;

/**
 * Created by Paul on 6/6/17.
 */

public abstract class ItimeBaseActivity<V extends MvpView, P extends MvpBasePresenter<V>> extends MvpActivity<V, P> {

    FragmentManager fragmentManager;

    public void clearFragmentStack(){
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public void openFragment(Fragment fragment) {
        openFragment(fragment, null, true);
    }

    public void openFragment(Fragment fragment, Bundle bundle){
        openFragment(fragment, bundle, true);
    }

    public void openFragment(Fragment fragment, Bundle bundle, boolean isAddedToStack){
        fragmentManager = getSupportFragmentManager();
        if(bundle != null){
            fragment.setArguments(bundle);
        }
        FragmentTransaction t = fragmentManager.beginTransaction();
        t.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        t.replace(getFragmentContainerId(), fragment, fragment.getClass().getSimpleName());

        if(isAddedToStack){
            t.addToBackStack(fragment.getClass().getSimpleName());
        }
        t.commit();
        fragmentManager.executePendingTransactions();
    }

    public void backFragment(Fragment fragment){
        backFragment(fragment, null);
    }

    public void backFragment(Fragment fragment, Bundle bundle){
        fragmentManager = getSupportFragmentManager();
        if (bundle!=null) {
            fragment.setArguments(bundle);
        }
        FragmentTransaction t = fragmentManager.beginTransaction();
        t.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        t.replace(getFragmentContainerId(), fragment);
        t.commit();
        fragmentManager.executePendingTransactions();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

    }


    protected abstract int getFragmentContainerId();
}
