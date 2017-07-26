package org.unimelb.itime.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseActivity;
import org.unimelb.itime.ui.fragment.event.FragmentEventCreate;
import org.unimelb.itime.ui.fragment.meeting.FragmentArchive;
import org.unimelb.itime.ui.presenter.MeetingPresenter;

/**
 * Created by Qiushuo Huang on 2017/6/21.
 */

public class ArchiveActivity extends ItimeBaseActivity {
    public final static String ARCHIVE_RECEIVE_RESULT = "0";
    public final static String ARCHIVE_BACK_RESULT = "1";
    public final static int ARCHIVE_BACK_RESULT_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        MeetingPresenter.FilterResult result = (MeetingPresenter.FilterResult) intent.getSerializableExtra(ARCHIVE_RECEIVE_RESULT);

        FragmentArchive fragment = new FragmentArchive();
        fragment.setData(result);
        getSupportFragmentManager().beginTransaction().add(R.id.archive_container,fragment, FragmentEventCreate.class.getSimpleName()).commit();

        setContentView(R.layout.activity_archive);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.archive_container;
    }

    @NonNull
    @Override
    public MvpPresenter createPresenter() {
        return new MvpBasePresenter();
    }
}
