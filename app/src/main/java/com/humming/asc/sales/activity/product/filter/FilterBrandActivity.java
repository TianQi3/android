package com.humming.asc.sales.activity.product.filter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.product.ProFilterActivity;
import com.humming.asc.sales.adapter.product.BrandAdapter;
import com.humming.asc.sales.adapter.product.ItemAdapter;
import com.humming.asc.sales.component.product.brand.AZWaveSideBarView;
import com.humming.asc.sales.component.product.brand.LettersComparator;
import com.humming.asc.sales.model.product.ProductDCListEntity;
import com.humming.dto.ecatalogResponse.bean.search.UDCList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FilterBrandActivity extends AbstractActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView submitTv, resetTv;
    private RecyclerView recyclerView;
    private List<ProductDCListEntity> dcListEntityList;
    private List<UDCList> getIntentSelectValues;
    public static List<UDCList> selectValues;
    private List<UDCList> udcLists;
    private BrandAdapter adapter;
    private AZWaveSideBarView mBarList;
    private ItemAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_filter_brand);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        selectValues = new ArrayList<>();
        udcLists = (List<UDCList>) getIntent().getExtras().getSerializable("response");
        getIntentSelectValues = (List<UDCList>) getIntent().getExtras().getSerializable("select_response");
        setTitle(getResources().getString(R.string.select) + " " + getIntent().getStringExtra("title"));
        initData();
    }

    private void initData() {
        selectValues.addAll(getIntentSelectValues);
        dcListEntityList = new ArrayList<>();
        if (udcLists != null && udcLists.size() > 0) {
            for (UDCList udcList : udcLists) {
                ProductDCListEntity entity = new ProductDCListEntity();
                entity.setUdcList(udcList);
                if (getIntentSelectValues.contains(udcList))
                    entity.setCheckSelect(true);
                else
                    entity.setCheckSelect(false);
                dcListEntityList.add(entity);
            }
        }
        Collections.sort(dcListEntityList, new LettersComparator());
        recyclerView.setAdapter(mAdapter = new ItemAdapter(dcListEntityList));
        /*adapter = new BrandAdapter(dcListEntityList);
        adapter.openLoadAnimation();
        recyclerView.setAdapter(adapter);
        adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (dcListEntityList.get(position).isCheckSelect()) {
                    dcListEntityList.get(position).setCheckSelect(false);
                    selectValues.remove(dcListEntityList.get(position).getUdcList());
                } else {
                    dcListEntityList.get(position).setCheckSelect(true);
                    selectValues.add(dcListEntityList.get(position).getUdcList());
                }
                adapter.notifyItemChanged(position);
            }
        });*/
    }

    private void initView() {
        mBarList = findViewById(R.id.bar_list);
        submitTv = findViewById(R.id.content_product_filter__brand_submit);
        submitTv.setOnClickListener(this);
        resetTv = findViewById(R.id.content_product_filter__brand_reset);
        resetTv.setOnClickListener(this);
        recyclerView = (RecyclerView) findViewById(R.id.brand_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        mBarList.setOnLetterChangeListener(new AZWaveSideBarView.OnLetterChangeListener() {
            @Override
            public void onLetterChange(String letter) {
                int position = mAdapter.getSortLettersFirstPosition(letter);
                if (position != -1) {
                    if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                        LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        manager.scrollToPositionWithOffset(position, 0);
                    } else {
                        recyclerView.getLayoutManager().scrollToPosition(position);
                    }
                }
            }
        });
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
            case R.id.content_product_filter__brand_submit:
                Bundle resultBundles = new Bundle();
                resultBundles.putSerializable(ProFilterActivity.FILTER_RESULT, (ArrayList<UDCList>) selectValues);
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(
                        RESULT_OK,
                        resultIntent);
                finish();
                break;
            case R.id.content_product_filter__brand_reset:
                selectValues.clear();
                initData();
                break;
        }
    }

}
