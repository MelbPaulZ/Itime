package org.unimelb.itime.ui.viewmodel.setting;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.text.SpannableString;
import android.view.View;

import org.unimelb.itime.BR;

/**
 * Created by Qiushuo Huang on 2017/3/6.
 */

public class HelpItemViewModel extends BaseObservable {
    private SpannableString question;
    private SpannableString answer;
    private boolean showAnswer;

    @Bindable
    public SpannableString getQuestion() {
        return question;
    }

    public void setQuestion(SpannableString question) {
        this.question = question;
        notifyPropertyChanged(BR.question);
    }

    @Bindable
    public SpannableString getAnswer() {
        return answer;
    }

    public void setAnswer(SpannableString answer) {
        this.answer = answer;
        notifyPropertyChanged(BR.answer);
    }

    @Bindable
    public boolean getShowAnswer() {
        return showAnswer;
    }

    public void setShowAnswer(boolean showAnswer) {
        this.showAnswer = showAnswer;
        notifyPropertyChanged(BR.showAnswer);
    }

    public View.OnClickListener onQuestionClick(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              setShowAnswer(!getShowAnswer());
            }
        };
    }
}
