package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public interface EditContactMvpView extends TaskBasedMvpView {
    Activity getActivity();
    void showAlert();
}
