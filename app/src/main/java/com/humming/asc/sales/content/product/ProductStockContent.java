package com.humming.asc.sales.content.product;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.BookingListRequest;
import com.humming.asc.sales.RequestData.StockByItemRequest;
import com.humming.asc.sales.RequestData.StockWarnRequest;
import com.humming.asc.sales.activity.product.BookingListActivity;
import com.humming.asc.sales.activity.product.ProductInfoAndStockActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.product.ProInfoStockAdapter;
import com.humming.asc.sales.adapter.product.ProInfoStockOnTheWayAdapter;
import com.humming.asc.sales.component.AutoRecyclerView;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.model.product.StockListEntity;
import com.humming.asc.sales.model.product.WhouseListEntity;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.bean.product.StockList;
import com.humming.dto.ecatalogResponse.bean.product.WotsList;
import com.humming.dto.ecatalogResponse.product.BookingResponse;
import com.humming.dto.ecatalogResponse.product.StockDetailResponse;
import com.humming.dto.response.base.ReturnStatus;
import com.squareup.okhttp.Request;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ztq on 3/27/19.
 */
public class ProductStockContent extends LinearLayout implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private final View view;
    private Context context;
    private LinearLayout mProductStockOut, mProductBookingView;
    private TextView mBookingValueTv;
    private AutoRecyclerView mListviewStock;
    private LinearLayout mStockBooking, mNowStockMoreView;
    private AutoRecyclerView mNowListviewStock;
    private ProInfoStockOnTheWayAdapter onTheWayAdapter;
    private ProInfoStockAdapter stockAdapter;
    private List<StockList> stock;
    private List<StockListEntity> stockListEntityList;
    private List<WotsList> wots;
    private String currentVintage = "";
    private int currentStock = 0;
    private String whouseName = "";
    private Integer earlyWarning = null;
    private BookingResponse bookingResponse;
    private CustomDialog customDialog;


    public ProductStockContent(Context context) {
        this(context, null);
    }

    public ProductStockContent(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = inflate(context, R.layout.content_product_stock, this);
        customDialog = new CustomDialog(Application.getInstance().getCurrentActivity(), "Loading...");
        initView();
        // initDatas();
    }

    public void setDatas(StockDetailResponse response) {
        stockListEntityList.clear();
        stock = response.getStock();
        wots = response.getWots();
        for (StockList list : stock) {
            StockListEntity entity = new StockListEntity();
            entity.setBJWH(new WhouseListEntity(list.getBJWH(), false));
            entity.setCDWH(new WhouseListEntity(list.getCDWH(), false));
            entity.setSHWH(new WhouseListEntity(list.getSHWH(), false));
            entity.setGZWH(new WhouseListEntity(list.getGZWH(), false));
            entity.setMCWH(new WhouseListEntity(list.getMCWH(), false));
            entity.setHKWH(new WhouseListEntity(list.getHKWH(), false));
            entity.setVintage(list.getVintage());
            stockListEntityList.add(entity);
        }
        //库存adapter
        stockAdapter = new ProInfoStockAdapter(stockListEntityList);
        stockAdapter.openLoadAnimation();
        mListviewStock.setAdapter(stockAdapter);
        stockAdapter.setOnRecyclerViewItemChildClickListener(ProductStockContent.this);

        //在途adapter
        //   if (wots.size() > 2) {
        //     onTheWayAdapter = new ProInfoStockOnTheWayAdapter(wots.subList(0, 2));
        //     mNowStockMoreView.setVisibility(View.VISIBLE);
        //  } else {
        onTheWayAdapter = new ProInfoStockOnTheWayAdapter(wots);
        mNowStockMoreView.setVisibility(View.GONE);
        //   }
        onTheWayAdapter.openLoadAnimation();
        mNowListviewStock.setAdapter(onTheWayAdapter);
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
                stockListEntityList.clear();
                stock = response.getStock();
                wots = response.getWots();
                for (StockList list : stock) {
                    StockListEntity entity = new StockListEntity();
                    entity.setBJWH(new WhouseListEntity(list.getBJWH(), false));
                    entity.setCDWH(new WhouseListEntity(list.getCDWH(), false));
                    entity.setSHWH(new WhouseListEntity(list.getSHWH(), false));
                    entity.setGZWH(new WhouseListEntity(list.getGZWH(), false));
                    entity.setMCWH(new WhouseListEntity(list.getMCWH(), false));
                    entity.setHKWH(new WhouseListEntity(list.getHKWH(), false));
                    entity.setVintage(list.getVintage());
                    stockListEntityList.add(entity);
                }
                //库存adapter
                stockAdapter = new ProInfoStockAdapter(stockListEntityList);
                stockAdapter.openLoadAnimation();
                mListviewStock.setAdapter(stockAdapter);
                stockAdapter.setOnRecyclerViewItemChildClickListener(ProductStockContent.this);

                //在途adapter
                if (wots.size() > 2) {
                    onTheWayAdapter = new ProInfoStockOnTheWayAdapter(wots.subList(0, 2));
                    mNowStockMoreView.setVisibility(View.VISIBLE);
                } else {
                    onTheWayAdapter = new ProInfoStockOnTheWayAdapter(wots);
                    mNowStockMoreView.setVisibility(View.GONE);
                }
                onTheWayAdapter.openLoadAnimation();
                mNowListviewStock.setAdapter(onTheWayAdapter);
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, StockDetailResponse.class);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        mProductBookingView.setVisibility(View.VISIBLE);
        currentVintage = stockListEntityList.get(position).getVintage();
        switch (view.getId()) {
            case R.id.item_pro_info_stock__shwh:
                whouseName = "SHWH";
                earlyWarning = stockListEntityList.get(position).getSHWH().getWhouseList().getEarlyWarning();
                currentStock = stockListEntityList.get(position).getSHWH().getWhouseList().getStock();
                setNotiy();
                stockListEntityList.get(position).getSHWH().setCheckSelect(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.item_pro_info_stock__bjwh:
                whouseName = "BJWH";
                earlyWarning = stockListEntityList.get(position).getBJWH().getWhouseList().getEarlyWarning();
                currentStock = stockListEntityList.get(position).getBJWH().getWhouseList().getStock();
                setNotiy();
                stockListEntityList.get(position).getBJWH().setCheckSelect(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.item_pro_info_stock__cdwh:
                whouseName = "CDWH";
                earlyWarning = stockListEntityList.get(position).getCDWH().getWhouseList().getEarlyWarning();
                currentStock = stockListEntityList.get(position).getCDWH().getWhouseList().getStock();
                setNotiy();
                stockListEntityList.get(position).getCDWH().setCheckSelect(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.item_pro_info_stock__gzwh:
                whouseName = "GZWH";
                earlyWarning = stockListEntityList.get(position).getGZWH().getWhouseList().getEarlyWarning();
                currentStock = stockListEntityList.get(position).getGZWH().getWhouseList().getStock();
                setNotiy();
                stockListEntityList.get(position).getGZWH().setCheckSelect(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.item_pro_info_stock__hkwh:
                whouseName = "HKWH";
                earlyWarning = stockListEntityList.get(position).getHKWH().getWhouseList().getEarlyWarning();
                currentStock = stockListEntityList.get(position).getHKWH().getWhouseList().getStock();
                setNotiy();
                stockListEntityList.get(position).getHKWH().setCheckSelect(true);
                adapter.notifyDataSetChanged();
                break;
            case R.id.item_pro_info_stock__mcwh:
                whouseName = "MCWH";
                currentStock = stockListEntityList.get(position).getMCWH().getWhouseList().getStock();
                earlyWarning = stockListEntityList.get(position).getMCWH().getWhouseList().getEarlyWarning();
                setNotiy();
                stockListEntityList.get(position).getMCWH().setCheckSelect(true);
                adapter.notifyDataSetChanged();
                break;
        }
        getBooking();
    }

    private void setNotiy() {
        for (StockListEntity entity : stockListEntityList) {
            entity.setBJWH(new WhouseListEntity(entity.getBJWH().getWhouseList(), false));
            entity.setSHWH(new WhouseListEntity(entity.getSHWH().getWhouseList(), false));
            entity.setCDWH(new WhouseListEntity(entity.getCDWH().getWhouseList(), false));
            entity.setGZWH(new WhouseListEntity(entity.getGZWH().getWhouseList(), false));
            entity.setMCWH(new WhouseListEntity(entity.getMCWH().getWhouseList(), false));
            entity.setHKWH(new WhouseListEntity(entity.getHKWH().getWhouseList(), false));
        }
    }

    private void initView() {
        mProductBookingView = (LinearLayout) view.findViewById(R.id.content_product_stock__booking_layout);
        mProductStockOut = (LinearLayout) view.findViewById(R.id.content_product_stock__out);
        mProductStockOut.setOnClickListener(this);
        mListviewStock = (AutoRecyclerView) view.findViewById(R.id.stock_listview);
        mStockBooking = (LinearLayout) view.findViewById(R.id.content_product_stock__booking);
        mStockBooking.setOnClickListener(this);
        mNowListviewStock = (AutoRecyclerView) view.findViewById(R.id.stock_now_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mNowListviewStock.setLayoutManager(linearLayoutManager);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext());
        mListviewStock.setLayoutManager(linearLayoutManager1);
        mNowStockMoreView = (LinearLayout) view.findViewById(R.id.stock_now_more_layout);
        mBookingValueTv = (TextView) view.findViewById(R.id.content_product_stock__booking_text);
        mNowStockMoreView.setOnClickListener(this);
        bookingResponse = new BookingResponse();
        stockListEntityList = new ArrayList<>();
    }

    //预订记录
    private void getBooking() {
        customDialog.show();
        final BookingListRequest request = new BookingListRequest();
        request.setVintage(currentVintage);
        request.setWareHouse(whouseName);
        request.setItemCode(ProductInfoAndStockActivity.itemCode);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_PRODUCT_BOOKING, new OkHttpClientManager.ResultCallback<BookingResponse>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(BookingResponse response) {
                customDialog.dismiss();
                bookingResponse = response;
                String content = currentVintage + "  " + whouseName + "  " + Application.getInstance().getCurrentActivity().getResources().getString(R.string.reserve) + "  " + "<=font color=\"#FFAB22\">" + response.getTotal() + "</font>";
                mBookingValueTv.setText(Html.fromHtml(content));
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, BookingResponse.class);
    }

    /**
     * show Dialog 设置预警
     */
    private void showSubmitDialog() {
        LayoutInflater inflater = LayoutInflater.from(context);
        View layout = inflater.inflate(R.layout.pro_stock_warn_dialog, null);
        final EditText editText = layout.findViewById(R.id.stock_warn_dialog_edittext);
        TextView textView = layout.findViewById(R.id.stock_warn_dialog_text);
        final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        String content = Application.getInstance().getCurrentActivity().getResources().getString(R.string.settings1) + "<=font color=\"#FFAB22\">" + currentVintage + "   " + whouseName + "  " + "</font>" + Application.getInstance().getCurrentActivity().getResources().getString(R.string.stock_warn_message);
        textView.setText(Html.fromHtml(content));
        if (earlyWarning != null) {
            editText.setText(earlyWarning + "");
        }
        layout.findViewById(R.id.stock_warn_dialog_cancel1).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });
        layout.findViewById(R.id.pro_stock_warn_dialog_cancle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // builder.dismiss();
                customDialog.show();
                final StockWarnRequest request = new StockWarnRequest();
                request.setVintage(currentVintage);
                request.setItemCode(ProductInfoAndStockActivity.itemCode);
                request.setWhouseName(whouseName);
                OkHttpClientManager.postAsyn(Config.ECATALLOG_STOCK_WARN_CANCEL, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
                    @Override
                    public void onError(Request request, Error info) {
                        customDialog.dismiss();
                        builder.dismiss();
                        Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                    }

                    @Override
                    public void onResponse(ReturnStatus response) {
                        customDialog.dismiss();
                        builder.dismiss();
                        if ("success".equals(response.getStatus())) {
                            initDatas();
                            Toast.makeText(Application.getInstance().getCurrentActivity(), R.string.cancel_success, Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(Application.getInstance().getCurrentActivity(), R.string.cancel_fail, Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onOtherError(Request request, Exception exception) {
                        builder.dismiss();
                        customDialog.dismiss();
                        Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                    }
                }, request, ReturnStatus.class);
            }
        });
        layout.findViewById(R.id.pro_stock_warn_dialog_submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(editText.getText().toString()) || editText.getText().toString() == null) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), R.string.enter_stock_warn, Toast.LENGTH_SHORT).show();
                } else {
                    if (Integer.parseInt(editText.getText().toString()) >= currentStock) {
                        Toast.makeText(Application.getInstance().getCurrentActivity(), R.string.warn_no_stock, Toast.LENGTH_SHORT).show();
                    } else {
                        final StockWarnRequest request = new StockWarnRequest();
                        request.setVintage(currentVintage);
                        request.setItemCode(ProductInfoAndStockActivity.itemCode);
                        request.setWhouseName(whouseName);
                        request.setEarlyWarning(editText.getText().toString());
                        OkHttpClientManager.postAsyn(Config.ECATALLOG_STOCK_WARN, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
                            @Override
                            public void onError(Request request, Error info) {
                                builder.dismiss();
                                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                            }

                            @Override
                            public void onResponse(ReturnStatus response) {
                                builder.dismiss();
                                if ("success".equals(response.getStatus())) {
                                    initDatas();
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), R.string.set_success, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), R.string.set_fail, Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onOtherError(Request request, Exception exception) {
                                builder.dismiss();
                                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
                            }
                        }, request, ReturnStatus.class);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_product_stock__out://设置库存预警
                if ("".equals(whouseName)) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), R.string.select_one_stock, Toast.LENGTH_SHORT).show();
                } else
                    showSubmitDialog();
                break;
            case R.id.content_product_stock__booking:// 预定记录
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), BookingListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("booking", (Serializable) bookingResponse);
                bundle.putString("vintage", currentVintage);
                bundle.putString("whouse_name", whouseName);
                intent.putExtras(bundle);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.stock_now_more_layout:// 查看全部在途库存
                mNowStockMoreView.setVisibility(GONE);
                onTheWayAdapter = new ProInfoStockOnTheWayAdapter(wots);
                onTheWayAdapter.openLoadAnimation();
                mNowListviewStock.setAdapter(onTheWayAdapter);
                break;
            default:
                break;
        }
    }

}
