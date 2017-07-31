package org.unimelb.itime.util;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Qiushuo Huang on 2017/7/31.
 */

public class FontUtil {
    private static Typeface sansLight;

    public static Typeface getSansLight(Context context){
        if(sansLight == null){
            sansLight = Typeface.createFromAsset(context.getResources().getAssets(),"fonts/sans_light.ttf");
        }
        return sansLight;
    }
}
