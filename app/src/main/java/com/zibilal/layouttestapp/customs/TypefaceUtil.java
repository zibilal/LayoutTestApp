package com.zibilal.layouttestapp.customs;

import android.content.Context;
import android.graphics.Typeface;

/**
 * Created by Bilal on 11/13/2015.
 */
public class TypefaceUtil {
    private static Typeface _tf;

    public static Typeface getTypeface(Context context) {
        if (_tf == null) {
            _tf = Typeface.createFromAsset(context.getAssets(), "icomoon.ttf");
        }
        return _tf;
    }

    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }
}
