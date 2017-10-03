package org.unimelb.itime.util;

import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import java.util.ArrayList;

/**
 * Created by Qiushuo Huang on 2017/2/9.
 */

public class PermissionUtil {
    static ArrayList<String> permissionList;

    public static String[] needPermissions(Context context, String... permisisons){
        permissionList = new ArrayList<>();
        for (String permission: permisisons){
            if (ActivityCompat.checkSelfPermission(context,permission)!= PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }
        return permissionList.toArray(new String[permissionList.size()]);
    }

    public static boolean allPermissionGranted(int[] grantResults) {
        int size = grantResults.length;
        for (int i = 0; i < size; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
