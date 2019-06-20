package com.humming.asc.sales.activity;

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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.vo.cp.CPSummaryResultVO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryVO;
import com.humming.asc.dp.presentation.vo.cp.DCSummaryInfoVO;
import com.humming.asc.dp.presentation.vo.cp.VersionInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.VersionInfoVO;
import com.humming.asc.dp.presentation.vo.cp.postn.PositionInfoVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.FilterEntity;
import com.humming.asc.sales.activity.Stocks.SimpleSelectorActivity;
import com.humming.asc.sales.activity.plans.DailyCallDateActivity;
import com.humming.asc.sales.activity.product.ProFilterActivity;
import com.humming.asc.sales.activity.product.SearchProductActivity;
import com.humming.asc.sales.component.LockableViewPager;
import com.humming.asc.sales.component.ViewPagerAdapter;
import com.humming.asc.sales.content.ApprovalContent;
import com.humming.asc.sales.content.CustomerContent;
import com.humming.asc.sales.content.MyContent;
import com.humming.asc.sales.content.PlansContent;
import com.humming.asc.sales.content.ProductContent;
import com.humming.asc.sales.content.SettingsContent;
import com.humming.asc.sales.content.StocksContent;
import com.humming.asc.sales.model.ApprovalNewCountEvent;
import com.humming.asc.sales.model.BackRefreshOneEvent;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.DownLoadReceive;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.VersionService;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AbstractActivity implements View.OnClickListener {
    private ViewPagerAdapter adapter;
    public static LockableViewPager viewPager;

    private long mExitTime; //退出时间
    private String CURRENT_KEYACCOUNT_LEADS_OTHER_ACCOUNT = "Key Account";
    private ArrayList<String> ItemCode;//存放task中的product里的itemcode
    private int[] tabIcons = {R.mipmap.tab_icon_customer_n, R.mipmap.tab_icon_product_n, R.mipmap.tab_icon_plan_n, R.mipmap.tab_icon_approval_n, R.mipmap.tab_icon_my_n};
    private int[] tabSelectedIcons = {R.mipmap.tab_icon_customer_s, R.mipmap.tab_icon_product_s, R.mipmap.tab_icon_plan_s, R.mipmap.tab_icon_approval_s, R.mipmap.tab_icon_my_s};
    //    private TabLayout.Tab[] tabs;
    private final String[] titles = {"Key Account", "Products", "My Plans", "Approval", "My Settings"};
    private DailyCallService dailyCallService;
    private VersionService versionService;
    private MenuItem plansFilterMenu, seacherFilterMenu;
    public static MenuItem chainMenu;
    private SearchView msearchView;
    private CPSummaryVO summary;

    private CustomerContent customerContent;
    private StocksContent stocksContent;
    private ProductContent productContent;
    private PlansContent plansContent;
    private ApprovalContent approvalContent;
    private SettingsContent settingsContent;
    private MyContent myContent;
    public static Toolbar toolbar;
    public static Activity activity;
    private Application mAPP;
    private MyHandler mHandler;
    private VersionInfoVO versionInfo;
    public DownloadManager downloadManager;
    public static String stockCityPosition = "";
    public static boolean chainOrOther = false;
    public static ActionBar actionBar;
    public DownLoadReceive receiver;
    public static int REQUESTPERMISSION = 110;
    private LinearLayout toolbarSearchLayout;
    private LinearLayout toolbarSearch;
    private TextView toolbarSearchEd;
    private ImageView toolbarFilter;
    private AppBarLayout appBarLayout;
    private TextView filterDot;
    public TabLayout.TabLayoutOnPageChangeListener onPageChangeListener;
    public TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        appBarLayout = findViewById(R.id.activity_main_appbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        ItemCode = new ArrayList<String>();
        mAPP = (Application) getApplication();
        mHandler = new MyHandler();
        mAPP.setHandler(mHandler);

        toolbarSearchLayout = (LinearLayout) findViewById(R.id.toolbar_search_layout);
        toolbarSearch = (LinearLayout) findViewById(R.id.toolbar_search);
        toolbarFilter = (ImageView) findViewById(R.id.toolbar_filter);
        filterDot = findViewById(R.id.filter_dot);
        toolbarSearchEd = (TextView) findViewById(R.id.toolbar_search_ed);
        //toolbarSearch.setOnClickListener(this);
        toolbarFilter.setOnClickListener(this);
        toolbarSearchEd.setOnClickListener(this);
        downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
        tabLayout = (TabLayout) findViewById(R.id.content_main_tab_layout);
        final TabLayout.Tab[] tabs = {tabLayout.newTab().setText(R.string.tab_customer).setIcon(tabIcons[0]), tabLayout.newTab().setText(R.string.tab_stocks).setIcon(tabIcons[1]), tabLayout.newTab().setText(R.string.tab_plans).setIcon(tabIcons[2]), tabLayout.newTab().setText(R.string.tab_apprval).setIcon(tabIcons[3]), tabLayout.newTab().setText(R.string.tab_settings).setIcon(tabIcons[4])};

        tabLayout.addTab(tabs[0]);
        tabLayout.addTab(tabs[1]);
        tabLayout.addTab(tabs[2]);
        tabLayout.addTab(tabs[3]);
        tabLayout.addTab(tabs[4]);
        activity = this;
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        viewPager = (LockableViewPager) findViewById(R.id.content_main_viewpager);
        initPageContent();
        List<View> list = new ArrayList<View>();
        list.add(customerContent);
        list.add(productContent);
        list.add(plansContent);
        list.add(approvalContent);
        list.add(myContent);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        adapter = new ViewPagerAdapter(list);
        viewPager.setSwipeable(false);
        viewPager.setAdapter(adapter);
        onPageChangeListener = new TabLayout.TabLayoutOnPageChangeListener(tabLayout) {
            private int lastIndex = -1;

            @Override
            public void onPageSelected(int position) {
                if (lastIndex >= 0) {
                    tabs[lastIndex].setIcon(tabIcons[lastIndex]);
                }
                tabs[position].setIcon(tabSelectedIcons[position]);
                lastIndex = position;
                if (position == 0) {
                    setTitle(CustomerContent.Title);
                } else {
                    setTitle(titles[position]);
                }
                if (position == 4) {
                    appBarLayout.setVisibility(View.GONE);
                    myContent.initData();
                } else {
                    appBarLayout.setVisibility(View.VISIBLE);
                }
                if (position == 1) {
                    toolbarSearchLayout.setVisibility(View.VISIBLE);
                    // productContent.initData();
                } else {
                    toolbarSearchLayout.setVisibility(View.GONE);
                }
                invalidateOptionsMenu();
            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
        TabLayout.OnTabSelectedListener onTabSelectedListener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        };
        tabLayout.setOnTabSelectedListener(onTabSelectedListener);
        onPageChangeListener.onPageSelected(0);

        dailyCallService = Application.getDailyCallService();
        versionService = Application.getVersionService();
        dailyCallService.getSummary(new ICallback<CPSummaryResultVO>() {
            @Override
            public void onDataReady(CPSummaryResultVO result) {
                summary = result.getData();
                DCSummaryInfoVO info = summary.getWeekInfo();
                setPlansFilterData(info);
                plansContent.setSummaryData(summary);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        //查询默认职位
        versionService.queryDefaultPostn(new ICallback<PositionInfoVO>() {
            @Override
            public void onDataReady(PositionInfoVO data) {
                settingsContent.setPosition(data.getPositionName());
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        /*SettingService settingService = new SettingService();
        settingService.getItemcodeList(new IDataReadyCallback<List<String>>() {
            @Override
            public void onDataReady(List<String> list,
                                    RESTException restException) {
                if (list == null) {
                    return;
                }
                ItemCode.addAll(list);
                Application.getInstance().setItemCodeLists(ItemCode);
            }
        });*/

        checkUpdate();

    }

    /*public static void setPageChange(int position) {
        onPageChangeListener.onPageSelected(position);
        viewPager.setCurrentItem(position);
        tabLayout.getTabAt(position).select();
    }*/

    private void initPageContent() {
        Context context = getBaseContext();
        customerContent = new CustomerContent(context);
        //  stocksContent = new StocksContent(context);
        productContent = new ProductContent(context);
        plansContent = new PlansContent(context);
        approvalContent = new ApprovalContent(context);
        settingsContent = new SettingsContent(context);
        myContent = new MyContent(context);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        plansFilterMenu = menu.findItem(R.id.action_plans);
        seacherFilterMenu = menu.findItem(R.id.action_search_customer);
        msearchView = (SearchView) MenuItemCompat.getActionView(seacherFilterMenu);
        MenuItemCompat.setOnActionExpandListener(seacherFilterMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //点击返回按钮
                if (chainOrOther) {
                    //这里是chain的搜索操作
                    customerContent.setChainData("chain");
                } else if (customerContent.searchChainCus) {
                    setQueryDataByChainKeyWord("");
                } else {
                    setQueryDataByKeyWord("");
                }
                return true;
            }
        });
        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (chainOrOther) {
                    //这里是chain的搜索操作
                    customerContent.setQueryChainByKeyWord(query);
                } else if (customerContent.searchChainCus) {
                    setQueryDataByChainKeyWord(query);
                } else {
                    setQueryDataByKeyWord(query);
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (chainOrOther) {
                    //这里是chain的搜索操作
                    customerContent.setQueryChainByKeyWord(newText);
                } else {
                }
                return false;
            }
        });
        return true;
    }

    public boolean onPrepareOptionsMenu(Menu menu) {
        int pageNum = viewPager.getCurrentItem();
        menu.findItem(R.id.action_customer).setVisible(pageNum == 0);
        // menu.findItem(R.id.action_stocks).setVisible(pageNum == 1);
        seacherFilterMenu.setVisible(pageNum == 0);
        plansFilterMenu.setVisible(pageNum == 2);
        menu.findItem(R.id.action_date).setVisible(pageNum == 2);
        chainMenu = menu.findItem(R.id.menu_customer_chain);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        DCSummaryInfoVO info;
        switch (id) {
            case R.id.menu_plans_week:
                info = summary.getWeekInfo();
                item.setChecked(true);
                setPlansFilterData(info);
                break;
            case R.id.menu_plans_month:
                info = summary.getMonthInfo();
                item.setChecked(true);
                setPlansFilterData(info);
                break;
            case R.id.menu_plans_year:
                info = summary.getYearInfo();
                item.setChecked(true);
                setPlansFilterData(info);
                break;
            case R.id.menu_customer_K:
                chainOrOther = false;
                CURRENT_KEYACCOUNT_LEADS_OTHER_ACCOUNT = customerContent.KEY_ACCOUNT;
                setScreenData(customerContent.KEY_ACCOUNT);
                item.setChecked(true);
                break;
            case R.id.menu_customer_target_l:
                chainOrOther = false;
                CURRENT_KEYACCOUNT_LEADS_OTHER_ACCOUNT = customerContent.TARGET_LEADS;
                setScreenData(customerContent.TARGET_LEADS);
                item.setChecked(true);
                break;
            case R.id.menu_customer_untarget_l:
                chainOrOther = false;
                CURRENT_KEYACCOUNT_LEADS_OTHER_ACCOUNT = customerContent.UNTARGET_LEADS;
                setScreenData(customerContent.UNTARGET_LEADS);
                item.setChecked(true);
                break;
            case R.id.menu_customer_O:
                chainOrOther = false;
                CURRENT_KEYACCOUNT_LEADS_OTHER_ACCOUNT = customerContent.OTHER_ACCOUNT;
                setScreenData(customerContent.OTHER_ACCOUNT);
                item.setChecked(true);
                break;
            case R.id.action_date:
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallDateActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_customer_chain:
                chainOrOther = true;
                customerContent.setChainData("chain");
                item.setChecked(true);
                break;
            case android.R.id.home:
                chainOrOther = true;
                getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                customerContent.setChainData("chain");
                item.setChecked(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - mExitTime > 2000) {
                Toast.makeText(this, Application.getInstance().getString(R.string.application_exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void setPlansFilterData(DCSummaryInfoVO info) {
        plansContent.setInfoData(info);
    }

    //customer筛选
    private void setScreenData(String msg) {
        customerContent.setScreenData(msg);
    }

    //customer关键字查询
    private void setQueryDataByKeyWord(String msg) {
        customerContent.setQueryDataByKeyWord(msg, CURRENT_KEYACCOUNT_LEADS_OTHER_ACCOUNT);
    }

    // chain customer关键字查询
    private void setQueryDataByChainKeyWord(String msg) {
        customerContent.setQueryDataByChainKeyWord(msg);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String text = "";

        if (data != null) {
            text = data.getStringExtra(SimpleSelectorActivity.SELECTED_STRING);
            stockCityPosition = data.getStringExtra(SimpleSelectorActivity.SELECTED_POSITION);
        } else {
        }
        switch (requestCode) {
            case StocksContent.codeCode:
                TextView etCode = (TextView) findViewById(R.id.content_inventory__input_code);
                etCode.setText(text);
                break;
            case StocksContent.brandCode:
                TextView etBrand = (TextView) findViewById(R.id.content_inventory__input_brand);
                etBrand.setText(text);
                break;
            case StocksContent.categoryCode:
                TextView etCategory = (TextView) findViewById(R.id.content_inventory__input_category);
                etCategory.setText(text);
                break;
            case StocksContent.cityCode:
                TextView etCity = (TextView) findViewById(R.id.content_inventory__input_city);
                etCity.setText(text);
                break;
            case CustomerContent.RESULT_CODE:
                customerContent.updateData(text);
                break;
            case ProductContent.SEARCH_RESULT_CODE://关键字搜索
                // 搜索返回关键字
                filterDot.setVisibility(View.GONE);
                Application.getInstance().setUdcListResponse(null);
                toolbarSearchEd.setText(text);
                productContent.serach(text);
                break;
            case ProductContent.SEARCH_RESULT_CODE2://筛选
                FilterEntity datas = (FilterEntity) data.getExtras().getSerializable("data");
                if (Application.getInstance().getUdcListResponse() != null && Application.getInstance().getUdcListResponse().isUpdate()) {
                    filterDot.setVisibility(View.VISIBLE);
                    if (datas != null) {
                        productContent.filter(datas);
                    }
                } else {
                    filterDot.setVisibility(View.GONE);
                    if (datas != null) {
                        productContent.filter(datas);
                    }
                }
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_search://搜索
                //去产品搜索页面
                Intent intent = new Intent(this, SearchProductActivity.class);
                startActivityForResult(intent, ProductContent.SEARCH_RESULT_CODE);
                break;
            case R.id.toolbar_search_ed://搜索
                //去产品搜索页面
                Intent intent2 = new Intent(this, SearchProductActivity.class);
                startActivityForResult(intent2, ProductContent.SEARCH_RESULT_CODE);
                break;
            case R.id.toolbar_filter:
                Intent intent1 = new Intent(this, ProFilterActivity.class);
                startActivityForResult(intent1, ProductContent.SEARCH_RESULT_CODE2);
                break;
        }
    }

    public class MyHandler extends Handler {

        public String msgContent;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            DCSummaryInfoVO info;
            summary = Application.getInstance().getSummary();
            info = summary.getWeekInfo();
            setPlansFilterData(info);
            plansContent.setSummaryData(summary);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        plansContent.mHandlers.sendEmptyMessageDelayed(1, 3000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        plansContent.mHandlers.removeMessages(1);
    }

    //检查更新
    private void checkUpdate() {
        String netType = GetNetworkType();
        versionService.query(new ICallback<VersionInfoResultVO>() {
            @Override
            public void onDataReady(VersionInfoResultVO data) {
                versionInfo = data.getData();
                PackageInfo pi = null;
                PackageManager pm = getPackageManager();
                try {
                    pi = pm.getPackageInfo(getPackageName(), 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (Float.parseFloat(versionInfo.getAndrVersion()) > Float.parseFloat((pi.versionName))) {
                    //提示更新
                    myContent.setNewVersionDot(true, versionInfo.getAndrVersion());
                    if ("1".equals(versionInfo.getAndrNeedUpd())) {
                        showUpdataDialog();
                    }
                } else {
                }
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        // showUpdataDialog();
    }

    //提示更新的dailog
    private void showUpdataDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        // builer.setTitle(versionInfo.getAndrTitle());
        //  builer.setMessage(versionInfo.getAndrDescription().toString());
        builer.setTitle(versionInfo.getAndrTitle());
        StringBuilder sb = new StringBuilder();
        for (String msg : versionInfo.getAndrDescription()) {
            sb.append(msg + "\n");
        }
        builer.setMessage(sb.toString());
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton(Application.getInstance().getString(R.string.date_submit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                String netType = GetNetworkType();
                if ("wifi".equalsIgnoreCase(netType)) {
                    if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        //申请权
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
                    } else {
                        downLoadApk();
                    }
                } else {
                    dialog.dismiss();
                    showNetWorkDialog();
                }
            }
        });
        //如果是AndrMustUpdate = 1; 必须更新  0 则可以选择取消
        if ("0".equals(versionInfo.getAndrMustUpdate())) {
            //当点取消按钮时进行登录
            builer.setNegativeButton(Application.getInstance().getString(R.string.date_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        } else {
        }

        AlertDialog dialog = builer.create();
        dialog.setCancelable(false);
        dialog.show();
    }


    //提示更新的dailog
    private void showNetWorkDialog() {
        AlertDialog.Builder builer = new AlertDialog.Builder(this);
        builer.setTitle(Application.getInstance().getString(R.string.update_prompt));

        builer.setMessage(Application.getInstance().getString(R.string.net_type_message));
        //当点确定按钮时从服务器上下载 新的apk 然后安装
        builer.setPositiveButton(Application.getInstance().getString(R.string.date_submit), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    //申请权
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
                } else {
                    downLoadApk();
                }
            }
        });

        //当点取消按钮时进行登录
        builer.setNegativeButton(Application.getInstance().getString(R.string.date_cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showUpdataDialog();
            }
        });

        AlertDialog dialog = builer.create();
        dialog.setCancelable(false);
        dialog.show();
    }

    //下载apk
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
                    //  String apkUrl = versionInfo.getAndrLink();
                    String apkUrl = "http://101.231.101.70:8080/mobile-sales.apk";
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
                    SharedPreferences spf = MainActivity.this.getSharedPreferences("download", Activity.MODE_PRIVATE);
                    SharedPreferences.Editor editor = spf.edit();
                    editor.putLong("download_id", refernece);//保存下载ID
                    editor.commit();
                    receiver = new DownLoadReceive();
                    MainActivity.this.registerReceiver(receiver, new IntentFilter(
                            DownloadManager.ACTION_DOWNLOAD_COMPLETE));
                    //  installApk(refernece);
                    //    pd.dismiss(); //结束掉进度条对话框
                } catch (Exception e) {
                }
            }
        }.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //   unregisterReceiver(receiver);
    }

    /*//安装apk
    private void installApk(Long id) {
        Intent intent = new Intent();
        //执行动作
        intent.setAction(Intent.ACTION_VIEW);
        //执行的数据类型
        Uri downloadFileUri = downloadManager.getUriForDownloadedFile(id);
        intent.setDataAndType(downloadFileUri, "application/vnd.android.package-archive");
        startActivity(intent);
    }*/
    public String GetNetworkType() {
        String strNetworkType = "";

        ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();

                Log.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);

                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

            }
        }
        return strNetworkType;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUESTPERMISSION) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    downLoadApk();
                } else {
                    //提示没有权限，安装不了咯
                    Toast.makeText(MainActivity.this, getResources().getString(R.string.no_perssion), Toast.LENGTH_SHORT).show();
                    ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUESTPERMISSION);
                }
            }
        }
    }

    //eventBus回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleEvent(ApprovalNewCountEvent myEvent) {
        approvalContent.initData();
    }

    //eventBus回调
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleMessageEvent(BackRefreshOneEvent myEvent) {
        if ("消息刷新".equals(myEvent.getMessage())) {
            myContent.initData();
        } else if ("报价单刷新".equals(myEvent.getMessage())) {
            myContent.initData();
        }
    }

}



