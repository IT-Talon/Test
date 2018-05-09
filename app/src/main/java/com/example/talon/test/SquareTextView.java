package com.example.talon.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import java.util.Random;

/**
 * Created by Talon on 2018/4/8.
 * 加正方形背景TextView
 */
public class SquareTextView extends android.support.v7.widget.AppCompatTextView {

    private Paint mPaint;
    private RectF mRect;

    public SquareTextView(Context context) {
        super(context);
        init();
    }

    public SquareTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SquareTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // 随机颜色
        Random random = new Random();
        int ranColor = 0xff000000 | random.nextInt(0x00ffffff);
        mPaint.setColor(ranColor);
        mRect = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // https://stackoverflow.com/questions/39256539/custom-android-square-textview-text-is-not-centered 正方形
        int width = this.getMeasuredWidth();
        int height = this.getMeasuredHeight();
        int size = Math.max(width, height);
        int widthSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        int heightSpec = MeasureSpec.makeMeasureSpec(size, MeasureSpec.EXACTLY);
        super.onMeasure(widthSpec, heightSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int a = Math.max(getMeasuredWidth(), getMeasuredHeight()); //a边长
        mRect.set(0, 0, a, a); // 画正方形
        canvas.drawRect(mRect, mPaint);
        super.onDraw(canvas);
    }
}
