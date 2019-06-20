package com.humming.asc.sales.activity.customer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.ro.ad.mfreqeust.AddMfRequestRO;
import com.humming.asc.dp.presentation.vo.AccountForMfVO;
import com.humming.asc.dp.presentation.vo.ItemCostVO;
import com.humming.asc.dp.presentation.vo.MfInfoVO;
import com.humming.asc.dp.presentation.vo.ad.InsertMfRequestVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.approval.CustomerListActivity;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.model.BackRefreshEvent;
import com.humming.asc.sales.service.ApprovalService;
import com.humming.asc.sales.service.ICallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Calendar;

public class CustomerMFRequestActivity extends AbstractActivity implements View.OnClickListener {

    private TextView mItemCodeTextView;
    private LinearLayout mItemCodeContain;
    private LinearLayout mCustomerContain;
    private LinearLayout mStartDateContain;
    private LinearLayout mEndDateContain;
    private TextView mNameCn;
    private TextView mNameEn;
    private TextView mCustomerCode;
    private TextView mCustomerName;
    private TextView mCurrency;
    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mRpContent;
    private EditText mPriceContent;
    private EditText mTradeValue;
    private TextView mRpDiscount;
    private TextView mSkuMargin;
    private TextView mAp;
    private ImageView customerImg;
    private TextView mAgreementNo;
    private TextView mContractMargin;
    private TextView mContractAp;
    private EditText mDescription;
    private TextView mCommit;
    private ApprovalService approvalService;
    private MfInfoVO mfInfoVO;
    private ItemCostVO itemCostVO;
    private AccountForMfVO accountForMfVO;
    private boolean formCustomer = false;
    private String customerCode = "";
    private int year, monthOfYear, dayOfMonth;
    private InputMethodManager imm;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mf_request);
        imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        approvalService = Application.getApprovalService();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        formCustomer = getIntent().getBooleanExtra("from_customer", false);
        customerCode = getIntent().getStringExtra("customer_code");
        //表示从客户那边进入 需要调接口
        if (formCustomer) {
            mNameEn.setVisibility(View.GONE);
            mNameCn.setVisibility(View.GONE);
            initFromCustomer();
            customerImg.setVisibility(View.GONE);
            mCustomerContain.setClickable(false);
        } else {
            mNameEn.setVisibility(View.GONE);
            mNameCn.setVisibility(View.GONE);
            mCustomerContain.setClickable(true);
            customerImg.setVisibility(View.VISIBLE);
        }
        initCurrencyData();
    }

    private void initFromCustomer() {
        approvalService.queryMFInfoFromCustomer(new ICallback<AccountForMfVO>() {

            @Override
            public void onDataReady(AccountForMfVO datas) {
                accountForMfVO = datas;
                mAgreementNo.setText(accountForMfVO.getAgreementNo());
                mCustomerCode.setText(accountForMfVO.getAccountcode());
                mCustomerName.setText(accountForMfVO.getAccountname());
                if (accountForMfVO.getContractMargin() != null && !"".equals(accountForMfVO.getContractMargin())) {
                    mContractMargin.setText(accountForMfVO.getContractMargin() + "%");
                } else {
                    mContractMargin.setText("");
                }
                if (!"".equals(accountForMfVO.getAgreementNo()) && accountForMfVO.getAgreementNo() != null) {
                    if (accountForMfVO.getSalesTarget() == 0 || accountForMfVO.getSupportTarget() == 0) {
                        mContractAp.setText("0.00%");
                    } else {
                        mContractAp.setText(String.format("%.2f", (accountForMfVO.getSupportTarget() / accountForMfVO.getSalesTarget()) * 100) + "%");
                    }
                } else {
                    mContractAp.setText("");
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, customerCode);
    }

    //获取货币信息
    private void initCurrencyData() {
        approvalService.queryCurrency(new ICallback<MfInfoVO>() {

            @Override
            public void onDataReady(MfInfoVO datas) {
                mfInfoVO = datas;
                mCurrency.setText(mfInfoVO.getCurrency());
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    private void initView() {
        customDialog = new CustomDialog(this, "Loading...");
        mItemCodeTextView = (TextView) findViewById(R.id.content_customer_mf_request__item_code);
        mItemCodeContain = (LinearLayout) findViewById(R.id.content_customer_mf_request__item_code_contain);
        mCustomerContain = (LinearLayout) findViewById(R.id.content_customer_mf_request__customer_code_contain);
        mItemCodeContain.setOnClickListener(this);
        mCustomerContain.setOnClickListener(this);
        mStartDateContain = (LinearLayout) findViewById(R.id.content_customer_mf_request__start_date_contain);
        mEndDateContain = (LinearLayout) findViewById(R.id.content_customer_mf_request__end_date_contain);
        mStartDateContain.setOnClickListener(this);
        mEndDateContain.setOnClickListener(this);
        customerImg = findViewById(R.id.content_customer_mf_request__customer_code_click);
        mNameCn = (TextView) findViewById(R.id.content_customer_mf_request__name_cn);
        mNameEn = (TextView) findViewById(R.id.content_customer_mf_request__name_en);
        mCustomerCode = (TextView) findViewById(R.id.content_customer_mf_request__customer_code);
        mCustomerName = (TextView) findViewById(R.id.content_customer_mf_request__customer_name);
        mCurrency = (TextView) findViewById(R.id.content_customer_mf_request__currency);
        mStartDate = (TextView) findViewById(R.id.content_customer_mf_request__start_date);
        mEndDate = (TextView) findViewById(R.id.content_customer_mf_request__end_date);
        mRpContent = (TextView) findViewById(R.id.content_customer_mf_request__rp);
        mPriceContent = (EditText) findViewById(R.id.content_customer_mf_request__price);
        mTradeValue = (EditText) findViewById(R.id.content_customer_mf_request__trade_value);
        mRpDiscount = (TextView) findViewById(R.id.content_customer_mf_request__rp_discount);
        mSkuMargin = (TextView) findViewById(R.id.content_customer_mf_request__sku_margin);
        mAp = (TextView) findViewById(R.id.content_customer_mf_request__ap);
        mAgreementNo = (TextView) findViewById(R.id.content_customer_mf_request__agreement_no);
        mContractMargin = (TextView) findViewById(R.id.content_customer_mf_request__contract_margin);
        mContractAp = (TextView) findViewById(R.id.content_customer_mf_request__contract_ap);
        mDescription = (EditText) findViewById(R.id.content_customer_mf_request__description);
        mCommit = (TextView) findViewById(R.id.content_customer_mf_request__commit);
        mCommit.setOnClickListener(this);
        mDescription.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mDescription.setFocusable(true);
                mDescription.setFocusableInTouchMode(true);
                mDescription.requestFocus();
                return false;
            }
        });

        //初始化虚拟键盘的状态
        mTradeValue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(mPriceContent.getText().toString()) && mPriceContent.getText().toString() != null) {
                    // mTradeValue.setFocusable(true);
                    //mTradeValue.setFocusableInTouchMode(true);
                    //mTradeValue.requestFocus();
                    //    InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    //    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    //    imm.hideSoftInputFromWindow(mTradeValue.getWindowToken(), 0);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerMFRequestActivity.this);
                    builder.setMessage(getResources().getString(R.string.input_price));
                    builder.setTitle(getResources().getString(R.string.tip));
                    builder.setPositiveButton(getResources().getString(R.string.determine), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        });
        mTradeValue.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //跳转页面
                    InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mTradeValue.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!"".equals(mPriceContent.getText().toString()) && mPriceContent.getText().toString() != null) {
                    } else {
                        if (mTradeValue.getText().toString().length() == 0) {
                            mTradeValue.setText("0");
                            setApValue(0 + "");
                        } else {
                            setApValue(mTradeValue.getText().toString());
                        }
                    }
                }
                return false;
            }
        });
        mTradeValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setApValue(mTradeValue.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        mTradeValue.setOnFocusChangeListener(new android.view.View.
                OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    // 此处为得到焦点时的处理内容
                } else {
                    // 此处为失去焦点时的处理内容
                    if ("".equals(mTradeValue.getText().toString()) || mTradeValue.getText().toString() == null) {
                        mTradeValue.setText("0");
                    }
                }
            }
        });
        //初始化虚拟键盘的状态
        mPriceContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(mRpContent.getText().toString()) && mRpContent.getText().toString() != null) {
                    //mPriceContent.setFocusable(true);
                    //mPriceContent.setFocusableInTouchMode(true);
                    //mPriceContent.requestFocus();
                    //   InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    //   imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    //   imm.hideSoftInputFromWindow(mPriceContent.getWindowToken(), 0);
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(CustomerMFRequestActivity.this);
                    builder.setMessage(getResources().getString(R.string.select_item_code));
                    builder.setTitle(getResources().getString(R.string.tip));
                    builder.setPositiveButton(getResources().getString(R.string.determine), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                }
            }
        });
        mPriceContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //跳转页面
                    InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPriceContent.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    if (!"".equals(mRpContent.getText().toString()) && mRpContent.getText().toString() != null) {
                    } else {
                        setRpDiscount(mPriceContent.getText().toString());
                    }
                }
                return false;
            }
        });
        mPriceContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                setRpDiscount(mPriceContent.getText().toString());
                setApValue(mTradeValue.getText().toString());

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    //设置rpDiscount
    public void setRpDiscount(String priceValue) {
        if (itemCostVO != null) {
            if (itemCostVO.getRp() == 0) {
                mRpDiscount.setText(0 + "");
            } else {
                if (!"".equals(priceValue) && priceValue != null) {
                    mRpDiscount.setText(String.format("%.2f", ((1 - (Double.parseDouble(priceValue) / itemCostVO.getRp()))) * 100) + "%");
                } else {
                    mRpDiscount.setText("");
                }
            }
        }

    }

    //设置ApValue
    public void setApValue(String value) {
        String priceValue = "0";
        if ("".equals(value) || value == null) {
            value = "0";
        }
        if ("".equals(mPriceContent.getText().toString()) || mPriceContent.getText().toString() == null) {
            mAp.setText("0");
        } else {
            priceValue = mPriceContent.getText().toString();
            mAp.setText(String.format("%.2f", Double.parseDouble(value) / Double.parseDouble(priceValue) * 100) + "%");
        }
        if (itemCostVO != null) {
            if ("0".equals(priceValue)) {
                mSkuMargin.setText("");
            } else {
                BigDecimal bg = new BigDecimal((1 - ((itemCostVO.getCost() + Double.parseDouble(value)) / (Double.parseDouble(priceValue) / mfInfoVO.getCurrencyValue()))) * 100
                ).setScale(2, RoundingMode.UP);
                mSkuMargin.setText(bg.doubleValue() + "%");
            }

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //选择ItemCoDe
            case R.id.content_customer_mf_request__item_code_contain:
                // TODO 18/06/22
                Intent intent = new Intent(CustomerMFRequestActivity.this, CustomerMFItemCodeListActivity.class);
                startActivity(intent);
                break;
            case R.id.content_customer_mf_request__customer_code_contain:
                // TODO 18/06/22
                Intent intent1 = new Intent(CustomerMFRequestActivity.this, CustomerListActivity.class);
                startActivity(intent1);
                break;
            case R.id.content_customer_mf_request__start_date_contain:
                // TODO 18/06/22
                showPopWindowDatePicker(true);
                break;
            case R.id.content_customer_mf_request__end_date_contain:
                // TODO 18/06/22
                showPopWindowDatePicker(false);
                break;
            case R.id.content_customer_mf_request__commit:
                addMf();
                break;
            default:
                break;
        }
    }

    //eventBus回调(Item Cost 返回的回调)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(ItemCostVO itemCostVOs) {
        itemCostVO = itemCostVOs;
        mItemCodeTextView.setText(itemCostVO.getItemCode());
        mNameEn.setVisibility(View.VISIBLE);
        mNameCn.setVisibility(View.VISIBLE);
        mNameCn.setText(itemCostVO.getNameCN());
        mNameEn.setText(itemCostVO.getNameEN());
        mRpContent.setText(itemCostVO.getRp() + "");

        if (mPriceContent.getText() != null && !"".equals(mPriceContent.getText().toString())) {
            setRpDiscount(mPriceContent.getText().toString());
        }
        if (mPriceContent.getText() != null && !"".equals(mPriceContent.getText().toString()) && mTradeValue.getText() != null && !"".equals(mTradeValue.getText().toString())) {
            setApValue(mTradeValue.getText().toString());
        }
    }

    //eventBus回调(Customer 返回的回调)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(AccountForMfVO accountForMfVOs) {
        accountForMfVO = accountForMfVOs;
        mAgreementNo.setText(accountForMfVO.getAgreementNo());
        mCustomerCode.setText(accountForMfVO.getAccountcode());
        mCustomerName.setText(accountForMfVO.getAccountname());
        if (accountForMfVO.getContractMargin() != null) {
            mContractMargin.setText(accountForMfVO.getContractMargin() + "%");
        } else {
            mContractMargin.setText("");
        }
        if (!"".equals(accountForMfVO.getAgreementNo()) && accountForMfVO.getAgreementNo() != null) {
            if (accountForMfVO.getSalesTarget() == 0 || accountForMfVO.getSupportTarget() == 0) {
                mContractAp.setText("0.00%");
            } else {
                mContractAp.setText(String.format("%.2f", (accountForMfVO.getSupportTarget() / accountForMfVO.getSalesTarget()) * 100) + "%");
            }
        } else {
            mContractAp.setText("");
        }
    }

    //添加MF
    private void addMf() {
        if (!judgeValue()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage(getResources().getString(R.string.Incomplete_information));
            builder.setTitle(getResources().getString(R.string.tip));
            builder.setPositiveButton(getResources().getString(R.string.determine), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            builder.create().show();
        } else {
            customDialog.show();
            AddMfRequestRO addMfRequestRO = new AddMfRequestRO();
            addMfRequestRO.setCustomerRowId(accountForMfVO.getAccountRowId());
            addMfRequestRO.setDescription(mDescription.getText().toString());
            addMfRequestRO.setEndDate(mEndDate.getText().toString());
            addMfRequestRO.setStartDate(mStartDate.getText().toString());
            addMfRequestRO.setItemCode(itemCostVO.getItemCode());
            addMfRequestRO.setPrice(Double.parseDouble(mPriceContent.getText().toString()));
            if (!"".equals(mTradeValue.getText().toString()) && mTradeValue.getText().toString() != null) {
                addMfRequestRO.setTradeApValue(Double.parseDouble(mTradeValue.getText().toString()));
            } else {
                addMfRequestRO.setTradeApValue(0);
            }
            addMfRequestRO.setItemRowId(itemCostVO.getRowId());
            addMfRequestRO.setRp(itemCostVO.getRp());
            approvalService.addMf(new ICallback<InsertMfRequestVO>() {
                @Override
                public void onDataReady(InsertMfRequestVO data) {
                    //添加成功
                    customDialog.dismiss();
                    Toast.makeText(CustomerMFRequestActivity.this, getResources().getString(R.string.submitted_successfully), Toast.LENGTH_SHORT).show();
                    showSubmitDialog(data.getMfRequestRowId());
                    //EventBus.getDefault().post(new BackRefreshEvent("1"));
                    // finish();
                }

                @Override
                public void onError(Throwable throwable) {
                    Toast.makeText(CustomerMFRequestActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                    customDialog.dismiss();
                }
            }, addMfRequestRO);
        }
    }

    private boolean judgeValue() {
        boolean result = true;
        if (accountForMfVO == null) {
            result = false;
        } else if (itemCostVO == null) {
            result = false;
        } else if ("".equals(mStartDate.getText().toString())) {
            result = false;
        } else if ("".equals(mEndDate.getText().toString())) {
            result = false;
        } else if ("".equals(mPriceContent.getText().toString())) {
            result = false;
        }
        return result;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_task_add, menu);
        return true;
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
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    /**
     * show Dialog 友情提示dialog
     */
    private void showSubmitDialog(final String requestRowId) {
        LayoutInflater inflater = LayoutInflater.from(CustomerMFRequestActivity.this);
        View layout = inflater.inflate(R.layout.mf_submit_alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(CustomerMFRequestActivity.this).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        title.setText(getResources().getString(R.string.toso) + "?");
        layout.findViewById(R.id.approval_commit_cancel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
                finish();
                EventBus.getDefault().post(new BackRefreshEvent("1"));
            }
        });
        layout.findViewById(R.id.approval_commit_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvalService.updateStage(new ICallback<ResultVO>() {
                    @Override
                    public void onDataReady(ResultVO data) {
                        //添加成功
                        Toast.makeText(CustomerMFRequestActivity.this, getResources().getString(R.string.submitted_successfully), Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new BackRefreshEvent("1"));
                        builder.dismiss();
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(CustomerMFRequestActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                        builder.dismiss();
                    }
                }, requestRowId);

            }
        });
    }


    //popwindow显示
    private void showPopWindowDatePicker(final boolean startOrEndDate) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_popup_datepicker, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(findViewById(R.id.mf_request_contain),
                Gravity.BOTTOM, 0, 0);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        if (startOrEndDate) {
            if (!"".equals(mStartDate.getText().toString())) {
                String times = mStartDate.getText().toString();
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
        } else {
            if (!"".equals(mEndDate.getText().toString())) {
                String times = mEndDate.getText().toString();
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
                monthOfYear = (Integer.parseInt(month) - 1);
                dayOfMonth = Integer.parseInt(day);
            } else {
                year = calendar.get(Calendar.YEAR);
                monthOfYear = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }
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
                date = year + "-" + month + "-" + day + " ";

                if (startOrEndDate) {
                    mStartDate.setText(date);
                    if (!"".equals(mEndDate.getText())) {
                        if (mStartDate.getText().toString().compareTo(mEndDate.getText().toString()) > 0) {
                            mEndDate.setText("");
                            Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.error_time), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    mEndDate.setText(date);
                    if (!"".equals(mEndDate.getText())) {
                        if (mStartDate.getText().toString().compareTo(mEndDate.getText().toString()) > 0) {
                            mEndDate.setText("");
                            Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.error_time), Toast.LENGTH_SHORT).show();
                        }
                    }
                }


                window.dismiss();
            }
        });
    }
}
