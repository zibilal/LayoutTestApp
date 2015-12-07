package com.zibilal.layouttestapp.customs.recycler.viewholder;

import android.view.View;

import com.zibilal.layouttestapp.R;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.WeakHashMap;


/**
 * Created by bilalmuhammad on 7/16/15.
 */
public class RecyclerViewHolderFactory {

    private static HashMap<Integer, String> classMap =  new HashMap<Integer, String>(){
        {
            put(R.layout.item_default_view, "com.zibilal.layouttestapp.customs.recycler.viewholder.DefaultItemViewHolder");
        }
    } ;
    public RecyclerViewHolderFactory() {
    }

    public RecyclerViewHolder createViewHolder(int layout, View itemView,
                                               RecyclerViewHolder.ItemClickListener listener){
        RecyclerViewHolder result = null;
        try {
            Class<?> clazz = Class.forName(classMap.get(layout));
            Constructor<?> ctor = clazz.getConstructor(View.class);
            result = (RecyclerViewHolder) ctor.newInstance(itemView);
            result.setItemClickListener(listener);
            itemView.setOnClickListener(result);
            result.initView();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
