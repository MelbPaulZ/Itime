package org.unimelb.itime.widget.popupmenu;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/5/22.
 */

public class PopupMenuViewModel extends BaseObservable {
    private List<PopupMenu.Item> items;
    private AdapterView.OnItemClickListener onItemClickListener;

    @Bindable
    public List<PopupMenu.Item> getItems() {
        return items;
    }

    public void setItems(List<PopupMenu.Item> items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
    }

    @Bindable
    public AdapterView.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyPropertyChanged(BR.onItemClickListener);
    }

//    public ItemView getItemView(){
//        return ItemView.of(BR.item, R.layout.item_popup_menu_item);
//    }
}
