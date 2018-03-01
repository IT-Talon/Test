package com.example.talon.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Talon on 2018/2/28.
 */

public class WaveView extends View {

    private final static String TAG = WaveView.class.getSimpleName();

    /**
     * 波浪从屏幕外开始，在屏幕外结束，这样效果更真实
     */
    private static final float EXTRA_DISTANCE = 200;


    private Paint mPaint;
    private Path mPath;
    private int mWidth, mHeight;
    private float mControlX, mControlY;
    private float mWaveY;
    private boolean mMoveControl;


    public WaveView(Context context) {
        super(context);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WaveView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.GREEN);

        mPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.d(TAG, "onMeasure");
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        Log.d(TAG, "onLayout");
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.d(TAG, "onSizeChanged");
        mWidth = w;
        mHeight = h;

        mControlY = mHeight - mHeight / 8;
        mWaveY = mHeight - mHeight / 32;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.d(TAG, "onDraw");
        mPath.moveTo(-EXTRA_DISTANCE, mWaveY);
        mPath.quadTo(mControlX, mControlY, mWidth + EXTRA_DISTANCE, mWaveY);
        mPath.lineTo(mWidth, mHeight);
        mPath.lineTo(0, mHeight);
        mPath.close();
        canvas.drawPath(mPath, mPaint);

        if (mControlX <= -EXTRA_DISTANCE) {
            mMoveControl = true;
        } else if (mControlX >= mWidth + EXTRA_DISTANCE) {
            mMoveControl = false;
        }
        mControlX = mMoveControl ? mControlX + 20 : mControlX - 20;

        if (mControlY >= 0) {
            mControlY -= 1;
            mWaveY -= 1;
        }
        Log.d(TAG, "mControlX: " + mControlX + " mControlY: " + mControlY + " mWaveY: " + mWaveY);
        mPath.reset();
        invalidate();
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        Log.d(TAG, "draw");
    }
}
