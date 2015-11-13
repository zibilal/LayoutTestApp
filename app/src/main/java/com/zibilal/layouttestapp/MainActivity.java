package com.zibilal.layouttestapp;

import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zibilal.layouttestapp.customs.IcoMoonDrawable;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @Bind(R.id.fab)
    FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        IcoMoonDrawable checkDrawable = new IcoMoonDrawable(getApplicationContext(), R.string.icon_checkmark);
        checkDrawable.setDPSize(12);
        checkDrawable.setColor(Color.CYAN);
        mFab.setImageDrawable(checkDrawable);
    }
}
