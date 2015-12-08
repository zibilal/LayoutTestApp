package com.zibilal.layouttestapp;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.zibilal.layouttestapp.customs.recycler.view.PieChart;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PieChartActivity extends AppCompatActivity {

    @Bind(R.id.pie_chart)
    PieChart pieChart;

    @OnClick(R.id.reset) public void onResetClick(View view) {
        pieChart.setCurrentItem(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        ButterKnife.bind(this);
        Resources res = getResources();
        pieChart.addItem("Agamemnon", 2, res.getColor(R.color.seafoam));
        pieChart.addItem("Bocephus", 3.5f, res.getColor(R.color.chartreuse));
        pieChart.addItem("Calliope", 2.5f, res.getColor(R.color.emerald));
        pieChart.addItem("Daedalus", 3, res.getColor(R.color.bluegrass));
        pieChart.addItem("Euripides", 1, res.getColor(R.color.turqoise));
        pieChart.addItem("Ganymede", 3, res.getColor(R.color.slate));
    }

}
