package com.humming.asc.sales.activity.ecatalog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.AddItemToEcatalogRequest;
import com.humming.asc.sales.RequestData.FilterEntity;
import com.humming.asc.sales.RequestData.ProductListRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.Stocks.SimpleSelectorActivity;
import com.humming.asc.sales.activity.product.ProFilterActivity;
import com.humming.asc.sales.activity.product.SearchProductActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.ecatalog.EcatalogAddProAdapter;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.component.Loading;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.model.product.ProductListEntity;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.bean.product.ProductList;
import com.humming.dto.ecatalogResponse.product.ProductListResponse;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class EcatalogAddProActivity extends AbstractActivity implements View.OnClickListener, BaseAdapter.RequestLoadMoreListener, BaseAdapter.OnRecyclerViewItemChildClickListener {
    private EcatalogAddProAdapter adapter;
    private RecyclerView productListView;
    private TextView mBrandText;
    private ImageView mBrandImg;
    private LinearLayout mBrandView;
    private TextView mTypeText;
    private ImageView mTypeImg;
    private LinearLayout mTypeView;
    private TextView mCountryText;
    private ImageView mCountryImg;
    private LinearLayout mCountryView;
    private TextView mPriceText;
    private ImageView mPriceImg;
    private LinearLayout mPriceView;
    private Loading mLoading;
    private LinearLayout nodataLayout;
    private List<ProductListEntity> rows;
    private List<ProductListEntity> productLists;
    private FilterEntity data;
    private View notLoadingView;
    private boolean hasMore = true;
    private String pageNo = "0";
    private String sortType = "";// 1为品牌, 2为类型, 3为国家, 4为价格
    private String search = "";// 关键字搜索
    public final static int SEARCH_RESULT_CODE = 19111;
    public final static int SEARCH_RESULT_CODE2 = 19112;
    private TextView noDataTv;
    private ImageView backView;
    private TextView mSearchEdToolbar;
    // private LinearLayout mSearchToolbar;
    private ImageView mFilterToolbar;
    // private TextView mDotFilter;
    private CheckBox mSelectCheckboxPro;
    private LinearLayout mSelectAllPro;
    private TextView mEcatalogAddProSize;
    private TextView mEcatalogAddPro;
    public boolean allSelect = false;
    private String ecatalogId = "";
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecatalog_add_pro);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.colorPrimary), true);
        ecatalogId = getIntent().getStringExtra("ecatalog_id");
        customDialog = new CustomDialog(this, "Loading...");
        initView();
        // mLoading.show();
        initData();
    }

    private void initData() {
        customDialog.show();
        final ProductListRequest request = new ProductListRequest();
        request.setPageNo(pageNo);
        request.setSearch(search);
        request.setSortType(sortType);
        request.setData(data);
        if (Application.getInstance().getMlAndHKChannelEntity() != null) {
            request.setExclusiveChannelHK(Application.getInstance().getMlAndHKChannelEntity().getExclusiveChannelHK());
            request.setExclusiveChannelML(Application.getInstance().getMlAndHKChannelEntity().getExclusiveChannelML());
        }
        OkHttpClientManager.postAsyn(Config.ECATALLOG_PRODUCT_LIST, new OkHttpClientManager.ResultCallback<ProductListResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                //mLoading.hide();
                customDialog.dismiss();
            }

            @Override
            public void onResponse(ProductListResponse response) {
                customDialog.dismiss();
                rows = new ArrayList<>();
                for (ProductList p : response.getRows()) {
                    ProductListEntity entity = new ProductListEntity();
                    entity.setProductList(p);
                    entity.setCheckSelect(2);
                    entity.setCollection(p.getCollection());
                    entity.setStockLists(null);
                    rows.add(entity);
                }
                if ("0".equals(pageNo)) {
                    mSelectCheckboxPro.setChecked(false);
                    if (rows.size() <= 0) {
                        productListView.setVisibility(View.GONE);
                        nodataLayout.setVisibility(View.VISIBLE);
                        if (!"".equals(search)) {
                            noDataTv.setText(getResources().getString(R.string.search_toast));
                        }
                    } else {
                        productListView.setVisibility(View.VISIBLE);
                        nodataLayout.setVisibility(View.GONE);
                        productLists.clear();
                        productLists.addAll(rows);
                        adapter = new EcatalogAddProAdapter(rows);
                        adapter.openLoadAnimation();
                        productListView.setAdapter(adapter);
                        adapter.openLoadMore(rows.size(), true);
                        hasMore = response.getHasMore();
                        pageNo = response.getPageNo() + "";
                    }

                } else {
                    productListView.setVisibility(View.VISIBLE);
                    nodataLayout.setVisibility(View.GONE);
                    productLists.addAll(rows);
                    if (response.getHasMore()) {
                        hasMore = true;
                        pageNo = response.getPageNo() + "";
                    } else {
                        hasMore = false;
                        pageNo = "0";
                    }
                    adapter.notifyDataChangedAfterLoadMore(rows, true);
                }
                adapter.setOnLoadMoreListener(EcatalogAddProActivity.this);
                adapter.setOnRecyclerViewItemChildClickListener(EcatalogAddProActivity.this);
                String str3 = getResources().getString(R.string.search_result) + "<font color='#BFA574'><big>" + productLists.size() + "</big></font>" + getResources().getString(R.string.kuan);
                mEcatalogAddProSize.setText(Html.fromHtml(str3));
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                // mLoading.hide();
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ProductListResponse.class);
    }

    private void initView() {
        data = new FilterEntity();
        productLists = new ArrayList<>();
        rows = new ArrayList<>();
        backView = findViewById(R.id.ecatalog_add_pro__back);
        backView.setOnClickListener(this);
        productListView = (RecyclerView) findViewById(R.id.product_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        productListView.setLayoutManager(linearLayoutManager);
        mBrandText = (TextView) findViewById(R.id.fragment_prodect__filter_brand_text);
        mBrandImg = (ImageView) findViewById(R.id.fragment_prodect__filter_brand_img);
        mBrandView = (LinearLayout) findViewById(R.id.fragment_prodect__filter_brand_view);
        mTypeText = (TextView) findViewById(R.id.fragment_prodect__filter_type_text);
        mTypeImg = (ImageView) findViewById(R.id.fragment_prodect__filter_type_img);
        mTypeView = (LinearLayout) findViewById(R.id.fragment_prodect__filter_type_view);
        mCountryText = (TextView) findViewById(R.id.fragment_prodect__filter_country_text);
        mCountryImg = (ImageView) findViewById(R.id.fragment_prodect__filter_country_img);
        mCountryView = (LinearLayout) findViewById(R.id.fragment_prodect__filter_country_view);
        mPriceText = (TextView) findViewById(R.id.fragment_prodect__filter_price_text);
        mPriceImg = (ImageView) findViewById(R.id.fragment_prodect__filter_price_img);
        mPriceView = (LinearLayout) findViewById(R.id.fragment_prodect__filter_price_view);
        mLoading = (Loading) findViewById(R.id.activity_loading);
        noDataTv = findViewById(R.id.frament_product_nodata_text);
        nodataLayout = (LinearLayout) findViewById(R.id.frament_product_nodata);
        notLoadingView = LayoutInflater.from(this).inflate(R.layout.not_loading, (ViewGroup) productListView.getParent(), false);

        mBrandView.setOnClickListener(this);
        mCountryView.setOnClickListener(this);
        mPriceView.setOnClickListener(this);
        mTypeView.setOnClickListener(this);
        mSearchEdToolbar = (TextView) findViewById(R.id.toolbar_search_ed);
        // mSearchToolbar = (LinearLayout) findViewById(R.id.toolbar_search);
        mFilterToolbar = (ImageView) findViewById(R.id.toolbar_filter);
        // mDotFilter = (TextView) findViewById(R.id.filter_dot);
        mFilterToolbar.setOnClickListener(this);
        mSearchEdToolbar.setOnClickListener(this);
        mSelectCheckboxPro = (CheckBox) findViewById(R.id.pro_select_checkbox);
        mSelectAllPro = (LinearLayout) findViewById(R.id.pro_select__all);
        mEcatalogAddProSize = (TextView) findViewById(R.id.content_ecatalog_add_pro_size);
        mEcatalogAddPro = (TextView) findViewById(R.id.content_ecatalog_add_pro);
        mEcatalogAddPro.setOnClickListener(this);

        mSelectAllPro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectCheckboxPro.isChecked()) {
                    mSelectCheckboxPro.setChecked(false);
                } else {
                    mSelectCheckboxPro.setChecked(true);
                }
            }
        });
        mSelectCheckboxPro.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (allSelect) {
                    allSelect = false;
                } else {
                    if (isChecked) {
                        for (ProductListEntity productListEntity : productLists) {
                            productListEntity.setCheckSelect(1);
                        }
                        adapter.notifyItemRangeChanged(0, productLists.size());
                    } else {
                        for (ProductListEntity productListEntity : productLists) {
                            productListEntity.setCheckSelect(2);
                        }
                        adapter.notifyItemRangeChanged(0, productLists.size());
                    }
                }
            }
        });
    }

    @Override
    public void onItemChildClick(final BaseAdapter adapter, final View view, final int position) {
        switch (view.getId()) {
            case R.id.item_product__radio:
                if (productLists.get(position).getCheckSelect() == 1) {
                    //当前选中。变成不选中
                    allSelect = true;
                    productLists.get(position).setCheckSelect(2);
                    mSelectCheckboxPro.setChecked(false);
                } else {
                    productLists.get(position).setCheckSelect(1);
                }
                adapter.notifyItemChanged(position);
                break;
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ecatalog_add_pro__back:
                finish();
                break;
            case R.id.fragment_prodect__filter_brand_view:
                refreshView();
                pageNo = "0";
                sortType = "1";
                mLoading.show();
                initData();
                mBrandText.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                mBrandImg.setImageResource(R.mipmap.product_nav_sort_s);
                break;
            case R.id.fragment_prodect__filter_country_view:
                refreshView();
                pageNo = "0";
                sortType = "3";
                mLoading.show();
                initData();
                mCountryText.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                mCountryImg.setImageResource(R.mipmap.product_nav_sort_s);
                break;
            case R.id.fragment_prodect__filter_price_view:
                refreshView();
                pageNo = "0";
                sortType = "4";
                mLoading.show();
                initData();
                mPriceText.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                mPriceImg.setImageResource(R.mipmap.product_nav_sort_s);
                break;
            case R.id.fragment_prodect__filter_type_view:
                refreshView();
                pageNo = "0";
                sortType = "2";
                mLoading.show();
                initData();
                mTypeText.setTextColor(this.getResources().getColor(R.color.colorPrimary));
                mTypeImg.setImageResource(R.mipmap.product_nav_sort_s);
                break;
            case R.id.toolbar_search_ed://搜索
                //去产品搜索页面
                Intent intent2 = new Intent(this, SearchProductActivity.class);
                startActivityForResult(intent2, SEARCH_RESULT_CODE);
                break;
            case R.id.toolbar_filter:
                Intent intent1 = new Intent(this, ProFilterActivity.class);
                startActivityForResult(intent1, SEARCH_RESULT_CODE2);
                break;
            case R.id.content_ecatalog_add_pro://加入报价单
                List<String> strings = new ArrayList<>();
                for (ProductListEntity list : productLists) {
                    if (list.getCheckSelect() == 1) {//选中
                        strings.add(list.getProductList().getItemCode());
                    }
                }
                if (strings.size() > 0) {
                    AddProToEcatalog(strings);
                } else {
                    Toast.makeText(EcatalogAddProActivity.this, getResources().getString(R.string.no_select_product), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
    }

    //添加产品到我的报价单
    private void AddProToEcatalog(List<String> strings) {
        customDialog.show();
        final AddItemToEcatalogRequest request = new AddItemToEcatalogRequest();
        request.setId(ecatalogId);
        request.setItemCode(strings);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ADD_ITEM, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                customDialog.dismiss();
                // if ("success".equals(response.getStatus())) {
                Toast.makeText(Application.getInstance().getCurrentActivity(), response, Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new BackRefreshOneEvent("2"));
                finish();
                // }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, String.class);
    }

    //刷新布局
    private void refreshView() {
        mPriceText.setTextColor(this.getResources().getColor(R.color._666));
        mCountryText.setTextColor(this.getResources().getColor(R.color._666));
        mTypeText.setTextColor(this.getResources().getColor(R.color._666));
        mBrandText.setTextColor(this.getResources().getColor(R.color._666));
        mPriceImg.setImageResource(R.mipmap.product_nav_sort_n);
        mBrandImg.setImageResource(R.mipmap.product_nav_sort_n);
        mTypeImg.setImageResource(R.mipmap.product_nav_sort_n);
        mCountryImg.setImageResource(R.mipmap.product_nav_sort_n);
    }

    @Override
    public void onLoadMoreRequested() {
        productListView.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    adapter.notifyDataChangedAfterLoadMore(false);
                    adapter.removeAllFooterView();
                    notLoadingView = LayoutInflater.from(Application.getInstance().getCurrentActivity()).inflate(R.layout.not_loading, (ViewGroup) productListView.getParent(), false);
                    adapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData();
                        }
                    }, 1500);
                }
            }

        });
    }

    //关键字返回请求
    public void serach(String keywords) {
        refreshView();
        data = new FilterEntity();
        search = keywords;
        sortType = "";
        pageNo = "0";
        initData();
    }

    //筛选返回请求
    public void filter(FilterEntity entity) {
        data = entity;
        refreshView();
        search = "";
        sortType = "";
        pageNo = "0";
        initData();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String text = "";
        if (data != null) {
            text = data.getStringExtra(SimpleSelectorActivity.SELECTED_STRING);
        } else {
        }
        switch (requestCode) {
            case SEARCH_RESULT_CODE://关键字搜索
                // 搜索返回关键字
                //filterDot.setVisibility(View.GONE);
                Application.getInstance().setUdcListResponse(null);
                mSearchEdToolbar.setText(text);
                serach(text);
                break;
            case SEARCH_RESULT_CODE2://筛选
                FilterEntity datas = (FilterEntity) data.getExtras().getSerializable("data");
                if (Application.getInstance().getUdcListResponse() != null && Application.getInstance().getUdcListResponse().isUpdate()) {
                    //  filterDot.setVisibility(View.VISIBLE);
                    if (datas != null) {
                        filter(datas);
                    }
                } else {
                    //  filterDot.setVisibility(View.GONE);
                }
                break;
        }
    }

}
