package org.unimelb.itime.widget.listview;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.SideBarListViewBinding;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;

import java.util.HashMap;
import java.util.Map;
import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by Qiushuo Huang on 2017/3/20.
 */

public class SideBarListView extends FrameLayout {
    private RecyclerView listView;
    private SideBar sideBar;
    private TextView dialog;
    private SideBarListViewBinding binding;
    private Map<String, Integer> positionMap = new HashMap<>();
    private SideBarListViewModel viewModel;
    private ObservableList<UserInfoViewModel> items;

    public SideBarListView(Context context) {
        super(context);
        init();
    }

    public SideBarListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SideBarListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public void setItems(ObservableList<UserInfoViewModel> items){
        this.items = items;
        viewModel.setItems(items);
        getPositionMap();
    }

    public void setItemBinding(ItemBinding itemBinding){
        viewModel.setItemBinding(itemBinding);
    }

    public void setOnItemClickListener(OnRecyclerItemClickListener.OnItemClickListener onItemClickListener){
        viewModel.setOnItemClickListener(onItemClickListener);
    }

    public void getPositionMap(){
        if(positionMap==null){
            positionMap = new HashMap<>();
        }
        positionMap.clear();
        for(int i=0;i<items.size();i++){
            UserInfoViewModel item = items.get(i);
            String letter = item.getSortLetter();
            if(positionMap.containsKey(letter)){
                item.setShowFirstLetter(false);
                continue;
            }else{
                positionMap.put(letter, i);
                item.setShowFirstLetter(true);
            }
        }
    }

    public void setShowSideBar(boolean showSideBar){
        if(showSideBar){
            sideBar.setVisibility(VISIBLE);
        }else{
            sideBar.setVisibility(GONE);
        }
    }

    public void init(){
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()),
                R.layout.side_bar_list_view, null, false);
        dialog = binding.dialog;
        sideBar = binding.sidrbar;
        sideBar.setTextView(dialog);
        listView = binding.sortedContactListView;
        // 设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                // 该字母首次出现的位置
                if (positionMap.containsKey(s)) {
                    listView.scrollToPosition(positionMap.get(s));
                }
            }
        });
        viewModel = new SideBarListViewModel();
        viewModel.setLayoutManager(new LinearLayoutManager(getContext()));
        binding.setContentVM(viewModel);
        this.addView(binding.getRoot());
    }

    public void hideSideBar() {
        sideBar.setVisibility(GONE);
    }

}
