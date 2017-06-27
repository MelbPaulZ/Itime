package org.unimelb.itime.ui.fragment.meeting;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by yuhaoliu on 23/06/2017.
 */

public class CusSwipeViewPager extends ViewPager {
    // if swiping by finger
    private boolean swipeEnable = true;
    //milliseconds
    private int swipingDuration = 1000;

    public CusSwipeViewPager(Context context) {
        super(context);
        setUpScroller();
    }

    public CusSwipeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setUpScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return swipeEnable && super.onInterceptTouchEvent(ev);
    }

    public boolean isSwipeEnable() {
        return swipeEnable;
    }

    public void setSwipeEnable(boolean swipeEnable) {
        this.swipeEnable = swipeEnable;
    }

    public int getSwipingDuration() {
        return swipingDuration;
    }

    public void setSwipingDuration(int swipingDuration) {
        this.swipingDuration = swipingDuration;
    }

    private void setUpScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new CustomizedScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class CustomizedScroller extends Scroller {
        private CustomizedScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, swipingDuration);
        }
    }
}
