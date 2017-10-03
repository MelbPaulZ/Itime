package org.unimelb.itime.ui.viewmodel.setting;

import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.SpinnerWrapper;
import org.unimelb.itime.ui.presenter.SettingPresenter;

import me.tatarka.bindingcollectionadapter2.ItemBinding;


/**
 * Created by Paul on 18/3/17.
 */

public class SettingLanguageViewModel extends BaseObservable {

    public final ObservableList<SpinnerWrapper> items = new ObservableArrayList<>();
    public final ItemBinding itemView = ItemBinding.of(BR.wrapper, R.layout.listview_setting_language);

    private SettingPresenter settingPresenter;
    public SettingLanguageViewModel(SettingPresenter settingPresenter) {
        this.settingPresenter = settingPresenter;
        init();
    }

    private void init(){
        items.add(new SpinnerWrapper("English", 2));
        items.add(new SpinnerWrapper("简体中文", 1));
        items.add(new SpinnerWrapper("繁体中文", 0));
    }

    public ListView.OnItemClickListener onChooseLanguage(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (SpinnerWrapper wrapper : items){
                    wrapper.setIsSelecte(0);
                }
                items.get(position).setIsSelecte(1);
            }
        };
    }
}
