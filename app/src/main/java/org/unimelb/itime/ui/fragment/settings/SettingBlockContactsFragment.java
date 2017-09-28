package org.unimelb.itime.ui.fragment.settings;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.fitness.data.Goal;

import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentBlocklistBinding;
import org.unimelb.itime.ui.fragment.contact.ProfileFragment;
import org.unimelb.itime.ui.mvpview.contact.BlockContactsMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.BlockContactsViewModel;

import java.util.List;

/**
 * Created by Qiushuo Huang on 2017/1/14.
 */

public class SettingBlockContactsFragment extends ItimeBaseFragment<BlockContactsMvpView, ContactPresenter<BlockContactsMvpView>> implements BlockContactsMvpView, ToolbarInterface {
    private FragmentBlocklistBinding binding;
    private ProfileFragment profileFragment;
    private BlockContactsViewModel viewModel;
    private ToolbarViewModel toolbarViewModel;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_blocklist, container, false);

        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new BlockContactsViewModel(presenter);
        binding.setContentVM(viewModel);
        viewModel.setMvpView(this);

        toolbarViewModel = new ToolbarViewModel<>(this);
        toolbarViewModel.setTitle(getString(R.string.setting_block_contacts));
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        binding.setToolbarVM(toolbarViewModel);
    }

    public void onStart(){
        super.onStart();
        viewModel.loadData();
    }

    public void onResume(){
        super.onResume();
    }

    public void goToProfileFragment(Contact user) {
        if (profileFragment == null) {
            profileFragment = new ProfileFragment();
        }
        profileFragment.setUserUid(user.getUserDetail().getUserUid());
        profileFragment.setStartMode(ProfileFragment.MODE_CONTACT);
        getBaseActivity().openFragment(profileFragment, null, true);
    }

    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter(getContext());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onBack() {
        getActivity().onBackPressed();
    }

    @Override
    public void onNext() {

    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        viewModel.setContactList((List<Contact>) data);
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
