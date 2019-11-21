package com.humming.asc.sales.content;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.RequestNull;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.activity.ecatalog.MyEcatalogActivity;
import com.humming.asc.sales.activity.settings.MyCollectActivity;
import com.humming.asc.sales.activity.settings.MyDraftsActivity;
import com.humming.asc.sales.activity.settings.MyMessageActivity;
import com.humming.asc.sales.activity.settings.MySettingActivity;
import com.humming.asc.sales.activity.settings.PositionListActivity;
import com.humming.asc.sales.service.DownLoadReceive;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.user.UserResponse;
import com.squareup.okhttp.Request;

/**
 * Created by Ztq on 3/27/19.
 */
public class MyContent extends LinearLayout implements View.OnClickListener {

    private final View view;
    private Context context;
    private ImageView mMyMessageToolbar;
    private ImageView mMyHeadimgToolbar;
    private TextView mMyUsernameToolbar;
    private TextView mMyCustomerFragment;
    private TextView mMyApprovalFragment;
    private TextView mMyEcatalogTextFragment;
    private LinearLayout mMyEcatalogFragment;
    private TextView mMyMessageValueFragment;
    private LinearLayout mMyCollectFragment;
    private LinearLayout mMyDraftFragment;
    private LinearLayout mMyPositionFragment;
    private LinearLayout mMySettingFragment;
    private LinearLayout mMyVersionFragment;
    private LinearLayout mMyMessageLayout;
    private LinearLayout mMyApprovalLayout;
    private LinearLayout mMyCustomerLayout;
    private TextView messageDot;
    private TextView versionDot;
    private TextView versionTv;
    private boolean isUpdate = false;
    private String version = "";
    public DownLoadReceive receiver;
    public DownloadManager downloadManager;
    private String andLink = "";

    public MyContent(Context context) {
        this(context, null);
    }

    public MyContent(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = inflate(context, R.layout.fragment_my, this);
        initView(view);
        //initData();
    }

    public void initData() {
        final RequestNull request = new RequestNull();
        OkHttpClientManager.postAsyn(Config.ECATALLOG_USER_DETAIL, new OkHttpClientManager.ResultCallback<UserResponse>() {
            @Override
            public void onError(Request request, Error info) {
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(UserResponse response) {
                Application.getInstance().setUserResponse(response);
                Glide.with(Application.getInstance().getCurrentActivity())
                        .load(response.getAtatarUrl())
                        .error(me.nereo.multi_image_selector.R.drawable.default_error)
                        .centerCrop()
                        .into(mMyHeadimgToolbar);
                mMyUsernameToolbar.setText(response.getName());
                mMyEcatalogTextFragment.setText(response.getEcatalogNum() + "");
                if (response.getNoticeNum() > 0) {
                    messageDot.setVisibility(View.VISIBLE);
                } else {
                    messageDot.setVisibility(View.GONE);
                }
                mMyMessageValueFragment.setText(response.getNotice());
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, UserResponse.class);
    }

    private void initView(View itemView) {
        downloadManager = (DownloadManager) Application.getInstance().getCurrentActivity().getSystemService(Application.getInstance().getCurrentActivity().DOWNLOAD_SERVICE);
        messageDot = itemView.findViewById(R.id.fragment_my_message_dot);
        versionDot = itemView.findViewById(R.id.fragment_my_version_dot);
        versionTv = itemView.findViewById(R.id.fragment_my_version_value);
        mMyMessageLayout = itemView.findViewById(R.id.fragment_my_message);
        mMyMessageLayout.setOnClickListener(this);
        mMyEcatalogFragment = itemView.findViewById(R.id.fragment_my_ecatalog);
        mMyMessageToolbar = (ImageView) itemView.findViewById(R.id.toolbar_my_message);
        mMyMessageToolbar.setOnClickListener(this);
        mMyHeadimgToolbar = (ImageView) itemView.findViewById(R.id.toolbar_my_headimg);
        mMyUsernameToolbar = (TextView) itemView.findViewById(R.id.toolbar_my_username);
        mMyCustomerFragment = (TextView) itemView.findViewById(R.id.fragment_my_customer_text);
        mMyApprovalFragment = (TextView) itemView.findViewById(R.id.fragment_my_approval_text);
        mMyEcatalogTextFragment = (TextView) itemView.findViewById(R.id.fragment_my_ecatalog_text);
        mMyEcatalogFragment.setOnClickListener(this);
        mMyMessageValueFragment = (TextView) itemView.findViewById(R.id.fragment_my_message_value);
        mMyCollectFragment = (LinearLayout) itemView.findViewById(R.id.fragment_my_collect);
        mMyCollectFragment.setOnClickListener(this);
        mMyDraftFragment = (LinearLayout) itemView.findViewById(R.id.fragment_my_draft);
        mMyDraftFragment.setOnClickListener(this);
        mMyPositionFragment = (LinearLayout) itemView.findViewById(R.id.fragment_my_position);
        mMyPositionFragment.setOnClickListener(this);
        mMySettingFragment = (LinearLayout) itemView.findViewById(R.id.fragment_my_setting);
        mMySettingFragment.setOnClickListener(this);
        mMyVersionFragment = (LinearLayout) itemView.findViewById(R.id.fragment_my_version);
        mMyVersionFragment.setOnClickListener(this);
        mMyApprovalLayout = itemView.findViewById(R.id.fragment_my_approval);
        mMyApprovalLayout.setOnClickListener(this);
        mMyCustomerLayout = itemView.findViewById(R.id.fragment_my_customer);
        mMyCustomerLayout.setOnClickListener(this);
        PackageManager pm = context.getPackageManager();
        try {
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(),
                    0);
            versionTv.setText("V  " + pi.versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_my_message:
                Intent intentMessage = new Intent(Application.getInstance().getCurrentActivity(), MyMessageActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intentMessage);
                break;
            case R.id.fragment_my_ecatalog:
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), MyEcatalogActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                break;
            case R.id.fragment_my_collect:
                Intent collectIntent = new Intent(Application.getInstance().getCurrentActivity(), MyCollectActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(collectIntent);

                break;
            case R.id.fragment_my_draft:
                Intent intents = new Intent(Application.getInstance().getCurrentActivity(), MyDraftsActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intents);
                break;
            case R.id.fragment_my_position:
                Application.getInstance().getCurrentActivity().startActivity(new Intent(Application.getInstance().getCurrentActivity(), PositionListActivity.class));

                break;
            case R.id.fragment_my_setting:
                Intent intentSetting = new Intent(Application.getInstance().getCurrentActivity(), MySettingActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intentSetting);
                break;
            case R.id.fragment_my_version:
                if (isUpdate) {
                    showTip(Application.getInstance().getCurrentActivity());
                } else {
                    Toast.makeText(context, R.string.latest_version_tip, Toast.LENGTH_SHORT).show();
                }
                // Intent versionIntent = new Intent(Application.getInstance().getCurrentActivity(), UserMangerActivity.class);
                // Application.getInstance().getCurrentActivity().startActivity(versionIntent);
                break;
            case R.id.fragment_my_message:
                Intent intentMessages = new Intent(Application.getInstance().getCurrentActivity(), MyMessageActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(intentMessages);
                break;
            case R.id.fragment_my_approval:
                //MainActivity.setPageChange(3);
                break;
            case R.id.fragment_my_customer:
                // MainActivity.setPageChange(0);
                break;
            default:
                break;
        }
    }

    private void showTip(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setMessage(context.getString(R.string.a_new_version) + " V " + version + " " + context.getString(R.string.is_update));
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        //设置正面按钮
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ContextCompat.checkSelfPermission(Application.getInstance().getCurrentActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权
                    ActivityCompat.requestPermissions(Application.getInstance().getCurrentActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MainActivity.REQUESTPERMISSION);
                } else {
                    downLoadApk();
                }
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

    public void setNewVersionDot(boolean b, String version, String link) {
        this.version = version;
        andLink = link;
        isUpdate = b;
        if (b) {
            versionDot.setVisibility(View.VISIBLE);
        } else
            versionDot.setVisibility(View.GONE);
    }

    public void downLoadApk() {
        //    final ProgressDialog pd;    //进度条对话框
        //   pd = new ProgressDialog(this);
        //   pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        //   pd.setMessage("正在下载更新");
        //    pd.show();
        //下载apk
        new Thread() {
            @Override
            public void run() {
                try {
                    String apkUrl = andLink;
                    // String apkUrl = "http://101.231.101.70:8080/mobile-sales.apk";
                    DownloadManager.Request request = new DownloadManager.Request(Uri.parse(apkUrl));
                    request.setDestinationInExternalPublicDir("download", "salesMobile.apk");
                    request.setDescription(Application.getInstance().getResources().getString(R.string.new_version));
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
                    request.setMimeType("application/vnd.android.package-archive");
                    // 设置为可被媒体扫描器找到
                    request.allowScanningByMediaScanner();
                    // 设置为可见和可管理
                    request.setVisibleInDownloadsUi(true);
                    long refernece = downloadManager.enqueue(request);
                    SharedPreferences spf = Application.getInstance().getCurrentActivity().getSharedPreferences("download", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putLong("download_id", refernece);//保存下载ID
                    editor.commit();
                    receiver = new DownLoadReceive();
                    Application.getInstance().getCurrentActivity().registerReceiver(receiver, new IntentFilter(
                            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    //  installApk(refernece);
                    //    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }

}
