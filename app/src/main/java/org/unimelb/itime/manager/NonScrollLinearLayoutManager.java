package org.unimelb.itime.manager;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by Paul on 2/6/17.
 */

public class NonScrollLinearLayoutManager extends LinearLayoutManager {
    public NonScrollLinearLayoutManager(Context context) {
        super(context);
    }

    @Override
    public boolean canScrollVertically() {
        return false;
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

    public static NonScrollLinearLayoutManager linear(Context context){
        return new NonScrollLinearLayoutManager(context);
    }
}
