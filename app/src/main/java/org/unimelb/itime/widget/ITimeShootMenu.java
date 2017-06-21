package org.unimelb.itime.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import org.unimelb.itime.R;

import java.util.ArrayList;
import java.util.List;

public class ITimeShootMenu extends ViewGroup {
    public static final int EXPAND_UP = 0;
    public static final int EXPAND_DOWN = 1;
    public static final int EXPAND_LEFT = 2;
    public static final int EXPAND_RIGHT = 3;

    public static final int LABELS_ON_LEFT_SIDE = 0;
    public static final int LABELS_ON_RIGHT_SIDE = 1;
    private static final int ANIMATION_DURATION = 300;
    private static final int COLLAPSE_ALPHA = 0;
    private static final int EXPAND_ALPHA = 255;
    private static final float COLLAPSED_PLUS_ROTATION = 0f;
    private static final float EXPANDED_PLUS_ROTATION = 90f + 45f;

    private AnimatorSet mExpandAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private AnimatorSet mCollapseAnimation = new AnimatorSet().setDuration(ANIMATION_DURATION);
    private RotatingDrawable mRotatingDrawable;

    private int mExpandDirection;
    private int currentColor;

    private int mButtonSpacing;
    private int mButtonSize;
    private int collapseIcon;
    private int expandIcon;
    private int mButtonBottomMargin;
    private int collapseColor = Color.RED;
    private int expandColor = Color.BLACK;
    private Drawable background;
    private Drawable transparent;

    private boolean mExpanded;
    private OnItemClickListener onItemClickListener;


    private View mAddButton;
    private int mMaxButtonWidth;
    private int mMaxButtonHeight;
    private int mButtonsCount;
    private List<Item> items = new ArrayList<>();
    private List<View> itemViews = new ArrayList<>();

    private OnFloatingActionsMenuUpdateListener mListener;

    public interface OnFloatingActionsMenuUpdateListener {
        void onMenuExpanded();
        void onMenuCollapsed();
    }

    public ITimeShootMenu(Context context) {
        this(context, null);
    }

    public ITimeShootMenu(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ITimeShootMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {

        mButtonSpacing = (int) (getResources().getDimension(R.dimen.fab_actions_spacing));

        TypedArray attr = context.obtainStyledAttributes(attributeSet, R.styleable.ITimeShootMenu, 0, 0);
        mExpandDirection = attr.getInt(R.styleable.ITimeShootMenu_shoot_expandDirection, EXPAND_UP);
        mButtonSpacing = attr.getDimensionPixelSize(R.styleable.ITimeShootMenu_shoot_itemSpacing, mButtonSpacing);
        mButtonSize = attr.getDimensionPixelSize(R.styleable.ITimeShootMenu_shoot_buttonSize, 50);
        collapseIcon = attr.getResourceId(R.styleable.ITimeShootMenu_shoot_collapseIcon, 0);
        expandIcon = attr.getResourceId(R.styleable.ITimeShootMenu_shoot_expandIcon, collapseIcon);
        mButtonBottomMargin = attr.getDimensionPixelSize(R.styleable.ITimeShootMenu_shoot_buttonBottomMargin, 0);
        attr.recycle();

        initAddButton();
        background = getBackground();
        transparent = new ColorDrawable(Color.parseColor("#00000000"));
        setBackground(transparent);

        mCollapseAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                if(mListener!=null){
                    mListener.onMenuCollapsed();
                }
            }

            @Override
            public void onAnimationEnd(Animator animator) {

            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //                if(!isExpanded()){
//                    return false;
//                }

        if(isExpanded()) {
            if (isIn(ITimeShootMenu.this, event)) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    for (int i = 0; i < itemViews.size(); i++) {
                        View v = itemViews.get(i);
                        if (isIn(v, event)) {
                            if (onItemClickListener != null)
                                onItemClickListener.onItemClick(i, items.get(i));
                            break;
                        }
                    }
                    collapse();
                }
                return true;
            }
        }
        return false;
    }

    private boolean isIn(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        RectF rectF =  new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());

        return rectF.contains(ev.getRawX(), ev.getRawY());
    }

    // 设置监听
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



    public void setOnFloatingActionsMenuUpdateListener(OnFloatingActionsMenuUpdateListener listener) {
        mListener = listener;
    }

    private boolean expandsHorizontally() {
        return mExpandDirection == EXPAND_LEFT || mExpandDirection == EXPAND_RIGHT;
    }

    public static class RotatingDrawable extends LayerDrawable {
        private Drawable collapseDrawable;
        private Drawable expandDrawable;
        public RotatingDrawable(Drawable collapseDrawable, Drawable expandDrawable) {
            super(new Drawable[] { collapseDrawable, expandDrawable });
            this.collapseDrawable = collapseDrawable;
            this.expandDrawable = expandDrawable;
        }

        private float mRotation;
        private int color;

        @SuppressWarnings("UnusedDeclaration")
        public float getRotation() {
            return mRotation;
        }

        @SuppressWarnings("UnusedDeclaration")
        public void setRotation(float rotation) {
            mRotation = rotation;
            invalidateSelf();
        }

        public void setColor(int color){
            this.color = color;
            ColorDrawable drawable = (ColorDrawable) getDrawable(0);
            drawable.setColor(color);
        }

        public void setAlpha(int alpha){
//            collapseDrawable.setAlpha(alpha);
            expandDrawable.setAlpha(alpha);
        }

        @Override
        public void draw(Canvas canvas) {
            canvas.save();
            canvas.rotate(mRotation, getBounds().centerX(), getBounds().centerY());
            super.draw(canvas);
            canvas.restore();
        }
    }

    public void addItem(View button) {
        addView(button, mButtonsCount - 1);
        itemViews.add(button);
        mButtonsCount++;
    }

    public void removeAllItem() {
        for(View view:itemViews){
            removeView(view);
            mButtonsCount--;
        }
        itemViews.clear();
    }

    private int getColor(@ColorRes int id) {
        return getResources().getColor(id);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        measureChildren(widthMeasureSpec, heightMeasureSpec);

        int width = 0;
        int height = 0;

        mMaxButtonWidth = 0;
        mMaxButtonHeight = 0;
        int maxLabelWidth = 0;

        for (int i = 0; i < mButtonsCount; i++) {
            View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            switch (mExpandDirection) {
                case EXPAND_UP:
                case EXPAND_DOWN:
                    mMaxButtonWidth = Math.max(mMaxButtonWidth, child.getMeasuredWidth());
                    height += child.getMeasuredHeight();
                    break;
                case EXPAND_LEFT:
                case EXPAND_RIGHT:
                    width += child.getMeasuredWidth();
                    mMaxButtonHeight = Math.max(mMaxButtonHeight, child.getMeasuredHeight());
                    break;
            }
        }

        if (!expandsHorizontally()) {
            width = mMaxButtonWidth + (maxLabelWidth > 0 ? maxLabelWidth : 0);
        } else {
            height = mMaxButtonHeight;
        }

        switch (mExpandDirection) {
            case EXPAND_UP:
            case EXPAND_DOWN:
                height += mButtonSpacing * (mButtonsCount - 1);
                height = adjustForOvershoot(height);
                break;
            case EXPAND_LEFT:
            case EXPAND_RIGHT:
                width += mButtonSpacing * (mButtonsCount - 1);
                width = adjustForOvershoot(width);
                break;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        setMeasuredDimension(width, height);
    }

    private int adjustForOvershoot(int dimension) {
        return dimension * 12 / 10;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        switch (mExpandDirection) {
            case EXPAND_UP:
            case EXPAND_DOWN:
                boolean expandUp = mExpandDirection == EXPAND_UP;

                int addButtonY = expandUp ? b - t - mAddButton.getMeasuredHeight()-mButtonBottomMargin : 0;
                // Ensure mAddButton is centered on the line where the buttons should be
                int buttonsHorizontalCenter = (r-l)/2;
                int addButtonLeft = buttonsHorizontalCenter - mAddButton.getMeasuredWidth() / 2;
                mAddButton.layout(addButtonLeft, addButtonY, addButtonLeft + mAddButton.getMeasuredWidth(), addButtonY + mAddButton.getMeasuredHeight());

                int nextY = expandUp ?
                        addButtonY - mButtonSpacing :
                        addButtonY + mAddButton.getMeasuredHeight() + mButtonSpacing;

                for (int i = mButtonsCount - 1; i >= 0; i--) {
                    final View child = getChildAt(i);

                    if (child == mAddButton || child.getVisibility() == GONE) continue;

                    int childX = buttonsHorizontalCenter - child.getMeasuredWidth() / 2;
                    int childY = expandUp ? nextY - child.getMeasuredHeight() : nextY;
                    child.layout(childX, childY, childX + child.getMeasuredWidth(), childY + child.getMeasuredHeight());

                    float collapsedTranslation = addButtonY - childY;
                    float expandedTranslation = 0f;

                    child.setTranslationY(mExpanded ? expandedTranslation : collapsedTranslation);
                    child.setAlpha(mExpanded ? 1f : 0f);

                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
                    params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
                    params.setAnimationsTarget(child);

                    nextY = expandUp ?
                            childY - mButtonSpacing :
                            childY + child.getMeasuredHeight() + mButtonSpacing;
                }
                break;

            case EXPAND_LEFT:
            case EXPAND_RIGHT:
                boolean expandLeft = mExpandDirection == EXPAND_LEFT;

                int addButtonX = expandLeft ? r - l - mAddButton.getMeasuredWidth() : 0;
                // Ensure mAddButton is centered on the line where the buttons should be
                int addButtonTop = b - t - mMaxButtonHeight + (mMaxButtonHeight - mAddButton.getMeasuredHeight()) / 2;
                mAddButton.layout(addButtonX, addButtonTop, addButtonX + mAddButton.getMeasuredWidth(), addButtonTop + mAddButton.getMeasuredHeight());

                int nextX = expandLeft ?
                        addButtonX - mButtonSpacing :
                        addButtonX + mAddButton.getMeasuredWidth() + mButtonSpacing;

                for (int i = mButtonsCount - 1; i >= 0; i--) {
                    final View child = getChildAt(i);

                    if (child == mAddButton || child.getVisibility() == GONE) continue;

                    int childX = expandLeft ? nextX - child.getMeasuredWidth() : nextX;
                    int childY = addButtonTop + (mAddButton.getMeasuredHeight() - child.getMeasuredHeight()) / 2;
                    child.layout(childX, childY, childX + child.getMeasuredWidth(), childY + child.getMeasuredHeight());

                    float collapsedTranslation = addButtonX - childX;
                    float expandedTranslation = 0f;

                    child.setTranslationX(mExpanded ? expandedTranslation : collapsedTranslation);
                    child.setAlpha(mExpanded ? 1f : 0f);

                    LayoutParams params = (LayoutParams) child.getLayoutParams();
                    params.mCollapseDir.setFloatValues(expandedTranslation, collapsedTranslation);
                    params.mExpandDir.setFloatValues(collapsedTranslation, expandedTranslation);
                    params.setAnimationsTarget(child);

                    nextX = expandLeft ?
                            childX - mButtonSpacing :
                            childX + child.getMeasuredWidth() + mButtonSpacing;
                }

                break;
        }
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(super.generateDefaultLayoutParams());
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(super.generateLayoutParams(attrs));
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(super.generateLayoutParams(p));
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return super.checkLayoutParams(p);
    }



    private Drawable getAddIconDrawable() {
        Drawable expand = getContext().getResources().getDrawable(expandIcon);
        expand.setAlpha(0);
        final RotatingDrawable rotatingDrawable = new RotatingDrawable(mAddButton.getBackground(),expand );
        mRotatingDrawable = rotatingDrawable;

        final OvershootInterpolator interpolator = new OvershootInterpolator();

        final ObjectAnimator collapseAnimator = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", EXPANDED_PLUS_ROTATION, COLLAPSED_PLUS_ROTATION);
        final ObjectAnimator expandAnimator = ObjectAnimator.ofFloat(rotatingDrawable, "rotation", COLLAPSED_PLUS_ROTATION, EXPANDED_PLUS_ROTATION);
        final ObjectAnimator collapseAlphaAnimator = ObjectAnimator.ofInt(rotatingDrawable, "alpha", EXPAND_ALPHA, COLLAPSE_ALPHA);
        final ObjectAnimator expandAlphaAnimator = ObjectAnimator.ofInt(rotatingDrawable, "alpha", COLLAPSE_ALPHA, EXPAND_ALPHA);

        collapseAnimator.setInterpolator(interpolator);
        expandAnimator.setInterpolator(interpolator);

//        mExpandAnimation.play(expandAnimator);
//        mCollapseAnimation.play(collapseAnimator);

        mExpandAnimation.playTogether(expandAnimator, expandAlphaAnimator);
        mCollapseAnimation.playTogether(collapseAnimator, collapseAlphaAnimator);

        return rotatingDrawable;
    }

    private static Interpolator sExpandInterpolator = new OvershootInterpolator();
    private static Interpolator sCollapseInterpolator = new DecelerateInterpolator(3f);
    private static Interpolator sAlphaExpandInterpolator = new DecelerateInterpolator();

    public void initAddButton() {
        removeView(mAddButton);
        mAddButton = new TextView(getContext());
        mAddButton.setBackground(getResources().getDrawable(collapseIcon));
        mAddButton.setLayoutParams(generateDefaultLayoutParams());
        mAddButton.getLayoutParams().width = mButtonSize;
        mAddButton.getLayoutParams().height = mButtonSize;
        mAddButton.setBackground(getAddIconDrawable());
        mAddButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                toggle();
            }
        });

        addView(mAddButton);
        mButtonsCount++;
    }

    protected class LayoutParams extends ViewGroup.LayoutParams {

        private ObjectAnimator mExpandDir = new ObjectAnimator();
        private ObjectAnimator mExpandAlpha = new ObjectAnimator();
        private ObjectAnimator mCollapseDir = new ObjectAnimator();
        private ObjectAnimator mCollapseAlpha = new ObjectAnimator();
        private boolean animationsSetToPlay;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            init();
        }

        public LayoutParams(int width, int height) {
            super(width,height);
            init();
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
            init();
        }

        private void init(){
            mExpandDir.setInterpolator(sExpandInterpolator);
            mExpandAlpha.setInterpolator(sAlphaExpandInterpolator);
            mCollapseDir.setInterpolator(sCollapseInterpolator);
            mCollapseAlpha.setInterpolator(sCollapseInterpolator);

            mCollapseAlpha.setProperty(View.ALPHA);
            mCollapseAlpha.setFloatValues(1f, 0f);

            mExpandAlpha.setProperty(View.ALPHA);
            mExpandAlpha.setFloatValues(0f, 1f);

            switch (mExpandDirection) {
                case EXPAND_UP:
                case EXPAND_DOWN:
                    mCollapseDir.setProperty(View.TRANSLATION_Y);
                    mExpandDir.setProperty(View.TRANSLATION_Y);
                    break;
                case EXPAND_LEFT:
                case EXPAND_RIGHT:
                    mCollapseDir.setProperty(View.TRANSLATION_X);
                    mExpandDir.setProperty(View.TRANSLATION_X);
                    break;
            }
        }

        public void setAnimationsTarget(View view) {
            mCollapseAlpha.setTarget(view);
            mCollapseDir.setTarget(view);
            mExpandAlpha.setTarget(view);
            mExpandDir.setTarget(view);

            // Now that the animations have targets, set them to be played
            if (!animationsSetToPlay) {
                addLayerTypeListener(mExpandDir, view);
                addLayerTypeListener(mCollapseDir, view);

                mCollapseAnimation.play(mCollapseAlpha);
                mCollapseAnimation.play(mCollapseDir);
                mExpandAnimation.play(mExpandAlpha);
                mExpandAnimation.play(mExpandDir);
                animationsSetToPlay = true;
            }
        }



        private void addLayerTypeListener(Animator animator, final View view) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setLayerType(LAYER_TYPE_NONE, null);
                }

                @Override
                public void onAnimationStart(Animator animation) {
                    view.setLayerType(LAYER_TYPE_HARDWARE, null);
                }
            });
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        bringChildToFront(mAddButton);
        mButtonsCount = getChildCount();
    }

    public void collapse() {
        collapse(false);
    }

    public void collapseImmediately() {
        collapse(true);
    }

    private void collapse(boolean immediately) {
        if (mExpanded) {
            mExpanded = false;
            setBackground(transparent);
            mCollapseAnimation.setDuration(immediately ? 0 : ANIMATION_DURATION);
            mCollapseAnimation.start();
            mExpandAnimation.cancel();
        }
    }



    public void toggle() {
        if (mExpanded) {
            collapse();
        } else {
            expand();
        }
    }

    public void expand() {
        if (!mExpanded) {
            mExpanded = true;
            setBackground(background);
            mCollapseAnimation.cancel();
            mExpandAnimation.start();

            if (mListener != null) {
                mListener.onMenuExpanded();
            }
        }
    }

    public boolean isExpanded() {
        return mExpanded;
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);

        mAddButton.setEnabled(enabled);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mExpanded = mExpanded;

        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mExpanded = savedState.mExpanded;

            if (mRotatingDrawable != null) {
                mRotatingDrawable.setColor(currentColor);
                mRotatingDrawable.setRotation(mExpanded ? EXPANDED_PLUS_ROTATION : COLLAPSED_PLUS_ROTATION);

            }

            super.onRestoreInstanceState(savedState.getSuperState());
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    public Drawable getRotatingDrawable(){
        return mRotatingDrawable;
    }

    public static class SavedState extends BaseSavedState {
        public boolean mExpanded;

        public SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mExpanded = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(@NonNull Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mExpanded ? 1 : 0);
        }

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }

    public void setItems(List<Item> list){
        if(items!=list){
            removeAllItem();
            items = list;
            for(int i=0;i<items.size();i++){
                View view = generateView(items.get(i));
                view.setTag(i);
                addItem(view);
            }
        }
    }



    private View generateView(final ITimeShootMenu.Item item){
        View itemView = LayoutInflater.from(getContext()).inflate(R.layout.item_menuitem, this, false);

        TextView textView = (TextView) itemView.findViewById(R.id.itemText);
        textView.setText(item.getText());

        ImageView imageView = (ImageView) itemView.findViewById(R.id.itemImg);
        imageView.setImageResource(item.getIcon());

        return itemView;
    }

    public static class Item{
        private int icon;
        private String text;

        public Item(int icon, int color, String text) {
            this.icon = icon;
            this.text = text;
        }

        public int getIcon() {
            return icon;
        }

        public void setIcon(int icon) {
            this.icon = icon;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Item item);
    }

    public void setExpandIcon(int expandIcon) {
        this.expandIcon = expandIcon;
    }

    public void setCollapseIcon(int collapseIcon) {
        this.collapseIcon = collapseIcon;
    }

    public void setButtonBottomMargin(int mButtonBottomMargin) {
        this.mButtonBottomMargin = mButtonBottomMargin;
    }


    public void setButtonSize(int mButtonSize) {
        this.mButtonSize = mButtonSize;
    }

    public void setItemSpacing(int spacing){
        mButtonSpacing = spacing;
    }
}
