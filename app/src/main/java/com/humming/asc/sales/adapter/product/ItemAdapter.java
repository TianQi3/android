package com.humming.asc.sales.adapter.product;


import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.FilterEntity;
import com.humming.asc.sales.activity.product.filter.FilterBrandActivity;
import com.humming.asc.sales.model.product.ProductDCListEntity;

import java.util.List;

public class ItemAdapter extends AZBaseAdapter<String, ItemAdapter.ItemHolder> {

    public ItemAdapter(List<ProductDCListEntity> dataList) {
        super(dataList);
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_brand, parent, false);
        final ItemHolder holder = new ItemHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                if (mDataList.get(position).isCheckSelect()) {
                    mDataList.get(position).setCheckSelect(false);
                    FilterBrandActivity.selectValues.remove(mDataList.get(position).getUdcList());
                } else {
                    mDataList.get(position).setCheckSelect(true);
                    FilterBrandActivity.selectValues.add(mDataList.get(position).getUdcList());
                }
                notifyItemChanged(position);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        String content = mDataList.get(position).getUdcList().getNameCn() + " " + "<=font color=\"#999999\">" + mDataList.get(position).getUdcList().getNameEn() + "</font>";
        holder.mTextName.setText(Html.fromHtml(content));
        if (mDataList.get(position).isCheckSelect()) {
            holder.checkBox.setChecked(true);
        } else {
            holder.checkBox.setChecked(false);
        }
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        TextView mTextName;
        CheckBox checkBox;

        ItemHolder(View itemView) {
            super(itemView);
            mTextName = itemView.findViewById(R.id.item_brand_name);
            checkBox = itemView.findViewById(R.id.item_brand_check);
        }
    }
}
