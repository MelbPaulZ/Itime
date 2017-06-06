package org.unimelb.itime.util;

import android.content.Context;

/**
 * Created by Paul on 6/6/17.
 */

public class SizeUtil {
    public static int dip2px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
