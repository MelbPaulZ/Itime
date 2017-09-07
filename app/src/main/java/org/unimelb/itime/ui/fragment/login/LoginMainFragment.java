package org.unimelb.itime.ui.fragment.login;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.greenrobot.eventbus.EventBus;
import org.unimelb.itime.R;
import org.unimelb.itime.base.ItimeBaseFragment;
import org.unimelb.itime.base.ToolbarInterface;
import org.unimelb.itime.databinding.FragmentLoginMainBinding;
import org.unimelb.itime.messageevent.MessageEvent;
import org.unimelb.itime.service.RemoteService;
import org.unimelb.itime.ui.activity.MainActivity;
import org.unimelb.itime.ui.mvpview.login.LoginMvpView;
import org.unimelb.itime.ui.presenter.LoginPresenter;
import org.unimelb.itime.ui.viewmodel.ToolbarViewModel;
import org.unimelb.itime.ui.viewmodel.login.LoginMainViewModel;

import rx.Observable;
import rx.Subscriber;

/**
 * Created by Paul on 29/8/17.
 */

public class LoginMainFragment extends ItimeBaseFragment<LoginMvpView, LoginPresenter<LoginMvpView>> implements LoginMvpView, ToolbarInterface{
    private FragmentLoginMainBinding binding;
    private ToolbarViewModel toolbarViewModel;
    private LoginMainViewModel vm;
    private AutoCompleteTextView emailAutoComplete;
    private ArrayAdapter adapter;
    private MaterialDialog popupDialog;

    @Override
    public LoginPresenter<LoginMvpView> createPresenter() {
        return new LoginPresenter<>(getContext());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null){
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login_main, container, false);
        }
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbarViewModel = new ToolbarViewModel(this);
        toolbarViewModel.setLeftIcon(getResources().getDrawable(R.drawable.icon_nav_back));
        toolbarViewModel.setRightText(getString(R.string.toolbar_login));
        toolbarViewModel.setDividerVisibility(View.GONE);
        binding.setToolbarVM(toolbarViewModel);
        vm = new LoginMainViewModel(getPresenter());
        vm.setTextChangeListener((loginEmail, loginPassWord) -> {
            if (loginEmail.length() == 0 || loginPassWord.length() == 0){
                toolbarViewModel.setRightEnable(false);
            }else{
                toolbarViewModel.setRightEnable(true);
            }
            if (loginEmail.contains("@") && loginEmail.length()>=2){
                adapter = new ArrayAdapter(getContext(),R.layout.login_list_item_1, vm.getPopupStrings());
                emailAutoComplete.setAdapter(adapter);
            }else{
                emailAutoComplete.setAdapter(null);
            }
        });
        binding.setVm(vm);

        emailAutoComplete = (AutoCompleteTextView) binding.getRoot().findViewById(R.id.auto_complete_email);
        emailAutoComplete.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vm.setEmailTextCleanVisibility(View.VISIBLE);
                }else{
                    vm.setEmailTextCleanVisibility(View.GONE);
                }

            }
        });

        EditText passwordEditText = (EditText) getActivity().findViewById(R.id.login_password_edittext);
        passwordEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    vm.setPasswordCleanVisibility(View.VISIBLE);
                }else{
                    vm.setPasswordCleanVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public void onNext() {
        presenter.loginByEmail(vm.getLoginEmail(), vm.getLoginPassWord());
    }

    @Override
    public void onBack() {

    }

    @Override
    public void onTaskStart(int taskId) {
        popupDialog = new MaterialDialog.Builder(getContext())
                .title(R.string.logging)
                .progress(true, 0)
                .show();
    }

    @Override
    public void onTaskSuccess(int taskId, Object data) {
        popupDialog.dismiss();
        EventBus.getDefault().post(new MessageEvent(MessageEvent.LOGIN_SUCCESS));
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onTaskError(int taskId, Object data) {
        popupDialog.dismiss();
        Toast.makeText(getContext(),"Login Failed /(ㄒoㄒ)/~~",Toast.LENGTH_LONG).show();
    }

    @Override
    public void toResetPassword() {
        LoginResetPasswordFragment fragment = new LoginResetPasswordFragment();
        getBaseActivity().openFragment(fragment);
    }

    @Override
    public void onStart() {
        super.onStart();
//        AutoCompleteTextView t = (AutoCompleteTextView) getActivity().findViewById(R.id.auto_complete_email);
//        InputMethodManager inputMethodManager =
//                (InputMethodManager)getContext().getSystemService(getContext().INPUT_METHOD_SERVICE);
//        inputMethodManager.toggleSoftInputFromWindow(
//                t.getApplicationWindowToken(),
//                InputMethodManager.SHOW_FORCED, 0);
    }
}
