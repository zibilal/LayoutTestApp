package com.zibilal.layouttestapp.customs;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.TextPaint;

/**
 * Created by Bilal on 11/13/2015.
 */
public class IcoMoonDrawable extends Drawable {

    private TextPaint mTextPaint;
    private int mSize = 0;
    private String mText;
    private int mAlpha = 255;
    private Rect mTextBounds;
    private int mColor = Color.BLACK;
    private Context mContext;

    public IcoMoonDrawable(Context context, String txt){
        initView(context, txt);
    }

    public IcoMoonDrawable(Context context, int txtId) {
        initView(context, context.getString(txtId));
    }

    private void initView(Context context, String text) {
        mContext = context;
        mText = text;
        mTextPaint = new TextPaint();
        mTextPaint.setTypeface(TypefaceUtil.getTypeface(context));
        mTextPaint.setStyle(Paint.Style.STROKE);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setUnderlineText(false);
        mTextPaint.setAntiAlias(true);
        mTextBounds = new Rect();
        mTextPaint.getTextBounds(mText, 0, 1, mTextBounds);
        mTextPaint.setColor(mColor);
    }

    public void setColor(int color) {
        mColor = color;
        mTextPaint.setColor(mColor);
        invalidateSelf();
    }

    @Override
    public int getIntrinsicWidth() {
        return mSize;
    }

    @Override
    public int getIntrinsicHeight() {
        return mSize;
    }

    public void setPXSze(int size) {
        mSize = size;
        mTextPaint.setTextSize(mSize/2);
        invalidateSelf();
    }

    public void setDPSize(int size) {
        mSize = TypefaceUtil.dip2px(mContext, size);
        mTextPaint.setTextSize(mSize/2);
        invalidateSelf();
    }

    @Override
    public void draw(Canvas canvas) {
        if (mSize == 0) {
            Rect rect = getBounds();
            mSize = rect.right - rect.left;
            mTextPaint.setTextSize(mSize/2);
        }

        canvas.drawText(mText, mSize/2.0f, mSize/2.0f-mTextBounds.centerY(), mTextPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mAlpha = alpha;
        mTextPaint.setAlpha(alpha);
        invalidateSelf();
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
