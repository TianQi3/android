package com.humming.asc.sales.activity.settings;

import android.os.Bundle;
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

import com.fasterxml.jackson.core.type.TypeReference;
import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.MyEcatalogRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.my.MessageAdapter;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.user.EcatalogNoticeResponse;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class MyMessageActivity extends AbstractActivity implements View.OnClickListener {

    private ImageView mMyBack;
    private RecyclerView mMyListviewMessage;
    private MessageAdapter adapter = new MessageAdapter(null);
    private LinearLayout nodataLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        mLoading.show();
        initData();
    }

    private void initData() {
        final MyEcatalogRequest request = new MyEcatalogRequest();
        OkHttpClientManager.postAsyn(Config.ECATALLOG_USER_NOTICE, new OkHttpClientManager.ResultCallback<List<EcatalogNoticeResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                mLoading.hide();
            }

            @Override
            public void onResponse(List<EcatalogNoticeResponse> response) {
                mLoading.hide();
                if (response != null && response.size() > 0) {
                    nodataLayout.setVisibility(View.GONE);
                    mMyListviewMessage.setVisibility(View.VISIBLE);
                    adapter = new MessageAdapter(response);
                    adapter.openLoadAnimation();
                    mMyListviewMessage.setAdapter(adapter);
                    adapter.removeAllFooterView();
                    View notLoadingView = LayoutInflater.from(Application.getInstance().getCurrentActivity()).inflate(R.layout.not_loading, (ViewGroup) mMyListviewMessage.getParent(), false);
                    TextView tv = notLoadingView.findViewById(R.id.no_data_tv);
                    LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) tv.getLayoutParams();
                    lp.bottomMargin = 0;
                    tv.setLayoutParams(lp);
                    adapter.addFooterView(notLoadingView);
                } else {
                    nodataLayout.setVisibility(View.VISIBLE);
                    mMyListviewMessage.setVisibility(View.GONE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                mLoading.hide();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, new TypeReference<List<EcatalogNoticeResponse>>() {
        });
    }

    private void initView() {
        mMyBack = findViewById(R.id.message_my__back);
        mMyBack.setOnClickListener(this);
        nodataLayout = findViewById(R.id.content_nodata);
        mMyListviewMessage = (RecyclerView) findViewById(R.id.message_my_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mMyListviewMessage.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.message_my__back:
                EventBus.getDefault().post(new BackRefreshOneEvent("消息刷新"));
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        EventBus.getDefault().post(new BackRefreshOneEvent("消息刷新"));
        finish();
        return super.onKeyDown(keyCode, event);
    }
}
