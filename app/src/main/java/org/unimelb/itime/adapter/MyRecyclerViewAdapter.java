package org.unimelb.itime.adapter;

import android.databinding.ViewDataBinding;
import android.support.annotation.LayoutRes;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import me.tatarka.bindingcollectionadapter2.BindingRecyclerViewAdapter;

/**
 * Created by Paul on 13/6/17.
 */

public class MyRecyclerViewAdapter<T> extends BindingRecyclerViewAdapter<T> {

    private String TAG = "MyRecyclerViewAdapter";
    
    @Override
    public ViewDataBinding onCreateBinding(LayoutInflater inflater, @LayoutRes int layoutId, ViewGroup viewGroup) {
        ViewDataBinding binding = super.onCreateBinding(inflater, layoutId, viewGroup);
        Log.d(TAG, "created binding: " + binding);
        return binding;
    }

    @Override
    public void onBindBinding(ViewDataBinding binding, int bindingVariable, @LayoutRes int layoutId, int position, T item) {
        super.onBindBinding(binding, bindingVariable, layoutId, position, item);
        Log.d(TAG, "bound binding: " + binding + " at position: " + position);
    }
}