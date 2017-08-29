package org.unimelb.itime.ui.viewmodel.login;

import org.unimelb.itime.base.ItimeBaseViewModel;
import org.unimelb.itime.ui.presenter.LoginPresenter;

/**
 * Created by Paul on 29/8/17.
 */

public class LoginMainViewModel extends ItimeBaseViewModel {
    private LoginPresenter presenter;

    public LoginMainViewModel(LoginPresenter presenter) {
        this.presenter = presenter;
    }
}
