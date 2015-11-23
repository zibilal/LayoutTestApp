package com.zibilal.layouttestapp.customs.recycler.adapter;

import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zibilal.layouttestapp.customs.recycler.view.XRecyclerView;
import com.zibilal.layouttestapp.customs.recycler.viewholder.RecyclerViewHolder;
import com.zibilal.layouttestapp.customs.recycler.viewholder.RecyclerViewHolderFactory;
import com.zibilal.layouttestapp.model.IDataAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by bilalmuhammad on 4/29/15.
 */
public class MultipleItemRecyclerAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {

    private static final String TAG = "MultipleItemRecyclerAdapter";
    private List<IDataAdapter> data;
    private int thumbnailType = 0;
    private Point thumbnailSize;
    private XRecyclerView parent;
    private ViewGroup viewGroup;
    private View emptyView;

    private HashMap<ItemType, Integer> layoutMap;
    private HashMap<ItemType, List<IDataAdapter>> dataMap;
    private HashMap<ItemType, Integer> dataSizeMap;
    protected HashMap<ItemType, RecyclerViewHolder.ItemClickListener> dataListener;
    protected HashMap<ItemType, RecyclerViewHolder.OnItemLongClickListener> dataLongClickListener;
    protected int totalData;

    private RecyclerViewHolder.ItemClickListener clickListener;

    protected RecyclerViewHolderFactory viewHolderFactory;

    public MultipleItemRecyclerAdapter() {
        this(null);
    }

    public MultipleItemRecyclerAdapter(Point thumbnailSize) {
        data = new ArrayList<>();
        layoutMap = new HashMap<>();
        dataMap = new HashMap<>();
        dataSizeMap = new HashMap<>();
        dataListener = new HashMap<>();
        dataLongClickListener = new HashMap<>();
        this.thumbnailSize = thumbnailSize;
        viewHolderFactory = new RecyclerViewHolderFactory();
    }


    public void setThumbnailType(int thumbnailType) {
        this.thumbnailType = thumbnailType;
    }

    public void setEmptyView(View view) {
        emptyView = view;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        ItemType itype = getType(viewType);
        totalData = countTotalData();
        View itemView = inflater.inflate(viewType, parent, false);
        RecyclerViewHolder.ItemClickListener listener = dataListener.get(itype);
        return viewHolderFactory.createViewHolder(viewType, itemView, listener);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        ItemDetail itemDetail = chooseType(position);
        List<IDataAdapter> data = dataMap.get(itemDetail.type);
        holder.bindData(data.get(itemDetail.position));
    }

    protected ItemType getType(int viewType) {
        for (Map.Entry<ItemType, Integer> e : layoutMap.entrySet()) {
            if (e.getValue() == viewType) {
                return e.getKey();
            }
        }
        return ItemType.BODY_ITEM;
    }

    private ItemDetail chooseType(int position) {
        int sHeader = dataSizeMap.get(ItemType.HEADER_ITEM) != null ? dataSizeMap.get(ItemType.HEADER_ITEM) : 0;
        int sFooter = dataSizeMap.get(ItemType.FOOTER_ITEM) != null ? dataSizeMap.get(ItemType.FOOTER_ITEM) : 0;
        int oFooter = totalData - sFooter;

        if (position < sHeader) {
            return new ItemDetail(ItemType.HEADER_ITEM, position);
        } else if (position >= oFooter) {
            return new ItemDetail(ItemType.FOOTER_ITEM, position - oFooter);
        } else if (position >= sHeader) {
            return new ItemDetail(ItemType.BODY_ITEM, position - sHeader);
        }

        return null;
    }

    public int countTotalData() {
        int count = 0;

        Iterator it = dataMap.values().iterator();
        while (it.hasNext()) {
            List<IDataAdapter> d = (List<IDataAdapter>) it.next();
            count += d.size();
        }

        return count;
    }

    @Override
    public int getItemCount() {
        if (data.size() == 0 && emptyView != null && parent != null) {
            parent.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else if (emptyView != null && parent != null) {
            parent.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }
        int itemCount = 0;
        for (List<IDataAdapter> d : dataMap.values()) {
            itemCount += d.size();
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (layoutMap.get(ItemType.HEADER_ITEM) != null) {
            return position == 0 ? layoutMap.get(ItemType.HEADER_ITEM) : layoutMap.get(ItemType.BODY_ITEM);
        }
        return layoutMap.get(ItemType.BODY_ITEM);
    }

    public void addItemLayout(ItemType type, int layout) {
        layoutMap.put(type, layout);
    }

    public void addDataMap(ItemType type, List<IDataAdapter> data) {
        dataMap.put(type, data);
        dataSizeMap.put(type, data.size());
    }

    public void notifyItemModified() {
        notifyDataSetChanged();
    }

    public void notifyItemModified(ItemType type) {
        int sHeader = dataSizeMap.get(ItemType.HEADER_ITEM) != null ? dataSizeMap.get(ItemType.HEADER_ITEM) : 0;
        int sFooter = dataSizeMap.get(ItemType.FOOTER_ITEM) != null ? dataSizeMap.get(ItemType.FOOTER_ITEM) : 0;
        int sBody = dataSizeMap.get(ItemType.BODY_ITEM) != null ? dataSizeMap.get(ItemType.BODY_ITEM) : 0;
        int oFooter = totalData - sFooter;

        if (type == ItemType.HEADER_ITEM) {
            notifyItemRangeChanged(0, sHeader);
        } else if (type == ItemType.FOOTER_ITEM) {
            notifyItemRangeChanged(oFooter, sFooter);
        } else {
            notifyItemRangeChanged(sHeader, sBody);
        }
    }

    public void removeData() {
        dataMap.clear();
        dataSizeMap.clear();
    }

    public void setOnItemClickListener(RecyclerViewHolder.ItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void addOnItemClickListener(ItemType type, RecyclerViewHolder.ItemClickListener clickListener) {
        dataListener.put(type, clickListener);
    }

    public void addOnItemLongClickListener(ItemType type, RecyclerViewHolder.OnItemLongClickListener onItemLongClickListener) {
        dataLongClickListener.put(type, onItemLongClickListener);
    }

    public void addData(ItemType type, List<IDataAdapter> data) {
        List<IDataAdapter> d = dataMap.get(type);
        if (d == null) {
            dataMap.put(type, data);
            dataSizeMap.put(type, data.size());
            notifyItemModified(type);
        } else {
            d.addAll(data);
            dataMap.put(type, d);
            dataSizeMap.put(type, d.size());
            notifyItemModified(type);
        }
    }

    public void updateData(ItemType type, List<IDataAdapter> data) {
        dataMap.put(type, data);
        dataSizeMap.put(type, data.size());
        notifyItemModified(type);
    }

    public IDataAdapter getData(int position){
        return dataMap.get(ItemType.BODY_ITEM).get(position);
    }

    public static enum ItemType {
        HEADER_ITEM(0), BODY_ITEM(1), FOOTER_ITEM(3);
        final int value;

        private ItemType(int v) {
            value = v;
        }

        public int getValue() {
            return value;
        }
    }

    static class ItemDetail {
        public int position;
        public ItemType type;

        public ItemDetail(ItemType t, int p) {
            type = t;
            position = p;
        }
    }
}
