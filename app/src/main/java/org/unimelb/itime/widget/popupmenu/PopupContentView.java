package org.unimelb.itime.widget.popupmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Qiushuo Huang on 2017/5/22.
 */

public class PopupContentView extends ViewGroup {
    private View contentView;
    private int targetLeft;
    private int targetRight;
    private int targetBottom;
    private int targetTop;
    private int targetWidth;
    private int targetHeight;
    public PopupContentView(Context context) {
        super(context);
    }

    public PopupContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PopupContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(MeasureSpec.UNSPECIFIED, heightMeasureSpec);
    }
    @Override
    protected void onLayout(boolean b, int i, int i1, int i2, int i3) {
        int maxRight = this.getRight();
        int maxLeft = this.getLeft();
        int maxTop = this.getTop();
        int maxBottom = this.getBottom();
        View child = getChildAt(0);
        int contentWid = child.getMeasuredWidth();
        int contentHeight = child.getMeasuredHeight();

        int left,right,top,bottom;

        if(targetLeft+contentWid>maxRight){
            left = maxRight - contentWid;
            right = maxRight;
        }else{
            left = targetLeft;
            right = targetLeft+contentWid;
        }

        if(targetTop+contentHeight>maxBottom){
            top = targetTop-contentHeight;
            bottom = targetTop;
        }else{
            top = targetBottom;
            bottom = targetBottom+contentHeight;
        }

        child.layout(left, top, right, bottom);
    }

    public void setContentView(View view){
        contentView = view;
        addView(contentView);
    }

    public void setTargetView(View view){
        targetBottom = view.getBottom();
        targetLeft = view.getLeft();
        targetRight = view.getRight();
        targetTop = view.getTop();
        targetWidth = view.getWidth();
        targetHeight = view.getHeight();
    }

}
