package com.humming.asc.sales.activity.approval;

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

import com.humming.asc.dp.presentation.ro.ad.payment.AuditPaymentRO;
import com.humming.asc.dp.presentation.vo.ad.ApprovalHisVO;
import com.humming.asc.dp.presentation.vo.ad.payment.PaymentBaseInfoVO;
import com.humming.asc.dp.presentation.vo.ad.payment.PaymentDetailVO;
import com.humming.asc.dp.presentation.vo.ad.payment.QueryPaymentDetailResultVO;
import com.humming.asc.dp.presentation.vo.ad.payment.QueryPaymentDetailVO;
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

public class PaymentInfoActivity extends AbstractActivity implements View.OnClickListener {

    private TextView mPaymentNoTv;
    private TextView mStatusTv;
    private TextView mCustomerCodeTv;
    private TextView mCustomerTv;
    private TextView mCurrencyTv;
    private TextView mExpenseCategoryTv;
    private TextView mBrandCategoryTv;
    private TextView mPaymentStatusTv;
    private TextView mTypeTv;
    private TextView mAgreementTv;
    private TextView mSupportTv;
    private TextView mPromitionTv;
    private TextView mIasCodeTv;
    private TextView mNetSalesTv;
    private TextView mDlTv;
    private TextView mPaymentByTv;
    private TextView mPaymentAccountTv;
    private TextView mActuralAmountTv;
    private TextView mApplicantIdTv;
    private TextView mApplicantNameTv;
    private TextView mDetailTypeTv;
    private TextView mDetailPayAccountTv;
    private TextView mDetailAmountTv;
    private TextView mDetailBrandTv;
    private TextView mDetailVendorTv;
    private TextView mDetailDescTv;
    private TextView approvalBt;
    private TextView disApprovalBt;
    private TextView undeterminedBt;
    private LinearLayout approvalBtContain;
    private LinearLayout mApprovalHistoryContain;
    private ApprovalService approvalService;
    private String rowId = "";
    private int type = 0;
    private QueryPaymentDetailVO data;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_payment_detail);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        approvalService = Application.getApprovalService();
        rowId = getIntent().getStringExtra("payment_info_id");
        type = getIntent().getIntExtra("payment_type", 0);
        if (type == 1) {//显示通过和驳回按钮
            approvalBtContain.setVisibility(View.VISIBLE);
        } else {
            approvalBtContain.setVisibility(View.GONE);
        }
        initData();
    }

    //初始化数据
    private void initData() {
        approvalService.paymentDetail(new ICallback<QueryPaymentDetailResultVO>() {

            @Override
            public void onDataReady(QueryPaymentDetailResultVO datas) {
                data = datas.getData();
                PaymentBaseInfoVO paymentBaseInfoVO = data.getPaymentBaseInfoVO();
                PaymentDetailVO paymentDetailVO = data.getPaymentDetailVO();
                mPaymentNoTv.setText(paymentBaseInfoVO.getPaymentNumber());
                mStatusTv.setText(paymentBaseInfoVO.getStatus());
                mCustomerCodeTv.setText(paymentBaseInfoVO.getCustomerCode());
                mCustomerTv.setText(paymentBaseInfoVO.getCustomer());
                mCurrencyTv.setText(paymentBaseInfoVO.getCurrency());
                mExpenseCategoryTv.setText(paymentBaseInfoVO.getExpenseCategory());
                mBrandCategoryTv.setText(paymentBaseInfoVO.getBrandCategory());
                mPaymentStatusTv.setText(paymentBaseInfoVO.getPaymentStatus());
                mTypeTv.setText(paymentBaseInfoVO.getType());
                mAgreementTv.setText(paymentBaseInfoVO.getAgreement());
                if (paymentBaseInfoVO.getSupport() != null && !"".equals(paymentBaseInfoVO.getSupport())) {
                    mSupportTv.setVisibility(View.VISIBLE);
                    mSupportTv.setText(paymentBaseInfoVO.getSupport());

                } else {
                    mSupportTv.setVisibility(View.GONE);
                }
                mPromitionTv.setText(paymentBaseInfoVO.getPromotion());
                mIasCodeTv.setText(paymentBaseInfoVO.getIas());
                if (paymentBaseInfoVO.getDc() != null && !"".equals(paymentBaseInfoVO.getDc())) {
                    mNetSalesTv.setText(paymentBaseInfoVO.getDc() + "%");
                }
                if (paymentBaseInfoVO.getDl() != null && !"".equals(paymentBaseInfoVO.getDl())) {
                    mDlTv.setText(paymentBaseInfoVO.getDl() + "%");
                }
                mActuralAmountTv.setText(paymentBaseInfoVO.getCurrencySymbol() + String.format("%.2f",paymentBaseInfoVO.getActualAmount()) + "");
                mDetailAmountTv.setText(paymentBaseInfoVO.getCurrencySymbol() + String.format("%.2f",paymentDetailVO.getAmount()) + "");
                mPaymentByTv.setText(paymentBaseInfoVO.getPaymentBy());
                mPaymentAccountTv.setText(paymentBaseInfoVO.getPaymentAccount());
                mApplicantIdTv.setText(paymentBaseInfoVO.getApplicationId());
                mApplicantNameTv.setText(paymentBaseInfoVO.getApplicationName());

                mDetailTypeTv.setText(paymentDetailVO.getType());
                mDetailPayAccountTv.setText(paymentDetailVO.getPaymentAccount());
                mDetailBrandTv.setText(paymentDetailVO.getBrand());
                mDetailVendorTv.setText(paymentDetailVO.getVendor());
                mDetailDescTv.setText(paymentDetailVO.getDescription());

                for (int i = 0; i < data.getApprovalHisList().size(); i++) {
                    mApprovalHistoryContain.addView(addHistoryView(data.getApprovalHisList().get(i)));
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, rowId, type + "");
    }

    private void initView() {
        customDialog = new CustomDialog(this, "Loading...");
        mPaymentNoTv = (TextView) findViewById(R.id.content_payment_detail__payment_no);
        mStatusTv = (TextView) findViewById(R.id.content_payment_detail__status);
        mCustomerCodeTv = (TextView) findViewById(R.id.content_payment_detail__customer_code);
        mCustomerTv = (TextView) findViewById(R.id.content_payment_detail__customer);
        mCurrencyTv = (TextView) findViewById(R.id.content_payment_detail__currency);
        mExpenseCategoryTv = (TextView) findViewById(R.id.content_payment_detail__expense_category);
        mBrandCategoryTv = (TextView) findViewById(R.id.content_payment_detail__brand_category);
        mPaymentStatusTv = (TextView) findViewById(R.id.content_payment_detail__payment_status);
        mTypeTv = (TextView) findViewById(R.id.content_payment_detail__type);
        mAgreementTv = (TextView) findViewById(R.id.content_payment_detail__agreement);
        mSupportTv = (TextView) findViewById(R.id.content_payment_detail__support);
        mPromitionTv = (TextView) findViewById(R.id.content_payment_detail__promition);
        mIasCodeTv = (TextView) findViewById(R.id.content_payment_detail__ias_code);
        mNetSalesTv = (TextView) findViewById(R.id.content_payment_detail__net_sales);
        mDlTv = (TextView) findViewById(R.id.content_payment_detail__dl);
        mPaymentByTv = (TextView) findViewById(R.id.content_payment_detail__payment_by);
        mPaymentAccountTv = (TextView) findViewById(R.id.content_payment_detail__payment_account);
        mActuralAmountTv = (TextView) findViewById(R.id.content_payment_detail__actural_amount);
        mApplicantIdTv = (TextView) findViewById(R.id.content_payment_detail__applicant_id);
        mApplicantNameTv = (TextView) findViewById(R.id.content_payment_detail__applicant_name);
        mDetailTypeTv = (TextView) findViewById(R.id.content_payment_detail__detail_type);
        mDetailPayAccountTv = (TextView) findViewById(R.id.content_payment_detail__detail_pay_account);
        mDetailAmountTv = (TextView) findViewById(R.id.content_payment_detail__detail_amount);
        mDetailBrandTv = (TextView) findViewById(R.id.content_payment_detail__detail_brand);
        mDetailVendorTv = (TextView) findViewById(R.id.content_payment_detail__detail_vendor);
        mDetailDescTv = (TextView) findViewById(R.id.content_payment_detail__detail_desc);
        mApprovalHistoryContain = (LinearLayout) findViewById(R.id.content_approval_history__contain);

        approvalBtContain = findViewById(R.id.content_payment_approval_bt_contain);
        approvalBt = findViewById(R.id.content_payment_approval_approval);
        disApprovalBt = findViewById(R.id.content_payment_approval_disapproval);
        undeterminedBt = findViewById(R.id.content_payment_approval_undetermined);
        undeterminedBt.setOnClickListener(this);
        approvalBt.setOnClickListener(this);
        disApprovalBt.setOnClickListener(this);
    }


    //动态添加sample approval history
    private View addHistoryView(ApprovalHisVO item) {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.list_item_approval_history, null, false);
        TextView mStatusTv1 = (TextView) view.findViewById(R.id.tv_approval_history__status);
        TextView mApprovarPositionTv = (TextView) view.findViewById(R.id.tv_approval_history__approvar_position);
        TextView mApprovarNameTv = (TextView) view.findViewById(R.id.tv_approval_history__approvar_name);
        TextView mPrimaryApprovarNameTv = (TextView) view.findViewById(R.id.tv_approval_history__primary_approvar_name);
        TextView mWineRecoveryTv = (TextView) view.findViewById(R.id.tv_approval_history__wine_recovery);
        TextView mDateTv = (TextView) view.findViewById(R.id.tv_approval_history__date);
        TextView mDescTv1 = (TextView) view.findViewById(R.id.tv_approval_history__desc);

        //赋值
        mStatusTv1.setText(item.getStatus());
        mApprovarPositionTv.setText(item.getApproverPosition());
        mApprovarNameTv.setText(item.getApproverName());
        mPrimaryApprovarNameTv.setText(item.getPrimaryApproverName());
        mWineRecoveryTv.setText(item.getWineryRecovery());
        mDateTv.setText(item.getDate());
        mDescTv1.setText(item.getDescription());
        return view;
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
                //  if (type == 2 || type == 4) {
                EventBus.getDefault().post(new BackRefreshOneEvent("1"));
                //  }
                finish();
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
            case R.id.content_payment_approval_approval:
                showConfigDialog(1);
                break;
            //驳回
            case R.id.content_payment_approval_disapproval:
                showConfigDialog(0);
                break;
            //待定
            case R.id.content_payment_approval_undetermined:
                showConfigDialog(2);
                break;
        }
    }

    public void auditPaymentrequest(int isPass, String wine, String desc) {
        customDialog.show();
        AuditPaymentRO requestRO = new AuditPaymentRO();
        requestRO.setPaymentRowId(rowId);
        requestRO.setIsPass(isPass);//1通过  0不通过
        requestRO.setComments(desc);
        requestRO.setWineryRecovery(wine);
        approvalService.auditPaymentRequest(new ICallback<ResultVO>() {
            @Override
            public void onDataReady(ResultVO data) {
                //审核成功
                customDialog.dismiss();
                Toast.makeText(PaymentInfoActivity.this, getResources().getString(R.string.submitted_successfully), Toast.LENGTH_SHORT).show();
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
        final EditText descEd = layout.findViewById(R.id.approval_commit_desc);
        final EditText wineryRecoveryEd = layout.findViewById(R.id.approval_commit_winery_recovery);
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
        wineryRecoveryEd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                wineryRecoveryEd.setFocusable(true);
                wineryRecoveryEd.setFocusableInTouchMode(true);
                wineryRecoveryEd.requestFocus();
                InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(wineryRecoveryEd.getWindowToken(), 0);
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
                auditPaymentrequest(isPass, wineryRecoveryEd.getText().toString(), descEd.getText().toString());
                builder.dismiss();
            }
        });
    }
}
