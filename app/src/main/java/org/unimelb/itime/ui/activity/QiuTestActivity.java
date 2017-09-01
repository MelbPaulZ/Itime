package org.unimelb.itime.ui.activity;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.bean.Invitee;
import org.unimelb.itime.bean.Location;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.bean.TimeSlot;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.event.FragmentEventDetail;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Qiushuo Huang on 2017/6/18.
 */

public class QiuTestActivity extends ItimeBaseActivity{
    public static String EVENT = "event";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_create);

        FragmentEventDetail fragment = new FragmentEventDetail();
        Event event = new Event();
        event.getStart().setDateTime("2017-05-23T12:25:00+10:00");
        event.getEnd().setDateTime("2017-05-23T13:25:00+10:00");
        List<PhotoUrl> photoUrls = new ArrayList<>();
        PhotoUrl p1 = new PhotoUrl();
        p1.setUrl("http://itime-1254199931.image.myqcloud.com/coverphoto_1.png");
        photoUrls.add(p1);
        PhotoUrl p2 = new PhotoUrl();
        p2.setUrl("http://itime-1254199931.image.myqcloud.com/coverphoto_2.png");
        photoUrls.add(p2);
        PhotoUrl p3 = new PhotoUrl();
        p3.setUrl("http://itime-1254199931.image.myqcloud.com/coverphoto_3.png");
        photoUrls.add(p3);
        PhotoUrl p4 = new PhotoUrl();
        p4.setUrl("http://itime-1254199931.image.myqcloud.com/coverphoto_4.png");
        photoUrls.add(p4);
        PhotoUrl p5 = new PhotoUrl();
        p5.setUrl("http://itime-1254199931.image.myqcloud.com/coverphoto_5.png");
        photoUrls.add(p5);
        PhotoUrl p6 = new PhotoUrl();
        p6.setUrl("http://itime-1254199931.image.myqcloud.com/coverphoto_6.png");
        photoUrls.add(p6);
        event.setPhoto(photoUrls);
        if(event!=null) {
            fragment.setData(event);
        }else {
            throw new RuntimeException("Event can't be null");
        }
        getSupportFragmentManager().beginTransaction().replace(R.id.frag_container_event_create,fragment, FragmentEventCreate.class.getSimpleName()).commit();
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.frag_container_event_create;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }
}
