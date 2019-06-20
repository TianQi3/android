package com.humming.asc.sales.adapter.ecatalog;

import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.EcatalogMarginRequest;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.BaseViewHolder;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogItemDetailAndroidResopnse;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogMarginResponse;
import com.squareup.okhttp.Request;

import java.util.List;

/**
 * Created by Ztq on 2018/06/20.
 * 报价单产品 List
 */

public class EcatalogDetailProAdapter extends BaseAdapter<EcatalogItemDetailAndroidResopnse> {

    public EcatalogDetailProAdapter(List<EcatalogItemDetailAndroidResopnse> data) {
        super(R.layout.list_item_ecatalog_pro, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final EcatalogItemDetailAndroidResopnse item, final int position) {
        final InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(Application.getInstance().getCurrentActivity().INPUT_METHOD_SERVICE);
        final EditText priceEd = helper.getView(R.id.item_ecatalog_pro_price);
        final EditText sizeEd = helper.getView(R.id.item_ecatalog_pro_size);
        final EditText tradValueEd = helper.getView(R.id.item_ecatalog_pro_trad_value);
        final TextView skuTv = helper.getView(R.id.item_ecatalog_pro_sku_margin);
        final TextView rpTv = helper.getView(R.id.item_ecatalog_pro_rp_discount);
        final TextView apTv = helper.getView(R.id.item_ecatalog_pro_ap);
        final LinearLayout parentLayout = helper.getView(R.id.item_ecatalog_pro_layout);
        final LinearLayout editLayout = helper.getView(R.id.item_ecatalog_pro_edit_layout);

        if (item.getHasColor().intValue() == 1) {//有颜色
            parentLayout.setBackgroundColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.yello));
            editLayout.setBackgroundColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.yello));

        } else {
            parentLayout.setBackgroundColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.WhiteBG));
            editLayout.setBackgroundColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.fcfcfc));
        }
        helper.setText(R.id.item_ecatalog_pro_name, item.getNameCn())
                .setText(R.id.item_ecatalog_pro_item_code, item.getItemCode())
                .setText(R.id.item_ecatalog_pro_name_en, item.getNameEn())
                .setText(R.id.item_ecatalog_pro_rp, "¥" + item.getRpPrice() + "")
                .setText(R.id.item_ecatalog_pro_feedback, item.getFeedback())
                .setText(R.id.item_ecatalog_pro_trad_value, item.getTradValue() + "")
                .setText(R.id.item_ecatalog_pro_price, item.getUnitPrice() + "")
                .setText(R.id.item_ecatalog_pro_size, item.getNum() + "")
                .setText(R.id.item_ecatalog_pro_sku_margin, item.getSkuMargin().toString() + "")
                .setText(R.id.item_ecatalog_pro_rp_discount, item.getRpDiscount().toString() + "")
                .setText(R.id.item_ecatalog_pro_ap, item.getAp().toString() + "")
                .setOnClickListener(R.id.item_ecatalog_pro_year_layout, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_ecatalog_pro_set_top, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_ecatalog_pro_name, new OnItemChildClickListener())
                .setOnClickListener(R.id.item_ecatalog_pro_name_en, new OnItemChildClickListener());
        if ("1".equals(item.getFeedbackStatus())) {//加入
            helper.setTextColor(R.id.item_ecatalog_pro_feedback, Application.getInstance().getResources().getColor(R.color.delete_green));
        } else if ("0".equals(item.getFeedbackStatus())) {//删除
            helper.setTextColor(R.id.item_ecatalog_pro_feedback, Application.getInstance().getResources().getColor(R.color.delete_red));
        } else {

        }
        if (position > 10) {
            helper.setText(R.id.item_ecatalog_pro_number, position + 1 + "");
        } else {
            int s = position + 1;
            helper.setText(R.id.item_ecatalog_pro_number, "0" + s + "");
        }

        if (item.getVintage() == null) {
            helper.setText(R.id.item_ecatalog_pro_year, Application.getInstance().getCurrentActivity().getResources().getString(R.string.vintages));
        } else {
            helper.setText(R.id.item_ecatalog_pro_year, item.getVintage() + Application.getInstance().getCurrentActivity().getResources().getString(R.string.year));
        }

        priceEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                imm.hideSoftInputFromWindow(priceEd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (priceEd.getText().toString().length() == 0) {
                    priceEd.setText("");
                }
                setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                return false;
            }
        });
        priceEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    if ("".equals(priceEd.getText().toString()) || priceEd.getText().toString() == null) {
                        priceEd.setText("");
                        setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                    } else {
                        setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                    }
                }
            }
        });
        sizeEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                imm.hideSoftInputFromWindow(sizeEd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (sizeEd.getText().toString().length() == 0) {
                    sizeEd.setText("1");
                }
                setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                return false;
            }
        });
        sizeEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    if ("".equals(sizeEd.getText().toString()) || sizeEd.getText().toString() == null) {
                        sizeEd.setText("1");
                        setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                    } else {
                        setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                    }
                }
            }
        });
        tradValueEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                imm.hideSoftInputFromWindow(tradValueEd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                if (tradValueEd.getText().toString().length() == 0) {
                    tradValueEd.setText("");
                }
                setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                return false;
            }
        });
        tradValueEd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    if ("".equals(tradValueEd.getText().toString()) || tradValueEd.getText().toString() == null) {
                        tradValueEd.setText("");
                        setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                    } else {
                        setMargin(editLayout, parentLayout, skuTv, rpTv, apTv, item.getId() + "", priceEd.getText().toString(), sizeEd.getText().toString(), tradValueEd.getText().toString(), position);
                    }
                }
            }
        });
    }

    //设置margin
    private void setMargin(final LinearLayout editLayout, final LinearLayout parentLayout, final TextView skuTv, final TextView rpTv, final TextView apTv, String id, String unitPrice, String num, String tradValue, int position) {
        final EcatalogMarginRequest request = new EcatalogMarginRequest();
        request.setId(id);
        request.setNum(num);
        request.setTradValue(tradValue);
        request.setUnitPrice(unitPrice);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_MARGIN, new OkHttpClientManager.ResultCallback<EcatalogMarginResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(EcatalogMarginResponse response) {
                skuTv.setText(response.getSkuMargin() + "");
                rpTv.setText(response.getRpDiscount() + "");
                apTv.setText(response.getAp() + "");
                if (response.getHasColor().intValue() == 1) {//有颜色
                    parentLayout.setBackgroundColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.yello));
                    editLayout.setBackgroundColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.yello));
                } else {
                    editLayout.setBackgroundColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.fcfcfc));
                    parentLayout.setBackgroundColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.WhiteBG));
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, EcatalogMarginResponse.class);
    }
}
