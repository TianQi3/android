package com.humming.asc.sales.adapter.product;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.asc.sales.model.product.ProductListEntity;
import com.humming.dto.ecatalogResponse.bean.product.StockDetailList;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * MF List
 */

public class ProductAdapter extends BaseAdapter<ProductListEntity> {

    public ProductAdapter(List<ProductListEntity> data) {
        super(R.layout.list_item_product, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ProductListEntity item, int position) {
        LinearLayout stockLayout = helper.getView(R.id.item_product_stock_contain);
        LinearLayout stockLayoutView = helper.getView(R.id.item_product_stock_header_layout);
        // HorizontalScrollView scrollView = helper.getView(R.id.item_product_stock_scroll);
        LinearLayout stockLayoutNoScroll = helper.getView(R.id.item_product_stock_contain_no_scroll);
        LinearLayout stockLayoutNoScroll1 = helper.getView(R.id.item_product_stock_contain1_no_scroll);
        ImageView imageView = helper.getView(R.id.item_product_action_down_n_s);
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
                .setText(R.id.item_product_stock, item.getProductList().getStockNum() + "");

        if (item.getStockLists() != null && item.getStockLists().size() > 0) {
            if (item.getStockLists().size() > 3) {
                stockLayoutNoScroll1.setVisibility(View.GONE);
                if (item.getCheckSelect() == 1) {
                    stockLayout.removeAllViews();
                    imageView.setImageResource(R.mipmap.product_action_down_s);
                    stockLayoutView.setVisibility(View.VISIBLE);
                    // stockLayout.addView(addStockViewHeader(helper));
                    for (StockDetailList s : item.getStockLists()) {
                        stockLayout.addView(addStockView(s, helper));
                    }
                } else {
                    stockLayout.removeAllViews();
                    stockLayoutView.setVisibility(View.GONE);
                    imageView.setImageResource(R.mipmap.product_action_down_n);
                }
            } else {
                stockLayoutView.setVisibility(View.GONE);
                if (item.getCheckSelect() == 1) {
                    stockLayoutNoScroll.removeAllViews();
                    imageView.setImageResource(R.mipmap.product_action_down_s);
                    stockLayoutNoScroll1.setVisibility(View.VISIBLE);
                    stockLayoutNoScroll.addView(addStockViewHeader1(helper));
                    for (StockDetailList s : item.getStockLists()) {
                        stockLayoutNoScroll.addView(addStockView1(s, helper));
                    }
                } else {
                    stockLayoutNoScroll.removeAllViews();
                    stockLayoutNoScroll1.setVisibility(View.GONE);
                    imageView.setImageResource(R.mipmap.product_action_down_n);
                }
            }
        } else {
            stockLayoutView.setVisibility(View.GONE);
            stockLayoutNoScroll1.setVisibility(View.GONE);
            stockLayout.removeAllViews();
            stockLayoutNoScroll.removeAllViews();
            if (item.getCheckSelect() == 1) {
                imageView.setImageResource(R.mipmap.product_action_down_s);
            } else {
                imageView.setImageResource(R.mipmap.product_action_down_n);
            }
        }
        if (item.getProductList().getImgUrl() != null) {
            Glide.with(helper.getConvertView().getContext())
                    .load(item.getProductList().getImgUrl())
                    // .placeholder(R.mipmap.image_loading)
                    .into((ImageView) helper.getView(R.id.item_product_img));
        }
        helper.setOnClickListener(R.id.item_product_stock_down, new OnItemChildClickListener());
        helper.setOnClickListener(R.id.item_product_add, new OnItemChildClickListener());
    }


    public SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.6f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

    //动态添加展开库存信息
    private View addStockView(StockDetailList stockList, BaseViewHolder helper) {

        View view = LayoutInflater.from(helper.getConvertView().getContext()).inflate(R.layout.list_item_pro_stock, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //lp.setMargins(0, 15, 0, 0);
        view.setLayoutParams(lp);
        TextView years = (TextView) view.findViewById(R.id.item_pro_stock_years);
        TextView shwhStock = (TextView) view.findViewById(R.id.item_pro_stock_shwh);
        TextView bjwhStock = (TextView) view.findViewById(R.id.item_pro_stock_bjwh);
        TextView cdwhStock = (TextView) view.findViewById(R.id.item_pro_stock_cdwh);
        TextView gzwhStock = (TextView) view.findViewById(R.id.item_pro_stock_gzwh);
        TextView hkwhStock = (TextView) view.findViewById(R.id.item_pro_stock_hkwh);
        TextView mcwhStock = (TextView) view.findViewById(R.id.item_pro_stock_mcwh);

        years.setText(stockList.getVintage());
        shwhStock.setText(stockList.getShwh() + "");
        bjwhStock.setText(stockList.getBjwh() + "");
        cdwhStock.setText(stockList.getCdwh() + "");
        gzwhStock.setText(stockList.getGzwh() + "");
        hkwhStock.setText(stockList.getHkwh() + "");
        mcwhStock.setText(stockList.getMcwh() + "");
        return view;
    }

    //动态添加展开库存信息
    private View addStockViewHeader(BaseViewHolder helper) {
        View view = LayoutInflater.from(helper.getConvertView().getContext()).inflate(R.layout.list_item_pro_stock_label, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        //lp.setMargins(0, 15, 0, 0);
        view.setLayoutParams(lp);
        return view;
    }

    //动态添加展开库存信息
    private View addStockView1(StockDetailList stockList, BaseViewHolder helper) {

        View view = LayoutInflater.from(helper.getConvertView().getContext()).inflate(R.layout.list_item_pro_stock1, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.weight = 1;
        //lp.setMargins(0, 15, 0, 0);
        view.setLayoutParams(lp);
        TextView years = (TextView) view.findViewById(R.id.item_pro_stock_years);
        TextView shwhStock = (TextView) view.findViewById(R.id.item_pro_stock_shwh);
        TextView bjwhStock = (TextView) view.findViewById(R.id.item_pro_stock_bjwh);
        TextView cdwhStock = (TextView) view.findViewById(R.id.item_pro_stock_cdwh);
        TextView gzwhStock = (TextView) view.findViewById(R.id.item_pro_stock_gzwh);
        TextView hkwhStock = (TextView) view.findViewById(R.id.item_pro_stock_hkwh);
        TextView mcwhStock = (TextView) view.findViewById(R.id.item_pro_stock_mcwh);

        years.setText(stockList.getVintage());
        shwhStock.setText(stockList.getShwh() + "");
        bjwhStock.setText(stockList.getBjwh() + "");
        cdwhStock.setText(stockList.getCdwh() + "");
        gzwhStock.setText(stockList.getGzwh() + "");
        hkwhStock.setText(stockList.getHkwh() + "");
        mcwhStock.setText(stockList.getMcwh() + "");
        return view;
    }

    //动态添加展开库存信息
    private View addStockViewHeader1(BaseViewHolder helper) {
        View view = LayoutInflater.from(helper.getConvertView().getContext()).inflate(R.layout.list_item_pro_stock_label1, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        //lp.setMargins(0, 15, 0, 0);
        lp.weight = 1;
        view.setLayoutParams(lp);
        return view;
    }
}
