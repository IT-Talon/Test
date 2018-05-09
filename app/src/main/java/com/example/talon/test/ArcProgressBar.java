package com.example.talon.test;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Talon on 2018/3/5.
 */

public class ArcProgressBar extends View {
    private static final int DEFAULT_SIZE = 24;
    private static final int DEFAULT_WIDTH = 50;
    private int mtextSize;
    private int mBackgroundWidth;
    private int mCoverWidth;
    private int mTextColor;
    private int mBackgroundColor;
    private int mCoverColor;
    private Paint mTextPaint, mBackgroundPaint, mCoverPaint; // 文字画笔、圆弧背景画笔、圆弧画笔
    private Path mBackgroundPath, mCoverPath; // 路径

    private RectF rect;
    private int maxAngle = 180; //最大弧度

    private int percent; // 滑动多少百分比

    public ArcProgressBar(Context context) {
        this(context, null);

    }

    public ArcProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcProgressBar, defStyleAttr, R.style.AppTheme);

        mtextSize = a.getDimensionPixelSize(R.styleable.ArcProgressBar_text_size, DEFAULT_SIZE);
        mBackgroundWidth = a.getDimensionPixelSize(R.styleable.ArcProgressBar_background_width, DEFAULT_WIDTH);
        mCoverWidth = a.getDimensionPixelSize(R.styleable.ArcProgressBar_cover_width, DEFAULT_WIDTH);
        mTextColor = a.getColor(R.styleable.ArcProgressBar_text_color, Color.BLACK);
        mBackgroundColor = a.getColor(R.styleable.ArcProgressBar_text_color, Color.GRAY);
        mCoverColor = a.getColor(R.styleable.ArcProgressBar_text_color, Color.GREEN);
        a.recycle();
        init();
    }

    private void init() {
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mtextSize);

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);
        mBackgroundPaint.setStyle(Paint.Style.STROKE);
        mBackgroundPaint.setStrokeWidth(mBackgroundWidth);
        mBackgroundPaint.setStrokeCap(Paint.Cap.ROUND);

        mCoverPaint = new Paint(Paint.ANTI_ALIAS_FLAG | Paint.DITHER_FLAG);
        mCoverPaint.setColor(mCoverColor);
        mCoverPaint.setStyle(Paint.Style.STROKE);
        mCoverPaint.setStrokeWidth(mCoverWidth);
        mCoverPaint.setStrokeCap(Paint.Cap.ROUND);

        rect = new RectF();

        mBackgroundPath = new Path();
        mCoverPath = new Path();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        measure(widthMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measureHeight(heightMeasureSpec));
    }

    private int measureWidth(int widthMeasureSpec) {
        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        if (widthSpecMode == MeasureSpec.EXACTLY) {
            return widthSpecSize;
        } else {
            return 200;
        }
    }

    private int measureHeight(int heightMeasureSpec) {
        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        if (heightSpecMode == MeasureSpec.EXACTLY) {
            return heightSpecSize;
        } else {
            return 100;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        rect.set(0 + mBackgroundWidth / 2, 0 + mBackgroundWidth / 2, getWidth() - mBackgroundWidth / 2, getHeight() - mBackgroundWidth / 2);
        drawBackgroundArc(canvas);
        drawArc(canvas);
        drawText(canvas);
        Log.d("Talon", "onDraw执行了---");
    }

    private void drawText(Canvas canvas) {
        canvas.drawText(percent + "", (getWidth() - mTextPaint.measureText(percent + "")) / 2, getHeight() / 2, mTextPaint);
    }

    private void drawArc(Canvas canvas) {
//        canvas.drawArc(rect, -180, 60, false, mCoverPaint);
        float sweepAngle = 180 * percent / 100;
        mCoverPath.reset();
        mCoverPath.addArc(rect, -180, sweepAngle);
        canvas.drawPath(mCoverPath, mCoverPaint);
        Log.d("Talon", "Draw里面的percent------" + percent + "     sweepAngle+" + sweepAngle);
    }

    private void drawBackgroundArc(Canvas canvas) {
//        canvas.drawArc(rect, -180, 180, false, mBackgroundPaint);
        mBackgroundPath.reset();
        mBackgroundPath.addArc(rect, -180, maxAngle);
        canvas.drawPath(mBackgroundPath, mBackgroundPaint);
    }

    public int getPercent() {
        return percent;
    }

    public void setPercent(int percent) {
        Log.d("Talon", "currentPercent-------" + percent);
        setAnimation(this.percent, percent, 1000);
        this.percent = percent;
    }

    private ValueAnimator progressAnimator;

    /**
     * 为进度设置动画
     *
     * @param last
     * @param current
     */
    private void setAnimation(int last, int current, int duration) {
        progressAnimator = ValueAnimator.ofInt(last, current);
        progressAnimator.setDuration(duration);
        progressAnimator.setTarget(percent);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                percent = (int) animation.getAnimatedValue();
                invalidate(); // 重新绘制
            }
        });
        progressAnimator.start();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                updateOnTouch(event);
                break;
            case MotionEvent.ACTION_MOVE:
                updateOnTouch(event);
                break;
            case MotionEvent.ACTION_UP:
                break;
            default:
                break;
        }
        return true;
    }

    private void updateOnTouch(MotionEvent event) {
        boolean validateTouch = validateTouch(event.getX(), event.getY());
        if (!validateTouch) {
            return;
        }
        setPressed(true);


        // 通过event的x、y求出对应的percent,然后设置、重新绘制
        int perc = 0;

        // 自己的想法
//        double L; //弦长
//        L = Math.sqrt(event.getX() * event.getX() + (rect.centerY() - event.getY()) * (rect.centerY() - event.getY()));
//
//        double X = 2 * Math.asin(L / (rect.width() + mBackgroundWidth)); //圆心角
//        perc = (int) (X * 100 / Math.PI);
//        Log.d("Talon", "L+=========" + L + "   X:圆心角========" + X + "      百分百：++++++" + perc + "  2R++++" + rect.width() + "   centerX+++++" + rect.centerX());

        // 别人的想法
        //Math.atan2(y, x)以弧度为单位计算并返回点 y /x 的夹角，该角度从圆的 x 轴（0 点在其上，0 表示圆心）沿逆时针方向测量。返回值介于正 pi 和负 pi 之间。
        //触摸点与圆心的夹角- Math.toRadians(225)是因为我们希望0°从圆弧的起点开始,默认角度从穿过圆心的X轴开始
        double angle = Math.toDegrees(Math.atan2(event.getY() - rect.centerY(), event.getX() - rect.centerX()) + (Math.PI / 2) - Math.toRadians(275));

        if (angle < 0) {
            angle = 360 + angle;
        }
        perc = (int) (angle * 100 / 180);
        setPercent(perc);
    }

    /**
     * 判断触摸是否有效
     *
     * @param xPos x
     * @param yPos y
     * @return is validate touch
     */
    private boolean validateTouch(float xPos, float yPos) {
        boolean validate = false;

        float x = xPos - rect.centerX();
        float y = yPos - rect.centerY();

        float touchRadius = (float) Math.sqrt(((x * x) + (y * y)));//触摸半径

//        double angle = Math.toDegrees(Math.atan2(y, x) + (Math.PI / 2) - Math.toRadians(275));
//
//        if (angle < 0) {
//            angle = 360 + angle;
//        }
//

        // 30 增加点触摸范围
        if (touchRadius+30 > (rect.centerY() - mBackgroundWidth) && (touchRadius-30 < rect.centerY()) && yPos < rect.centerY()) {
            validate = true;
        }

        Log.d("Talon", "validateTouch: " + validate);
        return validate;
    }
}
