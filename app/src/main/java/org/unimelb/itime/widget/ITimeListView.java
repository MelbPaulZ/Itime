package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by Qiushuo Huang on 2017/2/13.
 */

public class ITimeListView extends ListView {
    public ITimeListView(Context context) {
        super(context);
        init();
    }

    public ITimeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ITimeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        setVerticalScrollBarEnabled(false);
    }
}
