package com.humming.asc.sales.adapter;

import com.humming.asc.dp.presentation.vo.ItemCostVO;
import com.humming.asc.sales.R;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF item code List
 */

public class MFItemCodeAdapter extends BaseAdapter<ItemCostVO> {

    public MFItemCodeAdapter(List<ItemCostVO> data) {
        super(R.layout.list_item_approval_item_code, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ItemCostVO item, int position) {
        helper.setText(R.id.tv_approval_item_code_text, item.getItemCode());

    }
}
