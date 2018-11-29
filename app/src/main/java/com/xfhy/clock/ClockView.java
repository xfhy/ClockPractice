package com.xfhy.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by feiyang on 2018/11/29 16:43
 * Description :
 */
public class ClockView extends View {

    /**
     * 起始颜色
     */
    private static final int SCALE_START_COLOR = 0XFFCCCCCC;
    /**
     * 终止颜色
     */
    private static final int SCALE_STOP_COLOR = 0XFFFFFFFF;
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private SweepGradient mSweepGradient;
    /**
     * 半径
     */
    private int mRadius;
    /**
     * 刻度线长度
     */
    private float mScaleLength;
    private Rect mTextRect;
    private int mHeight;
    private int mWidth;
    /**
     * 中心点
     */
    private Point mCenterPoint;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        mTextPaint.setColor(SCALE_STOP_COLOR);
        //px 单位
        mTextPaint.setTextSize(16);


    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCenterPoint = new Point(w / 2, h / 2);

        //计算半径
        mRadius = Math.min(w - getPaddingLeft() - getPaddingRight(), h - getPaddingTop() - getPaddingBottom()) / 2;
        //计算刻度线长度
        mScaleLength = 0.12f * mRadius;//根据比例确定刻度线长度
        //梯度扫描渐变，以(w/2,h/2)为中心点，两种起止颜色梯度渐变
        //float数组表示，[0,0.75)为起始颜色所占比例，[0.75,1}为终止颜色渐变所占比例
        mSweepGradient = new SweepGradient(w / 2, h / 2, new int[]{SCALE_START_COLOR, SCALE_STOP_COLOR}, new float[]{0.75f, 1});
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawableText(canvas);
    }

    /**
     * 画外围文本
     */
    private void drawableText(Canvas canvas) {
        //12
        String text = "12";
        mTextRect = new Rect();
        //测量文字显示范围
        mTextPaint.getTextBounds(text, 0, text.length(), mTextRect);
        canvas.drawText(text, mCenterPoint.x - mTextRect.width(), mCenterPoint.y - mRadius + mTextRect.height(), mTextPaint);
        //3
        //6
        //9
    }
}
