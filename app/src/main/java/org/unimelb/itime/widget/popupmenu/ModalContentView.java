package org.unimelb.itime.widget.popupmenu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by Qiushuo Huang on 2017/5/22.
 */

public class ModalContentView extends FrameLayout {
    private View contentView;
    private int targetLeft;
    private int targetRight;
    private int targetBottom;
    private int targetTop;
    private int targetWidth;
    private int targetHeight;
    private int backgroundResource;
    private boolean hasTarget = false;
    private int MARGIN_LEFT = 100;
    private int MARGIN_RIGHT = 100;
    private int MARGIN_TOP = 100;
    private int MARGIN_BOTTOM = 100;


    public ModalContentView(Context context) {
        super(context);
        init();
    }

    public ModalContentView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ModalContentView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){

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

        int left, right, top, bottom;

        if(hasTarget) {
            if (targetLeft + contentWid > maxRight) {
                left = maxRight - contentWid;
                right = maxRight;
            } else {
                left = targetLeft;
                right = targetLeft + contentWid;
            }

            if (targetTop + contentHeight > maxBottom) {
                top = targetTop - contentHeight;
                bottom = targetTop;
            } else {
                top = targetBottom;
                bottom = targetBottom + contentHeight;
            }

            child.layout(left, top, right, bottom);
        }else{
            super.onLayout(b, i, i1, i2, i3);
        }
    }

    public void setContentView(View view){
        contentView = view;
        addView(contentView);
        LayoutParams params = (LayoutParams) view.getLayoutParams();
        params.gravity = Gravity.CENTER;
        contentView.setLayoutParams(params);
    }

    public void setTargetView(View view){
        if(view!=null) {
            targetBottom = view.getBottom();
            targetLeft = view.getLeft();
            targetRight = view.getRight();
            targetTop = view.getTop();
            targetWidth = view.getWidth();
            targetHeight = view.getHeight();
            hasTarget = true;
        }else{
            hasTarget = false;
        }
    }



    @Override
    public void setBackgroundResource(int backgroundResource) {
        this.backgroundResource = backgroundResource;
    }
}
