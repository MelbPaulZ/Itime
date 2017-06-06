package com.developer.paul.closabledatabindingview.closablelayouts;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.paul.closabledatabindingview.closableItem.ButtonItem;
import com.developer.paul.closabledatabindingview.interfaces.ClosableFactory;
import com.developer.paul.closabledatabindingview.utils.ClosableDataBindingUtil;

import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by Paul on 4/5/17.
 */

public class ClosableButtonLinearLayout extends ClosableBaseLinearLayout {
    public ClosableButtonLinearLayout(Context context) {
        super(context);
        init();
    }

    public ClosableButtonLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setOrientation(HORIZONTAL);
    }

    @Override
    protected ClosableFactory getFactory() {
        return new ButtonFactory();
    }

    private class ButtonFactory implements ClosableFactory<ButtonItem> {
        private final int BUTTON_LAYOUT_HEIGHT = 64;
        private final int ICON_SIZE = 20;
        private final int ICON_TOP_MARGIN = 15;
        private final int TEXT_SIZE = 11;
        private final int TEXT_BOTTOM_MARGIN = 7;
        private final int COLOR_TEXT = Color.parseColor("#424242");
        private OnClickListener onDeleteListener;

        private HashMap<ButtonItem, ClosableRelativeLayout> buttonHashMap;

        public ButtonFactory() {
            buttonHashMap = new HashMap<>();
        }

        @Override
        public ClosableRelativeLayout create(ButtonItem buttonItem) {
            ClosableRelativeLayout button = buttonHashMap.get(buttonItem);
            if (button==null){
                ClosableRelativeLayout newBtnLayout = getButtonLayout(buttonItem);
                addIcon(buttonItem, newBtnLayout);
                addText(buttonItem, newBtnLayout);
                buttonHashMap.put(buttonItem, newBtnLayout);
                button = newBtnLayout;
            }
            return button;
        }

        @Override
        public void updateClosableView(ButtonItem buttonItem) {

        }

        @Override
        public void remove(ButtonItem buttonItem) {
            buttonHashMap.remove(buttonItem);

        }

        @Override
        public OnClickListener getCloseOnClickListener(ButtonItem buttonItem) {
            final OnClickListener btnOnClickListener = buttonItem.getOnClickListener();
            if (btnOnClickListener==null){
                return onDeleteListener;
            }else{
                return new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnOnClickListener.onClick(v);
                        onDeleteListener.onClick(v);
                    }
                };
            }
        }

        @Override
        public void setOnDeleteListener(OnClickListener onDeleteListener) {
            this.onDeleteListener = onDeleteListener;
        }

        private ClosableRelativeLayout<ButtonItem> getButtonLayout(ButtonItem buttonItem){
            ClosableRelativeLayout newBtnLayout = new ClosableRelativeLayout(getContext());
            int layoutHeight = ClosableDataBindingUtil.dxTodp(getContext(), BUTTON_LAYOUT_HEIGHT);
            LayoutParams newBtnLp = new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, layoutHeight);
            newBtnLp.weight = 1;
            newBtnLayout.setLayoutParams(newBtnLp);
            newBtnLayout.setClosableItem(buttonItem);
            return newBtnLayout;
        }

        private void addIcon(ButtonItem buttonItem, RelativeLayout layout){
            ImageView iconImage = new ImageView(layout.getContext());
            iconImage.setImageDrawable(buttonItem.getIcon());
            int iconSize = ClosableDataBindingUtil.dxTodp(getContext(), ICON_SIZE);
            RelativeLayout.LayoutParams iconLp = new RelativeLayout.LayoutParams(iconSize, iconSize);
            iconLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            iconLp.topMargin = ClosableDataBindingUtil.dxTodp(getContext(), ICON_TOP_MARGIN);
            iconImage.setLayoutParams(iconLp);
            layout.addView(iconImage);

            // update Touch Area
            TextView touchView = new TextView(getContext());
            touchView.setLayoutParams(new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            touchView.setOnClickListener(getCloseOnClickListener(buttonItem));
            layout.addView(touchView);
        }

        private void addText(ButtonItem buttonItem, RelativeLayout layout){
            TextView textView = new TextView(getContext());
            textView.setText(buttonItem.getText());
            textView.setTextSize(TEXT_SIZE);
            textView.setTextColor(COLOR_TEXT);
            RelativeLayout.LayoutParams textLp = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textLp.bottomMargin = ClosableDataBindingUtil.dxTodp(getContext(), TEXT_BOTTOM_MARGIN);
            textLp.addRule(RelativeLayout.CENTER_HORIZONTAL);
            textLp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
            textView.setLayoutParams(textLp);
            layout.addView(textView);
        }
    }
}
