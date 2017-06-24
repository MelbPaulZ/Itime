package com.developer.paul.closabledatabindingview.closablelayouts;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.paul.closabledatabindingview.R;
import com.developer.paul.closabledatabindingview.closableItem.RowItem;
import com.developer.paul.closabledatabindingview.interfaces.ClosableFactory;
import com.developer.paul.closabledatabindingview.utils.ClosableDataBindingUtil;

import java.util.HashMap;

/**
 * Created by Paul on 3/5/17.
 */

public class ClosableRowLinearLayout extends ClosableBaseLinearLayout{


    public ClosableRowLinearLayout(Context context) {
        super(context);
        init();
    }

    public ClosableRowLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setOrientation(VERTICAL);
    }

    @Override
    protected ClosableFactory getFactory() {
        return new RowFactory();
    }

    public void setOnDeleteListener(View.OnClickListener onDeleteListener){
         closableFactory.setOnDeleteListener(onDeleteListener);
    }




    private class RowFactory implements ClosableFactory<RowItem>{
        private final int ICON_SIZE = 20;
        private final int TEXT_LEFT_MARGIN = 45;
        private final int CLOSE_RIGHT_MARGIN = 10;
        private final int CLOSE_SIZE = 20;
        private final int ROW_HEIGHT = 54;
        private HashMap<RowItem, ClosableRelativeLayout> rowHashMap;
        private OnClickListener onDeleteListener;

        RowFactory() {
            rowHashMap = new HashMap<>();
        }


        @Override
        public ClosableRelativeLayout create(RowItem rowItem) {
            ClosableRelativeLayout row = rowHashMap.get(rowItem);
            if (row==null){
                ClosableRelativeLayout rowRelativeLayout = getRelativeLayout(rowItem);
                if (rowItem.getRowCreateInterface()!=null){
                    View v = rowItem.getRowCreateInterface().onCreateMiddleView(rowItem);
                    addCustomMidView(v, rowRelativeLayout, rowItem.getClickListener());
                }else {
                    addDisplayText(rowItem.getText(), rowRelativeLayout, rowItem.getClickListener());
                }
                addIconView(rowItem.getIcon(), rowRelativeLayout);
                addClosableView(rowRelativeLayout, rowItem.getOnDeleteClickListener());
                rowHashMap.put(rowItem, rowRelativeLayout);
                row = rowRelativeLayout;
            }
            return row;
        }

        @Override
        public void updateClosableView(RowItem rowItem) {
            ClosableRelativeLayout row = rowHashMap.get(rowItem);
            View v = row.getChildAt(0);  // TODO: 6/6/17 change later
            if (rowItem.getRowCreateInterface()!=null){
                rowItem.getRowCreateInterface().updateClosableView(rowItem);
            }else {
                if (v instanceof TextView) {
                    ((TextView) v).setText(rowItem.getText());
                }
            }
        }

        @Override
        public void remove(RowItem rowItem) {

        }


        @Override
        public OnClickListener getCloseOnClickListener(RowItem rowItem) {
            return onDeleteListener;
        }

        @Override
        public void setOnDeleteListener(OnClickListener onDeleteListener) {
            this.onDeleteListener = onDeleteListener;
        }

        private void addCustomMidView(View v, RelativeLayout rowRelativeLayout, OnClickListener leftClickListener){
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            lp.leftMargin = ClosableDataBindingUtil.dxTodp(getContext(), TEXT_LEFT_MARGIN);
            lp.bottomMargin = ClosableDataBindingUtil.dxTodp(getContext(), 20);
            lp.addRule(RelativeLayout.CENTER_VERTICAL);
            v.setLayoutParams(lp);
            v.setOnClickListener(leftClickListener);
            rowRelativeLayout.addView(v);
        }


        private ClosableRelativeLayout getRelativeLayout(RowItem rowItem){
            ClosableRelativeLayout rowLayout = new ClosableRelativeLayout(getContext());
            rowLayout.setBackground(getResources().getDrawable(R.drawable.bg_divider_bottom));
            int height = ClosableDataBindingUtil.dxTodp(getContext(), ROW_HEIGHT);
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            rowLayout.setLayoutParams(lp);
            rowLayout.setMinimumHeight(height);
            rowLayout.setClosableItem(rowItem);
            return rowLayout;
        }

        private void addIconView(Drawable icon, RelativeLayout rowRelativeLayout){
            ImageView iconView = new ImageView(getContext());
            iconView.setImageDrawable(icon);
            int iconSize = ClosableDataBindingUtil.dxTodp(getContext(), ICON_SIZE);
            RelativeLayout.LayoutParams iconLp = new RelativeLayout.LayoutParams(
                    iconSize, iconSize);
            iconLp.topMargin = ClosableDataBindingUtil.dxTodp(getContext(), 20);
            iconLp.leftMargin = ClosableDataBindingUtil.dxTodp(getContext(), 14);
            iconView.setLayoutParams(iconLp);
            rowRelativeLayout.addView(iconView);
        }

        private void addDisplayText(String text, RelativeLayout rowRelativeLayout, OnClickListener leftClickListener){
            TextView displayText = new TextView(getContext());
            displayText.setText(text);
            displayText.setTextSize(16);
            displayText.setTextColor(Color.parseColor("#030303"));
            displayText.setMinWidth(ClosableDataBindingUtil.dxTodp(getContext(), 157));
            displayText.setOnClickListener(leftClickListener);
            displayText.setMaxWidth(ClosableDataBindingUtil.dxTodp(getContext(), 280));
            RelativeLayout.LayoutParams textLp = new RelativeLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
            );
            textLp.leftMargin = ClosableDataBindingUtil.dxTodp(getContext(), TEXT_LEFT_MARGIN);
            textLp.bottomMargin = ClosableDataBindingUtil.dxTodp(getContext(), 20);
            textLp.addRule(RelativeLayout.CENTER_VERTICAL);
            displayText.setLayoutParams(textLp);
            rowRelativeLayout.addView(displayText);
        }

        private void addClosableView(RelativeLayout rowRelativeLayout, View.OnClickListener rowDeleteListener){
            ImageView closeView = new ImageView(getContext());
            closeView.setImageDrawable(getResources().getDrawable(R.drawable.icon_event_closecell));
            int closeSize = ClosableDataBindingUtil.dxTodp(getContext(), CLOSE_SIZE);
            RelativeLayout.LayoutParams closableViewLp = new RelativeLayout.LayoutParams(closeSize, closeSize);
            closableViewLp.rightMargin = ClosableDataBindingUtil.dxTodp(getContext(), CLOSE_RIGHT_MARGIN);
            closableViewLp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            closableViewLp.addRule(RelativeLayout.CENTER_VERTICAL);
            closeView.setLayoutParams(closableViewLp);
            if (rowDeleteListener ==null) {
                closeView.setOnClickListener(onDeleteListener);
            }else{
                closeView.setOnClickListener(getOwnDeleteListener(onDeleteListener, rowDeleteListener));
            }
            rowRelativeLayout.addView(closeView);
        }

        private View.OnClickListener getOwnDeleteListener(final View.OnClickListener onDeleteListener, final View.OnClickListener rowDeleteListener){
            return new OnClickListener() {
                @Override
                public void onClick(View v) {
                    onDeleteListener.onClick(v);
                    rowDeleteListener.onClick(v);
                }
            };
        }

    }

}
