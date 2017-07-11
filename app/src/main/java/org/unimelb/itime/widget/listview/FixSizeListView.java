package org.unimelb.itime.widget.listview;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.unimelb.itime.R;

/**
 * Created by yuhaoliu on 10/7/17.
 */


@BindingMethods(
        {
                @BindingMethod(type = FixSizeListView.class, attribute = "fix_list:maxShownC", method="setMaxShownC"),
                @BindingMethod(type = FixSizeListView.class, attribute = "fix_list:scrollEnable", method="setScrollEnable")
        }
)
public class FixSizeListView extends ListView {
    private int maxShownC = -1;
    private boolean scrollEnable = false;

    public FixSizeListView(Context context) {
        super(context);
    }

    public FixSizeListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.loadAttributes(attrs, context);
    }

    public FixSizeListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.loadAttributes(attrs, context);
    }

    public void setMaxShownC(int maxShownC) {
        this.maxShownC = maxShownC;
    }

    public void setScrollEnable(boolean scrollEnable) {
        this.scrollEnable = scrollEnable;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if(!scrollEnable && ev.getAction()==MotionEvent.ACTION_MOVE)
            return true;

        return super.dispatchTouchEvent(ev);
    }

    private void loadAttributes(AttributeSet attrs, Context context) {
        if (attrs != null && context != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.FixSizeListView, 0, 0);
            try {
                maxShownC = typedArray.getInteger(R.styleable.FixSizeListView_max_show_c, maxShownC);
                scrollEnable = typedArray.getBoolean(R.styleable.FixSizeListView_scroll_enable, scrollEnable);
            } finally {
                typedArray.recycle();
            }
        }
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int totalHeight = Integer.MAX_VALUE >> 2;

        if (maxShownC != -1){
            int childCount = getAdapter().getCount();
            if (childCount >= maxShownC){
                //reset height
                totalHeight = 0;

                for (int i = 0; i < maxShownC; i++) {
                    View child = getAdapter().getView(i,null,this);
                    if (child != null){
                        child.measure(0,0);
                        totalHeight += child.getMeasuredHeight();
                    }
                }
            }
        }

        int heightMeasureSpec_custom = MeasureSpec.makeMeasureSpec(
                totalHeight, MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec_custom);
        ViewGroup.LayoutParams params = getLayoutParams();
        params.height = getMeasuredHeight();
    }
}
