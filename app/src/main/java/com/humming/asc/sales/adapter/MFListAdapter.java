package com.humming.asc.sales.adapter;

import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestListFromCustomerVO;
import com.humming.asc.sales.R;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF List
 */

public class MFListAdapter extends BaseAdapter<QueryMfRequestListFromCustomerVO> {

    public MFListAdapter(List<QueryMfRequestListFromCustomerVO> data) {
        super(R.layout.list_item_mf, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryMfRequestListFromCustomerVO item, int position) {
        helper.setText(R.id.list_item_mf_no, item.getRequestNo())
                .setText(R.id.list_item_mf_item_code, item.getItemCode())
                .setText(R.id.list_item_mf_name, item.getApproverName())
                .setText(R.id.list_item_mf_date, item.getDate());

    }
}
