package com.humming.asc.sales.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.ad.payment.QueryPaymentListResultVO;
import com.humming.asc.dp.presentation.vo.ad.payment.QueryPaymentListVO;
import com.humming.asc.dp.presentation.vo.cp.DCSummaryInfoVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.PaymentListAdapter;
import com.humming.asc.sales.component.TopRightMenu;
import com.humming.asc.sales.model.ApprovalNewCountEvent;
import com.humming.asc.sales.model.BackRefreshEvent;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.service.ApprovalService;
import com.humming.asc.sales.service.ICallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class PaymentListActivity extends AbstractActivity implements View.OnClickListener, BaseAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private EditText mSearchEd;
    private ImageView mSearchButton;
    private LinearLayout mSearchLayout;
    private RecyclerView mListview;
    private ApprovalService approvalService;
    private InputMethodManager imm;
    private SwipeRefreshLayout refresh;
    private int type = 0;//1:待办，2.已办，3.我的申请，4.我的办结
    private boolean hasMore = true;
    private int pageNo = 1;
    private List<QueryPaymentListVO> paymentListVOS;
    private List<QueryPaymentListVO> paymentListVOList;
    private PaymentListAdapter adapter;
    private String keyword = "";
    private TopRightMenu mTopRightMenu;
    private ImageView mConditionPayment;
    private String isRead = "";
    private int clickPosition = 0;//刷新已读和未读使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_payment_list);
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        type = getIntent().getIntExtra("query_payment_list_type", 1);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
        mLoading.show();
        approvalService = Application.getApprovalService();
        paymentListVOList = new ArrayList<>();
        initData(String.valueOf(pageNo), keyword);
    }

    private void initData(final String pageNos, String keywords) {
        approvalService.queryPaymentList(new ICallback<QueryPaymentListResultVO>() {

            @Override
            public void onDataReady(QueryPaymentListResultVO data) {
                paymentListVOS = data.getData();
                hasMore = true;
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    paymentListVOList.clear();
                    paymentListVOList.addAll(paymentListVOS);

                    adapter = new PaymentListAdapter(paymentListVOS, type);
                    adapter.openLoadAnimation();
                    mListview.setAdapter(adapter);
                    adapter.openLoadMore(paymentListVOS.size(), true);

                } else {
                    if (paymentListVOS.size() > 0) {
                        hasMore = true;
                    } else {
                        hasMore = false;
                    }
                    paymentListVOList.addAll(paymentListVOS);

                    adapter.notifyDataChangedAfterLoadMore(paymentListVOS, true);

                }
                adapter.setOnLoadMoreListener(PaymentListActivity.this);
                adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        clickPosition = position;
                        Intent intent = new Intent(PaymentListActivity.this, PaymentInfoActivity.class);
                        intent.putExtra("payment_info_id", paymentListVOList.get(position).getRowId());
                        intent.putExtra("payment_type", type);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                mLoading.hide();

            }
        }, pageNos, type, keywords, isRead);
    }

    private void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.find__refresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(this);
        mSearchEd = (EditText) findViewById(R.id.payment_search_ed);
        mSearchButton = (ImageView) findViewById(R.id.payment_search_button);
        mSearchButton.setOnClickListener(this);
        mSearchLayout = (LinearLayout) findViewById(R.id.payment_search__layout);
        mSearchLayout.setOnClickListener(this);
        mListview = (RecyclerView) findViewById(R.id.payment_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mListview.setLayoutManager(linearLayoutManager);
        mConditionPayment = (ImageView) findViewById(R.id.payment_condition);

        mConditionPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTopRightMenu = new TopRightMenu(PaymentListActivity.this);
                mTopRightMenu
                        .setHeight(0)     //默认高度480
                        .setWidth(0)      //默认宽度wrap_content
                        .showIcon(false)     //显示菜单图标，默认为true
                        .dimBackground(true)           //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                        .showDian(true)
                        .addMenuItems(new com.humming.asc.sales.component.MenuItem(getString(R.string.unread)))
                        .addMenuItems(new com.humming.asc.sales.component.MenuItem(getString(R.string.read)))
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                pageNo = 1;
                                isRead = position + "";
                                initData(String.valueOf(pageNo), mSearchEd.getText().toString());
                            }
                        })
                        .showAsDropDown(mConditionPayment, -55, 0);
            }
        });
        //初始化虚拟键盘的状态
        mSearchEd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mSearchEd.setFocusable(true);
                mSearchEd.setFocusableInTouchMode(true);
                mSearchEd.requestFocus();
                InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(mSearchEd.getWindowToken(), 0);
                return false;
            }
        });
        mSearchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //跳转页面
                    pageNo = 1;
                    keyword = mSearchEd.getText().toString();
                    initData(String.valueOf(pageNo), mSearchEd.getText().toString());
                    imm.hideSoftInputFromWindow(mSearchEd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        mSearchEd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_task_list, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        DCSummaryInfoVO info;
        switch (id) {
            case android.R.id.home:
                EventBus.getDefault().post(new ApprovalNewCountEvent("1"));
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.payment_search_button:
                //搜索
                break;
            case R.id.payment_search__layout:
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mListview.post(new Runnable() {
            @Override
            public void run() {
                if (!hasMore) {
                    adapter.notifyDataChangedAfterLoadMore(false);
                    //    adapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            pageNo++;
                            initData(String.valueOf(pageNo), keyword);
                        }
                    }, 1500);
                }
            }

        });

    }

    @Override
    public void onRefresh() {
        mListview.post(new Runnable() {
            @Override
            public void run() {
                pageNo = 1;
                initData(String.valueOf(pageNo), keyword);
                refresh.setRefreshing(false);
            }
        });
    }

    //eventBus回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(BackRefreshOneEvent myEvent) {
       // pageNo = 1;
       // keyword = "";
       // isRead = "";
       // initData(String.valueOf(pageNo), keyword);
        if(adapter.getData().get(clickPosition).getIsNew()==1){
            adapter.getData().get(clickPosition).setIsNew(0);
            adapter.notifyItemChanged(clickPosition);
        }
    }
    //eventBus回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(BackRefreshEvent myEvent) {
         pageNo = 1;
         keyword = "";
         isRead = "";
         initData(String.valueOf(pageNo), keyword);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
