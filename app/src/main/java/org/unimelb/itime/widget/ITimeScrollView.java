package org.unimelb.itime.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

/**
 * Created by Qiushuo Huang on 2017/2/13.
 */

public class ITimeScrollView extends ScrollView{

    public ITimeScrollView(Context context) {
        super(context);
        init();
    }

    public ITimeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public ITimeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setVerticalScrollBarEnabled(false);
    }
}
