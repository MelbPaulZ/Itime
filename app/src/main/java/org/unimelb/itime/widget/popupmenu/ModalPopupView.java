package org.unimelb.itime.widget.popupmenu;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

/**
 * Created by Qiushuo Huang on 2017/5/25.
 */

public class ModalPopupView {
    private boolean isModal = false;
    private ModalContentView popupContentView;
    private Context context;
    private PopupWindow popupWindow;
    private View contentView;
    private static final int DURATION = 200;
    private AnimatorSet showAnimation;
    private AnimatorSet hideAnimation;

    private void generateAnimator(View view){
        ObjectAnimator showXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f);
        ObjectAnimator showYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f);
        showAnimation.play(showXAnimator);
        showAnimation.play(showYAnimator);

        ObjectAnimator hideXAnimator = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0f);
        ObjectAnimator hideYAnimator = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0f);
        hideAnimation.play(hideXAnimator);
        hideAnimation.play(hideYAnimator);

        hideAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                popupWindow.dismiss();
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private void hide(){
        hideAnimation.start();
    }

    public ModalPopupView(Context context){
        this.context = context;
        showAnimation = new AnimatorSet().setDuration(DURATION);
        hideAnimation = new AnimatorSet().setDuration(DURATION);

        popupContentView = new ModalContentView(context);
        popupContentView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(motionEvent.getAction() == MotionEvent.ACTION_UP){
                    if(!isIn(contentView, motionEvent)){
                        hide();
                    }
                }
                return true;
            }
        });
    }

    public void showAtLocation(View target, int x, int y){
        popupContentView.setTargetView(target);
        popupWindow = new PopupWindow(popupContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(target, Gravity.TOP,0,0);
        showAnimation.start();
    }

    public void show(View parent){
        popupWindow = new PopupWindow(popupContentView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setFocusable(true);
        popupWindow.setTouchable(true);
        popupWindow.showAtLocation(parent, Gravity.TOP,0,0);
        showAnimation.start();
    }

    public ModalPopupView setContentView(View contentView){
        popupContentView.setContentView(contentView);
        this.contentView = contentView;
        generateAnimator(this.contentView);
        return this;
    }

    public ModalPopupView setBackground(Drawable background){
        popupContentView.setBackground(background);
        return this;
    }

    private boolean isIn(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        RectF rectF =  new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());

        return rectF.contains(ev.getRawX(), ev.getRawY());
    }

    public void dismiss() {
        popupWindow.dismiss();
    }
}
