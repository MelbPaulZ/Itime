package org.unimelb.itime.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.zhy.m.permission.MPermissions;

/**
 * Created by Paul on 2/6/17.
 */

public abstract class ItimeBaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> {

    private Fragment from;

    public ItimeBaseActivity getBaseActivity(){
        return (ItimeBaseActivity) getActivity();
    }
    protected ItimeBaseActivity baseActivity;
    protected ProgressDialog progressDialog;
    public Fragment getFrom() {
        return from;
    }

    public void setFrom(Fragment from) {
        this.from = from;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        baseActivity = (ItimeBaseActivity)getActivity();
        progressDialog = new ProgressDialog(getActivity());

        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        getBaseActivity().setCurFragment(this);
    }

    protected void showDialog(String title, String msg){
//        new me.fesky.library.widget.ios.AlertDialog(getActivity())
//                .builder()
//                .setTitle(title)
//                .setMsg(msg)
//                .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//
//                    }
//                }).show();
    }


//    protected me.fesky.library.widget.ios.AlertDialog getCustomisedDialog(){
//        return new me.fesky.library.widget.ios.AlertDialog(getActivity())
//                .builder();
//    }

    protected void showTwoButtonDialog(
            String title,
            String negBtnStr,
            View.OnClickListener negClickListener,
            String posBtnStr,
            View.OnClickListener posClickListener){
//        new me.fesky.library.widget.ios.AlertDialog(getActivity())
//                .builder()
//                .setTitle(title)
//                .setNegativeButton(negBtnStr, negClickListener)
//                .setPositiveButton(posBtnStr, posClickListener)
//                .show();
    }

    protected void showToggleTwoButtonDialog(
            String title,
            String negBtnStr,
            View.OnClickListener negClickListener,
            String posBtnStr,
            View.OnClickListener posClickListener){
//        new me.fesky.library.widget.ios.AlertDialog(getActivity())
//                .builder()
//                .toggle()
//                .setTitle(title)
//                .setNegativeButton(negBtnStr, negClickListener)
//                .setPositiveButton(posBtnStr, posClickListener)
//                .show();
    }



    /**
     * this method is for customizing show dialog on click listener
     * @param title
     * @param msg
     * @param onClickListener
     */
    protected void showDialog(String title, String msg, View.OnClickListener onClickListener){
//        new me.fesky.library.widget.ios.AlertDialog(getActivity())
//                .builder()
//                .setTitle(title)
//                .setMsg(msg)
//                .setPositiveButton(getString(R.string.ok), onClickListener).show();
    }



    public void showToast(String msg){
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    public void showProgressDialog(){
        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgressDialog(){
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    protected void cleanAllStack(){
        getFragmentManager().popBackStack();
    }

    public void cleanStack(){
        int len = getFragmentManager().getBackStackEntryCount();
        for (int i = 0; i < len-1; i++){
            String name = getFragmentManager().getBackStackEntryAt(i).getName();
            Fragment frag = getFragmentManager().findFragmentByTag(name);
            getFragmentManager().beginTransaction().remove(frag).commit();
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        // hide soft key board
        if (!hidden) {
            final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            if (getView() != null) {
                imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
            }
        }
    }

    /**
     * Added by Qiushuo Huang
     * 判断是否拥有权限
     * 说明：
     * String... permissions
     * 形参String...的效果其实就和数组一样，这里的实参可以写多个String,也就是权限
     *
     * @param permissions
     * @return
     */
    public boolean hasPermission(String... permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(getContext(), permission) != PackageManager.PERMISSION_GRANTED)
                return false;
        }
        return true;
    }

    /**
     * Added by Qiushuo Huang
     * @param code
     * @param permissions
     */
    public void requestPermission(int code, String... permissions) {
        requestPermissions(permissions, code);
    }

    /**
     * Added by Qiushuo Huang
     * @param grantResults all requirements
     * @return true if all granted, otherwise false
     */
    public boolean allPermissionGranted(int[] grantResults) {
        int size = grantResults.length;
        for (int i = 0; i < size; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        MPermissions.onRequestPermissionsResult(this, requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    public void hideSoftKeyBoard(){
        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getView() != null) {
            imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
        }
    }



}