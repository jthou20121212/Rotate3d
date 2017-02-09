package com.jthou.rotate3d;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

/**
 * Created by user on 2016/8/1.
 */
public class Rotate3dProgress extends RelativeLayout implements View.OnClickListener{

    private int[] imgs = new int[]{R.drawable.img_1, R.drawable.img_2, R.drawable.img_3, R.drawable.img_4};

    private RectF mRange;

    private Paint mPaint;

    /**
     * 控制显示图片索引
     */
    private int mIndex;

    public Rotate3dProgress(Context context) {
        super(context);
    }

    public Rotate3dProgress(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public Rotate3dProgress(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 大小固定100dp
        int width = DensityUtils.dp2px(getContext(), 100);
        int measureSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY);
        super.onMeasure(measureSpec, measureSpec);
        mRange = new RectF(0, 0, getMeasuredWidth(), getMeasuredHeight());
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        ImageView child = new ImageView(getContext());
        child.setImageResource(imgs[0]);
        addView(child);
        setOnClickListener(this);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        // 绘制白色背景
        canvas.drawRoundRect(mRange, 25f, 25f, mPaint);

        super.dispatchDraw(canvas);
    }

    /**
     * 从360旋转到180
     */
    private Runnable jack = new Runnable() {
        @Override
        public void run() {
            mIndex++;
            float centerX = getMeasuredWidth() / 2;
            float centerY = getMeasuredHeight() / 2;
            final Rotate3dAnimation rotation = new Rotate3dAnimation(360, 180, centerX, centerY, 0f, true);
            rotation.setDuration(600);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new LinearInterpolator());
            // 设置动画的监听器
            rotation.setAnimationListener(new SimpleAnimationListener(){

                @Override
                public void onAnimationStart(Animation animation) {
                    // 执行到一半的时候更换图片
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView)getChildAt(0)).setImageResource(imgs[mIndex % imgs.length]);
                        }
                    }, 300);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                       postDelayed(rose, 150);
                }
            });
            getChildAt(0).startAnimation(rotation);
        }
    };

    /**
     * 从180旋转到0
     */
    private Runnable rose = new Runnable() {
        @Override
        public void run() {
            mIndex++;
            float centerX = getMeasuredWidth() / 2;
            float centerY = getMeasuredHeight() / 2;
            final Rotate3dAnimation rotation = new Rotate3dAnimation(180, 0, centerX, centerY, 0f, true);
            rotation.setDuration(600);
            rotation.setFillAfter(true);
            rotation.setInterpolator(new LinearInterpolator());
            // 设置动画的监听器
            rotation.setAnimationListener(new SimpleAnimationListener(){

                @Override
                public void onAnimationStart(Animation animation) {
                    postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((ImageView)getChildAt(0)).setImageResource(imgs[mIndex % imgs.length]);
                        }
                    }, 300);
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    postDelayed(jack, 150);
                }
            });
            getChildAt(0).startAnimation(rotation);
        }
    };

    @Override
    public void onClick(View v) {
        post(jack);
    }

}
