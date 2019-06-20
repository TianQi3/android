package com.humming.asc.sales.content;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.AddItemToEcatalogRequest;
import com.humming.asc.sales.RequestData.FilterEntity;
import com.humming.asc.sales.RequestData.ProductListRequest;
import com.humming.asc.sales.RequestData.RequestNull;
import com.humming.asc.sales.RequestData.StockByItemRequest;
import com.humming.asc.sales.activity.ecatalog.CreateEcatalogActivity;
import com.humming.asc.sales.activity.product.ProductInfoAndStockActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.ecatalog.ProAddEcatalogAdapter;
import com.humming.asc.sales.adapter.product.ProductAdapter;
import com.humming.asc.sales.component.FastClickUtil;
import com.humming.asc.sales.component.Loading;
import com.humming.asc.sales.model.product.ProductListEntity;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.bean.ecatalog.EcatalogListNoPageableBean;
import com.humming.dto.ecatalogResponse.bean.product.ProductList;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogListNoPageableResponse;
import com.humming.dto.ecatalogResponse.product.ProductListResponse;
import com.humming.dto.ecatalogResponse.product.StockResponse;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ztq on 3/27/19.
 */
public class ProductContent extends LinearLayout implements BaseAdapter.RequestLoadMoreListener, View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener, SwipeRefreshLayout.OnRefreshListener {

    private final View view;
    private Context context;
    private ProductAdapter adapter;
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
    private SwipeRefreshLayout mRefresh;
    private LinearLayout nodataLayout;
    private List<ProductListEntity> rows;
    private List<ProductListEntity> productLists;
    private FilterEntity data;
    private LinearLayout parent;
    private View notLoadingView;
    private boolean hasMore = true;
    private String pageNo = "0";
    private String sortType = "";// 1为品牌, 2为类型, 3为国家, 4为价格
    private String search = "";// 关键字搜索
    public final static int SEARCH_RESULT_CODE = 19101;
    public final static int SEARCH_RESULT_CODE2 = 19102;
    private TextView noDataTv;

    public ProductContent(Context context) {
        this(context, null);
    }

    public ProductContent(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = inflate(context, R.layout.fragment_product, this);
        initView();
        mLoading.show();
        initData();
    }

    public void initData() {
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
                mLoading.hide();
            }

            @Override
            public void onResponse(ProductListResponse response) {
                mLoading.hide();
                // rows.clear();
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
                    if (rows.size() <= 0) {
                        mRefresh.setVisibility(View.GONE);
                        nodataLayout.setVisibility(View.VISIBLE);
                        if (!"".equals(search)) {
                            noDataTv.setText(getResources().getString(R.string.search_toast));
                        }
                    } else {
                        mRefresh.setVisibility(View.VISIBLE);
                        nodataLayout.setVisibility(View.GONE);
                        productLists.clear();
                        productLists.addAll(rows);
                        adapter = new ProductAdapter(rows);
                        adapter.openLoadAnimation();
                        productListView.setAdapter(adapter);
                        adapter.openLoadMore(rows.size(), true);
                        hasMore = response.getHasMore();
                        pageNo = response.getPageNo() + "";
                    }

                } else {
                    mRefresh.setVisibility(View.VISIBLE);
                    nodataLayout.setVisibility(View.GONE);
                    productLists.addAll(rows);
                    if (response.getHasMore()) {
                        hasMore = true;
                        pageNo = response.getPageNo() + "";
                    } else {
                        adapter.removeAllFooterView();
                        hasMore = false;
                        pageNo = "0";
                    }
                    adapter.notifyDataChangedAfterLoadMore(rows, true);
                }
                adapter.setOnLoadMoreListener(ProductContent.this);
                adapter.setOnRecyclerViewItemChildClickListener(ProductContent.this);
                adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), ProductInfoAndStockActivity.class);
                        intent.putExtra("itemCode", productLists.get(position).getProductList().getItemCode());
                        Application.getInstance().getCurrentActivity().startActivity(intent);
                    }
                });

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                mLoading.hide();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ProductListResponse.class);
    }

    @Override
    public void onItemChildClick(final BaseAdapter adapter, final View view, final int position) {
        switch (view.getId()) {
            case R.id.item_product_stock_down:
                //  LinearLayout linearLayout = (LinearLayout) view.getParent().getParent();
                // final LinearLayout stockLayout = linearLayout.findViewById(R.id.item_product_stock_contain);
                if (productLists.get(position).getCheckSelect() == 1) {
                    productLists.get(position).setCheckSelect(2);
                    productLists.get(position).setStockLists(null);
                    adapter.notifyItemChanged(position, productLists.size());
                } else {
                    StockByItemRequest stockByItemReques = new StockByItemRequest();
                    stockByItemReques.setItemCode(productLists.get(position).getProductList().getItemCode());
                    OkHttpClientManager.postAsyn(Config.ECATALLOG_STOCK_BY_ITEMCODE, new OkHttpClientManager.ResultCallback<StockResponse>() {
                        @Override
                        public void onError(Request request, Error info) {
                            Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                        }

                        @Override
                        public void onResponse(StockResponse response) {
                            productLists.get(position).setStockLists(response.getData());
                            productLists.get(position).setCheckSelect(1);
                            adapter.notifyItemChanged(position, productLists.size());
                        }

                        @Override
                        public void onOtherError(Request request, Exception exception) {
                            Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                        }
                    }, stockByItemReques, StockResponse.class);

                }
                break;
            case R.id.item_product_add:
                //获取数据
                if (!FastClickUtil.isFastClick()) {
                    getEcatalog(productLists.get(position).getProductList().getItemCode());
                }
                break;
        }

    }

    private void initView() {
        data = new FilterEntity();
        productLists = new ArrayList<>();
        rows = new ArrayList<>();
        productListView = (RecyclerView) findViewById(R.id.product_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
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
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.find__refresh);
        parent = (LinearLayout) findViewById(R.id.content_product__root);
        noDataTv = findViewById(R.id.frament_product_nodata_text);
        mRefresh.setOnRefreshListener(this);
        nodataLayout = (LinearLayout) findViewById(R.id.frament_product_nodata);
        notLoadingView = LayoutInflater.from(getContext()).inflate(R.layout.not_loading, (ViewGroup) productListView.getParent(), false);

        mBrandView.setOnClickListener(this);
        mCountryView.setOnClickListener(this);
        mPriceView.setOnClickListener(this);
        mTypeView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_prodect__filter_brand_view:
                refreshView();
                pageNo = "0";
                sortType = "1";
                mLoading.show();
                initData();
                mBrandText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mBrandImg.setImageResource(R.mipmap.product_nav_sort_s);
                break;
            case R.id.fragment_prodect__filter_country_view:
                refreshView();
                pageNo = "0";
                sortType = "3";
                mLoading.show();
                initData();
                mCountryText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mCountryImg.setImageResource(R.mipmap.product_nav_sort_s);
                break;
            case R.id.fragment_prodect__filter_price_view:
                refreshView();
                pageNo = "0";
                sortType = "4";
                mLoading.show();
                initData();
                mPriceText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mPriceImg.setImageResource(R.mipmap.product_nav_sort_s);
                break;
            case R.id.fragment_prodect__filter_type_view:
                refreshView();
                pageNo = "0";
                sortType = "2";
                mLoading.show();
                initData();
                mTypeText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                mTypeImg.setImageResource(R.mipmap.product_nav_sort_s);
                break;
            default:
                break;
        }
    }

    //刷新布局
    private void refreshView() {
        mPriceText.setTextColor(context.getResources().getColor(R.color._666));
        mCountryText.setTextColor(context.getResources().getColor(R.color._666));
        mTypeText.setTextColor(context.getResources().getColor(R.color._666));
        mBrandText.setTextColor(context.getResources().getColor(R.color._666));
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
                    //if (notLoadingView == null) {
                    notLoadingView = LayoutInflater.from(Application.getInstance().getCurrentActivity()).inflate(R.layout.not_loading, (ViewGroup) productListView.getParent(), false);
                    // }
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

    @Override
    public void onRefresh() {
        productListView.post(new Runnable() {
            @Override
            public void run() {
                pageNo = "0";
                initData();
                mRefresh.setRefreshing(false);
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


    class SpecPopupWindows extends PopupWindow {

        public SpecPopupWindows(Context mContext, final List<EcatalogListNoPageableBean> ecatalogListNoPageable, final String itemCode) {
            View view = View
                    .inflate(mContext, R.layout.popupwindows_product_select_ecatalog, null);
            LinearLayout addEcatalogLayout = (LinearLayout) view.findViewById(R.id.ecatalog_create_ecatalog);
            LinearLayout dissmissLayout = (LinearLayout) view.findViewById(R.id.ecatalog_pop_dismiss);
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.ecatalog_listview);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Application.getInstance().getCurrentActivity());
            recyclerView.setLayoutManager(linearLayoutManager);
            view.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_in));
            LinearLayout ll_popup = (LinearLayout) view
                    .findViewById(R.id.ll_popup);
            ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
                    R.anim.fade_out));

            setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
            setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);//5000
            setBackgroundDrawable(new BitmapDrawable());
            setFocusable(true);
            setOutsideTouchable(true);
            setContentView(view);
            setAnimationStyle(R.style.mypopwindow_anim_style);
            // 设置背景颜色变暗
            final WindowManager.LayoutParams lp = Application.getInstance().getCurrentActivity().getWindow().getAttributes();
            lp.alpha = 0.5f;
            Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);

            //设置数据
            if (ecatalogListNoPageable.size() > 0) {
                ProAddEcatalogAdapter adapter = new ProAddEcatalogAdapter(ecatalogListNoPageable);
                adapter.openLoadAnimation();
                recyclerView.setAdapter(adapter);
                adapter.setOnRecyclerViewItemChildClickListener(new BaseAdapter.OnRecyclerViewItemChildClickListener() {
                    @Override
                    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
                        switch (view.getId()) {
                            case R.id.item_pro_ecatalog_add:
                                EcatalogAddItem(ecatalogListNoPageable.get(position).getEcatalogId() + "", itemCode);
                                break;
                        }
                    }
                });

            }
            dissmissLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    lp.alpha = 1.0f;
                    dismiss();
                    Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
                }
            });
            setOnDismissListener(new OnDismissListener() {
                @Override
                public void onDismiss() {
                    lp.alpha = 1.0f;
                    Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
                }
            });
            //创建报价单
            addEcatalogLayout.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), CreateEcatalogActivity.class);
                    intent.putExtra("item_code", itemCode);
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                }
            });
        }

    }

    //获取我的报价单
    private void getEcatalog(final String itemCode) {
        final RequestNull request = new RequestNull();
        OkHttpClientManager.postAsyn(Config.ECATALLOG_MY_ECATALOG_NO_PAGE, new OkHttpClientManager.ResultCallback<EcatalogListNoPageableResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(EcatalogListNoPageableResponse response) {
                new SpecPopupWindows(Application.getInstance().getCurrentActivity(), response.getEcatalogListNoPageable(), itemCode);

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, EcatalogListNoPageableResponse.class);
    }

    //添加产品到我的报价单
    private void EcatalogAddItem(String id, String itemCode) {
        final AddItemToEcatalogRequest request = new AddItemToEcatalogRequest();
        request.setId(id);
        List<String> strings = new ArrayList<>();
        strings.add(itemCode);
        request.setItemCode(strings);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ADD_ITEM, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(String response) {
                //if ("success".equals(response.getStatus())) {
                Toast.makeText(Application.getInstance().getCurrentActivity(), response, Toast.LENGTH_SHORT).show();
                //}
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, String.class);
    }
}
