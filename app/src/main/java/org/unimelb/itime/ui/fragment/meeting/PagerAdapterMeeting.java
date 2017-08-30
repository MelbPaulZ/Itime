package org.unimelb.itime.ui.fragment.meeting;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuhaoliu on 22/06/2017.
 */

public class PagerAdapterMeeting extends FragmentStatePagerAdapter{
    private int mNumOfTabs;

    private List<Fragment> mData = new ArrayList<>();

    public PagerAdapterMeeting(FragmentManager fm, int mNumOfTabs) {
        super(fm);
        this.mNumOfTabs = mNumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        if (position < mData.size()){
            return mData.get(position);
        }else {
            throw new RuntimeException("Trying to get data out of bound.");
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }

    public List<Fragment> getmData() {
        return mData;
    }

    public void setmData(List<Fragment> mData) {
        this.mData = mData;
    }
}
