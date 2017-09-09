package org.unimelb.itime.ui.viewmodel.login;

import android.databinding.Bindable;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.presenter.LoginPresenter;

/**
 * Created by Paul on 30/8/17.
 */

public class LoginResetPasswordViewModel extends ItimeBaseViewModel {

    private LoginPresenter presenter;
    private String loginResetPWEmail = "";
    private OnEmailChangeListener onEmailChangeListener;

    public interface OnEmailChangeListener{
        void onEmailChange(String email);
    }

    public LoginResetPasswordViewModel(LoginPresenter presenter) {
        this.presenter = presenter;
    }

    public void setOnEmailChangeListener(OnEmailChangeListener onEmailChangeListener) {
        this.onEmailChangeListener = onEmailChangeListener;
    }

    @Bindable
    public String getLoginResetPWEmail() {
        return loginResetPWEmail;
    }

    public void setLoginResetPWEmail(String loginResetPWEmail) {
        this.loginResetPWEmail = loginResetPWEmail;
        if (onEmailChangeListener!=null){
            onEmailChangeListener.onEmailChange(loginResetPWEmail);
        }
        notifyPropertyChanged(BR.loginResetPWEmail);
    }

    public void cleanEmail(){
        setLoginResetPWEmail("");
    }
}
