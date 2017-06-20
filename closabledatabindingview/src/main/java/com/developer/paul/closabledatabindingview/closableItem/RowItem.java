package com.developer.paul.closabledatabindingview.closableItem;

import android.graphics.drawable.Drawable;
import android.view.View;

import com.developer.paul.closabledatabindingview.interfaces.ClosableItem;

/**
 * Created by Paul on 4/5/17.
 */

public class RowItem implements ClosableItem {
    private String itemName;
    private Drawable icon;
    private String text;
    private View.OnClickListener clickListener;
    private View.OnClickListener onDeleteClickListener;
    private RowCreateInterface rowCreateInterface;


    @Override
    public String getItemName() {
        return itemName;
    }

    @Override
    public void setItemName(String itemName) {
        this.itemName = itemName;
    }


    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
    }

    public View.OnClickListener getClickListener() {
        return clickListener;
    }

    public void setClickListener(View.OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public View.OnClickListener getOnDeleteClickListener() {
        return onDeleteClickListener;
    }

    public void setOnDeleteClickListener(View.OnClickListener onDeleteClickListener) {
        this.onDeleteClickListener = onDeleteClickListener;
    }

    public RowCreateInterface getRowCreateInterface() {
        return rowCreateInterface;
    }

    public void setRowCreateInterface(RowCreateInterface rowCreateInterface) {
        this.rowCreateInterface = rowCreateInterface;
    }

    public interface RowCreateInterface{
        View onCreateMiddleView(RowItem rowItem);
        void updateClosableView(RowItem rowItem);
    }
}
