package com.humming.asc.sales;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.alibaba.sdk.android.push.CloudPushService;
import com.alibaba.sdk.android.push.CommonCallback;
import com.alibaba.sdk.android.push.noonesdk.PushServiceFactory;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallVO;
import com.humming.asc.dp.presentation.vo.cp.leads.LeadsDetailVO;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.activity.TextEditorData;
import com.humming.asc.sales.activity.plans.TaskEditActivity;
import com.humming.asc.sales.model.DBDailycall;
import com.humming.asc.sales.model.DBLeads;
import com.humming.asc.sales.model.DBTask;
import com.humming.asc.sales.model.ImageItem;
import com.humming.asc.sales.model.product.MLAndHKChannelEntity;
import com.humming.asc.sales.model.product.UDCListResponse;
import com.humming.asc.sales.service.ApprovalService;
import com.humming.asc.sales.service.AuthService;
import com.humming.asc.sales.service.CustomerService;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.IAuthService;
import com.humming.asc.sales.service.ISettingService;
import com.humming.asc.sales.service.InfoService;
import com.humming.asc.sales.service.InventoryService;
import com.humming.asc.sales.service.LeadsService;
import com.humming.asc.sales.service.MAuthService;
import com.humming.asc.sales.service.SettingService;
import com.humming.asc.sales.service.Taskservice;
import com.humming.asc.sales.service.VersionService;
import com.humming.dto.ecatalogResponse.user.UserResponse;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Created by Zhtq on 12/22/15.
 */
public class Application extends android.app.Application {
    public static final String HEADER_TOKEN = "Token";
    private static IAuthService authService;
    private MainActivity.MyHandler handler = null;
    private TaskEditActivity.TaskHandler taskHandler = null;
    private static DailyCallService dailyCallService;

    private static CustomerService customerService;
    private static LeadsService leadsService;
    private static Taskservice taskservice;
    private static ISettingService settingService;
    private static InventoryService inventoryService;
    private static InfoService infoService;
    private static VersionService versionService;
    private static ApprovalService approvalService;
    private static Stack<Activity> activityStack;

    static {
        if (Config.DEBUG) {
            authService = new MAuthService();

        } else {
            authService = new AuthService();
            dailyCallService = new DailyCallService();
            customerService = new CustomerService();
            infoService = new InfoService();
            leadsService = new LeadsService();
            taskservice = new Taskservice();
            inventoryService = new InventoryService();
            versionService = new VersionService();
            approvalService = new ApprovalService();
        }
    }

    private static String token;
    private static String userId;
    private static int isLeadSale;


    private DailyCallVO dailyCall4Edit;
    private CPSummaryVO summary;


    private DailyCallDetailVO dailyCallDetail4Edit;
    private LeadsDetailVO leadsDetail4Edit;
    private TextEditorData textEditorData;
    private Activity currentActivity;
    private ArrayList<String> ItemCodeLists;
    private UDCListResponse udcListResponse;
    private UserResponse userResponse;
    private MLAndHKChannelEntity mlAndHKChannelEntity;

    // 数据库:
    private DBTask dbTask;
    private DBLeads dbLeads;
    private DBDailycall dbDailycall;

    public DBTask getDbTask() {
        return dbTask;
    }

    public void setDbTask(DBTask dbTask) {
        this.dbTask = dbTask;
    }

    public DBLeads getDbLeads() {
        return dbLeads;
    }

    public void setDbLeads(DBLeads dbLeads) {
        this.dbLeads = dbLeads;
    }

    public DBDailycall getDbDailycall() {
        return dbDailycall;
    }

    public void setDbDailycall(DBDailycall dbDailycall) {
        this.dbDailycall = dbDailycall;
    }

    public ArrayList<ImageItem> getImageList() {
        return imageList;
    }

    public void setImageList(ArrayList<ImageItem> imageList) {
        this.imageList = imageList;
    }

    private ArrayList<ImageItem> imageList;

    RetryPolicy policy = new DefaultRetryPolicy(10 * 1000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);

    /**
     * Log or request TAG
     */
    public static final String TAG = "Init";

    /**
     * Global request queue for Volley
     */
    private RequestQueue mRequestQueue;

    /**
     * A singleton instance of the application class for easy access in other places
     */
    private static Application sInstance;
    private static CloudPushService pushService;


    @Override
    public void onCreate() {
        super.onCreate();
        // initialize the singleton
        sInstance = this;
        initCloud();
        initCloudChannel(this);
    }

    //兼容8.0以上收不到推送
    private void initCloud() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            // 通知渠道的id
            String id = "1";
            // 用户可以看到的通知渠道的名字.
            CharSequence name = "notification channel";
            // 用户可以看到的通知渠道的描述
            String description = "notification description";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            @SuppressLint("WrongConstant")
            NotificationChannel mChannel = new NotificationChannel(id, name, importance);
            // 配置通知渠道的属性
            mChannel.setDescription(description);
            // 设置通知出现时的闪灯（如果 android 设备支持的话）
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.RED);
            // 设置通知出现时的震动（如果 android 设备支持的话）
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            //最后在notificationmanager中创建该通知渠道
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }


    /**
     * 初始化云推送通道
     *
     * @param applicationContext
     */
    private void initCloudChannel(Context applicationContext) {
        PushServiceFactory.init(applicationContext);
        pushService = PushServiceFactory.getCloudPushService();
        pushService.register(applicationContext, new CommonCallback() {
            @Override
            public void onSuccess(String response) {
                Log.d(TAG, "init cloudchannel success");
                //pushService.getDeviceId();
            }

            @Override
            public void onFailed(String errorCode, String errorMessage) {
                Log.d(TAG, "init cloudchannel failed -- errorcode:" + errorCode + " -- errorMessage:" + errorMessage);
            }
        });
    }

    /**
     * @return Application singleton instance
     */

    public static synchronized Application getInstance() {
        return sInstance;
    }


    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }
        activityStack.add(activity);
    }

    /**
     * @return The Volley Request queue, the queue will be created if it is null
     */
    public RequestQueue getRequestQueue() {
        // lazy initialize the request queue, the queue instance will be
        // created when it is accessed for the first time
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }

        return mRequestQueue;
    }

    /**
     * Adds the specified request to the global queue, if tag is specified
     * then it is used else Default TAG is used.
     *
     * @param req
     * @param tag
     */
    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);

        VolleyLog.d("Adding request to queue: %s", req.getUrl());

        getRequestQueue().add(req);
    }

    /**
     * Adds the specified request to the global queue using the Default TAG.
     *
     * @param req
     */
    public <T> void addToRequestQueue(Request<T> req) {
        // set the default tag if tag is empty
        req.setTag(TAG);
        req.setRetryPolicy(policy);
        getRequestQueue().add(req);
    }

    /**
     * Cancels all pending requests by the specified TAG, it is important
     * to specify a TAG so that the pending/ongoing requests can be cancelled.
     *
     * @param tag
     */
    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    public static String getToken() {
        return token;
    }

    public static void setToken(String token) {
        Application.token = token;
    }

    public static int getIsLeadSale() {
        return isLeadSale;
    }

    public static void setIsLeadSale(int isLeadSale) {
        Application.isLeadSale = isLeadSale;
    }

    public static String getUserId() {
        return userId;
    }

    public static void setUserId(final String userId) {
        Application.userId = userId;
        pushService.bindAccount(userId, new CommonCallback() {
            @Override
            public void onSuccess(String s) {
                Log.d(TAG, "init bind success");
            }

            @Override
            public void onFailed(String s, String s1) {
                Log.d(TAG, "init bind failed");
            }
        });
    }

    public Activity getCurrentActivity() {
        return currentActivity;
    }

    public void setCurrentActivity(Activity currentActivity) {
        this.currentActivity = currentActivity;
    }

    public CPSummaryVO getSummary() {
        return summary;
    }

    public void setSummary(CPSummaryVO summary) {
        this.summary = summary;
    }

    public DailyCallVO getDailyCall4Edit() {
        return dailyCall4Edit;
    }

    public void setDailyCall4Edit(DailyCallVO dailyCall4Edit) {
        this.dailyCall4Edit = dailyCall4Edit;
    }

    public TextEditorData getTextEditorData() {
        return textEditorData;
    }

    public void setTextEditorData(TextEditorData textEditorData) {
        this.textEditorData = textEditorData;
    }

    public IAuthService getAuthService() {
        return authService;
    }

    public static DailyCallService getDailyCallService() {
        return dailyCallService;
    }

    public static LeadsService getLeadsService() {
        return leadsService;
    }

    public static Taskservice getTaskservice() {
        return taskservice;
    }

    public ArrayList<String> getItemCodeLists() {
        return ItemCodeLists;
    }

    public void setItemCodeLists(ArrayList<String> itemCodeLists) {
        ItemCodeLists = itemCodeLists;
    }

    public static CustomerService getCustomerService() {
        return customerService;
    }

    public static InfoService getInfoService() {
        return infoService;
    }

    public DailyCallDetailVO getDailyCallDetail4Edit() {
        return dailyCallDetail4Edit;
    }

    public static VersionService getVersionService() {
        return versionService;
    }

    public static ApprovalService getApprovalService() {
        return approvalService;
    }

    public void setDailyCallDetail4Edit(DailyCallDetailVO dailyCallDetail4Edit) {
        this.dailyCallDetail4Edit = dailyCallDetail4Edit;
    }

    public LeadsDetailVO getLeadsDetail4Edit() {
        return leadsDetail4Edit;
    }

    public void setLeadsDetail4Edit(LeadsDetailVO leadsDetail4Edit) {
        this.leadsDetail4Edit = leadsDetail4Edit;
    }

    public static ISettingService getSettingService() {

        settingService = new SettingService();

        return settingService;
    }

    public static InventoryService getInventoryService() {
        return inventoryService;
    }

    // set方法
    public void setHandler(MainActivity.MyHandler handler) {
        this.handler = handler;
    }

    public TaskEditActivity.TaskHandler getTaskHandler() {
        return taskHandler;
    }

    public void setTaskHandler(TaskEditActivity.TaskHandler taskHandler) {
        this.taskHandler = taskHandler;
    }

    // get方法
    public MainActivity.MyHandler getHandler() {
        return handler;
    }


    public UDCListResponse getUdcListResponse() {
        return udcListResponse;
    }

    public void setUdcListResponse(UDCListResponse udcListResponse) {
        this.udcListResponse = udcListResponse;
    }

    public UserResponse getUserResponse() {
        return userResponse;
    }

    public void setUserResponse(UserResponse userResponse) {
        this.userResponse = userResponse;
    }

    public MLAndHKChannelEntity getMlAndHKChannelEntity() {
        return mlAndHKChannelEntity;
    }

    public void setMlAndHKChannelEntity(MLAndHKChannelEntity mlAndHKChannelEntity) {
        this.mlAndHKChannelEntity = mlAndHKChannelEntity;
    }
}
