package com.humming.asc.sales.activity.product;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.AddItemToEcatalogRequest;
import com.humming.asc.sales.RequestData.RequestNull;
import com.humming.asc.sales.RequestData.StockByItemRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.ecatalog.CreateEcatalogActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.ecatalog.ProAddEcatalogAdapter;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.content.product.ProductInfoContent;
import com.humming.asc.sales.content.product.ProductStockContent;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.bean.ecatalog.EcatalogListNoPageableBean;
import com.humming.dto.ecatalogResponse.ecatalog.CollectionResponse;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogListNoPageableResponse;
import com.humming.dto.ecatalogResponse.product.StockDetailResponse;
import com.humming.dto.response.base.ReturnStatus;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ztq on 2019/10/19.
 * 搜索
 */

public class ProductInfoAndStockActivity extends AbstractActivity implements View.OnClickListener {

    private ProductInfoContent productInfoContent;
    private ProductStockContent productStockContent;
    private ImageView mIBack;
    private TextView mInfoLabel;
    private TextView mInfo;
    private LinearLayout mInfoLayout;
    private View mInfoLine;
    private TextView mStockLabel;
    private TextView mStock;
    private View mStockLine;
    private LinearLayout mStockLayout;
    private ImageView mInfoShare;
    private ImageView mInfoCollectionIcon;
    private LinearLayout mInfoCollection;
    private TextView mInfoEcatalogAdd;
    public static String itemCode = "";
    private LinearLayout parent;
    private TextView collectTv;
    private boolean isCollection = false;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        itemCode = getIntent().getStringExtra("itemCode");
        setContentView(R.layout.activity_product_info_stock);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        customDialog = new CustomDialog(this, "Loading...");
        mInfo.setText("(" + itemCode + ")");
        initCollect();//判断是否收藏
        initDatas();//获取库存数据
    }

    private void initCollect() {
        final StockByItemRequest request = new StockByItemRequest();
        request.setItemCode(ProductInfoAndStockActivity.itemCode);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_ISCOLLECTION, new OkHttpClientManager.ResultCallback<CollectionResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(CollectionResponse response) {
                if ("true".equals(response.getIsCollection())) {
                    isCollection = true;
                    mInfoCollectionIcon.setImageResource(R.mipmap.productdetail_action_collection_s);
                    collectTv.setText(getResources().getString(R.string.collected));
                    collectTv.setTextColor(getResources().getColor(R.color.ffbfa574));
                } else {
                    isCollection = false;
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, CollectionResponse.class);
    }

    private void initDatas() {
        customDialog.show();
        final StockByItemRequest request = new StockByItemRequest();
        request.setItemCode(ProductInfoAndStockActivity.itemCode);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_PRODUCT_STOCK, new OkHttpClientManager.ResultCallback<StockDetailResponse>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(StockDetailResponse response) {
                customDialog.dismiss();
                productStockContent.setDatas(response);
                mStock.setText("(" + response.getTotal() + ")");
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, StockDetailResponse.class);
    }

    private void initView() {
        parent = findViewById(R.id.product_info_parent);
        productInfoContent = (ProductInfoContent) findViewById(R.id.product_info_content);
        productStockContent = (ProductStockContent) findViewById(R.id.product_stock_content);
        mIBack = (ImageView) findViewById(R.id.product_info_toolbar_back);
        mIBack.setOnClickListener(this);
        mInfoLabel = (TextView) findViewById(R.id.product_info_toolbar_info_label);
        mInfo = (TextView) findViewById(R.id.product_info_toolbar_info);
        mInfoLayout = (LinearLayout) findViewById(R.id.product_info_toolbar_info_layout);
        mInfoLayout.setOnClickListener(this);
        mInfoLine = (View) findViewById(R.id.product_info_toolbar_info_line);
        mStockLabel = (TextView) findViewById(R.id.product_info_toolbar_stock_label);
        mStock = (TextView) findViewById(R.id.product_info_toolbar_stock);
        mStockLine = (View) findViewById(R.id.product_info_toolbar_stock_line);
        mStockLayout = (LinearLayout) findViewById(R.id.product_info_toolbar_stock_layout);
        mStockLayout.setOnClickListener(this);
        mInfoShare = (ImageView) findViewById(R.id.product_info_toolbar_share);
        mInfoShare.setOnClickListener(this);
        mInfoCollectionIcon = (ImageView) findViewById(R.id.content_pro_info_collection_icon);
        mInfoCollection = (LinearLayout) findViewById(R.id.content_pro_info_collection);
        mInfoCollection.setOnClickListener(this);
        mInfoEcatalogAdd = (TextView) findViewById(R.id.content_pro_info_ecatalog_add);
        mInfoEcatalogAdd.setOnClickListener(this);
        collectTv = findViewById(R.id.content_pro_info_collection_text);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.product_info_toolbar_back:// TODO 19/04/04
                finish();
                break;
            case R.id.product_info_toolbar_info_layout:// TODO 19/04/04
                productInfoContent.setVisibility(View.VISIBLE);
                productStockContent.setVisibility(View.GONE);
                mInfo.setTextColor(getResources().getColor(R.color.ffbfa574));
                mInfoLabel.setTextColor(getResources().getColor(R.color.ffbfa574));
                mInfoLine.setBackgroundColor(getResources().getColor(R.color.ffbfa574));
                mStock.setTextColor(getResources().getColor(R.color.f33));
                mStockLabel.setTextColor(getResources().getColor(R.color.f33));
                mInfoLine.setVisibility(View.VISIBLE);
                mStockLine.setVisibility(View.GONE);
                break;
            case R.id.product_info_toolbar_stock_layout:// TODO 19/04/04
                productStockContent.setVisibility(View.VISIBLE);
                productInfoContent.setVisibility(View.GONE);
                mStock.setTextColor(getResources().getColor(R.color.ffbfa574));
                mStockLabel.setTextColor(getResources().getColor(R.color.ffbfa574));
                mStockLine.setBackgroundColor(getResources().getColor(R.color.ffbfa574));
                mInfo.setTextColor(getResources().getColor(R.color.f33));
                mInfoLabel.setTextColor(getResources().getColor(R.color.f33));
                mInfoLine.setVisibility(View.GONE);
                mStockLine.setVisibility(View.VISIBLE);
                break;
            case R.id.product_info_toolbar_share:// TODO 19/04/04
                break;
            case R.id.content_pro_info_collection:
                if (isCollection) {
                    deleteCollect(itemCode);
                } else
                    collectItem(itemCode);
                break;
            case R.id.content_pro_info_ecatalog_add:
                getEcatalog(itemCode);
                break;
        }

    }

    //取消收藏
    private void deleteCollect(String itemCode) {
        customDialog.show();
        final StockByItemRequest request = new StockByItemRequest();
        request.setItemCode(itemCode);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_COLLECT_DELITEM, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ReturnStatus response) {
                customDialog.dismiss();
                if (response != null && "success".equals(response.getStatus())) {
                    isCollection = false;
                    mInfoCollectionIcon.setImageResource(R.mipmap.product_detail_action_collection_n);
                    collectTv.setText(getResources().getString(R.string.add_collect));
                    collectTv.setTextColor(getResources().getColor(R.color._f999));
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.cancel_collect_success), Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ReturnStatus.class);
    }

    @Override
    protected void onDestroy() {
        if (productInfoContent.webView != null) {
            productInfoContent.webView.loadDataWithBaseURL(null, "", "text/html", "utf-8", null);
            productInfoContent.webView.clearHistory();

            ((ViewGroup) productInfoContent.webView.getParent()).removeView(productInfoContent.webView);
            productInfoContent.webView.destroy();
            productInfoContent.webView = null;
        }
        super.onDestroy();
    }

    //收藏
    private void collectItem(String itemCode) {
        final StockByItemRequest request = new StockByItemRequest();
        request.setItemCode(itemCode);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_COLLECT_PRODUCT, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ReturnStatus response) {
                if ("success".equals(response.getStatus())) {
                    isCollection = true;
                    mInfoCollectionIcon.setImageResource(R.mipmap.productdetail_action_collection_s);
                    collectTv.setText(getResources().getString(R.string.collected));
                    collectTv.setTextColor(getResources().getColor(R.color.ffbfa574));
                    Toast.makeText(Application.getInstance().getCurrentActivity(), R.string.add_collect_success, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ReturnStatus.class);
    }

    //添加产品到我的报价单
    private void EcatalogAddItem(String id, String itemCode) {
        customDialog.show();
        final AddItemToEcatalogRequest request = new AddItemToEcatalogRequest();
        request.setId(id);
        List<String> strings = new ArrayList<>();
        strings.add(itemCode);
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
                //if ("success".equals(response.getStatus())) {
                customDialog.dismiss();
                Toast.makeText(Application.getInstance().getCurrentActivity(), response, Toast.LENGTH_SHORT).show();
                // }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, String.class);
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
            dissmissLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lp.alpha = 1.0f;
                    dismiss();
                    Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
                }
            });
            setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    lp.alpha = 1.0f;
                    Application.getInstance().getCurrentActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                    Application.getInstance().getCurrentActivity().getWindow().setAttributes(lp);
                }
            });
            //创建报价单
            addEcatalogLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), CreateEcatalogActivity.class);
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                }
            });
        }

    }

    //获取我的报价单
    private void getEcatalog(final String itemCode) {
        customDialog.show();
        final RequestNull request = new RequestNull();
        OkHttpClientManager.postAsyn(Config.ECATALLOG_MY_ECATALOG_NO_PAGE, new OkHttpClientManager.ResultCallback<EcatalogListNoPageableResponse>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(EcatalogListNoPageableResponse response) {
                customDialog.dismiss();
                new SpecPopupWindows(Application.getInstance().getCurrentActivity(), response.getEcatalogListNoPageable(), itemCode);

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, EcatalogListNoPageableResponse.class);
    }
}