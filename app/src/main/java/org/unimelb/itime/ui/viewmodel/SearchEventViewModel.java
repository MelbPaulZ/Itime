package org.unimelb.itime.ui.viewmodel;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.Event;
import org.unimelb.itime.ui.mvpview.calendar.SearchEventMvpView;
import org.unimelb.itime.ui.presenter.SearchEventPresenter;
import org.unimelb.itime.ui.viewmodel.search.EventInfoViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import me.tatarka.bindingcollectionadapter2.ItemBinding;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public class SearchEventViewModel extends BaseObservable {

    private SearchEventMvpView mvpView;
    private SearchEventPresenter<SearchEventMvpView> presenter;

    private List<Map.Entry<Long,List<Event>>> eventResult = new ArrayList<>();

    private String resultHint = "";
    private String searchStr = "";

    public SearchEventViewModel(SearchEventPresenter<SearchEventMvpView> presenter) {
        this.presenter = presenter;
        this.mvpView = presenter.getView();
        refreshResultHint();
    }

    /**
     * listeners
     * @return
     */
    public View.OnClickListener onBackClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mvpView!=null){
                    mvpView.onBack();
                }
            }
        };
    }

    public View.OnClickListener onClearClick(){

        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setSearchStr("");
            }
        };
    }

    public TextWatcher onEdit(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                refreshResultHint();
                clearResults();
                presenter.search(searchStr);
            }
        };
    }

    private void refreshResultHint(){
        if (searchStr.isEmpty()){
            setResultHint("Try searching for keywords");
        }else{
            setResultHint("");
        }
    }

    private void clearResults(){
        eventResult.clear();
    }


    public List<Map.Entry<Long, List<Event>>> getEventResult() {
        return eventResult;
    }

    public void setEventResult(List<Map.Entry<Long, List<Event>>> eventResult) {
        this.eventResult = eventResult;
    }

    @Bindable
    public String getResultHint() {
        return resultHint;
    }

    public void setResultHint(String resultHint) {
        this.resultHint = resultHint;
        notifyPropertyChanged(BR.resultHint);
    }

    @Bindable
    public String getSearchStr() {
        return searchStr;
    }

    public void setSearchStr(String searchStr){
        this.searchStr = searchStr;
        notifyPropertyChanged(BR.searchStr);
    }

}
