package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import org.unimelb.itime.util.AppUtil;

import java.util.Locale;

/**
 * Created by David Liu on 21/10/17.
 * NowBoarding Ltd
 * lyhmelbourne@gmail.com
 */

public class SplashActivity extends AppCompatActivity{
    public final static String LOCALE_ZH = "zh";
    public final static String LOCALE_EN= "us";
    public final static String LOCALE_NONE = "none";
    public final static String LOCALE_KEY = "locale";

    public static Locale currentLocale = Locale.getDefault();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        changeLocale();

        Intent intent = new Intent(this, EmptyLoginActivity.class);
        startActivity(intent);
    }

    private void changeLocale(){
        SharedPreferences sharedPreferences = AppUtil.getSharedPreferences(this);
        String locale = sharedPreferences.getString(LOCALE_KEY, LOCALE_NONE);
        if (locale.equals(LOCALE_NONE)){
            return;
        }

        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        switch (locale){
            case LOCALE_ZH:
                currentLocale = Locale.CHINESE;
                config.locale = Locale.CHINESE;
                resources.updateConfiguration(config, dm);
                break;
            case LOCALE_EN:
                currentLocale = Locale.ENGLISH;
                config.locale = Locale.ENGLISH;
                resources.updateConfiguration(config, dm);
                break;
        }
    }

}
