package com.humming.asc.sales.activity.ecatalog;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.EcatalogDateRequest;
import com.humming.asc.sales.RequestData.EcatalogDetailRequest;
import com.humming.asc.sales.RequestData.EcatalogStatusRequest;
import com.humming.asc.sales.RequestData.EcatalogVintageRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.product.ProductInfoAndStockActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.ecatalog.EcatalogDetailProAdapter;
import com.humming.asc.sales.component.AutoRecyclerView;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.model.ecatalog.CreateEcatalogEntity;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogBaseDetailAndroidResponse;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogBaseDetailListAndroidResponse;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogDetailAndroidResponse;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogItemDetailAndroidResopnse;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogUpdateStatusResponse;
import com.humming.dto.response.base.ReturnStatus;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EcatalogDetailActivity extends AbstractActivity implements View.OnClickListener, BaseAdapter.OnRecyclerViewItemChildClickListener {

    private ImageView mCreateBack;
    private LinearLayout mDetailEdit;
    private TextView mDetailTitle;
    private TextView mDetailCustomer;
    private TextView mDetailContacts;
    private TextView mDetailPhone;
    private TextView mDetailCreater;
    private TextView mDetailCreateTime;
    private TextView mDetailStatusValue;
    private LinearLayout mDetailStatus;
    private TextView mDetailValidityValue;
    private LinearLayout mDetailValidity;
    private TextView mDetailScopeValue;
    private LinearLayout mDetailScope;
    private TextView mDetailQtyValue;
    private LinearLayout mDetailQty;
    private TextView mDetailAddProduct;
    private TextView mDetailAddProduct2;
    private LinearLayout mDetailNodata;
    private AutoRecyclerView mDetailListview;
    private String ecatalogId = "";
    private EcatalogDetailProAdapter adapter;
    private LinearLayout mfrLayout, settingLayout, seeLayout;
    private TextView sendTv;
    private List<EcatalogItemDetailAndroidResopnse> itemDetailAndroidResopnseList = new ArrayList<>();
    public static EcatalogDetailActivity activity;
    private int year, monthOfYear, dayOfMonth;
    private ImageView mDetailEditImg;
    private TextView mDetailEditText;
    private String editRole = "0";//1可编辑 0不可编辑
    private AlertDialog alertDialog1;
    private String preViewUrl = "";
    private CustomDialog customDialog;
    private String statusNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecatalog_detail);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        ecatalogId = getIntent().getStringExtra("ecatalog_id");
        activity = this;
        customDialog = new CustomDialog(this, "Loading...");
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initData();
    }

    private void initData() {
        customDialog.show();
        final EcatalogDetailRequest request = new EcatalogDetailRequest();
        request.setId(ecatalogId);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_DETAIL_ECATALOG, new OkHttpClientManager.ResultCallback<EcatalogDetailAndroidResponse>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(EcatalogDetailAndroidResponse response) {
                customDialog.dismiss();
                EcatalogBaseDetailAndroidResponse base = response.getEcatalogBaseDetailAndroidResponse();
                EcatalogBaseDetailListAndroidResponse statusResponse = response.getEcatalogBaseDetailListAndroidResponse();
                preViewUrl = response.getEcatalogBaseDetailAndroidResponse().getPreviewUrl();
                statusNumber = statusResponse.getStatusNumber();
                mDetailTitle.setText(base.getName());
                mDetailCustomer.setText(base.getUserName());
                mDetailContacts.setText(base.getContacts());
                mDetailPhone.setText(base.getPhone());
                mDetailCreater.setText(base.getCrtUserName());
                mDetailCreateTime.setText(base.getCrtTime());
                mDetailStatusValue.setText(statusResponse.getStatus());
                mDetailValidityValue.setText(statusResponse.getValidityDate());
                mDetailQtyValue.setText(statusResponse.getCount() + "");
                mDetailScopeValue.setText(statusResponse.getScope());
                itemDetailAndroidResopnseList = response.getEcatalogItemDetailAndroidResopnseList();
                editRole = base.getEditRole();
                if ("0".equals(editRole)) {
                    Drawable up = ContextCompat.getDrawable(Application.getInstance().getCurrentActivity(), R.mipmap.quotationdetail_action_edit);
                    Drawable drawableUp = DrawableCompat.wrap(up);
                    DrawableCompat.setTint(drawableUp, ContextCompat.getColor(Application.getInstance().getCurrentActivity(), R.color.actionbar_title_secondary));
                    mDetailEditImg.setImageDrawable(drawableUp);
                    mDetailEditText.setTextColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.actionbar_title_secondary));
                }
                if (itemDetailAndroidResopnseList != null && itemDetailAndroidResopnseList.size() > 0) {
                    mDetailNodata.setVisibility(View.GONE);
                    mDetailListview.setVisibility(View.VISIBLE);
                    adapter = new EcatalogDetailProAdapter(itemDetailAndroidResopnseList);
                    adapter.openLoadAnimation();
                    mDetailListview.setAdapter(adapter);
                    adapter.removeAllFooterView();
                    View notLoadingView = LayoutInflater.from(Application.getInstance().getCurrentActivity()).inflate(R.layout.not_loading, (ViewGroup) mDetailListview.getParent(), false);
                    TextView tv = notLoadingView.findViewById(R.id.no_data_tv);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
                    lp.bottomMargin = 0;
                    tv.setLayoutParams(lp);
                    adapter.addFooterView(notLoadingView);
                    adapter.setOnRecyclerViewItemChildClickListener(EcatalogDetailActivity.this);
                    adapter.setOnRecyclerViewItemLongClickListener(new BaseAdapter.OnRecyclerViewItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(View view, int position) {
                            showTip(itemDetailAndroidResopnseList.get(position).getId() + "", position);
                            return false;
                        }
                    });
                } else {
                    mDetailNodata.setVisibility(View.VISIBLE);
                    mDetailListview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, EcatalogDetailAndroidResponse.class);
    }

    //删除报价单产品
    private void deleteItem(String id, final int position) {
        customDialog.show();
        final EcatalogDetailRequest request = new EcatalogDetailRequest();
        request.setId(id);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_DELETE_ITEM, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
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
                    itemDetailAndroidResopnseList.remove(position);
                    adapter.notifyItemRemoved(position);
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
        mCreateBack = (ImageView) findViewById(R.id.ecatalog_create__back);
        mCreateBack.setOnClickListener(this);
        mDetailEdit = (LinearLayout) findViewById(R.id.ecatalog_detail_edit);
        mDetailEdit.setOnClickListener(this);
        mDetailTitle = (TextView) findViewById(R.id.ecatalog_detail_title);
        mDetailCustomer = (TextView) findViewById(R.id.ecatalog_detail_customer);
        mDetailContacts = (TextView) findViewById(R.id.ecatalog_detail_contacts);
        mDetailPhone = (TextView) findViewById(R.id.ecatalog_detail_phone);
        mDetailCreater = (TextView) findViewById(R.id.ecatalog_detail_creater);
        mDetailCreateTime = (TextView) findViewById(R.id.ecatalog_detail_create_time);
        mDetailStatusValue = (TextView) findViewById(R.id.ecatalog_detail_status_value);
        mDetailStatus = (LinearLayout) findViewById(R.id.ecatalog_detail_status);
        mDetailStatus.setOnClickListener(this);
        mDetailValidityValue = (TextView) findViewById(R.id.ecatalog_detail_validity_value);
        mDetailValidity = (LinearLayout) findViewById(R.id.ecatalog_detail_validity);
        mDetailValidity.setOnClickListener(this);
        mDetailScopeValue = (TextView) findViewById(R.id.ecatalog_detail_scope_value);
        mDetailScope = (LinearLayout) findViewById(R.id.ecatalog_detail_scope);
        mDetailScope.setOnClickListener(this);
        mDetailQtyValue = (TextView) findViewById(R.id.ecatalog_detail_qty_value);
        mDetailQty = (LinearLayout) findViewById(R.id.ecatalog_detail_qty);
        mDetailQty.setOnClickListener(this);
        mDetailAddProduct = (TextView) findViewById(R.id.ecatalog_detail_add_product);
        mDetailAddProduct.setOnClickListener(this);
        mDetailAddProduct2 = (TextView) findViewById(R.id.ecatalog_detail_add_product2);
        mDetailAddProduct2.setOnClickListener(this);
        mDetailNodata = (LinearLayout) findViewById(R.id.ecatalog_detail_nodata);
        mDetailListview = (AutoRecyclerView) findViewById(R.id.ecatalog_detail_listview);
        mDetailListview.setNestedScrollingEnabled(false);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mDetailListview.setLayoutManager(linearLayoutManager);
        String content = getResources().getString(R.string.please_go) + "<=font color=\"#448fe7\">" + getResources().getString(R.string.add_pro1) + "</font>";
        mDetailAddProduct2.setText(Html.fromHtml(content));

        sendTv = findViewById(R.id.ecatalog_detail_send);
        sendTv.setOnClickListener(this);
        seeLayout = findViewById(R.id.ecatalog_detail_see);
        seeLayout.setOnClickListener(this);
        settingLayout = findViewById(R.id.ecatalog_detail_setting);
        settingLayout.setOnClickListener(this);
        mfrLayout = findViewById(R.id.ecatalog_detail_mfr);
        mfrLayout.setOnClickListener(this);
        mDetailEditImg = (ImageView) findViewById(R.id.ecatalog_detail_edit_img);
        mDetailEditText = (TextView) findViewById(R.id.ecatalog_detail_edit_text);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ecatalog_create__back:
                finish();
                break;
            case R.id.ecatalog_detail_edit:
                if ("0".equals(editRole)) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.dont_edit), Toast.LENGTH_SHORT).show();
                } else {
                    CreateEcatalogEntity entity = new CreateEcatalogEntity();
                    entity.setTitle(mDetailTitle.getText().toString());
                    entity.setContract(mDetailContacts.getText().toString());
                    entity.setPhone(mDetailPhone.getText().toString());
                    entity.setDate(mDetailValidityValue.getText().toString());
                    entity.setUserName(mDetailCustomer.getText().toString());
                    entity.setEcatalogId(ecatalogId);
                    Intent intent = new Intent(this, EcatalogEditActivity.class);
                    Bundle resultBundles = new Bundle();
                    resultBundles.putSerializable("edit_ecatalog", entity);
                    intent.putExtras(resultBundles);
                    startActivity(intent);
                }
                break;
            case R.id.ecatalog_detail_status:
                Intent intent = new Intent(this, EcatalogTrackActivity.class);
                intent.putExtra("ecatalog_id", ecatalogId);
                startActivity(intent);
                break;
            case R.id.ecatalog_detail_validity:// 有效期
                if ("0".equals(editRole)) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.dont_edit), Toast.LENGTH_SHORT).show();
                } else
                    showPopWindowDatePicker();
                break;
            case R.id.ecatalog_detail_scope:
                //不能点
                break;
            case R.id.ecatalog_detail_qty:
                //不能点
                break;
            case R.id.ecatalog_detail_add_product:
                Intent intent1 = new Intent(this, EcatalogAddProActivity.class);
                intent1.putExtra("ecatalog_id", ecatalogId);
                startActivity(intent1);
                break;
            case R.id.ecatalog_detail_add_product2:
                Intent intent2 = new Intent(this, EcatalogAddProActivity.class);
                intent2.putExtra("ecatalog_id", ecatalogId);
                startActivity(intent2);
                break;
            case R.id.ecatalog_detail_mfr:// TODO 19/04/28
                break;
            case R.id.ecatalog_detail_setting:
                Intent settingIntent = new Intent(this, EcatalogSettingActivity.class);
                settingIntent.putExtra("ecatalog_id", ecatalogId);
                startActivity(settingIntent);
                break;
            case R.id.ecatalog_detail_see:
                Intent seeIntent = new Intent(this, EcatalogWebViewActivity.class);
                seeIntent.putExtra("ecatalog_url", preViewUrl);
                startActivity(seeIntent);
                break;
            case R.id.ecatalog_detail_send:
                sendChangeStatus();
                showSendDialog();
                break;
        }
    }

    //发送更改状态
    private void sendChangeStatus() {
        final EcatalogStatusRequest request = new EcatalogStatusRequest();
        request.setId(ecatalogId);
        request.setStatus(statusNumber);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_STATUS_UPDATE, new OkHttpClientManager.ResultCallback<EcatalogUpdateStatusResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(EcatalogUpdateStatusResponse response) {
                if ("success".equals(response.getStatus())) {
                    mDetailStatusValue.setText(response.getText());
                    EventBus.getDefault().post(new BackRefreshOneEvent("1"));
                    editRole = "0";
                    Drawable up = ContextCompat.getDrawable(Application.getInstance().getCurrentActivity(), R.mipmap.quotationdetail_action_edit);
                    Drawable drawableUp = DrawableCompat.wrap(up);
                    DrawableCompat.setTint(drawableUp, ContextCompat.getColor(Application.getInstance().getCurrentActivity(), R.color.actionbar_title_secondary));
                    mDetailEditImg.setImageDrawable(drawableUp);
                    mDetailEditText.setTextColor(Application.getInstance().getCurrentActivity().getResources().getColor(R.color.actionbar_title_secondary));

                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, EcatalogUpdateStatusResponse.class);
    }

    @Override
    public void onItemChildClick(BaseAdapter adapter, View view, int position) {
        switch (view.getId()) {
            case R.id.item_ecatalog_pro_year_layout://年份
                InputMethodManager imm1 = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                TextView textView1 = (TextView) view.findViewById(R.id.item_ecatalog_pro_year);
                textView1.setFocusable(true);
                textView1.setFocusableInTouchMode(true);
                textView1.requestFocus(); // 初始不让EditText得焦点
                textView1.requestFocusFromTouch();
                showVintageList((TextView) textView1, itemDetailAndroidResopnseList.get(position).getId() + "", itemDetailAndroidResopnseList.get(position).getVintageList());
                break;
            case R.id.item_ecatalog_pro_set_top://置顶
                InputMethodManager imm2 = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                mCreateBack.setFocusable(true);
                mCreateBack.setFocusableInTouchMode(true);
                mCreateBack.requestFocus(); // 初始不让EditText得焦点
                mCreateBack.requestFocusFromTouch();
                setProTop(itemDetailAndroidResopnseList.get(position).getId() + "");
                break;
            case R.id.item_ecatalog_pro_name://跳转产品详情
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), ProductInfoAndStockActivity.class);
                intent.putExtra("itemCode", itemDetailAndroidResopnseList.get(position).getItemCode());
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.item_ecatalog_pro_name_en://跳转产品详情
                Intent intens = new Intent(Application.getInstance().getCurrentActivity(), ProductInfoAndStockActivity.class);
                intens.putExtra("itemCode", itemDetailAndroidResopnseList.get(position).getItemCode());
                Application.getInstance().getCurrentActivity().startActivity(intens);
                break;
        }
    }

    //设置报价单产品置顶
    private void setProTop(String id) {
        customDialog.show();
        final EcatalogDetailRequest request = new EcatalogDetailRequest();
        request.setId(id);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_ROOF, new OkHttpClientManager.ResultCallback<List<EcatalogItemDetailAndroidResopnse>>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<EcatalogItemDetailAndroidResopnse> response) {
                customDialog.dismiss();
                itemDetailAndroidResopnseList = response;
                adapter.setNewData(response);
                /*adapter = new EcatalogDetailProAdapter(response);
                adapter.openLoadAnimation();
                mDetailListview.setAdapter(adapter);
                adapter.setOnRecyclerViewItemChildClickListener(EcatalogDetailActivity.this);*/
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, new TypeReference<List<EcatalogItemDetailAndroidResopnse>>() {
        });
    }

    //popwindow显示
    private void showPopWindowDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_popup_datepicker, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(findViewById(R.id.ecatalog_detail__parent),
                Gravity.BOTTOM, 0, 0);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        if (!"".equals(mDetailValidityValue.getText().toString())) {
            String times = mDetailValidityValue.getText().toString();
            String years = times.substring(0, 4);
            String month = times.substring(5, 7);
            String day = times.substring(8, 10);
            if ("0".equals(month.substring(0, 1))) {
                month = month.substring(1, 2);
            }
            if ("0".equals(day.substring(0, 1))) {
                day = day.substring(1, 2);
            }
            year = Integer.parseInt(years);
            monthOfYear = Integer.parseInt(month) - 1;
            dayOfMonth = Integer.parseInt(day);
        } else {
            year = calendar.get(Calendar.YEAR);
            monthOfYear = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        }
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int years,
                                      int monthOfYears, int dayOfMonths) {
                year = years;
                monthOfYear = monthOfYears;
                dayOfMonth = dayOfMonths;
            }
        });
        TextView dateCancel = (TextView) view.findViewById(R.id.date_cancel);
        TextView datesubmit = (TextView) view.findViewById(R.id.date_submit);
        dateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        datesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = "";
                String day = "";
                String date = "";
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }
                if (monthOfYear < 9) {
                    month = "0" + (monthOfYear + 1);
                } else {
                    month = (monthOfYear + 1) + "";
                }
                date = year + "-" + month + "-" + day;
                mDetailValidityValue.setText(date);
                window.dismiss();
                //设置有效期
                setvalidityDate(date);

            }
        });
    }

    public void setvalidityDate(String validityDate) {
        customDialog.show();
        final EcatalogDateRequest request = new EcatalogDateRequest();
        request.setValidityDate(validityDate+" 23:59:59");
        request.setId(ecatalogId);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_VALIDITYDATE, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
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
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.set_success), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ReturnStatus.class);
    }


    public void showVintageList(final TextView view, final String id, List<String> vintages) {
        final String[] items = vintages.toArray(new String[vintages.size()]);
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle(getResources().getString(R.string.select_vintage));
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                alertDialog1.dismiss();
                selectVintage(view, id, items[i]);
            }
        });
        alertDialog1 = alertBuilder.create();
        alertDialog1.show();
    }

    //选择年份
    private void selectVintage(final TextView view, String ids, final String vintage) {
        customDialog.show();
        EcatalogVintageRequest request = new EcatalogVintageRequest();
        request.setId(ids);
        request.setVintage(vintage);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_VINTAGE, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
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
                    view.setText(vintage);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ReturnStatus.class);
    }

    //删除报价单产品
    private void showTip(final String ids, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.delete_pro_sure));
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        //设置正面按钮
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteItem(ids, position);
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

    //eventBus回调(删除返回)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BackRefreshOneEvent myEvent) {
        if ("2".equals(myEvent.getMessage())) {
            EventBus.getDefault().post(new BackRefreshOneEvent("1"));
            initData();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * show Dialog 发送弹框
     */
    private void showSendDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View layout = inflater.inflate(R.layout.ecatalog_detail_send_dialog, null);
        TextView textView = layout.findViewById(R.id.ecatalog_send_dialog_text);
        final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        textView.setText(preViewUrl);
        layout.findViewById(R.id.ecatalog_send_dialog_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });
        layout.findViewById(R.id.ecatalog_send_dialog_copy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText(preViewUrl);
                Toast.makeText(EcatalogDetailActivity.this, getResources().getString(R.string.copy_success), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
