package com.humming.asc.sales.adapter;

import android.view.View;
import android.widget.ImageView;

import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestListVO;
import com.humming.asc.sales.R;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF List
 */

public class MFRequestListAdapter extends BaseAdapter<QueryMfRequestListVO> {
    private int type = 0;
    public MFRequestListAdapter(List<QueryMfRequestListVO> data,int type) {
        super(R.layout.list_item_mf_request, data);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, QueryMfRequestListVO item, int position) {
        ImageView imageView = helper.getView(R.id.list_item_mf_is_new);
        //if (type == 1 || type == 3) {//不显示
        //    imageView.setVisibility(View.GONE);
       // } else {
            if (item.getIsNew() == 1) {
                imageView.setVisibility(View.VISIBLE);
            } else {
                imageView.setVisibility(View.GONE);
            }
       // }
        helper.setText(R.id.list_item_mf_request__no, item.getRequestNo())
                .setText(R.id.list_item_mf_item_request__customer_code, item.getCustomerCode())
                .setText(R.id.list_item_mf_request__item_code, item.getItemCode())
                .setText(R.id.list_item_mf_request__applicant_name, item.getApplicationName())
                .setText(R.id.list_item_mf_request__approval_name, item.getApproverName())
                .setText(R.id.list_item_mf_date, item.getDate());

    }
}
