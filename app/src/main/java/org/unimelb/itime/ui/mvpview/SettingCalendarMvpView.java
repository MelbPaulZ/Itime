package org.unimelb.itime.ui.mvpview;

import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.bean.Calendar;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;

/**
 * Created by yinchuandong on 11/1/17.
 */

public interface SettingCalendarMvpView extends TaskBasedMvpView<Calendar>, ItimeBaseMvpView{
    void toAddCalendar();
    void toEditCalendar(Calendar calendar);
    void toDeleteCalendar(Calendar calendar);
}
