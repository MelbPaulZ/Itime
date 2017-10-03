package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Calendar;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface CalendarImportMvpView extends TaskBasedMvpView<Calendar>, ItimeBaseMvpView{
    void toGoogleCal();
    void toUnimebCal();
}
