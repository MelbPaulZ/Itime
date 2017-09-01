package org.unimelb.itime.util;

import android.content.Context;

import org.unimelb.itime.bean.Event;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/8/17.
 */

public class CoverPhotoUtil {
    private  static List<String> defaultPhotos = new ArrayList<>();
    private CoverPhotoUtil(){}

    public static List<String> getDefaultPhotos() {
        return defaultPhotos;
    }

    public static void setDefaultPhotos(List<String> defaultPhotos) {
        CoverPhotoUtil.defaultPhotos = defaultPhotos;
    }

    public static String getDefaultCoverPhoto(Event event){
        String cover = "";
        if(defaultPhotos.isEmpty()){
            return cover;
        }else {
            int hash = event.hashCode();
            int index = hash % defaultPhotos.size();
            return defaultPhotos.get(index);
        }

    }
}
