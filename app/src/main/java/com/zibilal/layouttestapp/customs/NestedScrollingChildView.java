package com.zibilal.layouttestapp.customs;

import android.content.Context;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Bilal on 11/16/2015.
 */
public class NestedScrollingChildView extends View implements NestedScrollingChild, GestureDetector.OnGestureListener{

    private final NestedScrollingChildHelper mNestedScrollingHelper;

    private GestureDetectorCompat mGestureDetector;

    public NestedScrollingChildView(Context context) {
        this(context, null, 0);
    }

    public NestedScrollingChildView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public NestedScrollingChildView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mNestedScrollingHelper = new NestedScrollingChildHelper(this);
        mGestureDetector = new GestureDetectorCompat(context, this);
        setNestedScrollingEnabled(true);
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mNestedScrollingHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mNestedScrollingHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mNestedScrollingHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mNestedScrollingHelper.stopNestedScroll();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int[] offsetInWindow) {
        return mNestedScrollingHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mNestedScrollingHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mNestedScrollingHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mNestedScrollingHelper.onDetachedFromWindow();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mNestedScrollingHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final boolean handled = mGestureDetector.onTouchEvent(event);
        if (!handled && event.getAction() == MotionEvent.ACTION_UP) {
            stopNestedScroll();
        }
        return true;
    }

    @Override
    public boolean onDown(MotionEvent e) {
        startNestedScroll(ViewCompat.SCROLL_AXIS_VERTICAL);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        dispatchNestedPreScroll(0, (int) distanceY, null, null);
        dispatchNestedScroll(0, 0, 0, 0, null);
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }
}
