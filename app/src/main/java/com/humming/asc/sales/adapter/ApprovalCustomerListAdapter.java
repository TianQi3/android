package com.humming.asc.sales.adapter;

import com.humming.asc.dp.presentation.vo.AccountForMfVO;
import com.humming.asc.sales.R;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF List
 */

public class ApprovalCustomerListAdapter extends BaseAdapter<AccountForMfVO> {

    public ApprovalCustomerListAdapter(List<AccountForMfVO> data) {
        super(R.layout.list_item_approval_customer, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AccountForMfVO item, int position) {
        helper.setText(R.id.list_item_approval_customer__customer_code, item.getAccountcode())
                .setText(R.id.list_item_approval_customer__customer, item.getAccountname());
    }
}
