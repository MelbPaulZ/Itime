package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.ui.mvpview.CalendarUniversityImportMvpView;
import org.unimelb.itime.ui.presenter.CalendarPresenter;

/**
 * Created by Paul on 9/2/17.
 */

public class CalendarImportUniversityViewModel extends BaseObservable{

    private CalendarPresenter<CalendarUniversityImportMvpView> presenter;
    private CalendarUniversityImportMvpView mvpView;
    private String importCalendarUserName, importCalendarPassword; // this has been changed to user name

    public CalendarImportUniversityViewModel(CalendarPresenter<CalendarUniversityImportMvpView> presenter) {
        this.presenter = presenter;
        this.mvpView = presenter.getView();

    }

    @Bindable
    public String getImportCalendarUserName() {
        return importCalendarUserName;
    }

    public void setImportCalendarUserName(String importCalendarUserName) {
        this.importCalendarUserName = importCalendarUserName;
        notifyPropertyChanged(BR.importCalendarUserName);
    }

    @Bindable
    public String getImportCalendarPassword() {
        return importCalendarPassword;
    }

    public void setImportCalendarPassword(String importCalendarPassword) {
        this.importCalendarPassword = importCalendarPassword;
        notifyPropertyChanged(BR.importCalendarPassword);
    }
}
