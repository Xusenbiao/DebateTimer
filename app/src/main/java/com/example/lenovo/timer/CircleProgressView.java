package com.example.lenovo.timer;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by lenovo on 2018/3/16.
 */

public class CircleProgressView extends View{
    private Paint mPaintBackground;//背景画笔
    private Paint mPaintProgress;//进度
    private Paint mPaintText;//百分比
    private int bgColor= Color.YELLOW;//圆形背景
    private int textColor=Color.BLACK;//文字颜色
    private int progressColor=Color.GREEN;//进度颜色
    private float mStrokeWidth=20;//圆环宽度
    private float mRadius=80;//默认半径
    private RectF rectPro;
    private int mProgress=0;//初始进度
    private int mMaxProgress=100;//最大进度
    private int mWidth,mHeight;
    private onProgressListener mOnProgressListener;
    public int maxtime=240;
    public void setmOnProgressListener(onProgressListener mOnProgressListener){
        this.mOnProgressListener = mOnProgressListener;
    }
    public void setMaxtime(int maxtimes){
        maxtime=maxtimes;
    }
    /**
     * 进度监听
     */
    public interface onProgressListener {
        public void onEnd();
    }
    public CircleProgressView(Context context) {
        this(context, null);
    }
    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }
    public CircleProgressView(Context context, AttributeSet attrs,
                              int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 自定义xml属性
        // TODO Auto-generated constructor stub
        if (attrs != null) {
            TypedArray ta = context.obtainStyledAttributes(attrs,
                    R.styleable.CircleProgress);
            int count = ta.getIndexCount();
            for (int i = 0; i < count; i++) {
                int attr = ta.getIndex(i);
                switch (attr) {
                    case R.styleable.CircleProgress_radius:
                        mRadius = ta.getDimension(R.styleable.CircleProgress_radius, mRadius);
                        break;
                    case R.styleable.CircleProgress_strokeWidth:
                        mStrokeWidth = ta.getDimension(R.styleable.CircleProgress_strokeWidth, mStrokeWidth);
                        break;
                    case R.styleable.CircleProgress_bgColor:
                        bgColor = ta.getColor(R.styleable.CircleProgress_bgColor, bgColor);
                        break;
                    case R.styleable.CircleProgress_progressColor:
                        progressColor = ta.getColor(R.styleable.CircleProgress_progressColor, progressColor);
                        break;
                    case R.styleable.CircleProgress_android_textColor:
                        textColor = ta.getColor(R.styleable.CircleProgress_android_textColor, textColor);
                        break;
                }
            }
            ta.recycle();
        }
        initPaint();
    }
    private void initPaint() {
        mPaintBackground = new Paint();
        mPaintBackground.setColor(bgColor);
        // 抗锯齿
        mPaintBackground.setAntiAlias(true);
        // 防抖动
        mPaintBackground.setDither(true);
        // 画笔样式
        mPaintBackground.setStyle(Paint.Style.STROKE);
        // 设置线宽
        mPaintBackground.setStrokeWidth(mStrokeWidth);


        mPaintProgress = new Paint();
        mPaintProgress.setColor(progressColor);
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setDither(true);
        // 设置画笔帽  画笔圆形
        mPaintProgress.setStrokeCap(Paint.Cap.ROUND);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setStrokeWidth(mStrokeWidth);


        // 绘制进度百分比
        mPaintText = new Paint();
        mPaintText.setColor(textColor);
        mPaintText.setAntiAlias(true);
        mPaintText.setTextAlign(Paint.Align.CENTER);
        mPaintText.setTextSize(90);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // TODO Auto-generated method stub
        mWidth = getRealSize(widthMeasureSpec);
        mHeight = getRealSize(heightMeasureSpec);
        setMeasuredDimension(mWidth, mHeight);
    }

    private void initRect() {
        if (rectPro == null) {
            rectPro = new RectF();
            int viewSize = (int) (mRadius * 2);
            int left = (mWidth - viewSize) / 2;
            int top = (mHeight - viewSize) / 2;
            int right = left + viewSize;
            int bottom = top + viewSize;
            rectPro.set(left, top, right, bottom);
        }
    }

    private int getRealSize(int measureSpec) {
        int result = -1;
        int mode = MeasureSpec.getMode(measureSpec);
        int size = MeasureSpec.getSize(measureSpec);
        if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            result = (int) (mRadius * 2 + mStrokeWidth * 2);
        } else {
            result = size;
        }
        return result;
    }

    /**
     * 设置更新进度条
     *
     * @param progress
     */
    public void setProgress(int progress) {
        this.mProgress = progress;
        invalidate();
    }
    public void setmMaxProgress(int MaxProgress) {
        this.mMaxProgress = MaxProgress;
        invalidate();
    }

    /**
     * 获取当前进度
     *
     * @return
     */
    public int getProgress() {
        return mProgress;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        float angle = mProgress / (mMaxProgress * 1.0f) * 360;
        initRect();
        //背景圆
        canvas.drawCircle(mWidth / 2, mHeight / 2, mRadius,
                mPaintBackground);
        //画进度
        canvas.drawArc(rectPro, -90, angle, false, mPaintProgress);
        //画进度百分比
        canvas.drawText(maxtime-mProgress + "秒", mWidth / 2, mHeight / 2, mPaintText);

        // 进度监听
        if (mOnProgressListener != null) {
            if (mProgress == mMaxProgress) {
                mOnProgressListener.onEnd();
            }
        }
    }
}
