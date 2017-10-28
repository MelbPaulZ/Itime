package org.unimelb.itime.ui.viewmodel.setting;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.databinding.BaseObservable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ITimeApplication;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.bean.SpinnerWrapper;
import org.unimelb.itime.ui.activity.SplashActivity;
import org.unimelb.itime.ui.presenter.SettingPresenter;
import org.unimelb.itime.util.AppUtil;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

import static org.unimelb.itime.ui.activity.SplashActivity.LOCALE_EN;
import static org.unimelb.itime.ui.activity.SplashActivity.LOCALE_ZH;


/**
 * Created by Paul on 18/3/17.
 */

public class SettingLanguageViewModel extends BaseObservable {

    public final ObservableList<SpinnerWrapper> items = new ObservableArrayList<>();
    public final ItemBinding itemView = ItemBinding.of(BR.wrapper, R.layout.listview_setting_language);
    private Context context;
    private SettingPresenter settingPresenter;
    private String currentLanguage;
    public SettingLanguageViewModel(SettingPresenter settingPresenter) {
        this.settingPresenter = settingPresenter;
        this.context = settingPresenter.getContext();
        SharedPreferences sharedPreferences = AppUtil.getSharedPreferences(context);
        currentLanguage = sharedPreferences.getString(SplashActivity.LOCALE_KEY, SplashActivity.LOCALE_NONE);
        init();
    }

    private void init(){
        int selectedIndex = getDefaultIndex();
        items.add(new SpinnerWrapper("English", selectedIndex == 0 ? 1 : 0));
        items.add(new SpinnerWrapper("简体中文", selectedIndex == 1 ? 1 : 0));
    }

    private int getDefaultIndex(){
        switch (currentLanguage){
            case LOCALE_EN:
                return 0;
            case LOCALE_ZH:
                return 1;
        }

        return 0;
    }

    public ListView.OnItemClickListener onChooseLanguage(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == getDefaultIndex()){
                    return;
                }

                for (SpinnerWrapper wrapper : items){
                    wrapper.setIsSelecte(0);
                }
                items.get(position).setIsSelecte(1);

                SharedPreferences sharedPreferences = AppUtil.getSharedPreferences(context);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                switch (position){
                    case 0:
                        editor.putString(SplashActivity.LOCALE_KEY, LOCALE_EN);
                        break;
                    case 1:
                        editor.putString(SplashActivity.LOCALE_KEY, SplashActivity.LOCALE_ZH);
                        break;
                }
                editor.apply();

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage(R.string.language_changed_restart);
                builder1.setCancelable(false);
                builder1.setPositiveButton(
                        context.getString(R.string.dialog_restart),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                AppUtil.logOut(context);
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();
            }
        };
    }


}
