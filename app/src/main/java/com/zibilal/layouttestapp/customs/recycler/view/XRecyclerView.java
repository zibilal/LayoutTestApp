package com.zibilal.layouttestapp.customs.recycler.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by bilalmuhammad on 1/27/15.
 */
public class XRecyclerView extends RecyclerView {

    private static final String TAG = "XRecyclerView";

    private int mFirstVisibleItem;
    private int maxLinesToDisplay=5;
    private InfiniteListener onLoadData;
    private AtomicBoolean loading = new AtomicBoolean(false);

    private ImageView backToTopControl;

    private OnScrollListener onScrollListener = new OnScrollListener() {
        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            LinearLayoutManager layoutManager = (LinearLayoutManager) getLayoutManager();
            int visibleItemCount = getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItem = layoutManager.findFirstVisibleItemPosition();


            if (totalItemCount != 0 && backToTopControl != null) {
                if (mFirstVisibleItem < firstVisibleItem && firstVisibleItem > maxLinesToDisplay) {
                    toggleControl(true);
                } else if (mFirstVisibleItem > firstVisibleItem && firstVisibleItem > maxLinesToDisplay) {
                    toggleControl(false);
                }
            }

            if ((visibleItemCount + firstVisibleItem) >= totalItemCount) {
                loading.set(true);
                onLoadData.loadData();
            }

            mFirstVisibleItem = firstVisibleItem;
        }
    };

    private Handler loadingHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            loading.set(false);
            return true;
        }
    });

    public Handler getLoadingHandler(){
        return loadingHandler;
    }

    private void toggleControl(boolean isDisplay) {
        if(isDisplay && backToTopControl.getVisibility() != VISIBLE) {
            backToTopControl.setVisibility(VISIBLE);
        } else if(!isDisplay && backToTopControl.getVisibility() == VISIBLE){
            backToTopControl.setVisibility(GONE);
        }
    }

    public static interface InfiniteListener {
        public void loadData();
    }

    public XRecyclerView(Context context) {
        super(context);
    }

    public XRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public XRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setInfiniteListener(InfiniteListener onLoadData) {
        this.onLoadData=onLoadData;
        clearOnScrollListeners();
        addOnScrollListener(onScrollListener);
    }

    public void setBackToTopControl(ImageView imageView) {
        backToTopControl=imageView;
        backToTopControl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scrollToPosition(0);
                backToTopControl.setVisibility(GONE);
            }
        });
    }
}
