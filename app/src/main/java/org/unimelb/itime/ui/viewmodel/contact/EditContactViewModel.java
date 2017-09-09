package org.unimelb.itime.ui.viewmodel.contact;

import android.databinding.BaseObservable;
import android.databinding.Bindable;
import android.view.View;

import com.android.databinding.library.baseAdapters.BR;

import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.ui.presenter.contact.ProfilePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.util.UserValidator;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public class EditContactViewModel extends BaseObservable {
    private ProfilePresenter presenter;
    private Contact contact;
    private String title;
    private String alias;
    private ToolbarViewModel toolbarViewModel;
    private int remainLength = UserValidator.NAME_MAX_LENGTH;

    public EditContactViewModel(ProfilePresenter presenter) {
        this.presenter = presenter;
    }

    @Bindable
    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
        notifyPropertyChanged(BR.alias);
        notifyPropertyChanged(BR.remainLength);
    }

    @Bindable
    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        setAlias(contact.getAliasName());
        notifyPropertyChanged(BR.contact);
    }

    public View.OnClickListener getCleanListener(){
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setAlias("");
            }
        };
    }

    public ToolbarViewModel getToolbarViewModel() {
        return toolbarViewModel;
    }

    public void setToolbarViewModel(ToolbarViewModel toolbarViewModel) {
        this.toolbarViewModel = toolbarViewModel;
    }

    @Bindable
    public int getRemainLength() {
        toolbarViewModel.setRightEnable(UserValidator.getInstance(presenter.getContext()).isValidName(alias));
        return UserValidator.NAME_MAX_LENGTH - alias.length();
    }

    public void setRemainLength(int remainLength) {
        this.remainLength = remainLength;
        notifyPropertyChanged(BR.remainLength);
    }
}
