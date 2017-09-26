package org.unimelb.itime.ui.mvpview.contact;

import android.app.Activity;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public interface BlockContactsMvpView extends ItimeBaseMvpView,TaskBasedMvpView {
    Activity getActivity();
    void goToProfileFragment(Contact contact);
}
