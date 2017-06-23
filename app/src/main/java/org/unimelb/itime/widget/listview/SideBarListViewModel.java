package org.unimelb.itime.widget.listview;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableList;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by 37925 on 2016/12/15.
 */

public class SideBarListViewModel extends BaseObservable {
    private ObservableList items;
    private ItemBinding itemView = ItemBinding.of(BR.viewModel, R.layout.listview_selectable_user_item);
    private OnRecyclerItemClickListener.OnItemClickListener onItemClickListener;
    private RecyclerView.LayoutManager layoutManager;

    @Bindable
    public ObservableList getItems() {
        return items;
    }

    public void setItems(ObservableList<UserInfoViewModel> items) {
        this.items = items;
        notifyPropertyChanged(BR.items);
    }

    @Bindable
    public RecyclerView.LayoutManager getLayoutManager() {
        return layoutManager;
    }

    public void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        notifyPropertyChanged(BR.layoutManager);
    }

    @Bindable
    public ItemBinding getItemBinding() {
        return itemView;
    }

    public void setItemBinding(ItemBinding itemView) {
        this.itemView = itemView;
        notifyPropertyChanged(BR.itemBinding);
    }

    @Bindable
    public OnRecyclerItemClickListener.OnItemClickListener getOnItemClickListener() {
        return onItemClickListener;
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
        notifyPropertyChanged(BR.onItemClickListener);
    }
}
