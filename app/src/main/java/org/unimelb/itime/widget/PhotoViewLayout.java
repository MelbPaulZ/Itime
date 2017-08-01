package org.unimelb.itime.widget;
import android.content.Context;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.unimelb.itime.R;
import org.unimelb.itime.bean.PhotoUrl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.squareup.picasso.MemoryPolicy.NO_CACHE;
import static com.squareup.picasso.MemoryPolicy.NO_STORE;

/**
 * Created by Paul on 29/4/17.
 */

public class PhotoViewLayout extends ViewGroup {
    private int height, width;
    private String TAG = "PhotoLayout";
    private static final int PADDING = 10;
    private List<ImageView> imageList;
    private List<PhotoUrl> photoUrlList;
    private ImageClickListener imageClickListener;
    private DisplayMetrics displaysMetrics;
    private int bigSize;
    private int smallSize;
    private static final int INIT_SIZE = 250;

    public PhotoViewLayout(Context context) {
        super(context);
        init();
    }

    public PhotoViewLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        displaysMetrics = getContext().getResources().getDisplayMetrics();
        imageList = new ArrayList<>();
    }

    public void setImageClickListener(ImageClickListener imageClickListener){
        this.imageClickListener = imageClickListener;
    }


    public void setPhotoUrls(List<PhotoUrl> photoUrlList) {
        this.photoUrlList = photoUrlList;
        updatePhotoUrl();
        requestLayout();
    }

    private void updatePhotoUrl(){
        this.removeAllViews();
        imageList.clear();
        int count = (photoUrlList.size()<3)? photoUrlList.size():6;
            for(int i = 0; i < count; i++) {
                ImageView imageView = new ImageView(getContext());
                imageList.add(imageView);
                addView(imageView);
                if(i<photoUrlList.size()) {
                    loadImage(imageView, photoUrlList.get(i));
                }else{
                    loadImage(imageView, null);
                }
            }
    }

    private void loadImage(ImageView view, PhotoUrl url){
        Context context = view.getContext();

        if(url==null){
            Picasso.with(context)//
                    .load(R.drawable.icon_details_photo_placeholder)//
                    .resize(INIT_SIZE, INIT_SIZE)//
                    .centerCrop()//
                    .into(view);
            return;
        }

        if (!url.getUrl().equals("")){
            Picasso.with(context)//
                    .load(url.getUrl())//
                    .placeholder(R.drawable.icon_details_photo_placeholder)//
                    .error(R.drawable.icon_details_photo_placeholder)//
                    .memoryPolicy(NO_CACHE, NO_STORE)
                    .resize(INIT_SIZE, INIT_SIZE)//
                    .centerCrop()//
                    .into(view);
        }else if (!url.getLocalPath().equals("")){
            File file = new File(url.getLocalPath());
            if (file.exists()){
                Picasso.with(context)//
                        .load(new File(url.getLocalPath()))//
                        .placeholder(R.drawable.icon_details_photo_placeholder)//
                        .error(R.drawable.icon_details_photo_placeholder)//
                        .memoryPolicy(NO_CACHE, NO_STORE)
                        .resize(INIT_SIZE, INIT_SIZE)//
                        .centerCrop()//
                        .into(view);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = (sizeWidth-2*PADDING)/3;

        bigSize = sizeHeight;
        smallSize = (sizeHeight-PADDING)/2;

        int bigMeasureSpec = MeasureSpec.makeMeasureSpec(bigSize, MeasureSpec.EXACTLY);
        int smallMeasureSpec = MeasureSpec.makeMeasureSpec(smallSize, MeasureSpec.EXACTLY);

        for(int i=0;i<getChildCount();i++){
            View child = getChildAt(i);
            if(i<2){
                child.measure(bigMeasureSpec, bigMeasureSpec);
            }else{
                child.measure(smallMeasureSpec, smallMeasureSpec);
            }
        }
        setMeasuredDimension(sizeWidth, sizeHeight);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for(int i=0;i<getChildCount();i++){
            ImageView child = (ImageView) getChildAt(i);
            if(i==0){
              child.layout(0, 0, bigSize, bigSize);
            }

            if(i==1) {
                child.layout(bigSize+PADDING, 0, bigSize+PADDING+bigSize, bigSize);
            }

            if(i==2) {
                child.layout((bigSize+PADDING)*2, 0, smallSize+(bigSize+PADDING)*2, smallSize);
            }

            if(i==3) {
                child.layout((bigSize+PADDING)*2+smallSize+PADDING, 0, smallSize*2+PADDING+(bigSize+PADDING)*2, smallSize);
            }

            if(i==4) {
                child.layout((bigSize+PADDING)*2, smallSize+PADDING, smallSize+(bigSize+PADDING)*2, smallSize*2+PADDING);
            }

            if(i==5) {
                child.layout((bigSize+PADDING)*2+smallSize+PADDING, smallSize+PADDING,  smallSize*2+PADDING+(bigSize+PADDING)*2, smallSize*2+PADDING);
            }
        }
    }

    public interface ImageClickListener{
        void onBigImageClick(ImageView view, int position);
        void onSmallImageClick();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction()== MotionEvent.ACTION_UP){
            for(int i=0;i<getChildCount();i++){
                if(i<2){
                    if(isIn(getChildAt(i), event)){
                        imageClickListener.onBigImageClick((ImageView) getChildAt(i), i);
                        return true;
                    }
                }else{
                    break;
                }
            }
            if(clickSmallImage(event)){
                imageClickListener.onSmallImageClick();
            }
        }
        return true;
    }

    private boolean clickSmallImage(MotionEvent event){
        if(getChildCount()<=2){
           return false;
        }

        int x = (int) event.getX();
        int y = (int) event.getY();
        int l = (bigSize+PADDING)*2;
        int r = getWidth()-PADDING;
        int t = PADDING;
        int b = getHeight()-PADDING;

        if(x<=r && x>=l && y>=t && y<=b){
            return true;
        }
        return false;
    }

    private boolean isIn(View view, MotionEvent ev){
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        RectF rectF =  new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());

        return rectF.contains(ev.getRawX(), ev.getRawY());
    }
}