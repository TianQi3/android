package com.humming.asc.sales.adapter.ecatalog;

import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.dto.ecatalogResponse.bean.ecatalog.EcatalogListNoPageableBean;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * 添加ecatalog
 */

public class ProAddEcatalogAdapter extends BaseAdapter<EcatalogListNoPageableBean> {

    public ProAddEcatalogAdapter(List<EcatalogListNoPageableBean> data) {
        super(R.layout.list_item_pro_ecatalog, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, EcatalogListNoPageableBean item, int position) {
        helper.setText(R.id.item_pro_ecatalog_accout, item.getUserName() + "")
                .setText(R.id.item_pro_ecatalog_type, item.getCategory() + "")
                .setText(R.id.item_pro_ecatalog_title, item.getName());
        helper.setOnClickListener(R.id.item_pro_ecatalog_add, new OnItemChildClickListener());
    }
}
