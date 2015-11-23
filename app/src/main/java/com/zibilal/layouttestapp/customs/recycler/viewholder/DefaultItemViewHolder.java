package com.zibilal.layouttestapp.customs.recycler.viewholder;

import android.view.View;
import android.widget.TextView;

import com.zibilal.layouttestapp.R;
import com.zibilal.layouttestapp.model.DefaultDataModel;
import com.zibilal.layouttestapp.model.IDataAdapter;
import com.zibilal.layouttestapp.model.WrongModelObjectException;

/**
 * Created by Bilal on 11/23/2015.
 */
public class DefaultItemViewHolder extends RecyclerViewHolder {

    private TextView textView;

    public DefaultItemViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void initView() {
        textView = (TextView) itemView.findViewById(R.id.item_text);
    }

    @Override
    public void bindData(IDataAdapter data) {
        try {
            DefaultDataModel model = DefaultDataModel.getThis(data);
            textView.setText(model.getTitle());
        } catch (WrongModelObjectException e) {
            e.printStackTrace();
        }
    }
}
