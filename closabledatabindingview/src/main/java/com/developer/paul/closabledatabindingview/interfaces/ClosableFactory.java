package com.developer.paul.closabledatabindingview.interfaces;

import android.view.View;

import com.developer.paul.closabledatabindingview.closablelayouts.ClosableRelativeLayout;

/**
 * Created by Paul on 4/5/17.
 */

public interface ClosableFactory<T> {
    ClosableRelativeLayout create(T t);
    void updateClosableView(T t);

    void remove(T t);

    View.OnClickListener getCloseOnClickListener(T t);
    void setOnDeleteListener(View.OnClickListener onDeleteListener);
}
