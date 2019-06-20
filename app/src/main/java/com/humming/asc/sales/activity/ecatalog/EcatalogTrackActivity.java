package com.humming.asc.sales.activity.ecatalog;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.EcatalogDetailRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.ecatalog.EcatalogTrackAdapter;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.ecatalog.EcatalogStatusResponse;
import com.squareup.okhttp.Request;

public class EcatalogTrackActivity extends AbstractActivity implements View.OnClickListener {

    private ImageView mTrackBack;
    private String ecatalogId = "";
    private RecyclerView recyclerView;
    private EcatalogTrackAdapter adapter;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecatalog_track);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        ecatalogId = getIntent().getStringExtra("ecatalog_id");
        customDialog = new CustomDialog(this, "Loading...");
        initView();
        initData();
    }

    private void initData() {
        customDialog.show();
        final EcatalogDetailRequest request = new EcatalogDetailRequest();
        request.setId(ecatalogId);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_STATUS_ECATALOG, new OkHttpClientManager.ResultCallback<EcatalogStatusResponse>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(EcatalogStatusResponse response) {
                customDialog.dismiss();
                adapter = new EcatalogTrackAdapter(response.getEcatalogStatusBeanList());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, EcatalogStatusResponse.class);
    }

    private void initView() {
        mTrackBack = (ImageView) findViewById(R.id.ecatalog_track__back);
        mTrackBack.setOnClickListener(this);
        recyclerView = findViewById(R.id.content_ecatalog_track_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ecatalog_track__back:
                finish();
                break;
        }
    }
}
