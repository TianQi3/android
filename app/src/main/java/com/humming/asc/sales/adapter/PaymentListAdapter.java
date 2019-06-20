package com.humming.asc.sales.adapter;

import android.view.View;
import android.widget.ImageView;

import com.humming.asc.dp.presentation.vo.ad.payment.QueryPaymentListVO;
import com.humming.asc.sales.R;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF List
 */

public class PaymentListAdapter extends BaseAdapter<QueryPaymentListVO> {
    private int type = 0;

    public PaymentListAdapter(List<QueryPaymentListVO> data, int type) {
        super(R.layout.list_item_payment, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryPaymentListVO item, int position) {
        ImageView imageView = helper.getView(R.id.list_item_payment_is_new);
       // if (type == 1 || type == 3) {//不显示
        //    imageView.setVisibility(View.GONE);
        //} else {
            if (item.getIsNew() == 1) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
       // }
        helper.setText(R.id.list_item_payment_no, item.getRequestNo())
                .setText(R.id.list_item_payment_customer_code, item.getCustomerCode())
                .setText(R.id.list_item_payment_applicant_name, item.getApplicationName())
                .setText(R.id.list_item_payment_approver_name, item.getApproverName())
                .setText(R.id.list_item_payment_date, item.getDate());
    }
}
