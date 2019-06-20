package com.humming.asc.sales.activity.customer;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestListFromCustomerResultVO;
import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestListFromCustomerVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.MFListAdapter;
import com.humming.asc.sales.component.TopRightMenu;
import com.humming.asc.sales.model.BackRefreshEvent;
import com.humming.asc.sales.service.ApprovalService;
import com.humming.asc.sales.service.ICallback;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class CustomerMFListActivity extends AbstractActivity implements BaseAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private EditText mSearchEdMf;
    private SwipeRefreshLayout refresh;
    private ImageView mConditionMf;
    private RecyclerView mListviewMf;
    private ApprovalService approvalService;
    private List<QueryMfRequestListFromCustomerVO> mfRequestListFromCustomerVOS;
    private List<QueryMfRequestListFromCustomerVO> mfRequestListFromCustomerVOSList;
    private MFListAdapter adapter;
    private boolean hasMore = true;
    private int pageNo = 1;
    private String customerCode = "";
    private String keyword = "";
    private TopRightMenu mTopRightMenu;
    private String rowId = "";
    private List<String> menus;
    private String menuValue = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mf_list);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        customerCode = getIntent().getStringExtra("cus_code");
        rowId = getIntent().getStringExtra("cusrowId");
        initView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLoading.show();
        approvalService = Application.getApprovalService();
        mfRequestListFromCustomerVOSList = new ArrayList<>();
        initData(String.valueOf(pageNo), "");
        initMenu();//初始化左上角的数据
    }

    private void initMenu() {
        approvalService.queryAduitMenu(new ICallback<List<String>>() {

            @Override
            public void onDataReady(List<String> data) {
                menus = data;
            }

            @Override
            public void onError(Throwable throwable) {
                Log.v("xxxx", throwable.getMessage());
            }
        });
    }

    //初始化数据
    private void initData(final String pageNos, String keyWords) {
        approvalService.queryMfListFromCustomer(new ICallback<QueryMfRequestListFromCustomerResultVO>() {

            @Override
            public void onDataReady(QueryMfRequestListFromCustomerResultVO data) {
                mfRequestListFromCustomerVOS = data.getData();
                hasMore = true;
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    mfRequestListFromCustomerVOSList.clear();
                    mfRequestListFromCustomerVOSList.addAll(mfRequestListFromCustomerVOS);

                    adapter = new MFListAdapter(mfRequestListFromCustomerVOS);
                    adapter.openLoadAnimation();
                    mListviewMf.setAdapter(adapter);
                    adapter.openLoadMore(mfRequestListFromCustomerVOS.size(), true);

                } else {
                    if (mfRequestListFromCustomerVOS.size() > 0) {
                        hasMore = true;
                    } else {
                        hasMore = false;
                    }
                    mfRequestListFromCustomerVOSList.addAll(mfRequestListFromCustomerVOS);

                    adapter.notifyDataChangedAfterLoadMore(mfRequestListFromCustomerVOS, true);

                }
                adapter.setOnLoadMoreListener(CustomerMFListActivity.this);
                adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        boolean isEdit = false;
                        if ("Declined".equals(mfRequestListFromCustomerVOSList.get(position).getMfStatus()) || "Not Submitted".equals(mfRequestListFromCustomerVOSList.get(position).getMfStatus())) {
                            if ("Sales".equals(mfRequestListFromCustomerVOSList.get(position).getStage())) {
                                isEdit = true;
                            } else {
                                isEdit = false;
                            }
                        }
                        Intent intent = new Intent(CustomerMFListActivity.this, CustomerMFInfoActivity.class);
                        intent.putExtra("mf_info_id", mfRequestListFromCustomerVOSList.get(position).getRowId());
                        intent.putExtra("is_edit", isEdit);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                Log.v("xxxx", throwable.getMessage());
            }
        }, pageNos, keyWords, rowId, menuValue);
    }

    private void initView() {
        refresh = (SwipeRefreshLayout) findViewById(R.id.find__refresh);
        refresh.setColorSchemeColors(getResources().getColor(R.color.colorAccent));
        refresh.setOnRefreshListener(this);
        mSearchEdMf = (EditText) findViewById(R.id.mf_search_ed);
        mConditionMf = (ImageView) findViewById(R.id.mf_condition);
        mListviewMf = (RecyclerView) findViewById(R.id.mf_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mListviewMf.setLayoutManager(linearLayoutManager);
        mConditionMf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTopRightMenu = new TopRightMenu(CustomerMFListActivity.this);
                for (String value : menus) {
                    mTopRightMenu.addMenuItems(new com.humming.asc.sales.component.MenuItem(value));
                }
                mTopRightMenu
                        .setHeight(0)     //默认高度480
                        .setWidth(0)      //默认宽度wrap_content
                        .showIcon(false)     //显示菜单图标，默认为true
                        .dimBackground(true)           //背景变暗，默认为true
                        .needAnimationStyle(true)   //显示动画，默认为true
                        .setAnimationStyle(R.style.TRM_ANIM_STYLE)  //默认为R.style.TRM_ANIM_STYLE
                        .showDian(true)
                        .setOnMenuItemClickListener(new TopRightMenu.OnMenuItemClickListener() {
                            @Override
                            public void onMenuItemClick(int position) {
                                pageNo = 1;
                                menuValue = menus.get(position);
                                initData(String.valueOf(pageNo), mSearchEdMf.getText().toString());
                            }
                        })
                        .showAsDropDown(mConditionMf, -55, 0);
            }
        });
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
                    pageNo = 1;
                    keyword = mSearchEdMf.getText().toString();
                    initData(String.valueOf(pageNo), mSearchEdMf.getText().toString());
                    InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mSearchEdMf.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_product, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_product_add:
                //添加request
                Intent intent5 = new Intent(this, CustomerMFRequestActivity.class);
                intent5.putExtra("from_customer", true);
                intent5.putExtra("customer_code", customerCode);
                startActivity(intent5);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //eventBus回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(BackRefreshEvent myEvent) {
        pageNo = 1;
        keyword = "";
        initData(String.valueOf(pageNo), keyword);
    }


    @Override
    public void onLoadMoreRequested() {
        mListviewMf.post(new Runnable() {
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
        mListviewMf.post(new Runnable() {
            @Override
            public void run() {
                pageNo = 1;
                initData(String.valueOf(pageNo), keyword);
                refresh.setRefreshing(false);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
