package com.sandyz.alltimers.common.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.IntRange;
import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ObjectFallingView extends View implements ValueAnimator.AnimatorUpdateListener {

    private Paint mPaint = null;
    private int mWidth;
    private int mHeight;

    private final List<ObjectItem> mObjectItems = new ArrayList<>();
    private final long mDuration = 3000;
    private ValueAnimator mUpdateFrameAnimator;
    private int mObjectCounter = 0;

    private long lastIncrementTime = 0;

    public ObjectFallingView(Context context) {
        this(context, null);
    }

    public ObjectFallingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ObjectFallingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        init();
    }

    private void init() {
        setClickable(true);
        setFocusable(true);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int warpDefaultSize = (int) dp2px(getContext(), 210);

        if (widthMode == MeasureSpec.AT_MOST && heightMode == MeasureSpec.AT_MOST) {
            widthSize = heightSize = warpDefaultSize;
        } else if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = warpDefaultSize;
        } else if (heightMode == MeasureSpec.AT_MOST) {
            heightSize = warpDefaultSize;
        }

        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mObjectItems.size(); i++) {
            ObjectItem objectItem = mObjectItems.get(i);
            if (!objectItem.isRunning) {
                continue;
            }
            objectItem.draw(canvas, mPaint);
        }

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldW, int oldH) {
        super.onSizeChanged(w, h, oldW, oldH);
        mWidth = w;
        mHeight = h;
    }


    public void addObjectItem(final int index) {

        final Path path = new Path();
        final PathMeasure pathMeasure = new PathMeasure();
        final ObjectItem objectItem = new ObjectItem();

        //绘制三阶贝塞尔曲线 起点位置
        PointF startPoint = new PointF();
        //贝塞尔结束点
        PointF endPoint = new PointF();

        //贝塞尔控制点1
        PointF control1 = new PointF();
        //贝塞尔控制点2
        PointF control2 = new PointF();

        initControlPoint(startPoint, control1, control2, endPoint, index);

        path.moveTo(startPoint.x, startPoint.y);
        path.cubicTo(control1.x, control1.y, control2.x, control2.y, endPoint.x, endPoint.y);
        pathMeasure.setPath(path, false);

        objectItem.index = index;
        objectItem.pathMeasure = pathMeasure;
        objectItem.startTime = System.currentTimeMillis();
        objectItem.endTime = objectItem.startTime + mDuration;
        objectItem.color = argb((int) (125 + (130) * Math.random()), 255, 255, 255);
        objectItem.angel = (float) (90 * Math.random());
        objectItem.size = (int) dp2px(getContext(), (float) (7 + 8 * Math.random()));
        mObjectItems.add(objectItem);
    }

    public static int argb(
            @IntRange(from = 0, to = 255) int alpha,
            @IntRange(from = 0, to = 255) int red,
            @IntRange(from = 0, to = 255) int green,
            @IntRange(from = 0, to = 255) int blue) {
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private void initControlPoint(PointF startPoint, PointF control1, PointF control2, PointF endPoint, int index) {

        int maxGroup = 3;
        int delta = (index % maxGroup);
        float average = (mWidth * 1.0f / maxGroup);

        float padding = dp2px(getContext(), 20);

        float bottomWidth = mWidth - padding;
        float averageBottom = ((bottomWidth) * 1.0f / maxGroup);


        startPoint.x = (float) (average * (delta) + average * Math.random());
        startPoint.y = 0;


        control1.x = (float) (average * (delta) + average * Math.random());
        control1.y = (float) (Math.random() * mHeight / 2);
        if (control1.y == 0) {
            control1.y = mHeight / 4f;
        }

        control2.x = (float) (average * (delta) + average * Math.random());
        control2.y = (float) (Math.random() * mHeight + control1.y);


        endPoint.x = padding / 2 + (float) (averageBottom * (delta) + averageBottom * Math.random());
        endPoint.y = mHeight;

    }

    @Override
    public void onAnimationUpdate(ValueAnimator animation) {

        int count = mObjectItems.size();
        long dx = System.currentTimeMillis() - lastIncrementTime;
        long incrementDuration = 200;
        if (count < 20 && dx > incrementDuration) {
            int num = (int) (Math.random() * 2);
            addObjectItem(mObjectCounter++);
            if (num > 0) {
                addObjectItem(mObjectCounter++);
            }
            if (mObjectCounter >= Integer.MAX_VALUE) {
                mObjectCounter = 0;
            }
            lastIncrementTime = System.currentTimeMillis();
        }

        Iterator<ObjectItem> iterator = mObjectItems.iterator();
        while (iterator.hasNext()) {

            ObjectItem objectItem = iterator.next();
            PathMeasure pathMeasure = objectItem.pathMeasure;
            float pathLength = pathMeasure.getLength();

            float currentTime = (System.currentTimeMillis() - objectItem.startTime);
            float fraction = currentTime / mDuration;
            if (fraction <= 1) {

                float[] pos = new float[2];
                pathMeasure.getPosTan(fraction * pathLength, pos, null);
                objectItem.x = pos[0];
                objectItem.y = pos[1];
                objectItem.isRunning = objectItem.y != 0;
                objectItem.fraction = fraction;

            } else {
                objectItem.isRunning = false;
                iterator.remove();
            }

        }
        invalidate();
    }


    static class ObjectItem {

        public int index;
        public float fraction;
        public float x;
        public float y;
        public boolean isRunning;
        public long startTime = System.currentTimeMillis();
        public long endTime = System.currentTimeMillis();
        public float angel;
        private PathMeasure pathMeasure;
        private int color = Color.WHITE;
        private int size = 1;

        public void draw(Canvas canvas, Paint paint) {
            paint.setColor(color);

            RectF rectF = new RectF();
            rectF.left = -size / 2f;
            rectF.top = -size / 2f;
            rectF.right = +size / 2f;
            rectF.bottom = +size / 2f;
            canvas.save();
            canvas.translate(x, y);
            canvas.rotate(angel);
            canvas.drawRect(rectF, paint);
            canvas.restore();
        }
    }


    public static float dp2px(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }


    public void startPlaying() {

        if (mUpdateFrameAnimator != null) {
            mUpdateFrameAnimator.removeAllUpdateListeners();
            mUpdateFrameAnimator.cancel();
        }
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 1.0f);
        //先加速后减速
        animator.setInterpolator(new LinearInterpolator());
        //动画的长短来控制速率
        animator.setDuration(50);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.addUpdateListener(this);

        animator.start();
        mUpdateFrameAnimator = animator;
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        if (mUpdateFrameAnimator != null) {
            mUpdateFrameAnimator.removeAllUpdateListeners();
            mUpdateFrameAnimator.cancel();
        }
    }
}