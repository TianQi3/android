package com.humming.asc.sales.adapter.ecatalog;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.asc.sales.model.product.ProductListEntity;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF List
 */

public class EcatalogAddProAdapter extends BaseAdapter<ProductListEntity> {

    public EcatalogAddProAdapter(List<ProductListEntity> data) {
        super(R.layout.list_item_ecatalog_add_pro, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductListEntity item, int position) {
        ImageView radioImg = helper.getView(R.id.item_product__radio);
        if (item.getCheckSelect() == 1) {
            radioImg.setImageResource(R.drawable.ecatalog_add_pro_radio_sel);
        } else {
            radioImg.setImageResource(R.drawable.ecatalog_add_product_radio);
        }
        SpannableString spannableString = changTVsize(item.getProductList().getRpPrice() + "");
        helper.setText(R.id.item_product_prize, spannableString);
        helper.setText(R.id.item_product__name_cn, item.getProductList().getNameCn())
                .setText(R.id.item_product__name_en, item.getProductList().getNameEn())
                .setText(R.id.tem_product__brand_cn, item.getProductList().getBrandNameCn())
                .setText(R.id.tem_product__brand_en, item.getProductList().getBrandNameEn())
                .setText(R.id.tem_product__type_cn, item.getProductList().getTypeNameCn())
                .setText(R.id.tem_product__type_en, item.getProductList().getTypeNameEn())
                .setText(R.id.tem_product__country_cn, item.getProductList().getCountryNameCn())
                .setText(R.id.tem_product__country_en, item.getProductList().getCountryNameEn())
                .setText(R.id.item_product_code, item.getProductList().getItemCode() + "")
                .setText(R.id.item_product_stock, item.getProductList().getStockNum() + "")
                .setOnClickListener(R.id.item_product__radio, new OnItemChildClickListener());
        if (item.getProductList().getImgUrl() != null) {
            Glide.with(helper.getConvertView().getContext())
                    .load(item.getProductList().getImgUrl())
                    // .placeholder(R.mipmap.image_loading)
                    .into((ImageView) helper.getView(R.id.item_product_img));
        }
    }


    public SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.6f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

}
