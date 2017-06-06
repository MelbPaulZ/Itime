package com.developer.paul.closabledatabindingview.closablelayouts;

import android.content.Context;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.ObservableList;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.developer.paul.closabledatabindingview.interfaces.ClosableFactory;
import com.developer.paul.closabledatabindingview.interfaces.ClosableItem;
import com.developer.paul.closabledatabindingview.utils.ClosableDataBindingUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Paul on 4/5/17.
 */

@BindingMethods({
        @BindingMethod(type = ClosableBaseLinearLayout.class, attribute = "ClosableBaseLinearLayout:orderHashMap", method = "setOrderHash"),
        @BindingMethod(type = ClosableBaseLinearLayout.class, attribute = "ClosableBaseLinearLayout:closableItems", method = "setList")
})
public abstract class ClosableBaseLinearLayout extends LinearLayout {
    protected ClosableFactory closableFactory;
    protected HashMap<String, Integer> orderHash;
    protected List<? extends ClosableItem> originList;


    public ClosableBaseLinearLayout(Context context) {
        super(context);
        init();
    }

    public ClosableBaseLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        orderHash = new HashMap<>();
        originList = new ArrayList<>();
        closableFactory = getFactory();
        closableFactory.setOnDeleteListener(onDeleteListener());
    }

    private OnClickListener onDeleteListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClosableRelativeLayout btnLayout = (ClosableRelativeLayout) v.getParent();
                if (btnLayout==null){
                    return;
                }

                LinearLayout btnLayoutParent = (LinearLayout) btnLayout.getParent();
                if (btnLayoutParent==null){
                    return;
                }

                ClosableItem item = btnLayout.getClosableItem();
                remove(item);
                btnLayoutParent.removeView(btnLayout);
            }
        };
    }

    public void setOrderHash(HashMap<String, Integer> orderHash) {
        this.orderHash = orderHash;
    }

    public void setOnDeleteListener(OnClickListener onDeleteListener) {
        this.closableFactory.setOnDeleteListener(onDeleteListener);
    }

    private ClosableItem findRemoveItem(List<? extends ClosableItem> originList, List<? extends ClosableItem> newList){
        for (ClosableItem item: originList){
            if (!newList.contains(item)){
                return item;
            }
        }
        return null;
    }


    public void setList(ObservableList<? extends ClosableItem> tList){
        ClosableDataBindingUtil.sortClosableItem(orderHash, tList);
        if (originList.size() > tList.size()){
            // item has been deleted
            ClosableItem item = findRemoveItem(originList, tList);
            ClosableRelativeLayout btnLayout = closableFactory.create(item);// find this view
            removeView(btnLayout); // remove this from parent
        }
        originList = tList;
        for (ClosableItem item: tList){
            update(item);
        }
    }

    public void update(ClosableItem t){
        ClosableRelativeLayout btnLayout = closableFactory.create(t);
        if (btnLayout.getParent()==null){
            int position = findPosition(t.getItemName());
            addView(btnLayout, position);
        }else{
            // update
            closableFactory.updateClosableView(t);
        }
    }

    public void remove(ClosableItem t){
        originList.remove(t);
    }

    private int findPosition(String rowName){
        int len = originList.size();
        int rowNumIndex = orderHash.get(rowName);
        for (int i = 0 ; i < len ; i++){
            int curViewIndex = orderHash.get(originList.get(i).getItemName());
            if (curViewIndex>=rowNumIndex){
                return i;
            }
        }
        return len;
    }

    protected abstract ClosableFactory getFactory();

}
