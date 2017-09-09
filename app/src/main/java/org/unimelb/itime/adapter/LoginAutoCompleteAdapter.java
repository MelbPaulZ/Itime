package org.unimelb.itime.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import java.util.List;

/**
 * Created by Paul on 30/8/17.
 */

public class LoginAutoCompleteAdapter<T> extends ArrayAdapter<T> {
    public LoginAutoCompleteAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<T> objects) {
        super(context, resource, objects);
    }


}
