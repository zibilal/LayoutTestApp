package com.zibilal.layouttestapp.customs;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by Bilal on 11/12/2015.
 */
public class IcoMoonTextView extends TextView {

    public IcoMoonTextView(Context context) {
        super(context);
        init();
    }

    public IcoMoonTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        setTypeface(TypefaceUtil.getTypeface(getContext()));
    }
}
