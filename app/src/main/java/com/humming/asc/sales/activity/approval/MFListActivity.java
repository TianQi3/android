package com.humming.asc.sales.activity.approval;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
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
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestListResultVO;
import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestListVO;
import com.humming.asc.dp.presentation.vo.cp.DCSummaryInfoVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.customer.CustomerMFInfoActivity;
import com.humming.asc.sales.activity.customer.CustomerMFRequestActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.MFRequestListAdapter;
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

public class MFListActivity extends AbstractActivity implements BaseAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener {

    private Toolbar toolbar;
    private EditText mSearchEdMf;
    private ImageView mConditionMf;
    private RecyclerView mListviewMf;
    private ApprovalService approvalService;
    private MFRequestListAdapter adapter;
    private SwipeRefreshLayout refresh;
    private List<QueryMfRequestListVO> mfRequestListVOS;
    private List<QueryMfRequestListVO> mfRequestListVOSList;
    private int type = 0;//1:待办，2.已办，3.我的申请，4.我的办结
    private String keyword = "";
    private boolean hasMore = true;
    private int pageNo = 1;
    private TopRightMenu mTopRightMenu;
    private String isRead = "";
    private int clickPosition = 0;//刷新已读和未读使用

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_mf_list);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        initView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mLoading.show();
        type = getIntent().getIntExtra("query_mf_list_type", 1);
        approvalService = Application.getApprovalService();
        mfRequestListVOSList = new ArrayList<>();
        initData(String.valueOf(pageNo), keyword);
    }

    private void initData(final String pageNos, String keyword) {
        approvalService.queryMfList(new ICallback<QueryMfRequestListResultVO>() {

            @Override
            public void onDataReady(QueryMfRequestListResultVO data) {
                mfRequestListVOS = data.getData();
                hasMore = true;
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    mfRequestListVOSList.clear();
                    mfRequestListVOSList.addAll(mfRequestListVOS);

                    adapter = new MFRequestListAdapter(mfRequestListVOS, type);
                    adapter.openLoadAnimation();
                    mListviewMf.setAdapter(adapter);
                    adapter.openLoadMore(mfRequestListVOS.size(), true);

                } else {
                    if (mfRequestListVOS.size() > 0) {
                        hasMore = true;
                    } else {
                        hasMore = false;
                    }
                    mfRequestListVOSList.addAll(mfRequestListVOS);

                    adapter.notifyDataChangedAfterLoadMore(mfRequestListVOS, true);

                }
                adapter.setOnLoadMoreListener(MFListActivity.this);
                adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        boolean isEdit = false;
                        clickPosition = position;
                        if ("Declined".equals(mfRequestListVOSList.get(position).getMfStatus()) || "Not Submitted".equals(mfRequestListVOSList.get(position).getMfStatus())) {
                            if ("Sales".equals(mfRequestListVOSList.get(position).getStage())) {
                                isEdit = true;
                            } else {
                                isEdit = false;
                            }

                        }
                        Intent intent = new Intent(MFListActivity.this, CustomerMFInfoActivity.class);
                        intent.putExtra("mf_info_id", mfRequestListVOSList.get(position).getRowId());
                        intent.putExtra("mf_type", type);
                        intent.putExtra("is_edit", isEdit);
                        startActivity(intent);
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {
                mLoading.hide();
            }
        }, pageNos, type, keyword, isRead);
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
                mTopRightMenu = new TopRightMenu(MFListActivity.this);
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
        DCSummaryInfoVO info;
        switch (id) {
            case android.R.id.home:
                EventBus.getDefault().post(new ApprovalNewCountEvent("1"));
                finish();
                break;
            case R.id.action_product_add:
                //添加request
                Intent intent5 = new Intent(this, CustomerMFRequestActivity.class);
                startActivity(intent5);
                break;
        }
        return super.onOptionsItemSelected(item);
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

    //eventBus回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(BackRefreshOneEvent myEvent) {
      //  pageNo = 1;
      //  keyword = "";
      //  isRead = "";
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
