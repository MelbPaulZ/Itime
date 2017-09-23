package org.unimelb.itime.ui.viewmodel.setting;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import org.unimelb.itime.BR;
import org.unimelb.itime.ui.mvpview.TaskBasedMvpView;
import org.unimelb.itime.ui.presenter.SettingPresenter;

/**
 * Created by Qiushuo Huang on 2017/3/6.
 */

public class SettingReportViewModel extends BaseObservable {
    private boolean showSend = true;
    private String reportText = "";
    private SettingPresenter<TaskBasedMvpView> presenter;

    public SettingReportViewModel(SettingPresenter<TaskBasedMvpView> presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public boolean isShowSend() {
        return showSend;
    }

    public void setShowSend(boolean showSend) {
        this.showSend = showSend;
        notifyPropertyChanged(BR.showSend);
    }

    @Bindable
    public String getReportText() {
        return reportText;
    }

    public void setReportText(String reportText) {
        this.reportText = reportText;
        notifyPropertyChanged(BR.reportText);
    }

    public View.OnClickListener onSendClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.sendReport(reportText);
            }
        };
    }
}
