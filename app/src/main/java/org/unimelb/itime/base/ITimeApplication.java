package org.unimelb.itime.base;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVOSCloud;

import java.lang.reflect.Field;
import java.util.Locale;

/**
 * Created by yinchuandong on 18/08/2016.
 */
public class ITimeApplication extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        registerLeanCloud();
        changeLocale();
    }

    private void registerLeanCloud(){
        AVOSCloud.initialize(this, "Sk9FQYePVwHdXtXQKQuNfdpr-gzGzoHsz",
                "1PsfeF7pA1S5xI7EmEoQviwT");

        // 启用崩溃错误统计
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);
    }

    private void changeLocale(){
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();
        // 应用用户选择语言
        config.locale = Locale.CHINESE;
        resources.updateConfiguration(config, dm);
    }
}
