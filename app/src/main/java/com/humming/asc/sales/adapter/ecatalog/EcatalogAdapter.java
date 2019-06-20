package com.humming.asc.sales.adapter.ecatalog;

import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.dto.ecatalogResponse.bean.ecatalog.EcatalogListBean;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * 我的报价单 List
 */

public class EcatalogAdapter extends BaseAdapter<EcatalogListBean> {

    public EcatalogAdapter(List<EcatalogListBean> data) {
        super(R.layout.list_item_ecatalog, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EcatalogListBean item, int position) {
        helper.setText(R.id.item_ecatalog_name, item.getName())
                .setText(R.id.item_ecatalog_status, item.getStatus())
                .setText(R.id.item_ecatalog_username, item.getUserName())
                .setText(R.id.item_ecatalog_contacts, item.getContacts())
                .setText(R.id.item_ecatalog_category, item.getCategory() + "")
                .setText(R.id.item_ecatalog_status, item.getStatus())
                .setText(R.id.item_ecatalog_time, item.getUpdTime())
                .setText(R.id.item_ecatalog_phone, item.getPhone())
                .setText(R.id.item_ecatalog_range, item.getScope());
        if ("3".equals(item.getStatusNo())) {
            helper.setBackgroundRes(R.id.item_ecatalog_status, R.drawable.bg_ecatalog_status_send)
                    .setTextColor(R.id.item_ecatalog_status, helper.getConvertView().getContext().getResources().getColor(R.color.f5a623));
        } else if ("2".equals(item.getStatusNo())) {
            helper.setBackgroundRes(R.id.item_ecatalog_status, R.drawable.bg_ecatalog_status_mfr)
                    .setTextColor(R.id.item_ecatalog_status, helper.getConvertView().getContext().getResources().getColor(R.color.ff7ed321));
        } else if ("1".equals(item.getStatusNo())) {
            helper.setBackgroundRes(R.id.item_ecatalog_status, R.drawable.bg_ecatalog_status_draft)
                    .setTextColor(R.id.item_ecatalog_status, helper.getConvertView().getContext().getResources().getColor(R.color._448fe7));
        } else {
            helper.setBackgroundRes(R.id.item_ecatalog_status, R.drawable.bg_ecatalog_status_send)
                    .setTextColor(R.id.item_ecatalog_status, helper.getConvertView().getContext().getResources().getColor(R.color.f5a623));
        }
    }
}
