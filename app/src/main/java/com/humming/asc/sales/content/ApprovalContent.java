package com.humming.asc.sales.content;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.ad.NewCountVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.approval.MFListActivity;
import com.humming.asc.sales.activity.approval.PaymentListActivity;
import com.humming.asc.sales.activity.approval.SampleListActivity;
import com.humming.asc.sales.service.ApprovalService;
import com.humming.asc.sales.service.ICallback;

/**
 * Created by Zhtq on 1/7/16.
 */
public class ApprovalContent extends LinearLayout implements View.OnClickListener {
    private View view;
    private LinearLayout mSampleDaiban;
    private TextView mASampleYibanNum;
    private LinearLayout mSampleYiban;
    private LinearLayout mSampleMyRequest;
    private TextView mSampleMyRequestNum;
    private TextView mSampleMyBanjieNum;
    private LinearLayout mSampleMyBanjie;
    private LinearLayout mPaymentDaiban;
    private TextView mPaymentDaibanNum;
    private TextView mPaymentYibanNum;
    private LinearLayout mPaymentYiban;
    private LinearLayout mPaymentMyRequest;
    private TextView mPaymentMyRequestNum;
    private TextView mPaymentMyBanjieNum;
    private LinearLayout mPaymentMyBanjie;
    private LinearLayout mMfRequestDaiban;
    private TextView mMfRequestYibanNum;
    private LinearLayout mMfRequestYiban;
    private LinearLayout mMfRequestMyRequest;
    private TextView mMfRequestMyBanjieNum;
    private LinearLayout mMfRequestMyBanjie;
    private ApprovalService approvalService;
    private TextView mSampleDaibanNum;
    private TextView mMfDaibanNum;
    private TextView mMfMyRequestNum;

    public ApprovalContent(Context context) {
        this(context, null);
    }

    public ApprovalContent(final Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_approval, this);
        approvalService = Application.getApprovalService();
        initView();
        initData();//初始化数字
    }

    public void initData() {
        approvalService.querynewCount(new ICallback<NewCountVO>() {

            @Override
            public void onDataReady(NewCountVO data) {
                if (data.getMfEnd() == 0) {//mf 办结
                    mMfRequestMyBanjieNum.setVisibility(View.GONE);
                } else {
                    mMfRequestMyBanjieNum.setVisibility(View.VISIBLE);
                    if (data.getMfEnd() > 99) {
                        mMfRequestMyBanjieNum.setText("99+");
                    } else {
                        mMfRequestMyBanjieNum.setText(data.getMfEnd() + "");
                    }
                }

                if (data.getMfHaveDone() == 0) {//mf 已办
                    mMfRequestYibanNum.setVisibility(View.GONE);
                } else {
                    mMfRequestYibanNum.setVisibility(View.VISIBLE);
                    if (data.getMfHaveDone() > 99) {
                        mMfRequestYibanNum.setText(99 + "+");
                    } else {
                        mMfRequestYibanNum.setText(data.getMfHaveDone() + "");

                    }
                }

                if (data.getPaymentHaveDone() == 0) {//payment 已办
                    mPaymentYibanNum.setVisibility(View.GONE);
                } else {
                    mPaymentYibanNum.setVisibility(View.VISIBLE);
                    if (data.getPaymentHaveDone() > 99) {
                        mPaymentYibanNum.setText("99+");
                    } else {
                        mPaymentYibanNum.setText(data.getPaymentHaveDone() + "");
                    }
                }

                if (data.getPaymentEnd() == 0) {//payment 办结
                    mPaymentMyBanjieNum.setVisibility(View.GONE);
                } else {
                    mPaymentMyBanjieNum.setVisibility(View.VISIBLE);
                    if (data.getPaymentEnd() > 99) {
                        mPaymentMyBanjieNum.setText("99+");
                    } else {
                        mPaymentMyBanjieNum.setText(data.getPaymentEnd() + "");
                    }
                }

                if (data.getSampleEnd() == 0) {//sample 办结
                    mSampleMyBanjieNum.setVisibility(View.GONE);
                } else {
                    mSampleMyBanjieNum.setVisibility(View.VISIBLE);
                    if (data.getSampleEnd() > 99) {
                        mSampleMyBanjieNum.setText("99+");
                    } else {
                        mSampleMyBanjieNum.setText(data.getSampleEnd() + "");
                    }
                }

                if (data.getSampleHaveDone() == 0) {//sample 已办
                    mASampleYibanNum.setVisibility(View.GONE);
                } else {
                    mASampleYibanNum.setVisibility(View.VISIBLE);
                    if (data.getSampleHaveDone() > 99) {
                        mASampleYibanNum.setText("99+");
                    } else {
                        mASampleYibanNum.setText(data.getSampleHaveDone() + "");
                    }
                }

                if (data.getPaymentThird() == 0) {//payment 申请
                    mPaymentMyRequestNum.setVisibility(View.GONE);
                } else {
                    mPaymentMyRequestNum.setVisibility(View.VISIBLE);
                    if (data.getPaymentThird() > 99) {
                        mPaymentMyRequestNum.setText("99+");
                    } else {
                        mPaymentMyRequestNum.setText(data.getPaymentThird() + "");
                    }
                }

                if (data.getPaymentFirst() == 0) {//payment 待办
                    mPaymentDaibanNum.setVisibility(View.GONE);
                } else {
                    mPaymentDaibanNum.setVisibility(View.VISIBLE);
                    if (data.getPaymentFirst() > 99) {
                        mPaymentDaibanNum.setText("99+");
                    } else {
                        mPaymentDaibanNum.setText(data.getPaymentFirst() + "");
                    }
                }

                if (data.getSampleThird() == 0) {//sample 申请
                    mSampleMyRequestNum.setVisibility(View.GONE);
                } else {
                    mSampleMyRequestNum.setVisibility(View.VISIBLE);
                    if (data.getSampleThird() > 99) {
                        mSampleMyRequestNum.setText("99+");
                    } else {
                        mSampleMyRequestNum.setText(data.getSampleThird() + "");
                    }
                }

                if (data.getSampleFirst() == 0) {//sample 待办
                    mSampleDaibanNum.setVisibility(View.GONE);
                } else {
                    mSampleDaibanNum.setVisibility(View.VISIBLE);
                    if (data.getSampleFirst() > 99) {
                        mSampleDaibanNum.setText("99+");
                    } else {
                        mSampleDaibanNum.setText(data.getSampleFirst() + "");
                    }
                }

                if (data.getMfFirst() == 0) {//mf 待办
                    mMfDaibanNum.setVisibility(View.GONE);
                } else {
                    mMfDaibanNum.setVisibility(View.VISIBLE);
                    if (data.getMfFirst() > 99) {
                        mMfDaibanNum.setText("99+");
                    } else {
                        mMfDaibanNum.setText(data.getMfFirst() + "");
                    }
                }

                if (data.getMfThird() == 0) {//mf 申请
                    mMfMyRequestNum.setVisibility(View.GONE);
                } else {
                    mMfMyRequestNum.setVisibility(View.VISIBLE);
                    if (data.getMfThird() > 99) {
                        mMfMyRequestNum.setText("99+");
                    } else {
                        mMfMyRequestNum.setText(data.getMfThird() + "");
                    }
                }

            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    private void initView() {
        mSampleDaiban = (LinearLayout) findViewById(R.id.fragment_approval_sample__daiban);
        mSampleDaiban.setOnClickListener(this);
        mASampleYibanNum = (TextView) findViewById(R.id.fragment_approval_sample_yiban__num);
        mASampleYibanNum.setOnClickListener(this);
        mSampleYiban = (LinearLayout) findViewById(R.id.fragment_approval_sample__yiban);
        mSampleYiban.setOnClickListener(this);
        mSampleMyRequest = (LinearLayout) findViewById(R.id.fragment_approval_sample__my_request);
        mSampleMyRequest.setOnClickListener(this);
        mSampleMyBanjieNum = (TextView) findViewById(R.id.fragment_approval_sample__my_banjie__num);
        mSampleMyBanjieNum.setOnClickListener(this);
        mSampleMyBanjie = (LinearLayout) findViewById(R.id.fragment_approval_sample__my_banjie);
        mSampleMyBanjie.setOnClickListener(this);
        mPaymentDaiban = (LinearLayout) findViewById(R.id.fragment_approval_payment__daiban);
        mPaymentDaiban.setOnClickListener(this);
        mPaymentYibanNum = (TextView) findViewById(R.id.fragment_approval_payment_yiban__num);
        mPaymentYibanNum.setOnClickListener(this);
        mPaymentYiban = (LinearLayout) findViewById(R.id.fragment_approval_payment__yiban);
        mPaymentYiban.setOnClickListener(this);
        mPaymentMyRequest = (LinearLayout) findViewById(R.id.fragment_approval_payment__my_request);
        mPaymentMyRequest.setOnClickListener(this);
        mPaymentMyBanjieNum = (TextView) findViewById(R.id.fragment_approval_payment__my_banjie__num);
        mPaymentMyBanjieNum.setOnClickListener(this);
        mPaymentMyBanjie = (LinearLayout) findViewById(R.id.fragment_approval_payment__my_banjie);
        mPaymentMyBanjie.setOnClickListener(this);
        mMfRequestDaiban = (LinearLayout) findViewById(R.id.fragment_approval_mf_request__daiban);
        mMfRequestDaiban.setOnClickListener(this);
        mMfRequestYibanNum = (TextView) findViewById(R.id.fragment_approval_mf_request_yiban__num);
        mMfRequestYibanNum.setOnClickListener(this);
        mMfRequestYiban = (LinearLayout) findViewById(R.id.fragment_approval_mf_request__yiban);
        mMfRequestYiban.setOnClickListener(this);
        mMfRequestMyRequest = (LinearLayout) findViewById(R.id.fragment_approval_mf_request__my_request);
        mMfRequestMyRequest.setOnClickListener(this);
        mMfRequestMyBanjieNum = (TextView) findViewById(R.id.fragment_approval_mf_request__my_banjie__num);
        mMfRequestMyBanjieNum.setOnClickListener(this);
        mMfRequestMyBanjie = (LinearLayout) findViewById(R.id.fragment_approval_mf_request__my_banjie);
        mMfRequestMyBanjie.setOnClickListener(this);
        mSampleDaibanNum = (TextView) findViewById(R.id.fragment_approval_sample_daiban__num);
        mSampleMyRequestNum = (TextView) findViewById(R.id.fragment_approval_sample_my_request__num);
        mPaymentDaibanNum = (TextView) findViewById(R.id.fragment_approval_payment_daiban__num);
        mPaymentMyRequestNum = (TextView) findViewById(R.id.fragment_approval_payment_my_request__num);
        mMfDaibanNum = (TextView) findViewById(R.id.fragment_approval_mf_daiban__num);
        mMfMyRequestNum = (TextView) findViewById(R.id.fragment_approval_mf_my_request__num);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //待办
            case R.id.fragment_approval_sample__daiban:
                // TODO 18/06/22
                goSampleListActivity(1);
                break;
            case R.id.fragment_approval_sample_yiban__num:
                // TODO 18/06/22
                break;
            case R.id.fragment_approval_sample__yiban:
                // TODO 18/06/22
                goSampleListActivity(2);
                break;
            case R.id.fragment_approval_sample__my_request:
                // TODO 18/06/22
                goSampleListActivity(3);
                break;
            case R.id.fragment_approval_sample__my_banjie__num:
                // TODO 18/06/22
                break;
            case R.id.fragment_approval_sample__my_banjie:
                // TODO 18/06/22
                goSampleListActivity(4);
                break;
            case R.id.fragment_approval_payment__daiban:
                // TODO 18/06/22
                goPaymentListActivity(1);
                break;
            case R.id.fragment_approval_payment_yiban__num:
                // TODO 18/06/22
                break;
            case R.id.fragment_approval_payment__yiban:
                // TODO 18/06/22
                goPaymentListActivity(2);
                break;
            case R.id.fragment_approval_payment__my_request:
                // TODO 18/06/22
                goPaymentListActivity(3);
                break;
            case R.id.fragment_approval_payment__my_banjie__num:
                // TODO 18/06/22
                break;
            case R.id.fragment_approval_payment__my_banjie:
                // TODO 18/06/22
                goPaymentListActivity(4);
                break;
            case R.id.fragment_approval_mf_request__daiban:
                // TODO 18/06/22
                goMFListActivity(1);
                break;
            case R.id.fragment_approval_mf_request_yiban__num:
                // TODO 18/06/22
                break;
            case R.id.fragment_approval_mf_request__yiban:
                // TODO 18/06/22
                goMFListActivity(2);
                break;
            case R.id.fragment_approval_mf_request__my_request:
                // TODO 18/06/22
                goMFListActivity(3);
                break;
            case R.id.fragment_approval_mf_request__my_banjie__num:
                // TODO 18/06/22
                break;
            case R.id.fragment_approval_mf_request__my_banjie:
                // TODO 18/06/22
                goMFListActivity(4);
                break;
            default:
                break;
        }
    }

    //跳转Sample列表界面
    private void goSampleListActivity(int type) {//1:待办，2.已办，3.我的申请，4.我的办结
        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), SampleListActivity.class);
        intent.putExtra("query_sample_list_type", type);
        Application.getInstance().getCurrentActivity().startActivity(intent);
    }

    //跳转Payment列表界面
    private void goPaymentListActivity(int type) {//1:待办，2.已办，3.我的申请，4.我的办结
        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), PaymentListActivity.class);
        intent.putExtra("query_payment_list_type", type);
        Application.getInstance().getCurrentActivity().startActivity(intent);
    }

    //跳转MF列表界面
    private void goMFListActivity(int type) {//1:待办，2.已办，3.我的申请，4.我的办结
        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MFListActivity.class);
        intent.putExtra("query_mf_list_type", type);
        Application.getInstance().getCurrentActivity().startActivity(intent);
    }
}
