package org.unimelb.itime.widget;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.unimelb.itime.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/5/2.
 */
@BindingMethods({
        @BindingMethod(type = ScalableLayout.class,
                attribute = "app:scalablelayout_expandheight",
                method = "setExpandHeight"),
        @BindingMethod(type = ScalableLayout.class,
                attribute = "app:scalablelayout_hideheight",
                method = "setHideHeight"),
        @BindingMethod(type = ScalableLayout.class,
                attribute = "app:scalablelayout_collapseheight",
                method = "setCollapseHeight"),
        @BindingMethod(type = ScalableLayout.class,
                attribute = "app:scalablelayout_onStatusChange",
                method = "setOnStatusChangeListener"),
        @BindingMethod(type = ScalableLayout.class,
                attribute = "app:scalablelayout_status",
                method = "setStatus")
})
public class ScalableLayout extends LinearLayout{
    public static final int STATUS_COLLAPSE = 0;
    public static final int STATUS_EXPAND = 1;
    public static final int STATUS_HIDE = 2;
    public static final int STATUS_SCROLL = 3;
    private int currentStatus = STATUS_COLLAPSE;
    private int lastX;
    private int lastY;
    private int lastRawY;
    private int lastXIntercept = 0;
    private int lastYIntercept = 0;
    private boolean scrolling = false;
    private int lastOffset;
    private int expandHeight = 1000;
    private int collapseHeight = 400;
    private int hideHeight = 80;
    private static final int velocity = 5;
    private int moveCount;
    private View recyclerView;
    private boolean firstMeasure = true;


    private static final int AUTO_THRESHOLD = 50;
    private int moveStart;
    private int moveCurrent;
    private OnStatusChangeListener onStatusChangeListener;
    int windowHeight = getResources().getDisplayMetrics().heightPixels;


    public ScalableLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        doAttrs(context, attrs, defStyleAttr);
        init();
    }

    public ScalableLayout(Context context) {
        super(context);
        init();
    }

    public ScalableLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        doAttrs(context, attrs, 0);
        init();
    }

    private void doAttrs(Context context, AttributeSet attrs, int defStyleAttr){
        TypedArray arr = context.getTheme().obtainStyledAttributes(attrs, R.styleable.ScalableLayout, defStyleAttr, 0);
        try {
            hideHeight =  arr.getDimensionPixelSize(R.styleable.ScalableLayout_scalablelayout_hideheight, hideHeight);
            collapseHeight = arr.getDimensionPixelSize(R.styleable.ScalableLayout_scalablelayout_collapseheight, collapseHeight);
            expandHeight = arr.getDimensionPixelSize(R.styleable.ScalableLayout_scalablelayout_expandheight, expandHeight);
            Log.e("s","");
        } finally {
            arr.recycle();
        }
    }

    private void init(){
        setOrientation(LinearLayout.VERTICAL);
    }

    private void measureBody(){
        View bodyView = getRecyclerView();
        bodyView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        int height = bodyView.getMeasuredHeight();
        Log.e("measuredheight", height+"");
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        int x = (int)event.getRawX();
        int y = (int)event.getRawY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                intercepted = false;
                moveCount = 0;
                lastRawY = y;
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                int deltaX = x - lastXIntercept;
                int deltaY = y - lastYIntercept;
                scrolling = true;
                float v = y-lastRawY;

                if (Math.abs(deltaY)>= Math.abs(deltaX) && Math.abs(v)>10) {
                    if(currentStatus == STATUS_EXPAND) {
                         if(deltaY<0 || ViewCompat.canScrollVertically(getRecyclerView(), -1)) {
                            intercepted = false;
                        }else{
                             intercepted = true;
                         }
                    }
                    else{
                        intercepted = true;
                    }
                }else {
                    intercepted = false;
                }
                break;
            }
            case MotionEvent.ACTION_UP: {
                intercepted = false;
                break;
            }
            default:
                break;
        }
        lastXIntercept = x;
        lastYIntercept = y;
        return intercepted;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public View getRecyclerView(){
        if(recyclerView == null){
            int count = getChildCount();
            for(int i = 0;i<count;i++){
                View child = getChildAt(i);
                if(child instanceof ListView){
                    recyclerView =  child;
                    break;
                }
            }
        }
        return recyclerView;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int y = (int) event.getRawY();

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
//                Log.e("down", "down");
                break;

            case MotionEvent.ACTION_MOVE:
                moveCurrent = y;
//                Log.e("y", lastRawY+":"+y);
                doMove((lastRawY -y));
//                Log.e("move", "move");
                break;

            case MotionEvent.ACTION_UP:
                if(currentStatus==STATUS_SCROLL) {
                    doUp();
                }
//                Log.e("up", "up");
                break;
        }
        lastRawY = y;
        return true;
    }

    private void doMove(int y){
        //处于collapse状态
//        Log.e("layout", y+"");
        moveCount+=y;
        setInternalStatus(STATUS_SCROLL);
        int height = getHeight();
        int scroll = (int) getTranslationY();
        if(height>collapseHeight){
            scale(y);
        }else if(height == collapseHeight && y>0 && scroll==0){
            scale(y);
        }else if(height == collapseHeight && y<0 && scroll==0){
            scroll(y);
        }else if(scroll>0){
            scroll(y);
        }
    }

    private void scale(int y){
        int h = getHeight()+y;
        if(h>= expandHeight){
            getLayoutParams().height = expandHeight;
        } else if(h>collapseHeight) {
            getLayoutParams().height += y;
        } else if (h<collapseHeight){
            getLayoutParams().height = collapseHeight;
            scroll(h-collapseHeight);
        } else if(h == collapseHeight) {
            getLayoutParams().height = collapseHeight;
        }
        requestLayout();
    }

    private void scroll(int y) {
        y = -y;
        int h = (int) (getTranslationY() + y);

        if(h>=collapseHeight-hideHeight){
            setTranslationY(collapseHeight-hideHeight);
        } else if(h>=0) {
            setTranslationY(getTranslationY()+y);
        } else if (h<0){
            setTranslationY(0);
            scale(-h);
        }
    }

    public void setInternalStatus(int status){
        int old = currentStatus;
        currentStatus = status;
        if(onStatusChangeListener!=null){
            onStatusChangeListener.onStatusChange(this, old, currentStatus);
        }
    }

    private void doUp(){
        //如果不是hide
        if(getLayoutParams().height>collapseHeight){
            autoScale();
        }
        //如果是hide
        else{
            autoTransition();
        }
    }

    private void autoTransition() {
        int move = moveCount;
        if(move>0){
            if(move>=AUTO_THRESHOLD){
                getShowAnimator(false).start();
            } else{
                getHideAnimator(false).start();
            }
        }else{
            if(-move>=AUTO_THRESHOLD){
                getHideAnimator(false).start();
            }else{
                getShowAnimator(false).start();
            }
        }
    }

    private void autoScale(){
        int move = moveCount;
        if(move>0){
            if(move>=AUTO_THRESHOLD){
                getExpandAnimator(false).start();
            } else{
                getCollapseAnimator(false).start();
            }
        }else{
            if(-move>=AUTO_THRESHOLD){
                getCollapseAnimator(false).start();
            }else{
                getExpandAnimator(false).start();
            }
        }
    }

    private ObjectAnimator getExpandAnimator(boolean immediately){
        int start = getLayoutParams().height;
        int end = expandHeight;
        int time = (end-start)>0?(end-start)/ velocity:0;
        ObjectAnimator animator =  ObjectAnimator.ofInt(this, "height", start, end);
        if(immediately){
            animator.setDuration(0);
        }else{
            animator.setDuration(time);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setInternalStatus(STATUS_SCROLL);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setInternalStatus(STATUS_EXPAND);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return animator;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if(firstMeasure) {
            firstMeasure = false;
            setOffsets();
            if(currentStatus==STATUS_HIDE){
                setTranslationY(collapseHeight-hideHeight);
            }
        }
    }

    private void setOffsets(){
        collapseHeight = Math.min(this.getMeasuredHeight(), collapseHeight);
        expandHeight = Math.min(this.getMeasuredHeight(), expandHeight);
//        hideHeight = Math.min(getChildAt(0).getMeasuredHeight(), hideHeight);
        setHeight(collapseHeight);
    }

    public void refreshLayout(){

        this.requestLayout();
    }

    private ObjectAnimator getCollapseAnimator(boolean immediately){
        int start = getLayoutParams().height;
        int end = collapseHeight;
        int time = (start-end)>0?(start-end)/ velocity:0;
        ObjectAnimator animator =  ObjectAnimator.ofInt(this, "height", start, end);
        if(immediately){
            animator.setDuration(0);
        }else{
            animator.setDuration(time);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setInternalStatus(STATUS_SCROLL);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setInternalStatus(STATUS_COLLAPSE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return animator;
    }

    private ObjectAnimator getShowAnimator(boolean immediately){
        int start = (int) getTranslationY();
        int end = 0;
        int time = (start-end)>0?(start-end)/ velocity:0;
        ObjectAnimator animator = ObjectAnimator.ofInt(this, "scroll", start, end);
        if(immediately){
            animator.setDuration(0);
        }else{
            animator.setDuration(time);
        }
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setInternalStatus(STATUS_SCROLL);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setInternalStatus(STATUS_COLLAPSE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return animator;
    }

    private ObjectAnimator getHideAnimator(boolean immediately){
        int start = (int)getTranslationY();
        int end = collapseHeight-hideHeight;
        int time = Math.max((end-start)/ velocity, 0);

        ObjectAnimator animator = ObjectAnimator.ofFloat(this, "translationY", start, end);
        if(immediately){
            animator.setDuration(1);
        }else{
            animator.setDuration(time);
        }

        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setInternalStatus(STATUS_SCROLL);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setInternalStatus(STATUS_HIDE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });

        return animator;
    }

    public void setScroll(int y){
        setTranslationY(y);
    }

    public void setHeight(int height){
        getLayoutParams().height = height;
        this.requestLayout();
    }

    public void expand(boolean immediately){
        switch (currentStatus){
            case STATUS_EXPAND:
                break;
            case STATUS_COLLAPSE:
                getExpandAnimator(immediately).start();
                break;
            case STATUS_HIDE:
                getHideToExpandAnimator(immediately).start();
                break;
        }
    }

    private ObjectAnimator getHideToExpandAnimator(boolean immediately){
        lastOffset = 0;
        ObjectAnimator hideToExpand = ObjectAnimator.ofInt(this, "hideToExpand", 0, expandHeight -hideHeight);
        if(immediately){
            hideToExpand.setDuration(0);
        }else{
            hideToExpand.setDuration((expandHeight -hideHeight)/ velocity);
        }
        hideToExpand.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setInternalStatus(STATUS_SCROLL);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setInternalStatus(STATUS_EXPAND);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return hideToExpand;
    }

    private ObjectAnimator getExpandToHideAnimator(boolean immediately){
        lastOffset = 0;
        ObjectAnimator expandToHide = ObjectAnimator.ofInt(this, "expandToHide", 0, expandHeight -hideHeight);
        if(immediately){
            expandToHide.setDuration(0);
        }else{
            expandToHide.setDuration((expandHeight -hideHeight)/ velocity);
        }
        expandToHide.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                setInternalStatus(STATUS_SCROLL);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                setInternalStatus(STATUS_HIDE);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        return expandToHide;
    }

    private void setExpandToHide(int y){
        y = -y;
        int height = getHeight();
        int scroll = (int) getTranslationY();
        if(height>=collapseHeight && scroll==0){
            scale(y-lastOffset);
        }else{
            scroll(y-lastOffset);
        }
        lastOffset = y;
//        Log.e("offset", lastOffset+":"+y);
    }

    private void setHideToExpand (int offset) {
        int height = getHeight();
        int scroll = (int) getTranslationY();
        int y = offset-lastOffset;
        if(scroll+y>=0){
            scroll(y);
        }else if(scroll>0 && scroll+y<0){
            scroll(0-scroll);
            scale(y+scroll);
        }else{
            scale(y);
        }
        lastOffset = offset;
    }


    public void collapse(boolean immediately){
        switch (currentStatus){
            case STATUS_COLLAPSE:
                break;
            case STATUS_EXPAND:
                getCollapseAnimator(immediately).start();
                break;
            case STATUS_HIDE:
                getShowAnimator(immediately).start();
                break;

        }
    }

    public void hide(boolean immediately){
        switch (currentStatus){
            case STATUS_HIDE:
                break;
            case STATUS_COLLAPSE:
                getHideAnimator(immediately).start();
                break;
            case STATUS_EXPAND:
                getExpandToHideAnimator(immediately).start();
                break;
        }
    }

    public int getStatus(){
        return currentStatus;
    }

    public void toggle(){
        if(currentStatus!=STATUS_HIDE){
            hide(false);
        }else {
            collapse(false);
        }
    }

    public interface OnStatusChangeListener{
        void onStatusChange(ScalableLayout layout, int oldStatus, int newStatus);
    }

    public int getExpandHeight() {
        return expandHeight;
    }

    public void setExpandHeight(int expandHeight) {
        this.expandHeight = expandHeight;
    }

    public int getCollapseHeight() {
        return collapseHeight;
    }

    public void setCollapseHeight(int collapseHeight) {
        this.collapseHeight = collapseHeight;
    }

    public int getHideHeight() {
        return hideHeight;
    }

    public void setHideHeight(int hideHeight) {
        this.hideHeight = hideHeight;
        Log.e("hideHeight",hideHeight+"");
    }

    public OnStatusChangeListener getOnStatusChangeListener() {
        return onStatusChangeListener;
    }

    public void setOnStatusChangeListener(OnStatusChangeListener listener){
        onStatusChangeListener = listener;
    }

    public void setStatus(int status){
        if(status==currentStatus || this.getVisibility()!=VISIBLE){
            return;
        }

        switch (status){
            case STATUS_COLLAPSE:
                collapse(false);
                break;
            case STATUS_HIDE:
                hide(false);
                break;
            case STATUS_EXPAND:
                expand(false);
                break;
        }
    }

    public void setInitStatus(int status){
        this.currentStatus = status;
//        if(status==currentStatus || this.getVisibility()!=VISIBLE){
//            return;
//        }
//
//        switch (status){
//            case STATUS_COLLAPSE:
//                collapse(true);
//                break;
//            case STATUS_HIDE:
//                hide(true);
//                break;
//            case STATUS_EXPAND:
//                expand(true);
//                break;
//        }
    }

    public void notifyRemeasure(){

    }
}
