package com.zibilal.layouttestapp;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.zibilal.layouttestapp.customs.recycler.adapter.MultipleItemRecyclerAdapter;
import com.zibilal.layouttestapp.customs.recycler.view.XRecyclerView;
import com.zibilal.layouttestapp.model.DefaultDataModel;
import com.zibilal.layouttestapp.model.IDataAdapter;

import java.util.ArrayList;
import java.util.List;

public class CollapsingToolbarLayoutActivity extends AppCompatActivity {

    MultipleItemRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collapsing_toolbar_layout);
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

        XRecyclerView listView = (XRecyclerView) findViewById(R.id.recycler_view);
        listView.setHasFixedSize(true);
        adapter = new MultipleItemRecyclerAdapter();
        adapter.addItemLayout(MultipleItemRecyclerAdapter.ItemType.BODY_ITEM, R.layout.item_default_view);
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    private void fetchData() {
        List<IDataAdapter> data = new ArrayList<>();
        data.add(new DefaultDataModel("Elem1"));
        data.add(new DefaultDataModel("Elem2"));
        data.add(new DefaultDataModel("Elem3"));
        data.add(new DefaultDataModel("Elem4"));
        data.add(new DefaultDataModel("Elem5"));
        data.add(new DefaultDataModel("Elem6"));
        data.add(new DefaultDataModel("Elem7"));
        data.add(new DefaultDataModel("Elem8"));
        data.add(new DefaultDataModel("Elem9"));
        data.add(new DefaultDataModel("Elem10"));
        data.add(new DefaultDataModel("Elem11"));
        adapter.updateData(MultipleItemRecyclerAdapter.ItemType.BODY_ITEM, data);
    }

}
