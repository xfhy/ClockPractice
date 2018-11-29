package com.xfhy.clock;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by feiyang on 2018/11/29 16:43
 * Description :
 *
 * 关键点:
 * 1.刻度盘: 用一个画笔很宽的线,画一个圆圈,然后在圆圈上面画横着的一条条的线(这线的颜色是背景色->骚不骚,有点像障眼法????  给用户一种刻度的感觉).
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
    /**
     * 外围文字画笔
     */
    private Paint mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 外围圆圈画笔
     */
    private Paint mOuterLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 刻度盘 圆  的弧宽
     */
    private Paint mScaleArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    /**
     * 刻度盘 圆弧 上 横着的线条
     */
    private Paint mScaleLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private SweepGradient mSweepGradient;
    /**
     * 半径
     */
    private int mRadius;
    /**
     * 刻度盘 圆弧的宽度
     */
    private float mScaleLength;
    private int mHeight;
    private int mWidth;
    /**
     * 中心点
     */
    private Point mCenterPoint;
    /**
     * 2个文字的显示范围
     */
    private Rect mTwoTextRect;
    /**
     * 一个文字的显示范围
     */
    private Rect mOneTextRect;

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
        mOuterLinePaint.setColor(SCALE_STOP_COLOR);
        mOuterLinePaint.setStrokeWidth(PixelUtil.dp2px(getContext(), 1));
        mOuterLinePaint.setStyle(Paint.Style.STROKE);
        //px 单位
        mTextPaint.setTextSize(PixelUtil.dp2px(getContext(), 16));

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

        //刻度盘圆弧的宽度
        mScaleLength = 0.12f * mRadius;
        //刻度盘的弧宽
        mScaleArcPaint.setStrokeWidth(mScaleLength);
        //刻度线的宽度
        mScaleLinePaint.setStrokeWidth(0.012f * mRadius);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawText(canvas);
        drawOuterCircle(canvas);
        drawDial(canvas);
    }

    /**
     * 画外围文本
     */
    private void drawText(Canvas canvas) {
        //12
        String text = "12";
        mTwoTextRect = new Rect();
        //测量文字显示范围
        mTextPaint.getTextBounds(text, 0, text.length(), mTwoTextRect);
        canvas.drawText(text, mCenterPoint.x - mTwoTextRect.width() / 2, mCenterPoint.y - mRadius + mTwoTextRect.height(), mTextPaint);
        //3
        text = "3";
        mOneTextRect = new Rect();
        //测量文字显示范围
        mTextPaint.getTextBounds(text, 0, text.length(), mOneTextRect);
        canvas.drawText(text, mCenterPoint.x + mRadius - mOneTextRect.width(), mCenterPoint.y + mOneTextRect.height() / 2, mTextPaint);
        //6
        text = "6";
        mTextPaint.getTextBounds(text, 0, text.length(), mOneTextRect);
        canvas.drawText(text, mCenterPoint.x - mOneTextRect.width() / 2, mCenterPoint.y + mRadius, mTextPaint);
        //9
        text = "9";
        mTextPaint.getTextBounds(text, 0, text.length(), mOneTextRect);
        canvas.drawText(text, mCenterPoint.x - mRadius, mCenterPoint.y + mOneTextRect.height() / 2, mTextPaint);
    }

    /**
     * 画外围圆圈
     */
    private void drawOuterCircle(Canvas canvas) {
        String text = "12";
        //测量文字显示范围
        mTextPaint.getTextBounds(text, 0, text.length(), mTwoTextRect);
        text = "3";
        //测量文字显示范围
        mTextPaint.getTextBounds(text, 0, text.length(), mOneTextRect);
        //外层圆圈范围
        RectF rectF = new RectF(getPaddingLeft() + mOneTextRect.width() / 2, getPaddingTop() + mTwoTextRect.height() / 2,
                mWidth - getPaddingRight() - mOneTextRect.width() / 2, mHeight - getPaddingBottom() - mOneTextRect.height() / 2);
        //4个圆弧 每个80°
        for (int i = 0; i < 4; i++) {
            canvas.drawArc(rectF, 5 + 90 * i, 80, false, mOuterLinePaint);
        }
    }

    /**
     * 画刻度盘
     */
    private void drawDial(Canvas canvas) {

    }
}
