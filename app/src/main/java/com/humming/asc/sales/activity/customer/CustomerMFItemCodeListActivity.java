package com.humming.asc.sales.activity.customer;

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

import com.humming.asc.dp.presentation.vo.ItemCostVO;
import com.humming.asc.dp.presentation.vo.cp.DCSummaryInfoVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.MFItemCodeAdapter;
import com.humming.asc.sales.service.ApprovalService;
import com.humming.asc.sales.service.ICallback;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

public class CustomerMFItemCodeListActivity extends AbstractActivity implements View.OnClickListener, BaseAdapter.RequestLoadMoreListener {

    private Application app = Application.getInstance();
    private Toolbar toolbar;
    private EditText mSearchEdMf;
    private ImageView mSearchButtonMf;
    private LinearLayout mSearchLayoutMf;
    private ImageView mConditionMf;
    private ImageView mCodeSearchButton;
    private LinearLayout mCodeSearchLayout;
    private RecyclerView mCodeListview;
    private ApprovalService approvalService;
    private MFItemCodeAdapter adapter;
    private String searchName = "";
    private int pageNo = 1;
    private List<ItemCostVO> itemCostVOS;
    private boolean hasMore = false;
    private List<ItemCostVO> itemCostVOList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mf_item_code_list);
        initView();
        approvalService = Application.getApprovalService();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemCostVOList = new ArrayList<>();
        mLoading.show();
        initData(String.valueOf(pageNo), "");
    }

    private void initData(final String pageNos, String searchNames) {
        approvalService.queryItemCode(new ICallback<List<ItemCostVO>>() {

            @Override
            public void onDataReady(final List<ItemCostVO> data) {
                mLoading.hide();
                itemCostVOS = data;
                hasMore = true;
                if ("1".equals(pageNos)) {
                    itemCostVOList.clear();
                    itemCostVOList.addAll(itemCostVOS);

                    adapter = new MFItemCodeAdapter(itemCostVOS);
                    adapter.openLoadAnimation();
                    mCodeListview.setAdapter(adapter);
                    adapter.openLoadMore(itemCostVOS.size(), true);
                } else {
                    if (itemCostVOS.size() > 0) {
                        hasMore = true;
                    } else {
                        hasMore = false;
                    }
                    itemCostVOList.addAll(itemCostVOS);

                    adapter.notifyDataChangedAfterLoadMore(itemCostVOS, true);

                }
                adapter.setOnLoadMoreListener(CustomerMFItemCodeListActivity.this);
                adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        EventBus.getDefault().post(itemCostVOList.get(position));
                        finish();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                mLoading.hide();
            }
        },pageNos, searchNames);
    }

    private void initView() {
        mSearchEdMf = (EditText) findViewById(R.id.item_code__search_ed);
        mCodeSearchButton = (ImageView) findViewById(R.id.item_code_search_button);
        mCodeSearchLayout = (LinearLayout) findViewById(R.id.item_code_search__layout);
        mCodeSearchLayout.setOnClickListener(this);
        mCodeListview = (RecyclerView) findViewById(R.id.item_code__listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mCodeListview.setLayoutManager(linearLayoutManager);

        //初始化虚拟键盘的状态
        mSearchEdMf.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mSearchEdMf.setFocusable(true);
                mSearchEdMf.setFocusableInTouchMode(true);
                mSearchEdMf.requestFocus();
                InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(mSearchEdMf.getWindowToken(), 0);
                return false;
            }
        });
        mSearchEdMf.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //跳转页面
                    searchName = mSearchEdMf.getText().toString();
                    pageNo = 1;
                    initData(String.valueOf(pageNo), searchName);
                    InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchEdMf.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.item_code_search__layout:
                // TODO 18/06/22
                break;
            default:
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        mCodeListview.post(new Runnable() {
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
                            initData(String.valueOf(pageNo), searchName);
                        }
                    }, 1500);
                }
            }

        });

    }
}
