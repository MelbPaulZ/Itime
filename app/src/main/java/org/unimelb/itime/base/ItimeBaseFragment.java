package org.unimelb.itime.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.inputmethod.InputMethodManager;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Paul on 2/6/17.
 */

public abstract class ItimeBaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {

    private Fragment from;

    public ItimeBaseActivity getBaseActivity(){
        return (ItimeBaseActivity) getActivity();
    }

    public Fragment getFrom() {
        return from;
    }

    public void setFrom(Fragment from) {
        this.from = from;
    }

    @Override
    public void onResume() {
        super.onResume();
        getBaseActivity().setCurFragment(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    public void hideSoftKeyBoard(){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

}