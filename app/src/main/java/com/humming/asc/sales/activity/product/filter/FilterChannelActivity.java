package com.humming.asc.sales.activity.product.filter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.product.ProFilterActivity;
import com.humming.asc.sales.adapter.product.TagAdapter;
import com.humming.asc.sales.component.product.FlowLayout;
import com.humming.asc.sales.component.product.TagFlowLayout;
import com.humming.asc.sales.model.product.MLAndHKChannelEntity;
import com.humming.asc.sales.model.product.ProductDCListEntity;
import com.humming.dto.ecatalogResponse.bean.search.UDCList;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterChannelActivity extends AbstractActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TagFlowLayout flowLayout;
    private TextView submitTv;
    private List<ProductDCListEntity> dcListEntityList;
    private TagAdapter tagAdapter;
    private List<UDCList> getIntentSelectValues;
    private List<UDCList> selectValues;
    private List<UDCList> udcLists;
    private RadioButton radioButton;
    private boolean isCheck = false;
    private String label = "ml";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_filter_country);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        selectValues = new ArrayList<>();
        udcLists = (List<UDCList>) getIntent().getExtras().getSerializable("response");
        getIntentSelectValues = (List<UDCList>) getIntent().getExtras().getSerializable("select_response");
        setTitle(getResources().getString(R.string.select) + "  " + getIntent().getStringExtra("title"));
        label = getIntent().getStringExtra("label");
        initData();
    }

    private void initData() {
        selectValues.addAll(getIntentSelectValues);
        final LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        dcListEntityList = new ArrayList<>();
        if (udcLists != null && udcLists.size() > 0) {
            for (UDCList udcList : udcLists) {
                ProductDCListEntity entity = new ProductDCListEntity();
                entity.setUdcList(udcList);
                if (getIntentSelectValues.contains(entity.getUdcList()))
                    entity.setCheckSelect(true);
                else
                    entity.setCheckSelect(false);
                dcListEntityList.add(entity);
            }
        }
        tagAdapter = new TagAdapter<ProductDCListEntity>(dcListEntityList) {
            @Override
            public View getView(FlowLayout parent, int position, ProductDCListEntity o) {
                TextView tv = (TextView) inflater.inflate(R.layout.tag_tv,
                        flowLayout, false);
                if ("chinese".equals(checkLanguage())) {
                    tv.setText(o.getUdcList().getNameCn());
                } else {
                    tv.setText(o.getUdcList().getNameEn());
                }
                if (o.isCheckSelect()) {
                    tv.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                    tv.setTextColor(getResources().getColor(R.color.ffbfa574));

                } else {
                    tv.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
                    tv.setTextColor(getResources().getColor(R.color._f999));
                }
                return tv;
            }
        };
        flowLayout.setAdapter(tagAdapter);
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view1, int position, FlowLayout parent) {
                TextView view = (TextView) view1;
                if (!dcListEntityList.get(position).isCheckSelect()) {
                    view.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                    view.setTextColor(getResources().getColor(R.color.ffbfa574));
                    if (isCheck) {
                        for (ProductDCListEntity entity : dcListEntityList) {
                            entity.setCheckSelect(false);
                        }
                        dcListEntityList.get(position).setCheckSelect(true);
                        tagAdapter.notifyDataChanged();
                        selectValues.clear();
                        selectValues.add(dcListEntityList.get(position).getUdcList());
                    } else {
                        dcListEntityList.get(position).setCheckSelect(true);
                        tagAdapter.notifyDataChanged();
                        selectValues.add(dcListEntityList.get(position).getUdcList());
                    }
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
                    view.setTextColor(getResources().getColor(R.color._f999));
                    dcListEntityList.get(position).setCheckSelect(false);
                    tagAdapter.notifyDataChanged();
                    if (isCheck) {
                        selectValues.clear();
                    } else {
                        selectValues.remove(dcListEntityList.get(position).getUdcList());
                    }
                }
                return false;
            }
        });
    }

    private void initView() {
        flowLayout = (TagFlowLayout) findViewById(R.id.content_product_filter__flowlayout);
        submitTv = (TextView) findViewById(R.id.content_product_filter__country_submit);
        submitTv.setOnClickListener(this);
        radioButton = findViewById(R.id.content_product_filter__radio);
        radioButton.setVisibility(View.VISIBLE);
        if ("hk".equals(label)) {//港澳渠道
            if (Application.getInstance().getMlAndHKChannelEntity() != null && "true".equals(Application.getInstance().getMlAndHKChannelEntity().getExclusiveChannelHK())) {
                isCheck = true;
                radioButton.setChecked(true);
            } else {
                isCheck = false;
                radioButton.setChecked(false);
            }
        } else {
            if (Application.getInstance().getMlAndHKChannelEntity() != null && "true".equals(Application.getInstance().getMlAndHKChannelEntity().getExclusiveChannelML())) {
                radioButton.setChecked(true);
                isCheck = true;
            } else {
                isCheck = false;
                radioButton.setChecked(false);
            }
        }
        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isCheck) {
                    radioButton.setChecked(false);
                    // setChannel(true);
                    isCheck = false;
                } else {
                    selectValues.clear();
                    getIntentSelectValues.clear();
                    initData();
                    radioButton.setChecked(true);
                    isCheck = true;
                    //setChannel(true);
                }
            }
        });
    }

    private void setChannel(boolean b) {
        if ("hk".equals(label)) {//港澳渠道
            if (Application.getInstance().getMlAndHKChannelEntity() != null) {
                if (b) {
                    Application.getInstance().getMlAndHKChannelEntity().setExclusiveChannelHK("true");
                } else {
                    Application.getInstance().getMlAndHKChannelEntity().setExclusiveChannelHK("false");
                }
            } else {
                MLAndHKChannelEntity entity = new MLAndHKChannelEntity();
                if (b) {
                    entity.setExclusiveChannelHK("true");
                } else {
                    entity.setExclusiveChannelHK("false");
                }
                Application.getInstance().setMlAndHKChannelEntity(entity);
            }
        } else {
            if (Application.getInstance().getMlAndHKChannelEntity() != null) {
                if (b) {
                    Application.getInstance().getMlAndHKChannelEntity().setExclusiveChannelML("true");
                } else {
                    Application.getInstance().getMlAndHKChannelEntity().setExclusiveChannelML("false");
                }
            } else {
                MLAndHKChannelEntity entity = new MLAndHKChannelEntity();
                if (b) {
                    entity.setExclusiveChannelML("true");
                } else {
                    entity.setExclusiveChannelML("false");
                }
                Application.getInstance().setMlAndHKChannelEntity(entity);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_product_filter__country_submit:
                Bundle resultBundles = new Bundle();
                resultBundles.putSerializable(ProFilterActivity.FILTER_RESULT, (ArrayList<UDCList>) selectValues);
                setChannel(isCheck);
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(
                        RESULT_OK,
                        resultIntent);
                finish();
                break;
        }
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
