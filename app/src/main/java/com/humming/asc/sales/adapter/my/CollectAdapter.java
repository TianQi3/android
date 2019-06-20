package com.humming.asc.sales.adapter.my;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.dto.ecatalogResponse.bean.product.ProductList;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF List
 */

public class CollectAdapter extends BaseAdapter<ProductList> {

    public CollectAdapter(List<ProductList> data) {
        super(R.layout.list_item_collect, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductList item, int position) {
        SpannableString spannableString = changTVsize(item.getRpPrice() + "");
        helper.setText(R.id.item_collect_prize, spannableString);
        helper.setText(R.id.item_collect__name_cn, item.getNameCn())
                .setText(R.id.item_collect__name_en, item.getNameEn())
                .setText(R.id.item_collect__brand_cn, item.getBrandNameCn())
                .setText(R.id.item_collect__brand_en, item.getBrandNameEn())
                .setText(R.id.item_collect__type_cn, item.getTypeNameCn())
                .setText(R.id.item_collect__type_en, item.getTypeNameEn())
                .setText(R.id.item_collect__country_cn, item.getCountryNameCn())
                .setText(R.id.item_collect__country_en, item.getCountryNameEn())
                .setText(R.id.item_collect__itemcode, item.getItemCode() + "")
                .setText(R.id.item_collect_stock, item.getStockNum() + "")
                .setOnClickListener(R.id.item_collect__delete, new OnItemChildClickListener());
        Glide.with(helper.getConvertView().getContext())
                .load(item.getImgUrl())
                // .placeholder(R.mipmap.image_loading)
                .into((ImageView) helper.getView(R.id.item_collect_img));
    }


    public SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.6f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
