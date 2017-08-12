package org.unimelb.itime.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.github.sundeepk.compactcalendarview.ITimeTimeslotCalendar;

import org.unimelb.itime.R;
import org.unimelb.itime.util.SizeUtil;

/**
 * Created by Paul on 10/8/17.
 */

public class BadgeView extends View {

    private String number = "";
    private int numberColor = Color.WHITE;
    private int bgColor = Color.RED;

    private Paint paint = new Paint();
    Rect rect = new Rect();

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.getClipBounds(rect);

        paint.setColor(bgColor);
        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2 ,paint);

        paint.setColor(numberColor);
        float[] coor = getCoordinate();
        canvas.drawText(number,  coor[0], coor[1], paint);
    }

    private float[] getCoordinate(){
        float[] coor = new float[2];
        int width = rect.width();
        int height = rect.height();

        paint.getTextBounds(number, 0, number.length(),  rect);

        coor[0] = (width - rect.width())/2 - rect.left ;
        coor[1] = (height + rect.height())/2 - rect.bottom;
        return coor;
    }

    public BadgeView(Context context) {
        super(context);
        init();
    }

    public BadgeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        loadAttributes(attrs, context);
        init();
    }



    private void loadAttributes(AttributeSet attrs, Context context) {
        if(attrs != null && context != null) {
            TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.BadgeView, 0, 0);

            try {
                this.numberColor = typedArray.getColor(R.styleable.BadgeView_numberColor, this.numberColor);
                this.bgColor = typedArray.getColor(R.styleable.BadgeView_bgColor, this.bgColor);
                this.number = typedArray.getString(R.styleable.BadgeView_number);
            } finally {
                typedArray.recycle();
            }
        }

    }

    private void init(){
        paint.setTextSize(SizeUtil.dip2px(getContext(), 12));
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.LEFT);

    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getNumberColor() {
        return numberColor;
    }

    public void setNumberColor(int numberColor) {
        this.numberColor = numberColor;
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
    }
}
