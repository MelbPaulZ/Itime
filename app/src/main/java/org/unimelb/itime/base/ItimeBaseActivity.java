package org.unimelb.itime.base;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.R;

/**
 * Created by Paul on 6/6/17.
 */

public abstract class ItimeBaseActivity<V extends MvpView, P extends MvpBasePresenter<V>> extends MvpActivity<V, P> {

    FragmentManager fragmentManager;
    private Fragment curFragment;


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

        if (fragment instanceof ItimeBaseFragment){
            ((ItimeBaseFragment)fragment).setFrom(curFragment);
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

    public void setCurFragment(Fragment curFragment) {
        this.curFragment = curFragment;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    protected abstract int getFragmentContainerId();
}
