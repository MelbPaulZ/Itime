package com.developer.paul.closabledatabindingview.closableItem;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.developer.paul.closabledatabindingview.interfaces.ClosableItem;

/**
 * Created by Paul on 4/5/17.
 */

public class ButtonItem implements ClosableItem {
    private String itemName;
    private Drawable icon;
    private View.OnClickListener onClickListener;
    private String text;

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public View.OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String getItemName() {
        return itemName;
    }
}
