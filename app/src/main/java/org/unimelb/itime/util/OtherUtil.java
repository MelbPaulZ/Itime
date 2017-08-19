package org.unimelb.itime.util;

/**
 * Created by Paul on 22/3/17.
 */

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * OtherUtil is used for common methods which will leave this app
 */
public class OtherUtil {
    public static void openUrl(Context context, String url){
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        context.startActivity(browserIntent);
    }
}
