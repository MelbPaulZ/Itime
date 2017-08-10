package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.databinding.BindingMethod;
import android.databinding.BindingMethods;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.databinding.library.baseAdapters.BR;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.unimelb.itime.R;
import org.unimelb.itime.databinding.MenuEventDetailToolbarBinding;
import org.unimelb.itime.databinding.ToolbarCollapseHeadbarBinding;
import org.unimelb.itime.ui.viewmodel.event.EventDetailViewModel;
import org.unimelb.itime.util.SizeUtil;
import org.unimelb.itime.widget.popupmenu.ModalPopupView;
import org.unimelb.itime.widget.popupmenu.PopupMenu;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Qiushuo Huang on 2017/5/14.
 */

@BindingMethods({
        @BindingMethod(type = CollapseHeadBar.class,
                attribute = "app:collapseheaderbar_name",
                method = "setName"),
        @BindingMethod(type = CollapseHeadBar.class,
                attribute = "app:collapseheaderbar_title",
                method = "setTitle"),
        @BindingMethod(type = CollapseHeadBar.class,
                attribute = "app:collapseheaderbar_avatar",
                method = "setAvatar"),
        @BindingMethod(type = CollapseHeadBar.class,
                attribute = "app:collapseheaderbar_backgroundimg",
                method = "setBackgroundImage"),
        @BindingMethod(type = CollapseHeadBar.class,
                attribute = "app:collapseheaderbar_onLeftClick",
                method = "setOnLeftClickListener"),
        @BindingMethod(type = CollapseHeadBar.class,
                attribute = "app:collapseheaderbar_onMenuItemClick",
                method = "setOnMenuItemClickListener"),
        @BindingMethod(type = CollapseHeadBar.class,
                attribute = "app:collapseheaderbar_menuItems",
                method = "setMenuItems"),
        @BindingMethod(type = CollapseHeadBar.class,
        attribute = "app:collapseheaderbar_inviteeCount",
        method = "setInviteeCount")
})

public class CollapseHeadBar extends AppBarLayout {
    private static final float PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR  = 0.9f;
    private static final float PERCENTAGE_TO_HIDE_TITLE_DETAILS     = 0.5f;
    private static final int ALPHA_ANIMATIONS_DURATION              = 200;
    private String trans_color = "#7F3C3C3C";

    private boolean mIsTheTitleVisible          = false;
    private boolean mIsTheTitleContainerVisible = true;
    private List<PopupMenu.Item> items;
    private Target backTarget;
    private CollapsingToolbarLayout contentView;
    private ImageView avatarView;
    private TextView smallTitle;
    private TextView nameView;
    private TextView bigTitleView;
    private LinearLayout statusIcons;
    private TextView inviteeCountView;
    private View leftButton;
    private View rightButton;
    private ModalPopupView menu;
    private int invieeCount;

    private String avatar="";
    private String backgroundImage ="";
    private String title="";
    private String name="";
    private int collapseColor;
    private EventDetailViewModel vm;
    private ToolbarCollapseHeadbarBinding binding;
    private MenuEventDetailToolbarBinding menuBinding;
    private OnMenuClickListener onMenuClickListener;

    public CollapseHeadBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        final TypedArray a = getContext().obtainStyledAttributes(attrs,
                R.styleable.CollapseHeadBar);
        avatar = a.getString(
                R.styleable.CollapseHeadBar_collapseheaderbar_avatar);
        name = a.getString(
                R.styleable.CollapseHeadBar_collapseheaderbar_name);
        title = a.getString(
                R.styleable.CollapseHeadBar_collapseheaderbar_title);
//        backgroundImage = a.getString(
//                R.styleable.CollapseHeadBar_collapseheaderbar_backgroundimg);
        backgroundImage = "http://cdn2.jianshu.io/assets/default_avatar/9-cceda3cf5072bcdd77e8ca4f21c40998.jpg?imageMogr2/auto-orient/strip|imageView2/1/w/144/h/144";
        invieeCount = a.getInt(
                R.styleable.CollapseHeadBar_collapseheaderbar_inviteeCount, 0);
        collapseColor = getResources().getColor(R.color.lightBlueTwo);
        a.recycle();

        initContentView();
    }

    private void initContentView(){
        binding =  DataBindingUtil.inflate(LayoutInflater.from(getContext()),R.layout.toolbar_collapse_headbar, this, false);
        if(vm!=null){
            binding.setContentVM(vm);
        }
        contentView = (CollapsingToolbarLayout) binding.getRoot();
//        setContentScrim(new ColorDrawable(Color.parseColor("#6E000000")));
        avatarView = (ImageView) contentView.findViewById(R.id.avatarView);
        smallTitle = (TextView) contentView.findViewById(R.id.smallTitle);
        bigTitleView = (TextView) contentView.findViewById(R.id.bigTitle);
        nameView = (TextView) contentView.findViewById(R.id.nameTextView);
        statusIcons = (LinearLayout) contentView.findViewById(R.id.statusIcons);
        inviteeCountView = (TextView) contentView.findViewById(R.id.inviteeCount);

        if(inviteeCountView.getText().toString().isEmpty()){
            inviteeCountView.setVisibility(GONE);
        }

        leftButton = contentView.findViewById(R.id.leftButton);
        rightButton = contentView.findViewById(R.id.rightButton);

        initMenu();

        setTitle(title);
        setName(name);
        setInvieeCount(invieeCount);
        loadAvatar();
        loadBackground();
        this.addView(contentView);

        this.addOnOffsetChangedListener(new OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                int maxScroll = appBarLayout.getTotalScrollRange();
                float percentage = (float) Math.abs(verticalOffset) / (float) maxScroll;

                handleAlphaOnTitle(percentage);
                handleToolbarTitleVisibility(percentage);
            }
        });
    }

    private void setContentScrim(Drawable scrimDrawable){
        contentView.setContentScrim(scrimDrawable);
    }

    private void loadAvatar(){
        if(avatar==null||avatar.isEmpty()){
            Picasso.with(avatarView.getContext()).load(R.drawable.invitee_selected_default_picture).into(avatarView);
        }else
            Picasso.with(avatarView.getContext()).load(avatar).placeholder(R.drawable.invitee_selected_default_picture)
                    .error(R.drawable.invitee_selected_default_picture).resize(100,100).centerCrop().into(avatarView);
    }

    private void initMenu(){
        menu = new ModalPopupView(getContext());
        menu.setBackground(getResources().getDrawable(R.color.mask_cover));
        menuBinding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.menu_event_detail_toolbar, new FrameLayout(getContext()), false);
        menuBinding.setVm(vm);
        onMenuClickListener = new OnMenuClickListener();
        onMenuClickListener.setVm(vm);
        menuBinding.setListener(onMenuClickListener);
        menu.setContentView(menuBinding.getRoot());
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.showAtLocation(rightButton, 0, 10);
            }
        });
    }

    private void loadBackground(){

        this.setBackgroundColor(getResources().getColor(R.color.lightblue));

        if(backgroundImage ==null|| backgroundImage.isEmpty()){

        }else {
            backTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    BitmapDrawable drawable = new BitmapDrawable(bitmap);
                    setBlurBackground(drawable);
                    Drawable trans = new ColorDrawable(Color.parseColor(trans_color));
                    LayerDrawable backDrawable = new LayerDrawable(new Drawable[] {drawable, trans});
                    CollapseHeadBar.this.setBackground(backDrawable);
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {

                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };

            Picasso.with(getContext()).load(backgroundImage)
                    .resize(getResources().getDisplayMetrics().widthPixels, SizeUtil.dip2px(getContext(), 225))
                    .centerCrop()
                    .into(backTarget);
        }
    }

    private void setBlurBackground(final Drawable drawable){


//        Observable<Drawable> drawableObservable = Observable.create(new Observable.OnSubscribe<Drawable>() {
//
//            @Override
//            public void call(Subscriber<? super Drawable> subscriber) {
//                Drawable scrim = BlurredBitmap.getInstance(getContext()).loadDrawable(drawable).radius(100).buildBlurredDrawable();
//                Drawable trans = new ColorDrawable(Color.parseColor(trans_color));
//                LayerDrawable scrimDrawable = new LayerDrawable(new Drawable[] {scrim, trans});
//                subscriber.onNext(scrimDrawable);
//                subscriber.onCompleted();
//            }
//        });
//
//        Subscriber<Drawable> subscriber = new Subscriber<Drawable>() {
//            @Override
//            public void onCompleted() {
//
//            }
//
//            @Override
//            public void onError(Throwable e) {
//
//            }
//
//            @Override
//            public void onNext(Drawable drawable) {
//                setContentScrim(drawable);
//            }
//        };
//
//        drawableObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(subscriber);
    }

    private void handleToolbarTitleVisibility(float percentage) {
        if (percentage >= PERCENTAGE_TO_SHOW_TITLE_AT_TOOLBAR) {

            if(!mIsTheTitleVisible) {
                startAlphaAnimation(smallTitle, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleVisible = true;
            }

        } else {

            if (mIsTheTitleVisible) {
                startAlphaAnimation(smallTitle, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleVisible = false;
            }
        }
    }

    private void handleAlphaOnTitle(float percentage) {
        if (percentage >= PERCENTAGE_TO_HIDE_TITLE_DETAILS) {
            if(mIsTheTitleContainerVisible) {
                startAlphaAnimation(bigTitleView, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                startAlphaAnimation(statusIcons, ALPHA_ANIMATIONS_DURATION, View.INVISIBLE);
                mIsTheTitleContainerVisible = false;
            }

        } else {

            if (!mIsTheTitleContainerVisible) {
                startAlphaAnimation(bigTitleView, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                startAlphaAnimation(statusIcons, ALPHA_ANIMATIONS_DURATION, View.VISIBLE);
                mIsTheTitleContainerVisible = true;
            }
        }
    }

    public static void startAlphaAnimation (View v, long duration, int visibility) {
        AlphaAnimation alphaAnimation = (visibility == View.VISIBLE)
                ? new AlphaAnimation(0f, 1f)
                : new AlphaAnimation(1f, 0f);

        alphaAnimation.setDuration(duration);
        alphaAnimation.setFillAfter(true);
        v.startAnimation(alphaAnimation);
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
        loadAvatar();
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
        loadBackground();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        smallTitle.setText(title);
        bigTitleView.setText(title);
//        contentView.setTitle(title);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        String nameStr = getResources().getString(R.string.event_detail_header_hosted_by)+" "+name;
        nameView.setText(nameStr);
    }

    public void setOnLeftClickListener(View.OnClickListener listener){
        if(leftButton!=null){
            leftButton.setOnClickListener(listener);
        }
    }

    public void setInvieeCount(int invieeCount) {
        this.invieeCount = invieeCount;
        String countStr = "";
        if(invieeCount!=0){
            countStr = "("+invieeCount+" "+ getResources().getString(R.string.event_detail_header_invitees)+")";
            inviteeCountView.setText(countStr);
            inviteeCountView.setVisibility(VISIBLE);
        }
    }

    public void setViewModel(EventDetailViewModel vm) {
        this.vm = vm;
        if(binding!=null){
            binding.setContentVM(vm);
        }

        if(menuBinding!=null){
            menuBinding.setVm(vm);
        }

        if(onMenuClickListener!=null){
            onMenuClickListener.setVm(vm);
        }
    }

    public class OnMenuClickListener{
        private EventDetailViewModel vm;

        public OnClickListener onMuteClick(){
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vm!=null){
                        vm.onMuteClick().onClick(view);
                    }
                    if(menu!=null) {
                        menu.dismiss();
                    }
                }
            };
        }

        public OnClickListener onPinClick(){
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vm!=null){
                        vm.onPinClick().onClick(view);
                    }
                    if(menu!=null) {
                        menu.dismiss();
                    }
                }
            };
        }

        public OnClickListener onArchiveClick(){
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vm!=null){
                        vm.onArchiveClick().onClick(view);
                    }
                    if(menu!=null) {
                        menu.dismiss();
                    }
                }
            };
        }

        public OnClickListener onShareClick(){
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vm!=null){
                        vm.onShareClick().onClick(view);
                    }
                    if(menu!=null) {
                        menu.dismiss();
                    }
                }
            };
        }

        public OnClickListener onRemindClick(){
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vm!=null){
                        vm.onRemindClick().onClick(view);
                    }
                    if(menu!=null) {
                        menu.dismiss();
                    }
                }
            };
        }

        public OnClickListener onHistoryClick(){
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vm!=null){
                        vm.onHistoryClick().onClick(view);
                    }
                    if(menu!=null) {
                        menu.dismiss();
                    }
                }
            };
        }

        public OnClickListener onDuplicateClick(){
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vm!=null){
                        vm.onDuplicateClick().onClick(view);
                    }
                    if(menu!=null) {
                        menu.dismiss();
                    }
                }
            };
        }

        public OnClickListener onDeleteClick(){
            return new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(vm!=null){
                        vm.onDeleteClick().onClick(view);
                    }
                    if(menu!=null) {
                        menu.dismiss();
                    }
                }
            };
        }

        public void setVm(EventDetailViewModel vm) {
            this.vm = vm;
        }
    }
}
