package com.humming.asc.sales.adapter.product;

import android.text.Html;
import android.widget.CheckBox;

import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.asc.sales.model.product.ProductDCListEntity;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * 预定记录 List
 */

public class BrandAdapter extends BaseAdapter<ProductDCListEntity> {

    public BrandAdapter(List<ProductDCListEntity> data) {
        super(R.layout.list_item_brand, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductDCListEntity item, int position) {
        String content = item.getUdcList().getNameCn() + " " + "<=font color=\"#999999\">" + item.getUdcList().getNameEn() + "</font>";
        helper.setText(R.id.item_brand_name, Html.fromHtml(content));
        CheckBox checkBox = helper.getView(R.id.item_brand_check);
        if (item.isCheckSelect()) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }
}
