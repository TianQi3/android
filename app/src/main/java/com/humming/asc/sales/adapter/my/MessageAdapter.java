package com.humming.asc.sales.adapter.my;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.dto.ecatalogResponse.user.EcatalogNoticeResponse;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF List
 */

public class MessageAdapter extends BaseAdapter<EcatalogNoticeResponse> {

    public MessageAdapter(List<EcatalogNoticeResponse> data) {
        super(R.layout.list_item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EcatalogNoticeResponse item, int position) {
        helper.setText(R.id.item_message_content, item.getNotice())
                .setText(R.id.item_message_date, item.getCrtTime());
        if ("0".equals(item.getStatus())) {//未读
            helper.setText(R.id.item_message_is_read_text, Application.getInstance().getCurrentActivity().getResources().getString(R.string.new_message));
            helper.setImageResource(R.id.item_message_is_read_img, R.mipmap.message_no_read);
        } else {
            helper.setText(R.id.item_message_is_read_text, Application.getInstance().getCurrentActivity().getResources().getString(R.string.reads));
            helper.setImageResource(R.id.item_message_is_read_img, R.mipmap.message_read);
        }
    }
}
