package com.humming.asc.sales.activity.approval;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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

import com.humming.asc.dp.presentation.vo.AccountForMfVO;
import com.humming.asc.dp.presentation.vo.cp.DCSummaryInfoVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.ApprovalCustomerListAdapter;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.service.ApprovalService;
import com.humming.asc.sales.service.ICallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class CustomerListActivity extends AbstractActivity implements View.OnClickListener, BaseAdapter.RequestLoadMoreListener {

    private Toolbar toolbar;
    private EditText mSearchEd;
    private ImageView mSearchButton;
    private LinearLayout mSearchLayout;
    private RecyclerView mListview;
    private ApprovalService approvalService;
    private ApprovalCustomerListAdapter adapter;
    private String keyword = "";
    private boolean hasMore = true;
    private int pageNo = 1;
    private List<AccountForMfVO> accountForMfVOS;
    private List<AccountForMfVO> accountForMfVOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval_customer_list);
        initView();
        approvalService = Application.getApprovalService();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.tab_customer));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        accountForMfVOList = new ArrayList<>();
        mLoading.show();

        intData(String.valueOf(pageNo), keyword);
    }

    private void intData(final String pageNos, String keyword) {
        approvalService.queryAccForMf(new ICallback<List<AccountForMfVO>>() {

            @Override
            public void onDataReady(final List<AccountForMfVO> data) {
                mLoading.hide();
                accountForMfVOS = data;
                hasMore = true;
                if ("1".equals(pageNos)) {
                    accountForMfVOList.clear();
                    accountForMfVOList.addAll(accountForMfVOS);

                    adapter = new ApprovalCustomerListAdapter(accountForMfVOS);
                    adapter.openLoadAnimation();
                    mListview.setAdapter(adapter);
                    adapter.openLoadMore(accountForMfVOS.size(), true);

                } else {
                    if (accountForMfVOS.size() > 0) {
                        hasMore = true;
                    } else {
                        hasMore = false;
                    }
                    accountForMfVOList.addAll(accountForMfVOS);
                    adapter.notifyDataChangedAfterLoadMore(accountForMfVOS, true);
                }
                adapter.setOnLoadMoreListener(CustomerListActivity.this);
                adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        EventBus.getDefault().post(accountForMfVOList.get(position));
                        finish();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                mLoading.hide();
            }
        }, pageNos, keyword);
    }

    private void initView() {
        mSearchEd = (EditText) findViewById(R.id.customer_search_ed);
        mSearchButton = (ImageView) findViewById(R.id.customer_search_button);
        mSearchButton.setOnClickListener(this);
        mSearchLayout = (LinearLayout) findViewById(R.id.customer_search__layout);
        mSearchLayout.setOnClickListener(this);
        mListview = (RecyclerView) findViewById(R.id.customer_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mListview.setLayoutManager(linearLayoutManager);

        mSearchEd.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                    intData(String.valueOf(pageNo), mSearchEd.getText().toString());
                    InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchEd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    //查询Task
    public void queryTask(final String rowId, final String pageNos) {
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
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
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
                            intData(String.valueOf(pageNo), keyword);
                        }
                    }, 1500);
                }
            }

        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.customer_search_button:
                // TODO 18/06/22
                break;
            case R.id.customer_search__layout:
                // TODO 18/06/22
                break;
            default:
                break;
        }
    }

}
