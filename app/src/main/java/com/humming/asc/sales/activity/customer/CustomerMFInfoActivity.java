package com.humming.asc.sales.activity.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.ro.ad.mfreqeust.AuditMfRequestRO;
import com.humming.asc.dp.presentation.vo.ad.ApprovalHisVO;
import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestDetailResultVO;
import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestDetailVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.model.BackRefreshEvent;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.service.ApprovalService;
import com.humming.asc.sales.service.ICallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class CustomerMFInfoActivity extends AbstractActivity implements View.OnClickListener {

    private TextView mItemCode;
    private TextView mNameCn;
    private TextView mNameEn;
    private TextView mRequestNo;
    private TextView mCustomerCode;
    private TextView mCustomerName;
    private TextView mCurrency;
    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mRp;
    private TextView mPrice;
    private TextView mTradeValue;
    private TextView mRpDiscount;
    private TextView mSkuMargin;
    private TextView mAp;
    private TextView mAgreementNo;
    private TextView mContractMargin;
    private TextView mContractAp;
    private TextView mApplicantName;
    private TextView mDescription;
    private TextView mStatus;
    private TextView approvalBt;
    private TextView disApprovalBt;
    private TextView undeterminedBt;
    private LinearLayout approvalBtContain;
    private LinearLayout mApprovalHistoryContain;
    private ApprovalService approvalService;
    private QueryMfRequestDetailVO data;
    private String rowId = "";
    private int type = 0;
    private boolean isEdit = false;
    private LinearLayout applySoLayout;
    private TextView applyTosoTv;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mf_detail);
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        approvalService = Application.getApprovalService();
        rowId = getIntent().getStringExtra("mf_info_id");
        type = getIntent().getIntExtra("mf_type", 0);
        isEdit = getIntent().getBooleanExtra("is_edit", false);
        if (type == 1) {//显示通过和驳回按钮
            approvalBtContain.setVisibility(View.VISIBLE);
        } else {
            approvalBtContain.setVisibility(View.GONE);
        }
        initData();

    }

    private void initData() {
        approvalService.mfDetail(new ICallback<QueryMfRequestDetailResultVO>() {
            @Override
            public void onDataReady(QueryMfRequestDetailResultVO datas) {
                data = datas.getData();
                mItemCode.setText(data.getItemCode());
                mNameCn.setText(data.getItemName());
                mNameEn.setText(data.getItemFrgnName());
                mRequestNo.setText(data.getRequestNumber());
                mCustomerCode.setText(data.getCustomerCode());
                mCustomerName.setText(data.getCustomerName());
                mCurrency.setText(data.getCurrency());
                mStartDate.setText(data.getStartDate());
                mEndDate.setText(data.getEndDate());
                mPrice.setText(data.getCurrencySymbol() + String.format("%.2f", data.getPrice()) + "");
                mRp.setText(data.getCurrencySymbol() + String.format("%.2f", data.getRp()) + "");
                mTradeValue.setText(data.getTradeApValue());
                mRpDiscount.setText(data.getRpDiscount() + "%");
                mSkuMargin.setText(data.getSkuMargin() + "%");
                mAp.setText(data.getApValue() + "%");
                mAgreementNo.setText(data.getAgreementNo());
                if (data.getContractMargin() != null && !"".equals(data.getContractMargin())) {
                    mContractMargin.setText(data.getContractMargin());
                }
                if (data.getContractApValue() != null && !"".equals(data.getContractApValue())) {
                    mContractAp.setText(data.getContractApValue());
                }
                mApplicantName.setText(data.getApplicantName());
                mDescription.setText(data.getDescription());
                mStatus.setText(data.getStatus());
                if ("Not Submitted".equals(data.getStatus()) && "Sales".equals(data.getStage())) {
                    applySoLayout.setVisibility(View.VISIBLE);
                }

                for (int i = 0; i < data.getApprovalHisList().size(); i++) {
                    mApprovalHistoryContain.addView(addHistoryView(data.getApprovalHisList().get(i)));
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, rowId, type + "");
    }


    //eventBus回调(修改mf返回刷新)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(BackRefreshEvent backRefreshEvent) {
        if ("0".equals(backRefreshEvent.getMessage())) {
            EventBus.getDefault().post(new BackRefreshEvent("1"));
            finish();
        } else {
            initData();
        }
    }

    private void initView() {
        customDialog = new CustomDialog(this, "Loading...");
        mItemCode = (TextView) findViewById(R.id.mf_item_code);
        mNameCn = (TextView) findViewById(R.id.mf_name_cn);
        mNameEn = (TextView) findViewById(R.id.mf_name_en);
        mStatus = (TextView) findViewById(R.id.content_mf_detail__status);
        mRequestNo = (TextView) findViewById(R.id.content_mf_detail__request_no);
        mCustomerCode = (TextView) findViewById(R.id.content_mf_detail__customer_code);
        mCustomerName = (TextView) findViewById(R.id.content_mf_detail__customer_name);
        mCurrency = (TextView) findViewById(R.id.content_mf_detail__currency);
        mStartDate = (TextView) findViewById(R.id.content_mf_detail__start_date);
        mEndDate = (TextView) findViewById(R.id.content_mf_detail__end_date);
        mRp = (TextView) findViewById(R.id.content_mf_detail__rp);
        mPrice = (TextView) findViewById(R.id.content_mf_detail__price);
        mTradeValue = (TextView) findViewById(R.id.content_mf_detail__trade_value);
        mRpDiscount = (TextView) findViewById(R.id.content_mf_detail__rp_discount);
        mSkuMargin = (TextView) findViewById(R.id.content_mf_detail__sku_margin);
        mAp = (TextView) findViewById(R.id.content_mf_detail__ap);
        mAgreementNo = (TextView) findViewById(R.id.content_mf_detail__agreement_no);
        mContractMargin = (TextView) findViewById(R.id.content_mf_detail__contract_margin);
        mContractAp = (TextView) findViewById(R.id.content_mf_detail__contract_ap);
        mApplicantName = (TextView) findViewById(R.id.content_mf__applicant_name);
        mDescription = (TextView) findViewById(R.id.content_mf__description);
        mApprovalHistoryContain = (LinearLayout) findViewById(R.id.content_mf_approval_history__contain);
        approvalBtContain = findViewById(R.id.content_mf_approval_bt_contain);
        applySoLayout = findViewById(R.id.content_mf_approval_to_so);
        applyTosoTv = findViewById(R.id.content_mf_approval_to_so_bt);
        approvalBt = findViewById(R.id.content_mf_approval_approval);
        disApprovalBt = findViewById(R.id.content_mf_approval_disapproval);

        undeterminedBt = findViewById(R.id.content_mf_approval_undetermined);
        undeterminedBt.setOnClickListener(this);
        approvalBt.setOnClickListener(this);
        disApprovalBt.setOnClickListener(this);
        applyTosoTv.setOnClickListener(this);
    }

    //动态添加mf approval history
    private View addHistoryView(ApprovalHisVO item) {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.list_item_approval_history, null, false);
        TextView mStatusTv1 = (TextView) view.findViewById(R.id.tv_approval_history__status);
        TextView mApprovarPositionTv = (TextView) view.findViewById(R.id.tv_approval_history__approvar_position);
        TextView mApprovarNameTv = (TextView) view.findViewById(R.id.tv_approval_history__approvar_name);
        TextView mPrimaryApprovarNameTv = (TextView) view.findViewById(R.id.tv_approval_history__primary_approvar_name);
        TextView mWineRecoveryTv = (TextView) view.findViewById(R.id.tv_approval_history__wine_recovery);
        TextView mDateTv = (TextView) view.findViewById(R.id.tv_approval_history__date);
        LinearLayout wineRecoveryLayout = view.findViewById(R.id.tv_approval_history__wine_recovery_contain);
        TextView mDescTv1 = (TextView) view.findViewById(R.id.tv_approval_history__desc);
        wineRecoveryLayout.setVisibility(View.GONE);
        //赋值
        mStatusTv1.setText(item.getStatus() + "");
        mApprovarPositionTv.setText(item.getApproverPosition());
        mApprovarNameTv.setText(item.getApproverName());
        mPrimaryApprovarNameTv.setText(item.getPrimaryApproverName());
        mWineRecoveryTv.setText(item.getWineryRecovery());
        mDateTv.setText(item.getDate());
        if (item.getDescription() != null) {
            mDescTv1.setText(item.getDescription());
        }

        return view;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (isEdit) {//是否可以编辑
            getMenuInflater().inflate(R.menu.menu_mf_edit, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                //if (type == 2 || type == 4) {
                EventBus.getDefault().post(new BackRefreshOneEvent("1"));
                //}
                finish();
                break;
            case R.id.mf_edit://进入编辑页面
                EventBus.getDefault().postSticky(data);
                Intent intent = new Intent(CustomerMFInfoActivity.this, CustomerMFEditActivity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            EventBus.getDefault().post(new BackRefreshOneEvent("1"));
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //通过
            case R.id.content_mf_approval_approval:
                showConfigDialog(1);
                break;
            //驳回
            case R.id.content_mf_approval_disapproval:
                showConfigDialog(0);
                break;
            //待定
            case R.id.content_mf_approval_undetermined:
                showConfigDialog(2);
                break;
            //待定
            case R.id.content_mf_approval_to_so_bt:
                showSubmitDialog(data.getRowId());
                break;
        }
    }


    private void showSubmitDialog(final String requestRowId) {
        LayoutInflater inflater = LayoutInflater.from(CustomerMFInfoActivity.this);
        View layout = inflater.inflate(R.layout.mf_submit_alert_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(CustomerMFInfoActivity.this).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        TextView title = (TextView) layout.findViewById(R.id.alert_dialog_title);
        title.setText(getResources().getString(R.string.toso) + "?");
        layout.findViewById(R.id.approval_commit_cancel1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder.dismiss();
            }
        });
        layout.findViewById(R.id.approval_commit_commit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approvalService.updateStage(new ICallback<ResultVO>() {
                    @Override
                    public void onDataReady(ResultVO data) {
                        //添加成功
                        Toast.makeText(CustomerMFInfoActivity.this, getResources().getString(R.string.submitted_successfully), Toast.LENGTH_SHORT).show();
                        EventBus.getDefault().post(new BackRefreshEvent("1"));
                        builder.dismiss();
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        Toast.makeText(CustomerMFInfoActivity.this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        builder.dismiss();
                    }
                }, requestRowId);

            }
        });
    }

    //审核请求
    public void auditMfrequest(int isPass, String desc) {
        customDialog.show();
        AuditMfRequestRO requestRO = new AuditMfRequestRO();
        requestRO.setMfRequestRowId(rowId);
        requestRO.setIsPass(isPass);//1通过  0不通过   2待定
        requestRO.setComments(desc);
        approvalService.auditMfrequest(new ICallback<ResultVO>() {
            @Override
            public void onDataReady(ResultVO data) {
                //审核成功
                customDialog.dismiss();
                Toast.makeText(CustomerMFInfoActivity.this, getResources().getString(R.string.submitted_successfully), Toast.LENGTH_SHORT).show();
                EventBus.getDefault().post(new BackRefreshEvent("1"));
                finish();
            }

            @Override
            public void onError(Throwable throwable) {
                customDialog.dismiss();
            }
        }, requestRO);
    }

    private void showConfigDialog(final int isPass) {
        LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        View layout = inflater.inflate(R.layout.approval_request_commit_dialog, null);
        final AlertDialog builder = new AlertDialog.Builder(Application.getInstance().getCurrentActivity()).create();
        builder.setView(layout);
        builder.setCancelable(false);
        builder.show();
        View wineryLayout = layout.findViewById(R.id.approval_commit_winery_recovery_layout);
        wineryLayout.setVisibility(View.GONE);
        final EditText descEd = layout.findViewById(R.id.approval_commit_desc);
        descEd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                descEd.setFocusable(true);
                descEd.setFocusableInTouchMode(true);
                descEd.requestFocus();
                InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(descEd.getWindowToken(), 0);
                return false;
            }
        });
        ImageView cancel1 = layout.findViewById(R.id.approval_commit_cancel);
        TextView cancel = layout.findViewById(R.id.approval_commit_cancel1);
        TextView commit = layout.findViewById(R.id.approval_commit_commit);
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.dismiss();
            }
        });
        commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auditMfrequest(isPass, descEd.getText().toString());
                builder.dismiss();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
