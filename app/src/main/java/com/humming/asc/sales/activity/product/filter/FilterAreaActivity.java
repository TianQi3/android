package com.humming.asc.sales.activity.product.filter;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.product.ProFilterActivity;
import com.humming.asc.sales.adapter.product.TagAdapter;
import com.humming.asc.sales.component.product.FlowLayout;
import com.humming.asc.sales.component.product.TagFlowLayout;
import com.humming.asc.sales.model.product.ProductDCListEntity;
import com.humming.dto.ecatalogResponse.bean.search.UDCList;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FilterAreaActivity extends AbstractActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TagFlowLayout mFilterAreaFlowlayout1;
    private TagFlowLayout mFilterAreaFlowlayout2;
    private TextView mFilterAreaSubmit;
    private TagAdapter tagAdapter1;
    private TagAdapter tagAdapter2;
    private List<UDCList> areaList;//一级产区
    private List<UDCList> areaSubList;//二级产区
    private List<UDCList> selectValues;//本页面选中的一级级产区
    private List<UDCList> selectSubValues;//本页面选中的二级级产区
    private List<UDCList> countryValues;//国家
    private List<UDCList> getIntentSelectValues;//上页面选中的一级级产区
    private List<UDCList> getIntentSelectSubValues;//上页面选中的二级级产区
    private LayoutInflater inflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_filter_area);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        selectValues = new ArrayList<>();
        selectSubValues = new ArrayList<>();
        areaList = (List<UDCList>) getIntent().getExtras().getSerializable("area_value");
        areaSubList = (List<UDCList>) getIntent().getExtras().getSerializable("area_sub_value");
        countryValues = (List<UDCList>) getIntent().getExtras().getSerializable("country_value");
        getIntentSelectValues = (List<UDCList>) getIntent().getExtras().getSerializable("select_area_value");
        getIntentSelectSubValues = (List<UDCList>) getIntent().getExtras().getSerializable("select_sub_area_value");
        inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        setTitle(R.string.select_areas);
        initData();
    }

    private void initData() {
        selectValues.addAll(getIntentSelectValues);
        selectSubValues.addAll(getIntentSelectSubValues);
        final List<ProductDCListEntity> arrayList = new ArrayList<>();
        for (UDCList list : countryValues) {
            for (UDCList udcList : areaList) {
                if (udcList.getParentId() != null && list.getId().longValue() == udcList.getParentId().longValue()) {
                    ProductDCListEntity entity = new ProductDCListEntity();
                    entity.setUdcList(udcList);
                    if (selectValues != null && selectValues.size() > 0) {
                        for (UDCList intentAreaList : selectValues) {
                            if (intentAreaList.getNameCn().equals(entity.getUdcList().getNameCn())) {
                                entity.setCheckSelect(true);
                                setTag2(entity.getUdcList().getId() + "");
                            }
                        }
                    } else
                        entity.setCheckSelect(false);
                    /*if (getIntentSelectValues != null && getIntentSelectValues.size() > 0 && getIntentSelectValues.get(0).getNameCn().equals(entity.getUdcList().getNameCn())) {
                        entity.setCheckSelect(true);
                        setTag2(entity.getUdcList().getId() + "");
                    } else
                        entity.setCheckSelect(false);*/
                    arrayList.add(entity);
                }
            }
        }
        tagAdapter1 = new TagAdapter<ProductDCListEntity>(arrayList) {
            @Override
            public View getView(FlowLayout parent, int position, ProductDCListEntity o) {
                TextView tv = (TextView) inflater.inflate(R.layout.tag_tv,
                        mFilterAreaFlowlayout1, false);
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
        mFilterAreaFlowlayout1.setAdapter(tagAdapter1);
        mFilterAreaFlowlayout1.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view1, int position, FlowLayout parent) {
                TextView view = (TextView) view1;
                if (!arrayList.get(position).isCheckSelect()) {
                    view.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                    view.setTextColor(getResources().getColor(R.color.ffbfa574));
                    /*for (ProductDCListEntity entity : arrayList) {
                        entity.setCheckSelect(false);
                    }*/
                    arrayList.get(position).setCheckSelect(true);
                    tagAdapter1.notifyDataChanged();
                    // selectValues.clear();
                    selectValues.add(arrayList.get(position).getUdcList());
                    setTag2(arrayList.get(position).getUdcList().getId() + "");
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
                    view.setTextColor(getResources().getColor(R.color._f999));
                    arrayList.get(position).setCheckSelect(false);
                    tagAdapter1.notifyDataChanged();

                    //  selectValues.clear();
                    selectValues.remove(arrayList.get(position).getUdcList());
                    //移除第二产区的选中的条目
                    if (selectSubValues != null && selectSubValues.size() > 0) {
                        for (UDCList intentSubList : selectSubValues) {
                            if (intentSubList.getParentId() != null && arrayList.get(position).getUdcList().getId() != null
                                    && intentSubList.getParentId().longValue() == arrayList.get(position).getUdcList().getId().longValue()) {
                                selectSubValues.remove(intentSubList);
                            }
                        }
                    }
                    setTag2("xxx");
                }
                return false;
            }
        });
    }

    private void setTag2(String parentIds) {
        //设置tag2
        String parentId = parentIds;
        final List<ProductDCListEntity> arrayList = new ArrayList<>();
        if (areaSubList != null && areaSubList.size() > 0) {
            for (UDCList udcList : areaSubList) {
                if (selectValues != null && selectValues.size() > 0) {
                    for (UDCList selectAreaList : selectValues) {
                        if (selectAreaList.getId() != null && udcList.getParentId() != null && selectAreaList.getId().toString().equals(udcList.getParentId().toString())) {
                            ProductDCListEntity entity = new ProductDCListEntity();
                            entity.setUdcList(udcList);
                            if (selectSubValues != null && selectSubValues.size() > 0)
                                for (UDCList intentSubList : selectSubValues) {
                                    if (intentSubList.getNameCn().equals(entity.getUdcList().getNameCn())) {
                                        entity.setCheckSelect(true);
                                    }
                                }
                    /*if (getIntentSelectValues.size() > 1 && getIntentSelectValues.get(1).getNameCn().equals(entity.getUdcList().getNameCn())) {
                        entity.setCheckSelect(true);
                    } else {
                        entity.setCheckSelect(false);
                    }*/
                            else {
                                entity.setCheckSelect(false);
                            }
                            arrayList.add(entity);
                        }
                    }
                }
            }
        }
        tagAdapter2 = new TagAdapter<ProductDCListEntity>(arrayList) {
            @Override
            public View getView(FlowLayout parent, int position, ProductDCListEntity o) {
                TextView tv = (TextView) inflater.inflate(R.layout.tag_tv,
                        mFilterAreaFlowlayout1, false);
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

        mFilterAreaFlowlayout2.setAdapter(tagAdapter2);
        mFilterAreaFlowlayout2.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view1, int position, FlowLayout parent) {
                TextView view = (TextView) view1;
                if (!arrayList.get(position).isCheckSelect()) {
                    view.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                    view.setTextColor(getResources().getColor(R.color.ffbfa574));
                    //   for (ProductDCListEntity entity : arrayList) {
                    //       entity.setCheckSelect(false);
                    //   }
                    arrayList.get(position).setCheckSelect(true);
                    tagAdapter2.notifyDataChanged();
                    //   if (selectValues.size() > 1) {
                    //      selectValues.remove(1);
                    //  }
                    selectSubValues.add(arrayList.get(position).getUdcList());
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
                    view.setTextColor(getResources().getColor(R.color._f999));
                    arrayList.get(position).setCheckSelect(false);
                    tagAdapter2.notifyDataChanged();
                    //  if (selectValues.size() > 1) {
                    //     selectValues.remove(1);
                    //  }
                    selectSubValues.remove(arrayList.get(position).getUdcList());
                }
                return false;
            }
        });
    }

    private void initView() {
        mFilterAreaFlowlayout1 = (TagFlowLayout) findViewById(R.id.content_product_filter_area_flowlayout1);
        mFilterAreaFlowlayout2 = (TagFlowLayout) findViewById(R.id.content_product_filter_area_flowlayout2);
        mFilterAreaSubmit = (TextView) findViewById(R.id.content_product_filter__area_submit);
        mFilterAreaSubmit.setOnClickListener(this);
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
            case R.id.content_product_filter__area_submit:
                Bundle resultBundles = new Bundle();
                resultBundles.putSerializable(ProFilterActivity.FILTER_AREA_RESULT, (Serializable) selectValues);
                resultBundles.putSerializable(ProFilterActivity.FILTER_SUB_AREA_RESULT, (Serializable) selectSubValues);
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
