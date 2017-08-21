package org.unimelb.itime.widget;

import android.app.Dialog;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/8/21.
 */

public class SelectAlertTimeDialog extends BaseObservable {
    private Dialog dialog;
    private OnTimeClickListener listener;
    private ObservableList<ItemViewModel> items = new ObservableArrayList<>();

    public ObservableList<ItemViewModel> getItems() {
        return items;
    }

    public void setItems(ObservableList<ItemViewModel> items) {
        this.items = items;
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(BR.vm, R.layout.listview_alerttime_item);
    }

    public interface OnTimeClickListener{
        void onClick(View v, long time);
    }

    public static class ItemViewModel extends BaseObservable{
        private String timeString="";
        private long time = 0L;
        private boolean selected = false;

        @Bindable
        public String getTimeString() {
            return timeString;
        }

        public void setTimeString(String timeString) {
            this.timeString = timeString;
            notifyPropertyChanged(BR.timeString);
        }

        @Bindable
        public long getTime() {
            return time;
        }

        public void setTime(long time) {
            this.time = time;
            notifyPropertyChanged(BR.time);
        }

        @Bindable
        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
            notifyPropertyChanged(BR.selected);
        }
    }
}
