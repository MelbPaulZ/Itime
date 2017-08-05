package org.unimelb.itime.ui.fragment.contact;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentEditAliasBinding;
import org.unimelb.itime.ui.mvpview.contact.EditContactMvpView;
import org.unimelb.itime.ui.presenter.contact.ProfilePresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.EditContactViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ProfileFragmentViewModel;
import org.unimelb.itime.util.NetworkUtil;
import org.unimelb.itime.util.UserValidator;

/**
 * Created by Qiushuo Huang on 2017/1/10.
 */

public class EditAliasFragment extends ItimeBaseFragment<EditContactMvpView, ProfilePresenter<EditContactMvpView>> implements EditContactMvpView, ToolbarInterface{

    private EditContactViewModel viewModel;
    private FragmentEditAliasBinding binding;
    private Contact contact;
    private ToolbarViewModel toolbarViewModel;

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new EditContactViewModel(presenter);
        if(contact!=null) {
            viewModel.setContact(contact);
        }
        binding.setViewModel(viewModel);

        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getContext().getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setTitle(getString(R.string.alias_title));
        toolbarViewModel.setRightEnable(false);
        toolbarViewModel.setRightText(getString(R.string.toolbar_save));
        viewModel.setToolbarViewModel(toolbarViewModel);
        binding.setToolbarVM(toolbarViewModel);
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_edit_alias, container, false);
        return binding.getRoot();
    }

    @Override
    public ProfilePresenter createPresenter() {
        return new ProfilePresenter(getContext());
    }

    public void setContact(Contact contact) {
        this.contact = contact;
        if(viewModel!=null){
            viewModel.setContact(contact);
        }
    }

    @Override
    public void onBack() {
        this.getActivity().onBackPressed();
    }

    @Override
    public void onNext() {
        if(!UserValidator.getInstance(getContext()).isValidName(viewModel.getAlias())){
                showAlert();
        }else {
            if(NetworkUtil.getInstance(getContext()).isAvailable()) {
                if (viewModel.getAlias().equals("")) {
                    contact.setAliasName(contact.getUserDetail().getPersonalAlias());
                } else {
                    contact.setAliasName(viewModel.getAlias());
                }
                presenter.editAlias(contact);
                onBack();
            }else{
                showToast(getString(R.string.network_error_please_try_again));
            }
        }
    }

    public void showAlert(){
        String alert = String.format(getString(R.string.edit_alias_alert_msg), UserValidator.NAME_MIN_LENGTH, UserValidator.NAME_MAX_LENGTH);
        showDialog(null, alert);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {

    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
