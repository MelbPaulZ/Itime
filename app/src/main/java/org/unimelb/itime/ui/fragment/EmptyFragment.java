package org.unimelb.itime.ui.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ItimeBaseMvpView;
import org.unimelb.itime.base.ItimeBasePresenter;

/**
 * Created by yuhaoliu on 13/06/2017.
 */

public class EmptyFragment extends ItimeBaseFragment<ItimeBaseMvpView, ItimeBasePresenter<ItimeBaseMvpView>> {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
    }

    @Override
    public ItimeBasePresenter<ItimeBaseMvpView> createPresenter() {
        return new ItimeBasePresenter<ItimeBaseMvpView>(getContext()) {
            @Override
            public Context getContext() {
                return super.getContext();
            }
        };
    }
}
