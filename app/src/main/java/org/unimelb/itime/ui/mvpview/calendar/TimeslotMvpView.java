package org.unimelb.itime.ui.mvpview.calendar;

import com.hannesdorfmann.mosby.mvp.MvpView;

import org.unimelb.itime.base.ToolbarInterface;

/**
 * Created by yuhaoliu on 8/06/2017.
 */

public interface TimeslotMvpView extends ToolbarInterface, MvpView{
    void onTimeslotSwitcherClick();
}
