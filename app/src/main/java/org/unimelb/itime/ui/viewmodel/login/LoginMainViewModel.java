package org.unimelb.itime.ui.viewmodel.login;

import android.databinding.Bindable;
import android.view.View;
import android.widget.Toast;

import org.unimelb.itime.BR;
import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.mvpview.login.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Paul on 29/8/17.
 */

public class LoginMainViewModel extends ItimeBaseViewModel {
    private LoginPresenter presenter;
    private String loginEmail = "";
    private String loginPassWord = "";
    private TextChangeListener textChangeListener;
    private String[] postFixAutoCompletes = new String[]{
        "student.monash.edu","student.unimelb.edu.au","student.rmit.edu.au","student.monash.edu"
    };

    public void setTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    public interface TextChangeListener{
        void onEmailChange(String loginEmail,String loginPassWord);
    }

    public LoginMainViewModel(LoginPresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public String getLoginEmail() {
        return loginEmail;
    }

    public void setLoginEmail(String loginEmail) {
        this.loginEmail = loginEmail;
        if (textChangeListener!=null){
            textChangeListener.onEmailChange(loginEmail, loginPassWord);
        }
        notifyPropertyChanged(BR.loginEmail);
    }

    @Bindable
    public String getLoginPassWord() {
        return loginPassWord;
    }

    public void setLoginPassWord(String loginPassWord) {
        this.loginPassWord = loginPassWord;
        if (textChangeListener!=null){
            textChangeListener.onEmailChange(loginEmail, loginPassWord);
        }
        notifyPropertyChanged(BR.loginPassWord);
    }

    public void cleanEmail(){
        setLoginEmail("");
    }

    public void cleanPassWord(){
        setLoginPassWord("");
    }

    public void onclickResetByEmail(){
        if (presenter.getView()!=null){
            ((LoginMvpView)presenter.getView()).toResetPassword();
        }
    }

    public List<String> getPopupStrings(){
        List<String> matchString = new ArrayList<>();
        String[] split = loginEmail.split("@");
        String prefix = split[0];
        String postfix = "";
        if (split.length > 1) {
            postfix = split[1];
        }
        for (int i = 0 ; i < 4 ; i++){
            if (postFixAutoCompletes[i].startsWith(postfix)){
                String showString = prefix + "@" + postFixAutoCompletes[i];
                matchString.add(showString);
            }
        }
        return matchString;
    }

}
