package com.humming.asc.sales.activity.ecatalog;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.MyEcatalogRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.BaseAdapter;
import com.humming.asc.sales.adapter.ecatalog.EcatalogAdapter;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.bean.ecatalog.EcatalogListBean;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogListResponse;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MyEcatalogActivity extends AbstractActivity implements BaseAdapter.RequestLoadMoreListener, View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private int hasMore = 1;
    private String pagable = "0";
    private LinearLayout createEcatalogTv;
    private ImageView mMyBackEcatalog;
    private LinearLayout mMyTemplateEcatalog;
    private LinearLayout mMyAllEcatalog;
    private TextView mMyAllTextEcatalog;
    private View mMyAllLineEcatalog;
    private TextView mMyDraftTextEcatalog;
    private View mMyDraftLineEcatalog;
    private LinearLayout mMyDraftEcatalog;
    private TextView mMySendTextEcatalog;
    private View mMySendLineEcatalog;
    private LinearLayout mMySendEcatalog;
    private TextView mMyMfrTextEcatalog;
    private View mMyMfrLineEcatalog;
    private LinearLayout mMyMfrEcatalog;
    private TextView mMyCompleteTextEcatalog;
    private View mMyCompleteLineEcatalog;
    private LinearLayout mMyCompleltEcatalog;
    // private ImageView mProductActionDownNSItem;
    // private LinearLayout mMySortEcatalog;
    private RecyclerView mMyListviewEcatalog;
    private EcatalogAdapter adapter = new EcatalogAdapter(null);
    private List<EcatalogListBean> rows;
    private List<EcatalogListBean> ecatalogListBeanList;
    private String sortType = "0";
    private LinearLayout nodataLayout;
    private SwipeRefreshLayout mRefresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecatalog_my);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        mLoading.show();
        initData(sortType, pagable + "");
    }

    private void initData(String sortType, final String pagables) {// 排序类型, 0为全部, 1为草稿, 2已发送, 3为MFR 4.完成
        final MyEcatalogRequest request = new MyEcatalogRequest();
        request.setSortType(sortType);
        request.setPagable(pagables);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_MY_ECATALOG, new OkHttpClientManager.ResultCallback<EcatalogListResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                mLoading.hide();
            }

            @Override
            public void onResponse(EcatalogListResponse response) {
                mLoading.hide();
                rows = response.getEcatalogList();
                if ("0".equals(pagables)) {
                    if (rows.size() <= 0) {
                        nodataLayout.setVisibility(View.VISIBLE);
                        mRefresh.setVisibility(View.GONE);
                        //adapter = new EcatalogAdapter(null);
                        // adapter.openLoadAnimation();
                        // mMyListviewEcatalog.setAdapter(adapter);
                    } else {
                        nodataLayout.setVisibility(View.GONE);
                        mRefresh.setVisibility(View.VISIBLE);
                        ecatalogListBeanList.clear();
                        ecatalogListBeanList.addAll(rows);
                        adapter = new EcatalogAdapter(rows);
                        adapter.openLoadAnimation();
                        mMyListviewEcatalog.setAdapter(adapter);
                        adapter.openLoadMore(rows.size(), true);
                        hasMore = response.getHasMore();
                        pagable = response.getPagable() + "";
                    }

                } else {
                    nodataLayout.setVisibility(View.GONE);
                    mRefresh.setVisibility(View.VISIBLE);
                    ecatalogListBeanList.addAll(rows);
                    hasMore = response.getHasMore();
                    if (response.getHasMore() == 1) {
                        pagable = response.getPagable() + "";
                    } else {
                        adapter.removeAllFooterView();
                        pagable = "0";
                    }
                    adapter.notifyDataChangedAfterLoadMore(rows, true);
                }
                adapter.setOnLoadMoreListener(MyEcatalogActivity.this);
                adapter.setOnRecyclerViewItemClickListener(new BaseAdapter.OnRecyclerViewItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), EcatalogDetailActivity.class);
                        intent.putExtra("ecatalog_id", ecatalogListBeanList.get(position).getEcatalogId() + "");
                        Application.getInstance().getCurrentActivity().startActivity(intent);
                    }
                });

            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                mLoading.hide();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, EcatalogListResponse.class);
    }

    private void initView() {
        createEcatalogTv = (LinearLayout) findViewById(R.id.ecatalog_my_create_ecatalog);
        createEcatalogTv.setOnClickListener(this);
        mMyListviewEcatalog = (RecyclerView) findViewById(R.id.ecatalog_my_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMyListviewEcatalog.setLayoutManager(linearLayoutManager);
        mMyBackEcatalog = (ImageView) findViewById(R.id.ecatalog_my__back);
        mMyBackEcatalog.setOnClickListener(this);
        mMyTemplateEcatalog = (LinearLayout) findViewById(R.id.ecatalog_my__template);
        mMyTemplateEcatalog.setOnClickListener(this);
        mMyAllEcatalog = (LinearLayout) findViewById(R.id.ecatalog_my_all);
        mMyAllEcatalog.setOnClickListener(this);
        mMyAllTextEcatalog = (TextView) findViewById(R.id.ecatalog_my_all_text);
        mMyAllLineEcatalog = (View) findViewById(R.id.ecatalog_my_all_line);
        mMyDraftTextEcatalog = (TextView) findViewById(R.id.ecatalog_my_draft_text);
        mMyDraftLineEcatalog = (View) findViewById(R.id.ecatalog_my_draft_line);
        mMyDraftEcatalog = (LinearLayout) findViewById(R.id.ecatalog_my_draft);
        mMyDraftEcatalog.setOnClickListener(this);
        mMySendTextEcatalog = (TextView) findViewById(R.id.ecatalog_my_send_text);
        mMySendLineEcatalog = (View) findViewById(R.id.ecatalog_my_send_line);
        mMySendEcatalog = (LinearLayout) findViewById(R.id.ecatalog_my_send);
        mMySendEcatalog.setOnClickListener(this);
        mMyMfrTextEcatalog = (TextView) findViewById(R.id.ecatalog_my_mfr_text);
        mMyMfrLineEcatalog = (View) findViewById(R.id.ecatalog_my_mfr_line);
        mMyMfrEcatalog = (LinearLayout) findViewById(R.id.ecatalog_my_mfr);
        mMyMfrEcatalog.setOnClickListener(this);
        mMyCompleteTextEcatalog = (TextView) findViewById(R.id.ecatalog_my_complete_text);
        mMyCompleteLineEcatalog = (View) findViewById(R.id.ecatalog_my_complete_line);
        mMyCompleltEcatalog = (LinearLayout) findViewById(R.id.ecatalog_my_complete);
        mMyCompleltEcatalog.setOnClickListener(this);
        nodataLayout = findViewById(R.id.content_nodata);
        //mProductActionDownNSItem = (ImageView) findViewById(R.id.item_product_action_down_n_s);
        //mMySortEcatalog = (LinearLayout) findViewById(R.id.ecatalog_my_sort);
        ecatalogListBeanList = new ArrayList<>();
        mRefresh = (SwipeRefreshLayout) findViewById(R.id.ecatalog__refresh);
        mRefresh.setOnRefreshListener(this);
    }

    @Override
    public void onLoadMoreRequested() {
        mMyListviewEcatalog.post(new Runnable() {
            @Override
            public void run() {
                if (hasMore != 1) {
                    adapter.removeAllFooterView();
                    adapter.notifyDataChangedAfterLoadMore(false);
                    View notLoadingView = LayoutInflater.from(Application.getInstance().getCurrentActivity()).inflate(R.layout.not_loading, (ViewGroup) mMyListviewEcatalog.getParent(), false);
                    TextView tv = notLoadingView.findViewById(R.id.no_data_tv);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
                    lp.bottomMargin = 0;
                    tv.setLayoutParams(lp);
                    adapter.addFooterView(notLoadingView);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            initData(sortType, pagable);
                        }
                    }, 1500);
                }
            }

        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ecatalog_my__template:
                break;
            case R.id.ecatalog_my__back:
                EventBus.getDefault().post(new BackRefreshOneEvent("报价单刷新"));
                finish();
                break;
            case R.id.ecatalog_my_create_ecatalog:
                Intent intent = new Intent(this, CreateEcatalogActivity.class);
                startActivity(intent);
                break;
            case R.id.ecatalog_my_all:
                setView();
                mMyAllTextEcatalog.setTextColor(getResources().getColor(R.color.ffbfa574));
                mMyAllLineEcatalog.setVisibility(View.VISIBLE);
                sortType = "0";
                pagable = "0";
                mLoading.show();
                initData(sortType, pagable);
                break;
            case R.id.ecatalog_my_draft:
                setView();
                mMyDraftTextEcatalog.setTextColor(getResources().getColor(R.color.ffbfa574));
                mMyDraftLineEcatalog.setVisibility(View.VISIBLE);
                sortType = "1";
                pagable = "0";
                mLoading.show();
                initData(sortType, pagable);
                break;
            case R.id.ecatalog_my_send:
                setView();
                mMySendTextEcatalog.setTextColor(getResources().getColor(R.color.ffbfa574));
                mMySendLineEcatalog.setVisibility(View.VISIBLE);
                sortType = "2";
                pagable = "0";
                mLoading.show();
                initData(sortType, pagable);
                break;
            case R.id.ecatalog_my_mfr:
                setView();
                mMyMfrTextEcatalog.setTextColor(getResources().getColor(R.color.ffbfa574));
                mMyMfrLineEcatalog.setVisibility(View.VISIBLE);
                sortType = "3";
                pagable = "0";
                mLoading.show();
                initData(sortType, pagable);
                break;
            case R.id.ecatalog_my_complete:
                setView();
                mMyCompleteTextEcatalog.setTextColor(getResources().getColor(R.color.ffbfa574));
                mMyCompleteLineEcatalog.setVisibility(View.VISIBLE);
                sortType = "4";
                pagable = "0";
                mLoading.show();
                initData(sortType, pagable);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRefresh() {
        mMyListviewEcatalog.post(new Runnable() {
            @Override
            public void run() {
                pagable = "0";
                initData(sortType, pagable);
                mRefresh.setRefreshing(false);
            }
        });
    }

    private void setView() {
        mMyDraftTextEcatalog.setTextColor(getResources().getColor(R.color._666));
        mMyDraftLineEcatalog.setVisibility(View.GONE);
        mMyMfrTextEcatalog.setTextColor(getResources().getColor(R.color._666));
        mMyMfrLineEcatalog.setVisibility(View.GONE);
        mMyAllTextEcatalog.setTextColor(getResources().getColor(R.color._666));
        mMyAllLineEcatalog.setVisibility(View.GONE);
        mMySendTextEcatalog.setTextColor(getResources().getColor(R.color._666));
        mMySendLineEcatalog.setVisibility(View.GONE);
        mMyCompleteTextEcatalog.setTextColor(getResources().getColor(R.color._666));
        mMyCompleteLineEcatalog.setVisibility(View.GONE);
    }

    //eventBus回调(删除返回)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventMainThread(BackRefreshOneEvent myEvent) {
        if ("1".equals(myEvent.getMessage()))
            initData(sortType, pagable);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        EventBus.getDefault().post(new BackRefreshOneEvent("报价单刷新"));
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
