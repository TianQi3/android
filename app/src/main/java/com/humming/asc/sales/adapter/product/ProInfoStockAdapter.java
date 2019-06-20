package com.humming.asc.sales.adapter.product;

import android.view.View;
import android.widget.ImageView;

import com.humming.asc.sales.R;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.asc.sales.model.product.StockListEntity;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * 产品库存 List
 */

public class ProInfoStockAdapter extends BaseAdapter<StockListEntity> {

    public ProInfoStockAdapter(List<StockListEntity> data) {
        super(R.layout.list_item_pro_info_stock, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, StockListEntity item, int position) {
        ImageView shwhImg = helper.getView(R.id.item_pro_info_stock__shwh_warn);
        ImageView bjwhImg = helper.getView(R.id.item_pro_info_stock__bjwh_warn);
        ImageView cdwhImg = helper.getView(R.id.item_pro_info_stock__cdwh_warn);
        ImageView gzwhImg = helper.getView(R.id.item_pro_info_stock__gzwh_warn);
        ImageView hkwhImg = helper.getView(R.id.item_pro_info_stock__hkwh_warn);
        ImageView mcwhImg = helper.getView(R.id.item_pro_info_stock__mcwh_warn);

        if (item.getSHWH().getWhouseList().getEarlyWarning() != null && item.getSHWH().getWhouseList().getEarlyWarning() > 0) {
            shwhImg.setVisibility(View.VISIBLE);
        } else {
            shwhImg.setVisibility(View.GONE);
        }
        if (item.getBJWH().getWhouseList().getEarlyWarning() != null && item.getBJWH().getWhouseList().getEarlyWarning() > 0) {
            bjwhImg.setVisibility(View.VISIBLE);
        } else {
            bjwhImg.setVisibility(View.GONE);
        }
        if (item.getCDWH().getWhouseList().getEarlyWarning() != null && item.getCDWH().getWhouseList().getEarlyWarning() > 0) {
            cdwhImg.setVisibility(View.VISIBLE);
        } else {
            cdwhImg.setVisibility(View.GONE);
        }
        if (item.getGZWH().getWhouseList().getEarlyWarning() != null && item.getGZWH().getWhouseList().getEarlyWarning() > 0) {
            gzwhImg.setVisibility(View.VISIBLE);
        } else {
            gzwhImg.setVisibility(View.GONE);
        }
        if (item.getHKWH().getWhouseList().getEarlyWarning() != null && item.getHKWH().getWhouseList().getEarlyWarning() > 0) {
            hkwhImg.setVisibility(View.VISIBLE);
        } else {
            hkwhImg.setVisibility(View.GONE);
        }
        if (item.getMCWH().getWhouseList().getEarlyWarning() != null && item.getMCWH().getWhouseList().getEarlyWarning() > 0) {
            mcwhImg.setVisibility(View.VISIBLE);
        } else {
            mcwhImg.setVisibility(View.GONE);
        }
        helper.setText(R.id.item_pro_info_stock__years, item.getVintage() + "")
                .setText(R.id.item_pro_info_stock__shwh_tv, item.getSHWH().getWhouseList().getStock() + "")
                .setText(R.id.item_pro_info_stock__bjwh_tv, item.getBJWH().getWhouseList().getStock() + "")
                .setText(R.id.item_pro_info_stock__cdwh_tv, item.getCDWH().getWhouseList().getStock() + "")
                .setText(R.id.item_pro_info_stock__gzwh_tv, item.getGZWH().getWhouseList().getStock() + "")
                .setText(R.id.item_pro_info_stock__hkwh_tv, item.getHKWH().getWhouseList().getStock() + "")
                .setText(R.id.item_pro_info_stock__mcwh_tv, item.getMCWH().getWhouseList().getStock() + "")
                .setText(R.id.item_pro_info_stock__shwh_booking, item.getSHWH().getWhouseList().getBooking() + "")
                .setText(R.id.item_pro_info_stock__bjwh_booking, item.getBJWH().getWhouseList().getBooking() + "")
                .setText(R.id.item_pro_info_stock__cdwh_booking, item.getCDWH().getWhouseList().getBooking() + "")
                .setText(R.id.item_pro_info_stock__gzwh_booking, item.getGZWH().getWhouseList().getBooking() + "")
                .setText(R.id.item_pro_info_stock__hkwh_booking, item.getHKWH().getWhouseList().getBooking() + "")
                .setText(R.id.item_pro_info_stock__mcwh_booking, item.getMCWH().getWhouseList().getBooking() + "")
                .setOnClickListener(R.id.item_pro_info_stock__shwh, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_pro_info_stock__bjwh, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_pro_info_stock__cdwh, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_pro_info_stock__gzwh, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_pro_info_stock__hkwh, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_pro_info_stock__mcwh, new OnItemChildClickListener());
        if (item.getSHWH().isCheckSelect()) {
            helper.setBackgroundColor(R.id.item_pro_info_stock__shwh, helper.getConvertView().getContext().getResources().getColor(R.color.f0f0f0));
        } else {
            helper.setBackgroundColor(R.id.item_pro_info_stock__shwh, helper.getConvertView().getContext().getResources().getColor(R.color.WhiteBG));
        }
        if (item.getGZWH().isCheckSelect()) {
            helper.setBackgroundColor(R.id.item_pro_info_stock__gzwh, helper.getConvertView().getContext().getResources().getColor(R.color.f0f0f0));
        } else {
            helper.setBackgroundColor(R.id.item_pro_info_stock__gzwh, helper.getConvertView().getContext().getResources().getColor(R.color.WhiteBG));
        }
        if (item.getBJWH().isCheckSelect()) {
            helper.setBackgroundColor(R.id.item_pro_info_stock__bjwh, helper.getConvertView().getContext().getResources().getColor(R.color.f0f0f0));
        } else {
            helper.setBackgroundColor(R.id.item_pro_info_stock__bjwh, helper.getConvertView().getContext().getResources().getColor(R.color.WhiteBG));
        }
        if (item.getCDWH().isCheckSelect()) {
            helper.setBackgroundColor(R.id.item_pro_info_stock__cdwh, helper.getConvertView().getContext().getResources().getColor(R.color.f0f0f0));
        } else {
            helper.setBackgroundColor(R.id.item_pro_info_stock__cdwh, helper.getConvertView().getContext().getResources().getColor(R.color.WhiteBG));
        }
        if (item.getMCWH().isCheckSelect()) {
            helper.setBackgroundColor(R.id.item_pro_info_stock__mcwh, helper.getConvertView().getContext().getResources().getColor(R.color.f0f0f0));
        } else {
            helper.setBackgroundColor(R.id.item_pro_info_stock__mcwh, helper.getConvertView().getContext().getResources().getColor(R.color.WhiteBG));
        }
        if (item.getHKWH().isCheckSelect()) {
            helper.setBackgroundColor(R.id.item_pro_info_stock__hkwh, helper.getConvertView().getContext().getResources().getColor(R.color.f0f0f0));
        } else {
            helper.setBackgroundColor(R.id.item_pro_info_stock__hkwh, helper.getConvertView().getContext().getResources().getColor(R.color.WhiteBG));
        }

    }

}
