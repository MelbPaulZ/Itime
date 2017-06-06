package com.developer.paul.closabledatabindingview.closablelayouts;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.developer.paul.closabledatabindingview.interfaces.ClosableItem;

/**
 * Created by Paul on 4/5/17.
 */

public class ClosableRelativeLayout<T extends ClosableItem> extends RelativeLayout {
    private T closableItem;
    private TextView changableText;

    public ClosableRelativeLayout(Context context) {
        super(context);
    }

    public ClosableRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public T getClosableItem() {
        return closableItem;
    }

    public void setClosableItem(T closableItem) {
        this.closableItem = closableItem;
    }

}
