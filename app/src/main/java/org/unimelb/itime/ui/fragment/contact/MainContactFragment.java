package org.unimelb.itime.ui.fragment.contact;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.bean.Contact;
import org.unimelb.itime.databinding.FragmentMainContactsBinding;
import org.unimelb.itime.databinding.HeaderContactMainListBinding;
import org.unimelb.itime.messageevent.MessageAddContact;
import org.unimelb.itime.messageevent.MessageEditContact;
import org.unimelb.itime.messageevent.MessageNewFriendRequest;
import org.unimelb.itime.messageevent.MessageRemoveContact;
import org.unimelb.itime.ui.activity.MoreFriendsActivity;
import org.unimelb.itime.ui.activity.NewFriendActivity;
import org.unimelb.itime.ui.activity.ProfileActivity;
import org.unimelb.itime.ui.activity.SearchActivity;
import org.unimelb.itime.ui.mvpview.contact.MainContactsMvpView;
import org.unimelb.itime.ui.presenter.contact.ContactPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.contact.ContactHomePageViewModel;
import org.unimelb.itime.widget.listview.SideBarListView;


import java.util.List;

/**
 * Created by 37925 on 2016/12/8.
 */

public class MainContactFragment extends ItimeBaseFragment<MainContactsMvpView, ContactPresenter<MainContactsMvpView>> implements MainContactsMvpView {
    private FragmentMainContactsBinding binding;
    private ContactHomePageViewModel viewModel;
//    private NetworkBarViewModel networkBarViewModel;
    private SideBarListView contactList;
    private HeaderContactMainListBinding headerBinding;

    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_main_contacts, container, false);
        headerBinding = DataBindingUtil.inflate(inflater,
                R.layout.header_contact_main_list, null, false);
        EventBus.getDefault().register(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(Bundle bundle){
        super.onActivityCreated(bundle);
        viewModel = new ContactHomePageViewModel(presenter);
        viewModel.setMvpView(this);
        binding.setContentVM(viewModel);
        headerBinding.setContentVM(viewModel);
        contactList = binding.sortListView;
        contactList.addHeader(headerBinding.getRoot());
//        networkBarViewModel = new NetworkBarViewModel();
//        binding.setNetworkVM(networkBarViewModel);
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void removeContact(MessageRemoveContact msg){
//        viewModel.removeContact(msg.contact);
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void addContact(MessageAddContact msg){
//        viewModel.addContact(msg.contact);
//    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRequestCount(MessageNewFriendRequest msg){
        viewModel.setRequestCount(msg.count);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void editContact(MessageEditContact msg){
        presenter.loadContacts();
    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void oneNetworkChange(MessageNetworkStatus message){
//        networkBarViewModel.setShow(!message.isAvailable);
//    }

    public void onStart(){
        super.onStart();
    }

    public void onResume(){
        super.onResume();
        presenter.loadContacts();
//        networkBarViewModel.setShow(!NetworkUtil.getInstance(getContext()).isAvailable());
//        viewModel.resetView();
    }



    public void goToNewFriendFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), NewFriendActivity.class);
        startActivity(intent);
    }

    public void goToProfileFragment(View view, Contact user) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), ProfileActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable(ProfileActivity.USER_UID,user.getUserDetail().getUserUid());
        intent.putExtras(bundle);
        if(view==null) {
            startActivity(intent);
        } else{
            ActivityOptionsCompat options =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(),
                            view, getString(R.string.transition_name_avatar));

            ActivityCompat.startActivity(getActivity(), intent, options.toBundle());
        }
    }

    @Override
    public void gotoSearch() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra(SearchActivity.TASK,SearchActivity.MULTIPLE_SEARCH);
        startActivity(intent);
    }

    @Override
    public void goToScanQR() {

    }

    public FragmentMainContactsBinding getBinding() {
        return binding;
    }

    public void goToAddFriendsFragment() {
        Intent intent = new Intent();
        intent.setClass(getActivity(), MoreFriendsActivity.class);
        startActivity(intent);
    }

    @Override
    public ContactPresenter createPresenter() {
        return new ContactPresenter(getContext());
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onTaskStart(int taskId) {

    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        switch (taskId){
            case ContactPresenter.TAST_ALL_CONTACT:
                if(data instanceof List)
                    viewModel.loadData((List<Contact>) data);
                break;
        }
    }

    @Override
    public void onTaskError(int taskId, Object data) {

    }
}
