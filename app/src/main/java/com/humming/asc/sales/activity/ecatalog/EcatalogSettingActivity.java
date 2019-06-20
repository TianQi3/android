package com.humming.asc.sales.activity.ecatalog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.EcatalogDetailRequest;
import com.humming.asc.sales.RequestData.EcatalogSettingRequest;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.ecatalog.SetResponse;
import com.humming.dto.response.base.ReturnStatus;
import com.squareup.okhttp.Request;

import org.greenrobot.eventbus.EventBus;

public class EcatalogSettingActivity extends AbstractActivity implements View.OnClickListener {

    private ImageView back;
    private Switch mReadNotificationSwitch;
    private Switch mTempateSwitch;
    private TextView mDelete;
    private String ecatalogId = "";
    private boolean isOneRead = false;//是否第一次
    private boolean isOneTempte = false;//是否第一次
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecatalog_setting);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        ecatalogId = getIntent().getStringExtra("ecatalog_id");
        customDialog = new CustomDialog(this, "Loading...");
        initView();
        initData();
    }

    private void initData() {
        customDialog.show();
        EcatalogSettingRequest request = new EcatalogSettingRequest();
        request.setId(ecatalogId);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_SET, new OkHttpClientManager.ResultCallback<SetResponse>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(SetResponse response) {
                customDialog.dismiss();
                if (response.getReadNotification() != null && response.getReadNotification().intValue() == 1) {
                    isOneRead = false;
                    mReadNotificationSwitch.setChecked(true);
                    isOneRead = true;
                } else {
                    isOneRead = true;
                }
                if (response.getSaveTemplate() != null && response.getSaveTemplate().intValue() == 1) {
                    isOneTempte = false;
                    mTempateSwitch.setChecked(true);
                    isOneTempte = true;
                } else {
                    isOneTempte = true;
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, SetResponse.class);
    }

    private void initView() {
        back = findViewById(R.id.ecatalog_create__back);
        back.setOnClickListener(this);

        mReadNotificationSwitch = (Switch) findViewById(R.id.content_setting_ecatalog_read_notification_switch);
        mTempateSwitch = (Switch) findViewById(R.id.content_setting_ecatalog_tempate_switch);
        mDelete = (TextView) findViewById(R.id.content_setting_ecatalog_delete);
        mDelete.setOnClickListener(this);
        //已读通知选中
        mReadNotificationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isOneRead) {

                } else {
                    if (isChecked) {
                        EcatalogSettingRequest request = new EcatalogSettingRequest();
                        request.setReadNotification("1");
                        saveSet(request);
                    } else {
                        EcatalogSettingRequest request = new EcatalogSettingRequest();
                        request.setReadNotification("0");
                        saveSet(request);
                    }
                }
            }
        });
        //生成模版选中
        mTempateSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isOneTempte) {

                } else {
                    if (isChecked) {
                        EcatalogSettingRequest request = new EcatalogSettingRequest();
                        request.setSaveTemplate("1");
                        saveSet(request);
                    } else {
                        EcatalogSettingRequest request = new EcatalogSettingRequest();
                        request.setSaveTemplate("0");
                        saveSet(request);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ecatalog_create__back:
                finish();
                break;
            case R.id.content_setting_ecatalog_delete:
                showTip();
                break;
        }
    }

    private void showTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_tip);
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        //设置正面按钮
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                deleteEcatalog();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton(getResources().getString(R.string.cancel1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    //删除报价单
    private void deleteEcatalog() {
        final EcatalogDetailRequest request = new EcatalogDetailRequest();
        request.setId(ecatalogId);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_DELETE, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ReturnStatus response) {
                if ("success".equals(response.getStatus())) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.delect_success), Toast.LENGTH_SHORT).show();
                    EventBus.getDefault().post(new BackRefreshOneEvent("1"));
                    finish();
                    EcatalogDetailActivity.activity.finish();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ReturnStatus.class);
    }


    //修改报价单
    private void saveSet(EcatalogSettingRequest request) {
        customDialog.show();
        request.setId(ecatalogId);
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ECATALOG_SAVESET, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
                Toast.makeText(Application.getInstance().getCurrentActivity(), info.getInfo(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(ReturnStatus response) {
                customDialog.dismiss();
                if (response != null && "success".equals(response.getStatus())) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.edit_success), Toast.LENGTH_SHORT).show();
                    // finish();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ReturnStatus.class);
    }

}
