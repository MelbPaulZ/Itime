package org.unimelb.itime.ui.viewmodel;

import android.animation.Animator;
import android.content.Context;
import android.databinding.BaseObservable;
import android.databinding.BindingAdapter;
import android.graphics.Bitmap;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.PhotoUrl;
import org.unimelb.itime.widget.OnRecyclerItemClickListener;

import java.io.File;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by Qiushuo Huang on 2016/12/15.
 */

public class BindLoader extends BaseObservable {

    @BindingAdapter("bind:qrcode")
    public static void loadQRCode(ImageView iv, Bitmap img) {
        iv.setImageBitmap(img);
    }

    @BindingAdapter("bind:smallAvatar")
    public static void loadSmallAvartar(ImageView iv, String img) {
        if(img==null||img.isEmpty()){
            Picasso.with(iv.getContext()).load(R.drawable.invitee_selected_default_picture).into(iv);
        }else
            Picasso.with(iv.getContext()).load(img).placeholder(R.drawable.invitee_selected_default_picture)
                .error(R.drawable.invitee_selected_default_picture).resize(100,100).centerCrop().into(iv);
    }

    @BindingAdapter("bind:avatar")
    public static void bindAvatar(ImageView view, int img){
        Picasso.with(view.getContext()).load(img).placeholder(R.drawable.invitee_selected_default_picture)
                .error(R.drawable.invitee_selected_default_picture).into(view);
    }

    @BindingAdapter("bind:onBoarding")
    public static void bindOnBoarding(ImageView view, int img){
        Picasso.with(view.getContext()).load(img).resize(375, 514).into(view);
    }

    @BindingAdapter("bind:textWatcher")
    public static void bindTextWatcher(EditText view, TextWatcher watcher){
        view.addTextChangedListener(watcher);
    }

//    @BindingAdapter("bind:badgeCount")
//    public static void setBadgeCount(WideBadgeArrowButton view, int count){
//        view.setBadgeCount(count);
//    }

    @BindingAdapter("bind:onItemClickListener")
    public static void setOnItemClickListener(ListView view, AdapterView.OnItemClickListener listener){
        view.setOnItemClickListener(listener);
    }

    @BindingAdapter("bind:onItemClickListener")
    public static void setOnRecyclerItemClickListener(RecyclerView view, OnRecyclerItemClickListener.OnItemClickListener listener){
        OnRecyclerItemClickListener onRecyclerItemClickListener = new OnRecyclerItemClickListener(view, listener);
        view.addOnItemTouchListener(onRecyclerItemClickListener);
    }

    @BindingAdapter("bind:onPageChangeListener")
    public static void setOnPageChangeListener(ViewPager view, ViewPager.OnPageChangeListener listener){
        view.setOnPageChangeListener(listener);
    }

    @BindingAdapter("bind:photo")
    public static void bindPhoto(ImageView view, PhotoUrl url){
        Context context = view.getContext();

        DisplayMetrics displaysMetrics = new DisplayMetrics();
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(displaysMetrics);

        if (!url.getUrl().equals("")){
            Picasso.with(context)//
                    .load(url.getUrl())//
                    .placeholder(R.drawable.icon_details_photo_placeholder)//
                    .error(R.drawable.icon_details_photo_placeholder)//
                    .memoryPolicy(NO_CACHE, NO_STORE)
                    .resize(displaysMetrics.widthPixels, displaysMetrics.heightPixels)//
                    .centerInside()//
                    .into(view);
        }else if (!url.getLocalPath().equals("")){
            File file = new File(url.getLocalPath());
            if (file.exists()){
                Picasso.with(context)//
                        .load(new File(url.getLocalPath()))//
                        .placeholder(R.drawable.icon_details_photo_placeholder)//
                        .error(R.drawable.icon_details_photo_placeholder)//
                        .memoryPolicy(NO_CACHE, NO_STORE)
                        .resize(displaysMetrics.widthPixels, displaysMetrics.heightPixels)//
                        .centerInside()//
                        .into(view);
            }
        }
    }

    @BindingAdapter("bind:smallPhoto")
    public static void bindSmallPhoto(ImageView view, PhotoUrl url){
        Context context = view.getContext();
        if (!url.getUrl().equals("")){
            Picasso.with(context)//
                    .load(url.getUrl())//
                    .placeholder(R.drawable.invitee_selected_default_picture)//
                    .error(R.drawable.invitee_selected_default_picture)//
                    .memoryPolicy(NO_CACHE, NO_STORE)
                    .resize(100, 100)//
                    .centerCrop()
                    .into(view);
        }else if (!url.getLocalPath().equals("")){
            File file = new File(url.getLocalPath());
            if (file.exists()){
                Picasso.with(context)//
                        .load(new File(url.getLocalPath()))//
                        .placeholder(R.drawable.invitee_selected_default_picture)//
                        .error(R.drawable.invitee_selected_default_picture)//
                        .memoryPolicy(NO_CACHE, NO_STORE)
                        .resize(100, 100)//
                        .centerCrop()
                        .into(view);
            }
        }
    }

    /**
     *  Time is milliseconds
     * @param view
     * @param show
     * @param time
     */
    @BindingAdapter({"bind:showTips", "bind:tipsDuration"})
    public static void bindTips(final View view, Boolean show, final int time){
        AlphaAnimation appearAnimation = new AlphaAnimation(0, 1);
        appearAnimation.setDuration(500);

        final AlphaAnimation disappearAnimation = new AlphaAnimation(1, 0);
        disappearAnimation.setDuration(500);

        if(show){
            view.startAnimation(appearAnimation);
            view.setVisibility(View.VISIBLE);

            Observable<Boolean> observable = Observable.create(new Observable.OnSubscribe<Boolean>() {
                @Override
                public void call(Subscriber<? super Boolean> subscriber) {
                    try {
                        Thread.sleep(time);
                    } catch (InterruptedException e) {
                        subscriber.onError(e);
                    }
                    subscriber.onCompleted();
                }
            });

            Subscriber<Boolean> subscriber = new Subscriber<Boolean>() {
                @Override
                public void onCompleted() {
                    view.startAnimation(disappearAnimation);
                    disappearAnimation.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}

                        @Override
                        public void onAnimationRepeat(Animation animation) {}

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            view.setVisibility(View.GONE);
                        }
                    });
                }

                @Override
                public void onError(Throwable e) {
                }

                @Override
                public void onNext(Boolean bool) {

                }
            };
            observable.subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(subscriber);

        }
    }

    @BindingAdapter("bind:showTips")
    public static void bindTips(final View view, Boolean show){
        if(view==null){
            return;
        }

        if(show){
            view.setVisibility(View.VISIBLE);
            YoYo.with(Techniques.BounceIn)
                    .duration(1200)
                    .repeat(1)
                    .onEnd(new YoYo.AnimatorCallback() {
                        @Override
                        public void call(Animator animator) {

                        }
                    })
                    .playOn(view);
        }else{
            if(view.getVisibility()== View.VISIBLE) {
                YoYo.with(Techniques.ZoomOut)
                        .duration(700)
                        .repeat(1)
                        .onEnd(new YoYo.AnimatorCallback() {
                            @Override
                            public void call(Animator animator) {
                                view.setVisibility(View.GONE);
                            }
                        })
                        .playOn(view);
            }
        }
    }
}










