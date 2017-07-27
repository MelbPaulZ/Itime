package org.unimelb.itime.widget.listview;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableList;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.unimelb.itime.R;
import org.unimelb.itime.adapter.BaseRecyclerAdapter;
import org.unimelb.itime.databinding.SideBarListViewBinding;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;

import java.util.ArrayList;
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
    private ItemBinding itemBinding;
    private MyAdapter myAdapter;

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
        getPositionMap();
        myAdapter.addDatas(items);
    }

    public void setItemBinding(ItemBinding itemBinding){
        this.itemBinding = itemBinding;
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

    public void addHeader(View view){
        myAdapter.setHeaderView(view);
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
        myAdapter = new MyAdapter();
        listView.setLayoutManager(new LinearLayoutManager(getContext()));
        listView.setAdapter(myAdapter);
    }

    public void hideSideBar() {
        sideBar.setVisibility(GONE);
    }

    public class MyAdapter<T> extends BaseRecyclerAdapter<T> {

        @Override
        public RecyclerView.ViewHolder onCreate(ViewGroup parent, int viewType) {
            ViewDataBinding binding =  DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), itemBinding.layoutRes(), parent, false);
            MyHolder myHolder =  new MyHolder(binding.getRoot());
            myHolder.setBinding(binding);
            return myHolder;
        }

        @Override
        public void onBind(RecyclerView.ViewHolder viewHolder, int RealPosition, T data) {
            ((MyHolder) viewHolder).binding.setVariable(itemBinding.variableId(), data);
        }

        class MyHolder extends BaseRecyclerAdapter.Holder {
            ViewDataBinding binding;
            public MyHolder(View itemView) {
                super(itemView);
            }

            public ViewDataBinding getBinding() {
                return binding;
            }

            public void setBinding(ViewDataBinding binding) {
                this.binding = binding;
            }
        }
    }

}
