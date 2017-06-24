package org.unimelb.itime.widget.listview;

import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.ITimeUserInfoInterface;
import org.unimelb.itime.util.CharacterParser;

/**
 * Created by Qiushuo Huang on 2017/6/22.
 */

public class UserInfoViewModel<T> extends BaseObservable {
    private String pinyin;
    private T data;
    private boolean showDetail = true;
    private View.OnClickListener listener;
    private ITimeUserInfoInterface info;
    private boolean select = false;
    private boolean checkable = true;
    private boolean showFirstLetter = false;
    private String sortLetter;
    private SpannableString name;
    private SpannableString secondInfo;
    private int matchColor = Color.parseColor("#0089FF");
    private boolean showSecondInfo;
    private String matchStr = "";
    private String photo;
    private boolean match = false;

    @Bindable
    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
        notifyPropertyChanged(BR.photo);
    }

    @Bindable
    public String getSortLetter() {
        return sortLetter;
    }

    public void setSortLetter(String sortLetter) {
        this.sortLetter = sortLetter;
        notifyPropertyChanged(BR.sortLetter);
    }

    @Bindable
    public boolean isShowDetail() {
        return showDetail;
    }

    public void setShowDetail(boolean showDetail) {
        this.showDetail = showDetail;
        notifyPropertyChanged(BR.showDetail);
    }

    @Bindable
    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
        setInfo((ITimeUserInfoInterface) data);
        notifyPropertyChanged(BR.data);
    }

    @Bindable
    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
        notifyPropertyChanged(BR.select);
    }

    @Bindable
    public View.OnClickListener getListener() {
        return listener;
    }

    public void setListener(View.OnClickListener listener) {
        this.listener = listener;
        notifyPropertyChanged(BR.listener);
    }

    @Bindable
    public ITimeUserInfoInterface getInfo() {
        return info;
    }

    public void setInfo(ITimeUserInfoInterface info) {
        this.info = info;

        CharacterParser characterParser = CharacterParser.getInstance();

        // 汉字转换成拼音
        pinyin = characterParser.getSelling(info.getShowName().toLowerCase());
        if(pinyin.length()>0) {
            String sortString = pinyin.substring(0, 1).toUpperCase();

            // 正则表达式，判断首字母是否是英文字母
            if (sortString.matches("[A-Z]")) {
                setSortLetter(sortString.toUpperCase());
            } else {
                setSortLetter("#");
            }
        }

        setName(new SpannableString(info.getShowName()));
        setSecondInfo(new SpannableString(info.getSecondInfo()));
        setPhoto(info.getShowPhoto());
        notifyPropertyChanged(BR.info);
    }

    @Bindable
    public boolean getCheckable() {
        return checkable;
    }

    public void setCheckable(boolean checkable) {
        this.checkable = checkable;
        notifyPropertyChanged(BR.checkable);
    }

    public SpannableString changeMatchColor(String str, String matchStr){
        SpannableString span = new SpannableString(str);
        if(matchStr.equals("")){
            return span;
        }
        int begin = str.toLowerCase().indexOf(matchStr.toLowerCase());
        int end = begin+matchStr.length();
        if(begin==-1){
            return span;
        }
        span.setSpan(new ForegroundColorSpan(matchColor),
                begin, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return span;
    }
    public void setMatchColor(int color){
        matchColor = color;
    }

    @Bindable
    public boolean getShowFirstLetter() {
        return showFirstLetter;
    }

    public void setShowFirstLetter(boolean showFirstLetter) {
        this.showFirstLetter = showFirstLetter;
        notifyPropertyChanged(BR.showFirstLetter);
    }

    @Bindable
    public SpannableString getName() {
        return changeMatchColor(info.getShowName(), getMatchStr());
    }

    public void setName(SpannableString name) {
        this.name = name;
        notifyPropertyChanged(BR.name);
    }

    public String getMatchStr(){
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
        notifyPropertyChanged(BR.secondInfo);
        notifyPropertyChanged(BR.name);
    }

    @Bindable
    public SpannableString getSecondInfo() {
        return changeMatchColor(info.getSecondInfo(), getMatchStr());
    }

    public void setSecondInfo(SpannableString secondInfo) {
        this.secondInfo = secondInfo;
        notifyPropertyChanged(BR.secondInfo);
    }

    //请保证传入的是全小写字符串
    public boolean tryMatch(String matchStr){
        setMatchStr("");
        if(info.getShowName().toLowerCase().contains(matchStr)
                || info.getSecondInfo().toLowerCase().contains(matchStr)
                || pinyin.toLowerCase().contains(matchStr)){
            setMatchStr(matchStr);
            return true;
        }
        return false;
    }
}
