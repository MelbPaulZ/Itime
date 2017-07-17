package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

/**
 * Created by yuhaoliu on 13/7/17.
 */

public interface MeetingMvpView extends ItimeBaseMvpView {
    void onLoadResult(MeetingPresenter.FilterResult result);
}
