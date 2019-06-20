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

import com.humming.asc.dp.presentation.ro.ad.sample.AuditSampleRO;
import com.humming.asc.dp.presentation.vo.ad.ApprovalHisVO;
import com.humming.asc.dp.presentation.vo.ad.sample.QuerySampleDetailResultVO;
import com.humming.asc.dp.presentation.vo.ad.sample.QuerySampleDetailVO;
import com.humming.asc.dp.presentation.vo.ad.sample.SampleDetailVO;
import com.humming.asc.dp.presentation.vo.ad.sample.SampleHeadVO;
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

public class SampleInfoActivity extends AbstractActivity implements View.OnClickListener {

    private TextView mSampleNoTv;
    private TextView mStatusTv;
    private TextView mCustomerCodeTv;
    private TextView mCustomerTv;
    private TextView mCurrencyTv;
    private TextView mExpenseCategoryTv;
    private TextView mTypeTv;
    private TextView mTypeGroupTv;
    private TextView mDescTv;
    private TextView mPromitionTv;
    private TextView mBrandCategoryTv;
    private TextView mBrandToChangeTv;
    private TextView mAgreementTv;
    private TextView mSupportTv;
    private TextView mIasCodeTv;
    private TextView mNetSalesTv;
    private TextView mlDlTv;
    private TextView mReasonTv;
    private TextView mApplicantIdTv;
    private TextView mApplicantNameTv;
    private TextView mTotalAmountTv;
    private TextView approvalBt;
    private TextView disApprovalBt;
    private TextView undeterminedBt;
    private LinearLayout mSampleDetailContain;
    private LinearLayout approvalBtContain;
    private LinearLayout mApprovalHistoryContain;
    private ApprovalService approvalService;
    private String rowId = "";
    private int type = 0;
    private QuerySampleDetailVO data;
    private String currency = "$";
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_sample_detail);
        initView();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        approvalService = Application.getApprovalService();
        rowId = getIntent().getStringExtra("sample_info_id");
        type = getIntent().getIntExtra("sample_type", 0);
        if (type == 1) {//显示通过和驳回按钮
            approvalBtContain.setVisibility(View.VISIBLE);
        } else {
            approvalBtContain.setVisibility(View.GONE);
        }
        initData();
    }

    //获取sample数据
    private void initData() {
        approvalService.sampleDetail(new ICallback<QuerySampleDetailResultVO>() {

            @Override
            public void onDataReady(QuerySampleDetailResultVO datas) {
                data = datas.getData();
                SampleHeadVO sampleHeadVO = data.getSampleHeadVO();
                mSampleNoTv.setText(sampleHeadVO.getSampleNumber());
                mStatusTv.setText(sampleHeadVO.getStatus());
                mCustomerCodeTv.setText(sampleHeadVO.getCustomerCode());
                mCustomerTv.setText(sampleHeadVO.getCustomer());
                mCurrencyTv.setText(sampleHeadVO.getCurrency());
                mExpenseCategoryTv.setText(sampleHeadVO.getExpenseCategory());
                mTypeTv.setText(sampleHeadVO.getType());
                mTypeGroupTv.setText(sampleHeadVO.getTypeGroup());
                mDescTv.setText(sampleHeadVO.getDescription());
                mPromitionTv.setText(sampleHeadVO.getPromotion());
                mBrandCategoryTv.setText(sampleHeadVO.getBrandCategory());
                mBrandToChangeTv.setText(sampleHeadVO.getBrandToCharge());
                mAgreementTv.setText(sampleHeadVO.getAgreement());
                if (sampleHeadVO.getSupport() != null && !"".equals(sampleHeadVO.getSupport())) {
                    mSupportTv.setVisibility(View.VISIBLE);
                    mSupportTv.setText(sampleHeadVO.getSupport());
                } else {
                    mSupportTv.setVisibility(View.GONE);
                }
                mIasCodeTv.setText(sampleHeadVO.getIas());
                if (sampleHeadVO.getDc() != null && !"".equals(sampleHeadVO.getDc())) {
                    mNetSalesTv.setText(sampleHeadVO.getDc() + "%");
                }
                if (sampleHeadVO.getDl() != null && !"".equals(sampleHeadVO.getDl())) {
                    mlDlTv.setText(sampleHeadVO.getDl() + "%");
                }
                if (sampleHeadVO.getReason() != null && !"".equals(sampleHeadVO.getReason())) {
                    mReasonTv.setVisibility(View.VISIBLE);
                    mReasonTv.setText(sampleHeadVO.getReason());
                } else {
                    mReasonTv.setVisibility(View.GONE);
                }
                currency = sampleHeadVO.getCurrencySymbol();
                mTotalAmountTv.setText(sampleHeadVO.getCurrencySymbol() + String.format("%.2f",sampleHeadVO.getTotalAmount()) + "");
                mApplicantIdTv.setText(sampleHeadVO.getApplicationId());
                mApplicantNameTv.setText(sampleHeadVO.getApplicationName());

                for (int i = 0; i < data.getApprovalHisList().size(); i++) {
                    mApprovalHistoryContain.addView(addHistoryView(data.getApprovalHisList().get(i)));
                }
                for (int i = 0; i < data.getSampleDetailList().size(); i++) {
                    mSampleDetailContain.addView(addDetailView(data.getSampleDetailList().get(i)));
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, rowId, type + "");
    }

    private void initView() {
        customDialog = new CustomDialog(this, "Loading...");
        mSampleNoTv = (TextView) findViewById(R.id.content_sample_detail__sample_no);
        mStatusTv = (TextView) findViewById(R.id.content_sample_detail__status);
        mCustomerCodeTv = (TextView) findViewById(R.id.content_sample_detail__customer_code);
        mCustomerTv = (TextView) findViewById(R.id.content_sample_detail__customer);
        mCurrencyTv = (TextView) findViewById(R.id.content_sample_detail__currency);
        mExpenseCategoryTv = (TextView) findViewById(R.id.content_sample_detail__expense_category);
        mTypeTv = (TextView) findViewById(R.id.content_sample_detail__type);
        mTypeGroupTv = (TextView) findViewById(R.id.content_sample_detail__type_group);
        mDescTv = (TextView) findViewById(R.id.content_sample_detail__desc);
        mPromitionTv = (TextView) findViewById(R.id.content_sample_detail__promition);
        mBrandCategoryTv = (TextView) findViewById(R.id.content_sample_detail__brand_category);
        mBrandToChangeTv = (TextView) findViewById(R.id.content_sample_detail__brand_to_change);
        mAgreementTv = (TextView) findViewById(R.id.content_sample_detail__agreement);
        mSupportTv = (TextView) findViewById(R.id.content_sample_detail__support);
        mIasCodeTv = (TextView) findViewById(R.id.content_sample_detail__ias_code);
        mNetSalesTv = (TextView) findViewById(R.id.content_sample_detail__net_sales);
        mlDlTv = (TextView) findViewById(R.id.content_sample_detail__dl);
        mReasonTv = (TextView) findViewById(R.id.content_sample_detail__reason);
        mApplicantIdTv = (TextView) findViewById(R.id.content_sample_detail__applicant_id);
        mApplicantNameTv = (TextView) findViewById(R.id.content_sample_detail__applicant_name);
        mTotalAmountTv = (TextView) findViewById(R.id.content_sample_detail__total_amount);
        mSampleDetailContain = (LinearLayout) findViewById(R.id.content_sample_detail__contain);
        mApprovalHistoryContain = (LinearLayout) findViewById(R.id.content_approval_history__contain);

        approvalBtContain = findViewById(R.id.content_sample_approval_bt_contain);
        approvalBt = (TextView) findViewById(R.id.content_sample_approval_approval);
        disApprovalBt = (TextView) findViewById(R.id.content_sample_approval_disapproval);
        undeterminedBt = findViewById(R.id.content_sample_approval_undetermined);
        undeterminedBt.setOnClickListener(this);
        approvalBt.setOnClickListener(this);
        disApprovalBt.setOnClickListener(this);
    }


    //动态添加sample detailx
    private View addDetailView(SampleDetailVO item) {
        View view = LayoutInflater.from(getBaseContext()).inflate(R.layout.list_item_sample_detail, null, false);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 15, 0, 0);
        view.setLayoutParams(lp);
        TextView mItemNameCn = (TextView) view.findViewById(R.id.list_item_sample_detail__name_cn);
        TextView mItemCode = (TextView) view.findViewById(R.id.list_item_sample_detail__code);
        TextView mItemNameEn = (TextView) view.findViewById(R.id.list_item_sample_detail__name_en);
        TextView mItemBrand = (TextView) view.findViewById(R.id.list_item_sample_detail__brand);
        TextView mItemQuantity = (TextView) view.findViewById(R.id.list_item_sample_detail__quantity);
        TextView mItemRp = (TextView) view.findViewById(R.id.list_item_sample_detail__rp);
        TextView mItemTotalPrice = (TextView) view.findViewById(R.id.list_item_sample_detail__total_price);

        mItemNameCn.setText(item.getItemName());
        mItemNameEn.setText(item.getFrgnName());
        mItemBrand.setText(item.getBrand());
        mItemQuantity.setText(item.getQuantity() + "");
        mItemRp.setText(currency + String.format("%.2f",item.getRp()) + "");
        mItemTotalPrice.setText(currency + String.format("%.2f",item.getTotalPrice()) + "");
        mItemCode.setText(item.getItemCode() + "");
        return view;
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
                // if (type == 2 || type == 4) {
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
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //通过
            case R.id.content_sample_approval_approval:
                showConfigDialog(1);
                break;
            //驳回
            case R.id.content_sample_approval_disapproval:
                showConfigDialog(0);
                break;
            //待定
            case R.id.content_sample_approval_undetermined:
                showConfigDialog(2);
                break;
        }
    }

    public void auditSamplerequest(int isPass, String wineRecovery, String desc) {
        customDialog.show();
        AuditSampleRO requestRO = new AuditSampleRO();
        requestRO.setSampleRowId(rowId);
        requestRO.setIsPass(isPass);//1通过  0不通过 2待定
        requestRO.setComments(desc);
        requestRO.setWineryRecovery(wineRecovery);
        approvalService.auditSampleRequest(new ICallback<ResultVO>() {
            @Override
            public void onDataReady(ResultVO data) {
                //审核成功
                customDialog.dismiss();
                Toast.makeText(SampleInfoActivity.this, getResources().getString(R.string.submitted_successfully), Toast.LENGTH_SHORT).show();
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
                auditSamplerequest(isPass, wineryRecoveryEd.getText().toString(), descEd.getText().toString());
                builder.dismiss();
            }
        });
    }
}
