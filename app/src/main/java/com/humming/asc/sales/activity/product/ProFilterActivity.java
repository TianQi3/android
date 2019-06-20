package com.humming.asc.sales.activity.product;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.FilterEntity;
import com.humming.asc.sales.RequestData.RequestNull;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.product.filter.FilterActivity;
import com.humming.asc.sales.activity.product.filter.FilterAreaActivity;
import com.humming.asc.sales.activity.product.filter.FilterBrandActivity;
import com.humming.asc.sales.activity.product.filter.FilterChannelActivity;
import com.humming.asc.sales.activity.product.filter.FilterPriceActivity;
import com.humming.asc.sales.model.product.UDCListResponse;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.bean.search.UDCList;
import com.humming.dto.ecatalogResponse.product.SearchUDCResponse;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProFilterActivity extends AbstractActivity implements View.OnClickListener {

    public static final int PODUCT_FILTER_COUNTRY_RESULT_CODE = 19001;
    public static final int PODUCT_FILTER_PRICE_RESULT_CODE = 19002;
    public static final int PODUCT_FILTER_AREA_RESULT_CODE = 19003;
    public static final int PODUCT_FILTER_TYPE_RESULT_CODE = 19004;
    public static final int PODUCT_FILTER_SWEET_RESULT_CODE = 19005;
    public static final int PODUCT_FILTER_COLOR_RESULT_CODE = 19006;
    public static final int PODUCT_FILTER_BODY_RESULT_CODE = 19007;
    public static final int PODUCT_FILTER_VOLUMN_RESULT_CODE = 19008;
    public static final int PODUCT_FILTER_COLOUR_RESULT_CODE = 19009;
    public static final int PODUCT_FILTER_VINTAGE_RESULT_CODE = 19010;
    public static final int PODUCT_FILTER_CHANNELML_RESULT_CODE = 19011;
    public static final int PODUCT_FILTER_CHANNELHK_RESULT_CODE = 19012;
    public static final int PODUCT_FILTER_BRAND_RESULT_CODE = 19013;
    public static final int PODUCT_FILTER_GRAPE_RESULT_CODE = 19014;
    public static final String FILTER_RESULT = "result";
    public static final String FILTER_AREA_RESULT = "area_result";
    public static final String FILTER_SUB_AREA_RESULT = "area_sub_result";
    private Toolbar toolbar;
    private TextView mFilterCountryTv;
    private LinearLayout mFilterCountry;
    private TextView mFilterAreaTv;
    private LinearLayout mFilterArea;
    private TextView mFilterBrandTv;
    private LinearLayout mFilterBrand;
    private TextView mFilterTypeTv;
    private LinearLayout mFilterType;
    private TextView mFilterPriceTv;
    private LinearLayout mFilterPrice;
    private TextView mFilterSweetnessTv;
    private LinearLayout mFilterSweetness;
    private TextView mFilterColorTv;
    private LinearLayout mFilterColor;
    private TextView mFilterLiquorbodyTv;
    private LinearLayout mFilterLiquorbody;
    private TextView mFilterCapacityTv;
    private LinearLayout mFilterCapacity;
    private TextView mFilterAlcoholTv;
    private LinearLayout mFilterAlcohol;
    private TextView mFilterYearTv;
    private LinearLayout mFilterYear;
    private TextView mFilterGrapeTv;
    private LinearLayout mFilterGrape;
    private TextView mFilterMainlandChannelTv;
    private LinearLayout mFilterMainlandChannel;
    private TextView mFilterHkandmcChannelTv;
    private LinearLayout mFilterHkandmcChannel;
    private TextView mFilterReset;
    private TextView mFilterSubmit;
    private SearchUDCResponse response;
    UDCListResponse listResponse;
    /*private List<UDCList> countryValues;
    private List<UDCList> typeValues;
    private List<UDCList> brandValues;
    private List<UDCList> areaValues;
    private List<UDCList> sweetValues;
    private List<UDCList> colorValues;
    private List<UDCList> bodyValues;
    private List<UDCList> volumnValues;
    private List<UDCList> colourValues;
    private List<UDCList> vintageValues;
    private List<UDCList> channelMLValues;
    private List<UDCList> channelHKValues;*/
    private String priceValue = "";
    private String priceNumber = "";////其他为列表返回 -1为自定义

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_filter);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        initData();
        initValue();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.filtera);
    }


    private void initData() {
        mLoading.show();
        final RequestNull request = new RequestNull();
        OkHttpClientManager.postAsyn(Config.ECATALLOG_SEARCH_UDC, new OkHttpClientManager.ResultCallback<SearchUDCResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                mLoading.hide();
            }

            @Override
            public void onResponse(SearchUDCResponse responses) {
                mLoading.hide();
                response = responses;
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                mLoading.hide();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, SearchUDCResponse.class);
    }


    private void initView() {
        mFilterCountryTv = (TextView) findViewById(R.id.product_filter_country_tv);
        mFilterCountry = (LinearLayout) findViewById(R.id.product_filter_country);
        mFilterCountry.setOnClickListener(this);
        mFilterAreaTv = (TextView) findViewById(R.id.product_filter_area_tv);
        mFilterArea = (LinearLayout) findViewById(R.id.product_filter_area);
        mFilterArea.setOnClickListener(this);
        mFilterBrandTv = (TextView) findViewById(R.id.product_filter_brand_tv);
        mFilterBrand = (LinearLayout) findViewById(R.id.product_filter_brand);
        mFilterBrand.setOnClickListener(this);
        mFilterTypeTv = (TextView) findViewById(R.id.product_filter_type_tv);
        mFilterType = (LinearLayout) findViewById(R.id.product_filter_type);
        mFilterType.setOnClickListener(this);
        mFilterPriceTv = (TextView) findViewById(R.id.product_filter_price_tv);
        mFilterPrice = (LinearLayout) findViewById(R.id.product_filter_price);
        mFilterPrice.setOnClickListener(this);
        mFilterSweetnessTv = (TextView) findViewById(R.id.product_filter_sweetness_tv);
        mFilterSweetness = (LinearLayout) findViewById(R.id.product_filter_sweetness);
        mFilterSweetness.setOnClickListener(this);
        mFilterColorTv = (TextView) findViewById(R.id.product_filter_color_tv);
        mFilterColor = (LinearLayout) findViewById(R.id.product_filter_color);
        mFilterColor.setOnClickListener(this);
        mFilterLiquorbodyTv = (TextView) findViewById(R.id.product_filter_liquorbody_tv);
        mFilterLiquorbody = (LinearLayout) findViewById(R.id.product_filter_liquorbody);
        mFilterLiquorbody.setOnClickListener(this);
        mFilterCapacityTv = (TextView) findViewById(R.id.product_filter_capacity_tv);
        mFilterCapacity = (LinearLayout) findViewById(R.id.product_filter_capacity);
        mFilterCapacity.setOnClickListener(this);
        mFilterAlcoholTv = (TextView) findViewById(R.id.product_filter_alcohol_tv);
        mFilterAlcohol = (LinearLayout) findViewById(R.id.product_filter_alcohol);
        mFilterAlcohol.setOnClickListener(this);
        mFilterYearTv = (TextView) findViewById(R.id.product_filter_year_tv);
        mFilterYear = (LinearLayout) findViewById(R.id.product_filter_year);
        mFilterYear.setOnClickListener(this);
        mFilterGrapeTv = (TextView) findViewById(R.id.product_filter_grape_tv);
        mFilterGrape = (LinearLayout) findViewById(R.id.product_filter_grape);
        mFilterGrape.setOnClickListener(this);
        mFilterMainlandChannelTv = (TextView) findViewById(R.id.product_filter_mainland_channel_tv);
        mFilterMainlandChannel = (LinearLayout) findViewById(R.id.product_filter_mainland_channel);
        mFilterMainlandChannel.setOnClickListener(this);
        mFilterHkandmcChannelTv = (TextView) findViewById(R.id.product_filter_hkandmc_channel_tv);
        mFilterHkandmcChannel = (LinearLayout) findViewById(R.id.product_filter_hkandmc_channel);
        mFilterHkandmcChannel.setOnClickListener(this);
        mFilterReset = (TextView) findViewById(R.id.product_filter_reset);
        mFilterReset.setOnClickListener(this);
        mFilterSubmit = (TextView) findViewById(R.id.product_filter_submit);
        mFilterSubmit.setOnClickListener(this);
        /*countryValues = new ArrayList<>();
        areaValues = new ArrayList<>();
        typeValues = new ArrayList<>();
        sweetValues = new ArrayList<>();
        colorValues = new ArrayList<>();
        bodyValues = new ArrayList<>();
        channelHKValues = new ArrayList<>();
        channelMLValues = new ArrayList<>();
        colourValues = new ArrayList<>();
        vintageValues = new ArrayList<>();
        colourValues = new ArrayList<>();
        volumnValues = new ArrayList<>();
        brandValues = new ArrayList<>();*/
        listResponse = new UDCListResponse();
    }

    //附值
    private void initValue() {
        UDCListResponse response = Application.getInstance().getUdcListResponse();
        if (response != null) {//不为空带入列表
            listResponse = response;
            returnCountryValue(response.getCountryValues());
            returnTypeValue(response.getTypeValues());
            returnBrandValue(response.getBrandValues());
            returnGrapeValue(response.getGrapeValues());
            returnAreaValue(response.getAreaValues());
            returnSubAreaValue(response.getSubAreaValues());
            returnSweetValue(response.getSweetValues());
            returnColorValue(response.getColorValues());
            returnBodyValue(response.getBodyValues());
            returnVolumnValue(response.getVolumnValues());
            returnColourValue(response.getColourValues());
            returnVintageValue(response.getVintageValues());
            returnChannelMLValue(response.getChannelMLValues());
            returnChannelHKValue(response.getChannelHKValues());
            if (response.getPriceValue() != null && !"".equals(response.getPriceValue())) {
                returnPriceValue(response.getPriceValue());
            }
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Bundle resultBundles = new Bundle();
                resultBundles.putSerializable("data", null);
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(
                        RESULT_OK,
                        resultIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.product_filter_country:
                intent = new Intent(ProFilterActivity.this, FilterActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("response", (Serializable) response.getCountry());
                bundle.putSerializable("select_response", (Serializable) listResponse.getCountryValues());
                bundle.putString("title", getResources().getString(R.string.countrys));
                intent.putExtras(bundle);
                this.startActivityForResult(intent, PODUCT_FILTER_COUNTRY_RESULT_CODE);
                break;
            case R.id.product_filter_area:
                if (listResponse.getCountryValues().size() <= 0) {
                    Toast.makeText(this, getResources().getString(R.string.please_select_country), Toast.LENGTH_SHORT).show();
                } else {
                    intent = new Intent(ProFilterActivity.this, FilterAreaActivity.class);
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("area_value", (Serializable) response.getArea());//一级产区
                    bundle2.putSerializable("area_sub_value", (Serializable) response.getSubArea());//二级产区
                    bundle2.putSerializable("country_value", (Serializable) listResponse.getCountryValues());//国家
                    bundle2.putSerializable("select_area_value", (Serializable) listResponse.getAreaValues());//选中的一级产区
                    bundle2.putSerializable("select_sub_area_value", (Serializable) listResponse.getSubAreaValues());//选中的二级产区
                    intent.putExtras(bundle2);
                    this.startActivityForResult(intent, PODUCT_FILTER_AREA_RESULT_CODE);
                }
                break;
            case R.id.product_filter_brand:
                intent = new Intent(ProFilterActivity.this, FilterBrandActivity.class);
                Bundle brandBundle = new Bundle();
                brandBundle.putSerializable("response", (Serializable) response.getBrand());
                brandBundle.putSerializable("select_response", (Serializable) listResponse.getBrandValues());
                brandBundle.putString("title", getResources().getString(R.string.brand));
                intent.putExtras(brandBundle);
                this.startActivityForResult(intent, PODUCT_FILTER_BRAND_RESULT_CODE);
                break;
            case R.id.product_filter_grape:
                intent = new Intent(ProFilterActivity.this, FilterBrandActivity.class);
                Bundle grapeBundle = new Bundle();
                grapeBundle.putSerializable("response", (Serializable) response.getGrape());
                grapeBundle.putSerializable("select_response", (Serializable) listResponse.getGrapeValues());
                grapeBundle.putString("title", getResources().getString(R.string.grape));
                intent.putExtras(grapeBundle);
                this.startActivityForResult(intent, PODUCT_FILTER_GRAPE_RESULT_CODE);
                break;
            case R.id.product_filter_type:
                intent = new Intent(ProFilterActivity.this, FilterActivity.class);
                Bundle bundle3 = new Bundle();
                bundle3.putSerializable("response", (Serializable) response.getType());
                bundle3.putSerializable("select_response", (Serializable) listResponse.getTypeValues());
                bundle3.putString("title", getResources().getString(R.string.types));
                intent.putExtras(bundle3);
                this.startActivityForResult(intent, PODUCT_FILTER_TYPE_RESULT_CODE);
                break;
            case R.id.product_filter_price:
                intent = new Intent(ProFilterActivity.this, FilterPriceActivity.class);
                Bundle priceBundle = new Bundle();
                priceBundle.putSerializable("response", (Serializable) response.getPrice());
                priceBundle.putString("price_value", priceValue);
                priceBundle.putString("price_number", priceNumber);
                intent.putExtras(priceBundle);
                this.startActivityForResult(intent, PODUCT_FILTER_PRICE_RESULT_CODE);
                break;
            case R.id.product_filter_sweetness:
                intent = new Intent(ProFilterActivity.this, FilterActivity.class);
                Bundle bundle4 = new Bundle();
                bundle4.putSerializable("response", (Serializable) response.getSweent());
                bundle4.putSerializable("select_response", (Serializable) listResponse.getSweetValues());
                bundle4.putString("title", getResources().getString(R.string.sweet));
                intent.putExtras(bundle4);
                this.startActivityForResult(intent, PODUCT_FILTER_SWEET_RESULT_CODE);
                break;
            case R.id.product_filter_color:
                intent = new Intent(ProFilterActivity.this, FilterActivity.class);
                Bundle bundle5 = new Bundle();
                bundle5.putSerializable("response", (Serializable) response.getColor());
                bundle5.putSerializable("select_response", (Serializable) listResponse.getColorValues());
                bundle5.putString("title", getResources().getString(R.string.color1));
                intent.putExtras(bundle5);
                this.startActivityForResult(intent, PODUCT_FILTER_COLOR_RESULT_CODE);
                break;
            case R.id.product_filter_liquorbody:
                intent = new Intent(ProFilterActivity.this, FilterActivity.class);
                Bundle bundle6 = new Bundle();
                bundle6.putSerializable("response", (Serializable) response.getBody());
                bundle6.putSerializable("select_response", (Serializable) listResponse.getBodyValues());
                bundle6.putString("title", getResources().getString(R.string.body1));
                intent.putExtras(bundle6);
                this.startActivityForResult(intent, PODUCT_FILTER_BODY_RESULT_CODE);
                break;
            case R.id.product_filter_capacity:
                intent = new Intent(ProFilterActivity.this, FilterActivity.class);
                Bundle bundle7 = new Bundle();
                bundle7.putSerializable("response", (Serializable) response.getVolumn());
                bundle7.putSerializable("select_response", (Serializable) listResponse.getVolumnValues());
                bundle7.putString("title", getResources().getString(R.string.volumn1));
                intent.putExtras(bundle7);
                this.startActivityForResult(intent, PODUCT_FILTER_VOLUMN_RESULT_CODE);
                break;
            case R.id.product_filter_alcohol:
                intent = new Intent(ProFilterActivity.this, FilterActivity.class);
                Bundle bundle8 = new Bundle();
                bundle8.putSerializable("response", (Serializable) response.getColour());
                bundle8.putSerializable("select_response", (Serializable) listResponse.getColourValues());
                bundle8.putString("title", getResources().getString(R.string.colour));
                intent.putExtras(bundle8);
                this.startActivityForResult(intent, PODUCT_FILTER_COLOUR_RESULT_CODE);
                break;
            case R.id.product_filter_year:
               /* intent = new Intent(ProFilterActivity.this, FilterActivity.class);
                Bundle bundle9 = new Bundle();
                bundle9.putSerializable("response", (Serializable) response.getVintage());
                bundle9.putSerializable("select_response", (Serializable) listResponse.getVintageValues());
                bundle9.putString("title", "年份");
                intent.putExtras(bundle9);
                this.startActivityForResult(intent, PODUCT_FILTER_VINTAGE_RESULT_CODE);
                break;*/
            case R.id.product_filter_mainland_channel:
                intent = new Intent(ProFilterActivity.this, FilterChannelActivity.class);
                Bundle bundle10 = new Bundle();
                bundle10.putSerializable("response", (Serializable) response.getChannelML());
                bundle10.putSerializable("select_response", (Serializable) listResponse.getChannelMLValues());
                bundle10.putString("title", getResources().getString(R.string.mainland));
                bundle10.putString("label", "ml");
                intent.putExtras(bundle10);
                this.startActivityForResult(intent, PODUCT_FILTER_CHANNELML_RESULT_CODE);
                break;
            case R.id.product_filter_hkandmc_channel:
                intent = new Intent(ProFilterActivity.this, FilterChannelActivity.class);
                Bundle bundle11 = new Bundle();
                bundle11.putSerializable("response", (Serializable) response.getChannelHK());
                bundle11.putSerializable("select_response", (Serializable) listResponse.getChannelHKValues());
                bundle11.putString("title", getResources().getString(R.string.hk));
                bundle11.putString("label", "hk");
                intent.putExtras(bundle11);
                this.startActivityForResult(intent, PODUCT_FILTER_CHANNELHK_RESULT_CODE);
                break;
            case R.id.product_filter_reset:
                clearView();
                break;
            case R.id.product_filter_submit:
                FilterEntity entity = getParmater();
                Application.getInstance().setUdcListResponse(listResponse);
                Bundle resultBundles = new Bundle();
                resultBundles.putSerializable("data", entity);
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(
                        RESULT_OK,
                        resultIntent);
                finish();
                break;
            default:
                break;
        }
    }

    private void clearView() {
        mFilterCountryTv.setText(getResources().getString(R.string.select_country));
        mFilterAlcoholTv.setText(getResources().getString(R.string.select_alcoho));
        mFilterAreaTv.setText(getResources().getString(R.string.select_area));
        mFilterBrandTv.setText(getResources().getString(R.string.select_brands));
        mFilterCapacityTv.setText(getResources().getString(R.string.selectcapacity));
        mFilterColorTv.setText(getResources().getString(R.string.select_color));
        mFilterHkandmcChannelTv.setText(getResources().getString(R.string.select_hk));
        mFilterYearTv.setText(getResources().getString(R.string.select_vintage));
        mFilterTypeTv.setText(getResources().getString(R.string.select_type));
        mFilterLiquorbodyTv.setText(getResources().getString(R.string.select_body));
        mFilterMainlandChannelTv.setText(getResources().getString(R.string.select_mainland));
        mFilterPriceTv.setText(getResources().getString(R.string.select_price));
        mFilterSweetnessTv.setText(getResources().getString(R.string.select_sweet));
        mFilterGrapeTv.setText(getResources().getString(R.string.select_grape));
        priceValue = "";
       /* channelMLValues.clear();
       sweetValues.clear();
        channelHKValues.clear();
        countryValues.clear();
        areaValues.clear();
        typeValues.clear();
        colorValues.clear();
        bodyValues.clear();
        colourValues.clear();
        vintageValues.clear();
        colourValues.clear();
        volumnValues.clear();
        brandValues.clear();*/
        listResponse = new UDCListResponse();
        Application.getInstance().setUdcListResponse(null);
        Application.getInstance().setMlAndHKChannelEntity(null);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        listResponse.setUpdate(true);
        Bundle resultBundle = data.getExtras();
        switch (requestCode) {
            case PODUCT_FILTER_COUNTRY_RESULT_CODE://国家
                listResponse.setCountryValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                listResponse.setAreaValues(new ArrayList<UDCList>());
                returnCountryValue(listResponse.getCountryValues());
                break;
            case PODUCT_FILTER_AREA_RESULT_CODE://产区
                listResponse.setAreaValues((List<UDCList>) resultBundle.getSerializable(FILTER_AREA_RESULT));
                listResponse.setSubAreaValues((List<UDCList>) resultBundle.getSerializable(FILTER_SUB_AREA_RESULT));
                returnAreaValue(listResponse.getAreaValues());
                returnSubAreaValue(listResponse.getSubAreaValues());
                break;
            case PODUCT_FILTER_PRICE_RESULT_CODE://价格
                priceValue = resultBundle.getString("select_price_value");
                priceNumber = resultBundle.getString("select_price_number");
                listResponse.setPriceValue(priceValue);
                Log.v("xxxxxxx", "number：" + priceNumber + "value：" + priceValue);
                returnPriceValue(priceValue);
                break;
            case PODUCT_FILTER_TYPE_RESULT_CODE://类型
                listResponse.setTypeValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnTypeValue(listResponse.getTypeValues());
                break;
            case PODUCT_FILTER_SWEET_RESULT_CODE://甜度
                listResponse.setSweetValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnSweetValue(listResponse.getSweetValues());
                break;
            case PODUCT_FILTER_COLOR_RESULT_CODE://色泽
                listResponse.setColorValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnColorValue(listResponse.getColorValues());
                break;
            case PODUCT_FILTER_BODY_RESULT_CODE://酒体
                listResponse.setBodyValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnBodyValue(listResponse.getBodyValues());
                break;
            case PODUCT_FILTER_VOLUMN_RESULT_CODE://容量
                listResponse.setVolumnValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnVolumnValue(listResponse.getVolumnValues());
                break;
            case PODUCT_FILTER_COLOUR_RESULT_CODE://酒精度
                listResponse.setColourValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnColourValue(listResponse.getColourValues());
                break;
            case PODUCT_FILTER_VINTAGE_RESULT_CODE://年份
                listResponse.setVintageValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnVintageValue(listResponse.getVintageValues());
                break;
            case PODUCT_FILTER_CHANNELML_RESULT_CODE://大陆渠道
                listResponse.setChannelMLValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnChannelMLValue(listResponse.getChannelMLValues());
                break;
            case PODUCT_FILTER_CHANNELHK_RESULT_CODE://港澳渠道
                listResponse.setChannelHKValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnChannelHKValue(listResponse.getChannelHKValues());
                break;
            case PODUCT_FILTER_BRAND_RESULT_CODE://品牌
                listResponse.setBrandValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnBrandValue(listResponse.getBrandValues());
                break;
            case PODUCT_FILTER_GRAPE_RESULT_CODE://葡萄品种
                listResponse.setGrapeValues((List<UDCList>) resultBundle.getSerializable(FILTER_RESULT));
                returnGrapeValue(listResponse.getGrapeValues());
                break;
        }
    }

    //forResult返回数据
    private void returnCountryValue(List<UDCList> countryValues) {
        if (countryValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < countryValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(countryValues.get(i).getNameCn());
                } else {
                    sb.append(countryValues.get(i).getNameEn());
                }
                if (i != countryValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterCountryTv.setText(sb.toString());
        } else {
            mFilterCountryTv.setText(getResources().getString(R.string.select_country));
        }
        mFilterAreaTv.setText(getResources().getString(R.string.select_area));
    }

    private void returnChannelHKValue(List<UDCList> channelHKValues) {
        if (channelHKValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < channelHKValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(channelHKValues.get(i).getNameCn());
                } else {
                    sb.append(channelHKValues.get(i).getNameEn());
                }
                if (i != channelHKValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterHkandmcChannelTv.setText(sb.toString());
        } else {
            mFilterHkandmcChannelTv.setText(getResources().getString(R.string.select_hk));
        }
    }

    private void returnVolumnValue(List<UDCList> volumnValues) {
        if (volumnValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < volumnValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(volumnValues.get(i).getNameCn());
                } else {
                    sb.append(volumnValues.get(i).getNameEn());
                }
                if (i != volumnValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterCapacityTv.setText(sb.toString());
        } else {
            mFilterCapacityTv.setText(getResources().getString(R.string.selectcapacity));
        }
    }

    private void returnBodyValue(List<UDCList> bodyValues) {
        if (bodyValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bodyValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(bodyValues.get(i).getNameCn());
                } else {
                    sb.append(bodyValues.get(i).getNameEn());
                }
                if (i != bodyValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterLiquorbodyTv.setText(sb.toString());
        } else {
            mFilterLiquorbodyTv.setText(getResources().getString(R.string.select_body));
        }
    }

    private void returnVintageValue(List<UDCList> vintageValues) {
        if (vintageValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < vintageValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(vintageValues.get(i).getNameCn());
                } else {
                    sb.append(vintageValues.get(i).getNameEn());
                }
                if (i != vintageValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterYearTv.setText(sb.toString());
        } else {
            mFilterYearTv.setText(getResources().getString(R.string.select_vintage));
        }
    }

    private void returnChannelMLValue(List<UDCList> channelMLValues) {
        if (channelMLValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < channelMLValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(channelMLValues.get(i).getNameCn());
                } else {
                    sb.append(channelMLValues.get(i).getNameEn());
                }
                if (i != channelMLValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterMainlandChannelTv.setText(sb.toString());
        } else {
            mFilterMainlandChannelTv.setText(getResources().getString(R.string.select_mainland));
        }
    }

    private void returnColourValue(List<UDCList> colourValues) {
        if (colourValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < colourValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(colourValues.get(i).getNameCn());
                } else {
                    sb.append(colourValues.get(i).getNameEn());
                }
                if (i != colourValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterAlcoholTv.setText(sb.toString());
        } else {
            mFilterAlcoholTv.setText(getResources().getString(R.string.select_alcoho));
        }
    }

    private void returnSweetValue(List<UDCList> sweetValues) {
        if (sweetValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < sweetValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(sweetValues.get(i).getNameCn());
                } else {
                    sb.append(sweetValues.get(i).getNameEn());
                }
                if (i != sweetValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterSweetnessTv.setText(sb.toString());
        } else {
            mFilterSweetnessTv.setText(getResources().getString(R.string.select_sweet));
        }
    }

    private void returnAreaValue(List<UDCList> areaValues) {
        if (areaValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < areaValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(areaValues.get(i).getNameCn());
                } else {
                    sb.append(areaValues.get(i).getNameEn());
                }
                if (i != areaValues.size() - 1) {
                    //sb.append("-");
                    sb.append("、");
                }
            }
            mFilterAreaTv.setText(sb.toString());
        } else {
            mFilterAreaTv.setText(getResources().getString(R.string.select_area));
        }
    }

    private void returnSubAreaValue(List<UDCList> subAreaValues) {
        if (subAreaValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < subAreaValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(subAreaValues.get(i).getNameCn());
                } else {
                    sb.append(subAreaValues.get(i).getNameEn());
                }
                if (i != subAreaValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterAreaTv.setText(sb.toString());
        } else {
            if (listResponse.getAreaValues().size() > 0) {

            } else {
                mFilterAreaTv.setText(getResources().getString(R.string.select_area));
            }
        }
    }

    private void returnTypeValue(List<UDCList> typeValues) {
        if (typeValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < typeValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(typeValues.get(i).getNameCn());
                } else {
                    sb.append(typeValues.get(i).getNameEn());
                }
                if (i != typeValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterTypeTv.setText(sb.toString());
        } else {
            mFilterTypeTv.setText(getResources().getString(R.string.select_type));
        }
    }

    private void returnBrandValue(List<UDCList> brandValues) {
        if (brandValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < brandValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(brandValues.get(i).getNameCn());
                } else {
                    sb.append(brandValues.get(i).getNameEn());
                }
                if (i != brandValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterBrandTv.setText(sb.toString());
        } else {
            mFilterBrandTv.setText(getResources().getString(R.string.select_brands));
        }
    }

    private void returnGrapeValue(List<UDCList> grapeValues) {
        if (grapeValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < grapeValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(grapeValues.get(i).getNameCn());
                } else {
                    sb.append(grapeValues.get(i).getNameEn());
                }
                if (i != grapeValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterGrapeTv.setText(sb.toString());
        } else {
            mFilterGrapeTv.setText(getResources().getString(R.string.select_grape));
        }
    }

    private void returnColorValue(List<UDCList> colorValues) {
        if (colorValues.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < colorValues.size(); i++) {
                if ("chinese".equals(checkLanguage())) {
                    sb.append(colorValues.get(i).getNameCn());
                } else {
                    sb.append(colorValues.get(i).getNameEn());
                }
                if (i != colorValues.size() - 1) {
                    sb.append("、");
                }
            }
            mFilterColorTv.setText(sb.toString());
        } else {
            mFilterColorTv.setText(getResources().getString(R.string.select_color));
        }
    }

    private void returnPriceValue(String priceValue) {
        if ("".equals(priceValue)) {
            mFilterPriceTv.setText(getResources().getString(R.string.select_price));
        } else {
            mFilterPriceTv.setText(priceValue);
        }
    }


    //设置筛选参数
    public FilterEntity getParmater() {
        FilterEntity entity = new FilterEntity();
        List<UDCList> countryValues = listResponse.getCountryValues();
        if (countryValues.size() > 0) {
            String[] country = new String[countryValues.size()];
            for (int i = 0; i < countryValues.size(); i++) {
                country[i] = countryValues.get(i).getId() + "";
            }
            entity.setCountry(country);
        }
        List<UDCList> brandValues = listResponse.getBrandValues();
        if (brandValues.size() > 0) {
            String[] brand = new String[brandValues.size()];
            for (int i = 0; i < brandValues.size(); i++) {
                brand[i] = brandValues.get(i).getId() + "";
            }
            entity.setBrand(brand);
        }
        List<UDCList> grapeValues = listResponse.getGrapeValues();
        if (grapeValues.size() > 0) {
            String[] grape = new String[grapeValues.size()];
            for (int i = 0; i < grapeValues.size(); i++) {
                grape[i] = grapeValues.get(i).getId() + "";
            }
            entity.setGrape(grape);
        }
        List<UDCList> typeValues = listResponse.getTypeValues();
        if (typeValues.size() > 0) {
            String[] type = new String[typeValues.size()];
            for (int i = 0; i < typeValues.size(); i++) {
                type[i] = typeValues.get(i).getId() + "";
            }
            entity.setType(type);
        }
        List<UDCList> sweetValues = listResponse.getSweetValues();
        if (sweetValues.size() > 0) {
            String[] sweent = new String[sweetValues.size()];
            for (int i = 0; i < sweetValues.size(); i++) {
                sweent[i] = sweetValues.get(i).getId() + "";
            }
            entity.setSweet(sweent);
        }
        List<UDCList> colorValues = listResponse.getColorValues();
        if (colorValues.size() > 0) {
            String[] color = new String[colorValues.size()];
            for (int i = 0; i < colorValues.size(); i++) {
                color[i] = colorValues.get(i).getId() + "";
            }
            entity.setColor(color);
        }
        List<UDCList> bodyValues = listResponse.getBodyValues();
        if (bodyValues.size() > 0) {
            String[] body = new String[bodyValues.size()];
            for (int i = 0; i < bodyValues.size(); i++) {
                body[i] = bodyValues.get(i).getId() + "";
            }
            entity.setBody(body);
        }
        List<UDCList> volumnValues = listResponse.getVolumnValues();
        if (volumnValues.size() > 0) {
            String[] volumn = new String[volumnValues.size()];
            for (int i = 0; i < volumnValues.size(); i++) {
                volumn[i] = volumnValues.get(i).getId() + "";
            }
            entity.setVolumn(volumn);
        }
        List<UDCList> colourValues = listResponse.getColourValues();
        if (colourValues.size() > 0) {
            String[] colour = new String[colourValues.size()];
            for (int i = 0; i < colourValues.size(); i++) {
                colour[i] = colourValues.get(i).getId() + "";
            }
            entity.setColour(colour);
        }
        List<UDCList> vintageValues = listResponse.getVintageValues();
        if (vintageValues.size() > 0) {
            String[] vintage = new String[vintageValues.size()];
            for (int i = 0; i < vintageValues.size(); i++) {
                vintage[i] = vintageValues.get(i).getId() + "";
            }
            entity.setVintage(vintage);
        }
        List<UDCList> channelMLValues = listResponse.getChannelMLValues();
        if (channelMLValues.size() > 0) {
            String[] channelML = new String[channelMLValues.size()];
            for (int i = 0; i < channelMLValues.size(); i++) {
                channelML[i] = channelMLValues.get(i).getId() + "";
            }
            entity.setChannelML(channelML);
        }
        List<UDCList> channelHKValues = listResponse.getChannelHKValues();
        if (channelHKValues.size() > 0) {
            String[] channelHK = new String[channelHKValues.size()];
            for (int i = 0; i < channelHKValues.size(); i++) {
                channelHK[i] = channelHKValues.get(i).getId() + "";
            }
            entity.setChannelHK(channelHK);
        }

        List<UDCList> areaValues = listResponse.getAreaValues();
        if (areaValues.size() > 0) {
            String[] area = new String[areaValues.size()];
            for (int i = 0; i < areaValues.size(); i++) {
                area[i] = areaValues.get(i).getId() + "";
            }
            entity.setArea(area);
        }
        List<UDCList> subAreaValues = listResponse.getSubAreaValues();
        if (subAreaValues.size() > 0) {
            String[] subArea = new String[subAreaValues.size()];
            for (int i = 0; i < subAreaValues.size(); i++) {
                subArea[i] = subAreaValues.get(i).getId() + "";
            }
            entity.setSubArea(subArea);
        }
        /*List<UDCList> areaValues = listResponse.getAreaValues();
        if (areaValues.size() > 0) {
            String[] area = new String[1];
            area[0] = areaValues.get(0).getId() + "";
            entity.setArea(area);
        }
        if (areaValues.size() > 1) {
            String[] subArea = new String[1];
            subArea[0] = areaValues.get(1).getId() + "";
            entity.setSubArea(subArea);
        }*/

        if (!"".equals(priceNumber)) {
            String[] price = new String[1];
            if ("-1".equals(priceNumber)) {//自定义
                //截取字符串
                String str1 = priceValue.substring(0, priceValue.indexOf("-"));
                String str2 = priceValue.substring(str1.length() + 1, priceValue.length());
                if (str2 == null || "".equals(str2)) {
                    price[0] = setNoMaxPrice(str1);
                } else {
                    price[0] = setPrice(str1, str2);
                }
            } else {
                price[0] = priceNumber;
            }
            entity.setPrice(price);
        }
        return entity;
    }

    public String setNoMaxPrice(String min) {
        return "{\"min\":" + min + "}";
    }

    public String setPrice(String min, String max) {
        return "{\"min\":" + min + ",max:" + max + "}";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                Bundle resultBundles = new Bundle();
                resultBundles.putSerializable("data", null);
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(
                        RESULT_OK,
                        resultIntent);
                finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    //判断中英文
    private String checkLanguage() {
        SharedPreferences preferences = getSharedPreferences("language", Activity.MODE_PRIVATE);
        String currentLanguage = preferences.getString("currentLanguage", "");
        if ("".equals(currentLanguage)) {
            Locale curLocal = getResources().getConfiguration().locale;
            if ("zh".equals(curLocal.getLanguage())) {
                return "chinese";
            } else {
                return "english";
            }
        } else {
            if ("english".equals(currentLanguage)) {
                return "english";
            } else {
                return "chinese";
            }
        }
    }
}
