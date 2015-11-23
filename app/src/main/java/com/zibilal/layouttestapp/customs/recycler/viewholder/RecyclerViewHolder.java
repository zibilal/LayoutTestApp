package com.zibilal.layouttestapp.customs.recycler.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.zibilal.layouttestapp.customs.recycler.view.XRecyclerView;
import com.zibilal.layouttestapp.model.IDataAdapter;

/**
 * Created by bilalmuhammad on 2/10/15.
 */
public abstract class RecyclerViewHolder extends RecyclerView.ViewHolder
        implements View.OnClickListener, View.OnLongClickListener{

    public interface ItemClickListener {
        void onClick(View v, int position);
    }

    public interface OnItemLongClickListener {
        void onLongClick(View v, int position);
    }

    public interface OnItemDoubleTapListener {
        void onDoubleTap(View v, int position);
    }

    private ItemClickListener mListener;
    private OnItemLongClickListener mLongClickListener;
    private OnItemDoubleTapListener mDoubleTapListener;

    XRecyclerView listView;

    public RecyclerViewHolder(View itemView) {
        super(itemView);
    }

    public void setItemClickListener(ItemClickListener listener){
        mListener = listener;
    }
    public ItemClickListener getItemClickListener() {
        return mListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
        mLongClickListener = onItemLongClickListener;
    }
    public OnItemLongClickListener getOnItemLongClickListener() {
        return mLongClickListener;
    }

    public void setOnDoubleTapListener(OnItemDoubleTapListener onDoubleTapListener){
        mDoubleTapListener = onDoubleTapListener;
    }
    public OnItemDoubleTapListener getOnDoubleTapListener() {
        return mDoubleTapListener;
    }


    public abstract void initView();
    public abstract void bindData(IDataAdapter data);

    @Override
    public void onClick(View v) {
        if(mListener!=null) {
            mListener.onClick(v, getLayoutPosition());
        }
    }

    @Override
    public boolean onLongClick(View v) {
        if(mLongClickListener!=null) {
            mLongClickListener.onLongClick(v, getLayoutPosition());
        }
        return false;
    }

    public boolean assertValue(Object v) {
        if(v instanceof String) {
            return v != null && ((String) v).length() > 0;
        }
        return v != null;
    }
}
