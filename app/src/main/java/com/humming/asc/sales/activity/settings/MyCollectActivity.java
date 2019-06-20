package com.humming.asc.sales.activity.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.MyEcatalogRequest;
import com.humming.asc.sales.RequestData.StockByItemRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.product.ProductInfoAndStockActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.my.CollectAdapter;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.bean.product.ProductList;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogCollectionListResponse;
import com.humming.dto.response.base.ReturnStatus;
import com.squareup.okhttp.Request;

import java.util.ArrayList;
import java.util.List;

public class MyCollectActivity extends AbstractActivity implements BaseAdapter.RequestLoadMoreListener, View.OnClickListener {

    private int hasMore = 1;
    private String pagable = "0";
    private ImageView mMyBack;
    private RecyclerView mMyListview;
    private CollectAdapter adapter = new CollectAdapter(null);
    private List<ProductList> collectionProductList;
    private List<ProductList> collectionProductLists;
    private LinearLayout nodataLayout;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collect);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        customDialog = new CustomDialog(this, "Loading...");
        mLoading.show();
        initData(pagable + "");
    }

    private void initData(final String pagables) {
        final MyEcatalogRequest request = new MyEcatalogRequest();
        request.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_COLLECT_LIST, new OkHttpClientManager.ResultCallback<EcatalogCollectionListResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                mLoading.hide();
            }

            @Override
            public void onResponse(EcatalogCollectionListResponse response) {
                mLoading.hide();
                if (response != null && response.getCollectionProductList().size() > 0) {
                    nodataLayout.setVisibility(View.GONE);
                    mMyListview.setVisibility(View.VISIBLE);
                    collectionProductList = response.getCollectionProductList();
                    if ("0".equals(pagables)) {
                        collectionProductLists.clear();
                        collectionProductLists.addAll(collectionProductList);
                        adapter = new CollectAdapter(collectionProductList);
                        adapter.openLoadAnimation();
                        mMyListview.setAdapter(adapter);
                        adapter.openLoadMore(collectionProductList.size(), true);
                        hasMore = response.getHasMore();
                        pagable = response.getPagable() + "";

                    } else {
                        collectionProductLists.addAll(collectionProductList);
                        hasMore = response.getHasMore();
                        if (response.getHasMore() == 1) {
                            pagable = response.getPagable() + "";
                        } else {
                            pagable = "0";
                        }
                        adapter.notifyDataChangedAfterLoadMore(collectionProductList, true);
                    }
                    adapter.setOnLoadMoreListener(MyCollectActivity.this);
                    adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), ProductInfoAndStockActivity.class);
                            intent.putExtra("itemCode", collectionProductLists.get(position).getItemCode());
                            Application.getInstance().getCurrentActivity().startActivity(intent);
                        }
                    });
                    adapter.setOnRecyclerViewItemChildClickListener(new BaseAdapter.OnRecyclerViewItemChildClickListener() {
                        @Override
                        public void onItemChildClick(BaseAdapter adapter, View view, int position) {
                            switch (view.getId()) {
                                case R.id.item_collect__delete:
                                    showTip(collectionProductLists.get(position).getItemCode() + "", position);
                                    break;
                            }
                        }
                    });
                } else {
                    nodataLayout.setVisibility(View.VISIBLE);
                    mMyListview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                mLoading.hide();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, EcatalogCollectionListResponse.class);
    }

    private void deleteCollect(String itemCode, final int position) {
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
                    collectionProductLists.remove(position);
                    adapter.remove(position);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ReturnStatus.class);
    }

    private void initView() {
        nodataLayout = findViewById(R.id.content_nodata);
        collectionProductLists = new ArrayList<>();
        mMyBack = findViewById(R.id.collect_my__back);
        mMyBack.setOnClickListener(this);
        mMyListview = (RecyclerView) findViewById(R.id.collect_my_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMyListview.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onLoadMoreRequested() {
        mMyListview.post(new Runnable() {
            @Override
            public void run() {
                if (hasMore != 1) {
                    // adapter.removeAllFooterView();
                    adapter.notifyDataChangedAfterLoadMore(false);
                    // View notLoadingView = LayoutInflater.from(Application.getInstance().getCurrentActivity()).inflate(R.layout.not_loading, (ViewGroup) mMyListview.getParent(), false);
                    //  adapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData(pagable);
                        }
                    }, 1500);
                }
            }

        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.collect_my__back:
                finish();
                break;
            default:
                break;
        }
    }

    //删除取消收藏
    private void showTip(final String itemCode, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.cancel_favorite));
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        //设置正面按钮
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteCollect(itemCode, position);
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton(getResources().getString(R.string.cancel1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
