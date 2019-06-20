package com.humming.asc.sales.adapter.product;

import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.dto.ecatalogResponse.bean.product.WotsList;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * 产品库存 List
 */

public class ProInfoStockOnTheWayAdapter extends BaseAdapter<WotsList> {

    public ProInfoStockOnTheWayAdapter(List<WotsList> data) {
        super(R.layout.list_item_pro_info_on_the_way_stock, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, WotsList item, int position) {
        helper.setText(R.id.item_wotos, item.getDlQty()+"")
                .setText(R.id.item_date, item.getDate())
                .setText(R.id.item_inland, item.getDlQty() + "")
                .setText(R.id.item_hk, item.getHkQty() + "");
    }
}
