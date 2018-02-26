package com.example.talon.test;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;

/**
 * 自定义热门标签
 * Created by Talon on 2018/2/26.
 */

public class LabelView extends AppCompatImageView {
    private Paint bgPaint, textPaint;
    private Path bgPath, textPath;
    private Rect textRect;

    public LabelView(Context context) {
        super(context);
        init();
    }

    public LabelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LabelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //背景画笔
        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(Color.RED);
        bgPaint.setStyle(Paint.Style.FILL);
        //文字画笔
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setTextSize(50);

        bgPath = new Path();
        textPath = new Path();

        //用来获取文字宽高
        textRect = new Rect();
        textPaint.getTextBounds("Hot", 0, "Hot".length(), textRect);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        calculatePath(getMeasuredWidth(), getMeasuredHeight());
        canvas.drawPath(bgPath, bgPaint);
        canvas.drawTextOnPath("Hot", textPath, (float) ((Math.sqrt(80000) - textRect.width()) / 2), -(float) ((50 * Math.sqrt(2) - textRect.height()) / 2), textPaint);
    }

    private void calculatePath(int measuredWidth, int measuredHeight) {
        //背景路徑
        bgPath.reset();
        bgPath.moveTo(measuredWidth - 100 - 100, 0);
        bgPath.lineTo(measuredWidth - 100, 0);
        bgPath.lineTo(measuredWidth, 100);
        bgPath.lineTo(measuredWidth, 100 + 100);
        bgPath.close();

        //文字路徑
        textPath.reset();
        textPath.moveTo(measuredWidth - 100 - 100, 0);
        textPath.lineTo(measuredWidth, 100 + 100);
        textPath.close();
    }
}
