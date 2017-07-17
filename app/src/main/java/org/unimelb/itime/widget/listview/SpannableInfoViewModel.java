package org.unimelb.itime.widget.listview;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;

import org.unimelb.itime.bean.Event;

/**
 * Created by yuhaoliu on 10/7/17.
 */

public abstract class SpannableInfoViewModel extends BaseObservable {
    private int matchColor = Color.parseColor("#0089FF");
    protected String matchStr = "";

    @Bindable
    public String getMatchStr() {
        return matchStr;
    }

    public void setMatchStr(String matchStr) {
        this.matchStr = matchStr;
    }

    SpannableString changeMatchColor(String str, String matchStr){
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
}
