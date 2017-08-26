package org.unimelb.itime.widget.popupmenu;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ObservableList;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.DialogSelectAlerttimeBinding;
import org.unimelb.itime.util.AppUtil;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/8/21.
 */

public class SelectAlertTimeDialog extends BaseObservable {
    private OnTimeClickListener listener;
    private ObservableList<ItemViewModel> items = new ObservableArrayList<>();
    private Context context;
    private DialogSelectAlerttimeBinding binding;
    private ModalPopupView popupView;
    private final int[] alertTimes;
    private ItemViewModel selectedItem;

    public SelectAlertTimeDialog(Context context){
        this.context = context;
        binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.dialog_select_alerttime, new FrameLayout(context), false);
        binding.setVm(this);
        popupView = new ModalPopupView(context);
        popupView.setContentView(binding.getRoot());
        popupView.setBackground(context.getResources().getDrawable(R.color.mask_cover));
        alertTimes = AppUtil.getDefaultAlertMins();
        generateItems();
    }

    public void show(View parent){
        popupView.show(parent);
    }

    public void setSelectedTime(int time){
        for(int i=0;i<alertTimes.length;i++){
            if(alertTimes[i]==time){
                select(i);
                return;
            }
        }
    }

    public OnTimeClickListener getListener() {
        return listener;
    }

    public void setListener(OnTimeClickListener listener) {
        this.listener = listener;
    }

    private void select(int position){
        ItemViewModel clickedItem = items.get(position);
        if(selectedItem!=null) {
            selectedItem.setSelected(false);
        }
        clickedItem.setSelected(true);
        selectedItem = clickedItem;
    }

    private void generateItems(){
        String[] alertStrings = AppUtil.getAllAlertStr(context);
        items.clear();
        for(int i=0;i<alertStrings.length;i++){
            ItemViewModel viewModel = new ItemViewModel();
            viewModel.setSelected(false);
            viewModel.setTimeString(alertStrings[i]);
            items.add(viewModel);
        }
    }

    public ObservableList<ItemViewModel> getItems() {
        return items;
    }

    public void setItems(ObservableList<ItemViewModel> items) {
        this.items = items;
    }

    public ItemBinding getItemBinding(){
        return ItemBinding.of(BR.vm, R.layout.listview_alerttime_item);
    }

    public AdapterView.OnItemClickListener getOnItemClick(){
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (listener != null) {
                    listener.onClick(view, alertTimes[i]);
                }
                select(i);
                popupView.dismiss();
            }
        };
    }

    public interface OnTimeClickListener{
        void onClick(View v, int time);
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
