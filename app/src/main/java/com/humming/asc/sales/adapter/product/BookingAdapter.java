package com.humming.asc.sales.adapter.product;

import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.dto.ecatalogResponse.bean.product.BookingList;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * 预定记录 List
 */

public class BookingAdapter extends BaseAdapter<BookingList> {

    public BookingAdapter(List<BookingList> data) {
        super(R.layout.list_item_pro_booking, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, BookingList item, int position) {
        helper.setText(R.id.item_pro_booking_qty, item.getQty() + "")
                .setText(R.id.item_pro_booking_accName, item.getAccName() + "")
                .setText(R.id.item_pro_booking_salesName, item.getSalesName())
                .setText(R.id.item_pro_booking_date, item.getDateNum());
    }
}
