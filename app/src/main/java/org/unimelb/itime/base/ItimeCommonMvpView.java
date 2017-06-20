package org.unimelb.itime.base;

import com.hannesdorfmann.mosby.mvp.MvpView;

/**
 * Created by Paul on 5/1/17.
 */

public interface ItimeCommonMvpView extends MvpView {
    void onBack();
    void onNext();
}
