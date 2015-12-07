package com.zibilal.layouttestapp.customs.recycler.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Build;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.zibilal.layouttestapp.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bilal on 12/7/2015.
 */
public class PieChart extends ViewGroup {

    public static final int AUTOCENTER_ANIM_DURATION = 250;
    public static final int FLING_VELOCITY_DOWNSCALE = 4;

    private List<Item> mData = new ArrayList<>();

    private boolean mShowText;
    private float mTextX;
    private float mTextY;
    private float mTextWidth;
    private float mTextHeight;
    private int mTextPos;
    private float mHighlightStrength;
    private int mPieRotation;
    private float mPointerRadius;
    private boolean mAutoCenterInSlice;
    private int mTextColor;

    private Paint mShadowPaint;
    private RectF mShadowBounds;

    private Paint mTextPaint;
    private float mPointerY;
    private float mPointerX;
    private float mTotal=0.0f;

    private Paint mPiePaint;

    private PieView mPieView;
    private PointerView mPointerView;

    private ObjectAnimator mAutoCenterAnimator;
    private Scroller mScroller;
    private ValueAnimator mScrollAnimator;
    private GestureDetector mDetector;

    private int mCurrentItemAngle;
    private int mCurrentItem;

    private RectF mPieBounds = new RectF();

    private OnCurrentItemChangedListener mCurrentItemChangedListener = null;

    public interface OnCurrentItemChangedListener {
        void OnCurrentItemChanged(PieChart source, int currentItem);
    }

    public PieChart(Context context) {
        super(context);
        init();
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.PieChart, 0, 0);
        try {
            mShowText = a.getBoolean(R.styleable.PieChart_showText, false);
            mTextY = a.getDimension(R.styleable.PieChart_labelY, 0.0f);
            mTextWidth = a.getDimension(R.styleable.PieChart_labelWidth, 0.0f);
            mTextHeight = a.getDimension(R.styleable.PieChart_labelHeight, 0.0f);
            mTextPos = a.getInteger(R.styleable.PieChart_labelPosition, 0);
            mTextColor = a.getColor(R.styleable.PieChart_labelColor, 0xff000000);
            mHighlightStrength = a.getFloat(R.styleable.PieChart_highlightStrength, 1.0f);
            mPieRotation = a.getInt(R.styleable.PieChart_pieRotation, 0);
            mPointerRadius = a.getDimension(R.styleable.PieChart_pointerRadius, 2.0f);
            mAutoCenterInSlice = a.getBoolean(R.styleable.PieChart_autoCenterPoninterInSlice, false);
        } finally {
            a.recycle();
        }
        init();
    }

    private void init(){
        // Force the background to software rendering because the Blur filter won't work
        setLayerToSW(this);

        // Set up the paint for the label text
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(mTextColor);
        if (mTextHeight == 0) {
            mTextHeight = mTextPaint.getTextSize();
        } else {
            mTextPaint.setTextSize(mTextHeight);
        }

        // Set up the paint for the pie slices
        mShadowPaint = new Paint(0);
        mShadowPaint.setColor(0xff101010);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(8, BlurMaskFilter.Blur.NORMAL));

        // Add the child view to draw the pie. Putting this in a child view
        // makes it possible to draw it on a separate hardware layer that rotates
        // independently
        mPieView = new PieView(getContext());
        addView(mPieView);
        mPieView.rotateTo(mPieRotation);

        // The pointer doesn't need hardware acceleration, but in order to show up
        // in front of the pie it also needs to be on separate view.
        mPointerView = new PointerView(getContext());
        addView(mPointerView);

        // Set up an animator to animate the PieRotation property. This is used to
        // correct the pie's orientation after the user lets go of it.

        if (Build.VERSION.SDK_INT >= 11) {
            mAutoCenterAnimator = ObjectAnimator.ofInt(PieChart.this, "PieRotation", 0);
            // Add a listener to hook the onAnimatedEnd event so that we can do
            // some cleanup when the pi stops moving.
            mAutoCenterAnimator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    mPieView.decelerate();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        // Create a scroller to handle the fling gesture.
        if (Build.VERSION.SDK_INT < 11) {
            mScroller = new Scroller(getContext());
        } else {
            mScroller = new Scroller(getContext(), null, true);
        }

        // The scrolelr doesn't have any built-in animation function--it just supplies
        // values when we ask it to. So we have to have a way to call it every frame
        // until the fling ends. This code (ab)uses a ValueAnimator object to generate
        // a callback on every animation frame. We don't use the animated value at all.
        if (Build.VERSION.SDK_INT >= 11) {
            mScrollAnimator = ValueAnimator.ofFloat(0, 1);
            mScrollAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    tickScrollAnimation();
                }
            });
        }

        // Create a gesture detector to handle onTouch messages
        mDetector = new GestureDetector(PieChart.this.getContext(), new GestureListener());

        // Turn off long press--this control doesn't use it, and if long press is enabled,
        // you can't scroll for a bit, pause, then scroll some more (the pause is interpreted
        // as a long press, apparently)
        mDetector.setIsLongpressEnabled(false);

        // In edit mode it's nice to have some demo data, so add that here.
        if (this.isInEditMode()) {
            Resources res = getResources();
            addItem("Annabelle", 3, res.getColor(R.color.bluegrass));
            addItem("Brunhilde", 4, res.getColor(R.color.chartreuse));
            addItem("Carolina", 2, res.getColor(R.color.emerald));
            addItem("Dahlia", 3, res.getColor(R.color.seafoam));
            addItem("Ekaterina", 1, res.getColor(R.color.slate));
        }
    }

    public int addItem(String label, float value, int color) {
        Item it = new Item();
        it.mLabel = label;
        it.mValue = value;
        it.mColor = color;

        // Calculate the highlight color. Saturate at 0xff to make sure that high values
        // don't result in aliasing.
        it.mHighlight = Color.argb(
                0xff,
                Math.min((int)(mHighlightStrength * (float) Color.red(color)), 0xff),
                Math.min((int)(mHighlightStrength * (float) Color.green(color)), 0xff),
                Math.min((int)(mHighlightStrength * (float) Color.blue(color)), 0xff)
        );
        mTotal += value;
        mData.add(it);
        onDataChanged();
        return mData.size() - 1;
    }

    private void onDataChanged() {
        // When the data changes, we have to recalculate
        // all of the angles.
        int currentAngle=0;
        for(Item it: mData) {
            it.mStartAngle = currentAngle;
            it.mEndAngle = (int) ((float) currentAngle + it.mValue * 360.0f / mTotal);
            currentAngle = it.mEndAngle;

            // Recalculate the gradient shaders. There are
            // these values in this gradient, even though only
            // two are necessary, in order to work around
            // a bug in certain versions of the graphics engine
            // that expects at least three values if the positions array is non-null.
            it.mShader = new SweepGradient(mPieBounds.width() / 2.0f, mPieBounds.height() / 2.0f, new int[] {it.mHighlight, it.mHighlight, it.mColor, it.mColor},
                    new float[] {0, (float) (360 - it.mEndAngle) / 360.0f, (float) (360 - it.mStartAngle) / 360.0f, 1.0f});
        }
        calcCurrentItem();
        onScrollFinished();
    }

    private void setLayerToSW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
    }

    private void setLayerToHW(View v) {
        if (!v.isInEditMode() && Build.VERSION.SDK_INT >= 11) {
            setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
    }

    private void tickScrollAnimation() {
        if (!mScroller.isFinished()) {
            mScroller.computeScrollOffset();
            setPieRotation(mScroller.getCurrY());
        } else {
            if (Build.VERSION.SDK_INT >= 11) {
                mScrollAnimator.cancel();
            }
            onScrollFinished();
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        // PieChart lays out its children in onSizeChanged()
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        // Set dimension for text, pie chart etc
        // Account for padding
        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingTop() + getPaddingTop());

        // Account for the label
        if(mShowText) xpad += mTextWidth;

        float ww = (float) w - xpad;
        float hh = (float) h - ypad;

        // Figure out how big we can make the pie.
        float diameter = Math.min(ww, hh);
        mPieBounds = new RectF(0.0f, 0.0f, diameter, diameter);
        mPieBounds.offsetTo(getPaddingLeft(), getPaddingTop());

        //
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw the shadow
        canvas.drawOval(mShadowBounds, mShadowPaint);
        if (isShowText()) {
            canvas.drawText(mData.get(mCurrentItem).mLabel, mTextX, mTextY, mTextPaint);
        }

        if (Build.VERSION.SDK_INT < 11) {
            tickScrollAnimation();
            if (!mScroller.isFinished()) {
                postInvalidate();
            }
        }
    }

    public boolean isShowText() {
        return mShowText;
    }

    public void setShowText(boolean showText) {
        mShowText=showText;
    }

    public int getPieRotation() {
        return mPieRotation;
    }

    public void setPieRotation( int rotation) {
        rotation = (rotation % 360 + 360) % 360;
        mPieRotation = rotation;
        mPieView.rotateTo(rotation);

        calcCurrentItem();
    }

    public int getCurrentItem() {
        return mCurrentItem;
    }

    public void setCurrentItem(int currentItem) {
        setCurrentItem(currentItem, true);
    }

    private void setCurrentItem(int currentItem, boolean scrollIntoView) {
        mCurrentItem = currentItem;
        if (mCurrentItemChangedListener != null){
            mCurrentItemChangedListener.OnCurrentItemChanged(this, currentItem);
        }
        if (scrollIntoView) {
            centerOnCurrentItem();
        }
        invalidate();
    }

    private void centerOnCurrentItem() {
        Item current = mData.get(getCurrentItem());
        int targetAngle = current.mStartAngle + (current.mEndAngle - current.mStartAngle) / 2;
        targetAngle -= mCurrentItemAngle;
        if (targetAngle < 90 && mPieRotation > 180) targetAngle += 360;

        if (Build.VERSION.SDK_INT >= 11) {
            mAutoCenterAnimator.setIntValues(targetAngle);
            mAutoCenterAnimator.setDuration(AUTOCENTER_ANIM_DURATION).start();
        } else {

        }
    }

    public void setOnCurrentItemChangedListener(OnCurrentItemChangedListener listener) {
        mCurrentItemChangedListener = listener;
    }

    /**
     * Calculate which pie slice is under the pointer, and set the current item
     * field accordingly.
     */
    private void calcCurrentItem() {
        int pointerAngle = (mCurrentItemAngle + 360 + mPieRotation) % 360;
        for (int i=0; i < mData.size(); ++i) {
            Item it = mData.get(i);
            if (it.mStartAngle <= pointerAngle && pointerAngle <= it.mEndAngle) {
                if (i != mCurrentItem) {
                    setCurrentItem(i, false);
                }
                break;
            }
        }
    }

    private void stopScrolling() {
        mScroller.forceFinished(true);
        if (Build.VERSION.SDK_INT >= 11) {
            mAutoCenterAnimator.cancel();
        }

        onScrollFinished();
    }

    private void onScrollFinished() {
        if (mAutoCenterInSlice) {
            centerOnCurrentItem();
        } else {
            mPieView.decelerate();
        }
    }

    @Override
    protected int getSuggestedMinimumWidth() {
        return (int) mTextWidth * 2;
    }

    @Override
    protected int getSuggestedMinimumHeight() {
        return (int) mTextWidth;
    }

    private class PieView extends View {
        private float mRotation=0;
        private Matrix mTransform = new Matrix();
        private PointF mPivot = new PointF();
        private RectF mBounds;

        public PieView(Context context) {
            super(context);
        }

        public void accelerate() {
            setLayerToHW(this);
        }

        public void decelerate() {
            setLayerToSW(this);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);

            if (Build.VERSION.SDK_INT < 11) {
                mTransform.set(getMatrix());
                mTransform.preRotate(mRotation, mPivot.x, mPivot.y);
                canvas.setMatrix(mTransform);
            }

            for(Item it: mData) {
                mPiePaint.setShader(it.mShader);
                canvas.drawArc(mBounds, 360-it.mEndAngle, it.mEndAngle - it.mStartAngle, true, mPiePaint);
            }
        }

        @Override
        protected void onSizeChanged(int w, int h, int oldw, int oldh) {
            mBounds = new RectF(0, 0, w, h);
        }

        public void rotateTo(float pieRotation) {
            mRotation = pieRotation;
            if (Build.VERSION.SDK_INT >= 11) {
                setRotation(pieRotation);
            } else {
                invalidate();
            }
        }

        public void setPivot(float x, float y) {
            mPivot.x=x;
            mPivot.y=y;
            if (Build.VERSION.SDK_INT >= 11) {
                setPivotX(x);
                setPivotY(y);
            } else {
                invalidate();
            }
        }
    }

    private class PointerView extends View {

        public PointerView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            canvas.drawLine(mTextX, mPointerY, mPointerX, mPointerY, mTextPaint);
            canvas.drawCircle(mPointerX, mPointerY, mPointerRadius, mTextPaint);
        }
    }

    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            // set the pie rotation directly
            float scrollTheta = vectorToScalarScroll(distanceX, distanceY,
                    e2.getX() - mPieBounds.centerX(),
                    e2.getY() - mPieBounds.centerY());
            setPieRotation(getPieRotation() - (int) scrollTheta / FLING_VELOCITY_DOWNSCALE);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float scrollTheta = vectorToScalarScroll(
                    velocityX,
                    velocityY,
                    e2.getX() - mPieBounds.centerX(),
                    e2.getY() - mPieBounds.centerY()
            );
            mScroller.fling(0, getPieRotation(), 0, (int) scrollTheta / FLING_VELOCITY_DOWNSCALE, 0, 0, Integer.MIN_VALUE, Integer.MAX_VALUE);

            if (Build.VERSION.SDK_INT >= 11) {
                mScrollAnimator.setDuration(mScroller.getDuration());
                mScrollAnimator.start();
            }
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            mPieView.accelerate();
            if (isAnimationRunning()) {
                stopScrolling();
            }
            return true;
        }
    }

    private boolean isAnimationRunning() {
        return !mScroller.isFinished() || (Build.VERSION.SDK_INT >= 11 && mAutoCenterAnimator.isRunning());
    }

    private class Item {
        public String mLabel;
        public float mValue;
        public int mColor;

        public int mStartAngle;
        public int mEndAngle;

        public int mHighlight;
        public Shader mShader;
    }


    private static float vectorToScalarScroll(float dx, float dy, float x, float y) {
        // get the length of the vector
        float l = (float) Math.sqrt(dx * dx + dy * dy);

        // decide if the scalar should be negative or positive by finding
        // the dot product of the vector perpendicular to (x,y).
        float crossX = -y;
        float crossY = x;
        float dot = (crossX * dx + crossY * dy);
        float sign = Math.signum(dot);

        return l * sign;
    }
}
