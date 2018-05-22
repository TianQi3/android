package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.ro.cp.leads.AddLeadsRO;
import com.humming.asc.dp.presentation.ro.cp.leads.UpdateLeadsRO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryResultVO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.leads.LeadsDetailVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.activity.Stocks.SimpleSelectorActivity;
import com.humming.asc.sales.activity.TextEditorActivity;
import com.humming.asc.sales.activity.TextEditorData;
import com.humming.asc.sales.activity.customer.CustomerSelectorActivity;
import com.humming.asc.sales.activity.settings.DraftsLeadsListActivity;
import com.humming.asc.sales.datebase.DBManger;
import com.humming.asc.sales.model.DBLeads;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.LeadsService;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Zhtq on 16/1/19.
 */
public class LeadsAddActivity extends AbstractActivity {
    public static String LEADS_ITEM_ROWID = "leads_item_rowid";
    private Switch switch1;
    private TextEditorData textEditorData;
    public static final String TYPE_OR_STATUS_NAME = "name";
    public static final String TYPE_OR_STATUS_SALE_EID = "eid";
    public static final String TARGET_OPEN_CLOSE = "target_open_close";
    public static final String TARGET_SCROLL = "target_scroll";
    public static final String CUSTOMER_TARGET_BACK = "customer_target_back";
    public static final String DRAFTS_LEADS = "drafts_leads";
    private boolean modifyEnble = false;
    private Context context = Application.getInstance().getBaseContext();
    private TextView salesNameValue, englishNameValue, chineseNameValue, ClassessValue, statusValue, reginValue, provinceValue, cityValue,
            addressValue, channelValue, subChannelValue, chainValue, subChainValue, contactFristNameValue, contactLastNameValue, jobTitleValue, workPhoneValue,
            mobilePhoneValue, emailValue, customerValue, stageValue, estimateMonthValue, estimateSalesValue, customerDemandValue, developerPlanValue,
            customerTag1value, customerTag2value, customerTag3value, customerTag4value, customerTag5value, switchCheck, dailycallValue;
    private View itemSalesName, itemenglishName, itemChineseName, itemClasses, itemStatus, itemRegion, itemProvince, itemCity,
            itemaddress, itemchannel, itemsubChannel, itemchain, itemsubChain, itemcontactFristName, itemcontactLastName, itemjobTitle, itemworkPhone,
            itemmobilePhone, itememail, itemcustomer, itemstage, itemestimateMonth, itemestimateSales, itemcustomerDemand, itemdeveloperPlan,
            itemcustomerTag1, itemcustomerTag2, itemcustomerTag3, itemcustomerTag4, itemcustomerTag5, itemDailyCall;
    private View TargetShow;
    private int year, monthOfYear, dayOfMonth;
    private static final int RESULT_SALES = 10040;
    private LeadsDetailVO leadsVo;
    private boolean editOrAdd = false;
    private LeadsService service;
    private String customerId = "";
    private String salesEid = "";
    public static String CUSTOMER_TYPE = "cus_tp";
    private ScrollView nes;
    private Application mAPP;
    private String draftsOrNo = "";
    private MainActivity.MyHandler mHandler;
    private DailyCallService dailyCallService;
    private CPSummaryVO summary;
    private DBLeads dbLeads;
    private DBManger dbManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_leads);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        nes = (ScrollView) findViewById(R.id.activity_add_leads_scroll);
        mAPP = (Application) getApplication();
        // 获得共享变量实例
        mHandler = mAPP.getHandler();
        dailyCallService = Application.getDailyCallService();
        String switchs = getIntent().getStringExtra(TARGET_OPEN_CLOSE);
        service = Application.getLeadsService();
        initView();
        leadsVo = Application.getInstance().getLeadsDetail4Edit();
        draftsOrNo = getIntent().getStringExtra(DRAFTS_LEADS);
        //判断是否是草稿箱来的
        if ("true".equalsIgnoreCase(draftsOrNo)) {
            dbLeads = Application.getInstance().getDbLeads();
            //设置title
            if ("new".equalsIgnoreCase(dbLeads.getRemark())) {
                actionBar.setTitle(R.string.title_activity_add_leads);
                itemDailyCall.setVisibility(View.GONE);
            } else {
                editOrAdd = true;
                actionBar.setTitle(R.string.title_activity_edit_leads);
                itemDailyCall.setVisibility(View.GONE);
                salesEid = dbLeads.getSaleEid();
                if ("Y".equalsIgnoreCase(dbLeads.getTargetFlg())) {
                    itemDailyCall.setVisibility(View.VISIBLE);
                    switch1.setChecked(true);
                    TargetShow.setVisibility(View.VISIBLE);
                    switchCheck.setText(Application.getInstance().getString(R.string.leads_target_yes));
                }
            }
            setDraftsLeads();
        } else {
            if (leadsVo == null) {//add leads
                actionBar.setTitle(R.string.title_activity_add_leads);
                itemDailyCall.setVisibility(View.GONE);
            } else {//edit leads
                editOrAdd = true;
                itemDailyCall.setVisibility(View.GONE);
                salesEid = leadsVo.getSaleEid();
                actionBar.setTitle(R.string.title_activity_edit_leads);
                setvalue();
                if (switchs != null) {
                    itemDailyCall.setVisibility(View.VISIBLE);
                    switch1.setChecked(true);
                    TargetShow.setVisibility(View.VISIBLE);
                    switchCheck.setText(Application.getInstance().getString(R.string.leads_target_yes));
                    if (getIntent().getStringExtra(TARGET_SCROLL) != null) {
                        nes.post(new Runnable() {
                            @Override
                            public void run() {
                                scrollToView(nes, switch1);
                            }
                        });
                    }
                }
            }
        }
    }

    private void setDraftsLeads() {
        salesNameValue.setText(dbLeads.getSaleName().toString());
        englishNameValue.setText(dbLeads.getNameEn().toString());
        chineseNameValue.setText(dbLeads.getNameCn().toString());
        ClassessValue.setText(dbLeads.getClasses().toString());
        statusValue.setText(dbLeads.getStatus().toString());
        reginValue.setText(dbLeads.getRegion().toString());
        provinceValue.setText(dbLeads.getProvince().toString());
        cityValue.setText(dbLeads.getCity().toString());
        addressValue.setText(dbLeads.getAddress().toString());
        channelValue.setText(dbLeads.getChannel().toString());
        subChannelValue.setText(dbLeads.getSubChannel().toString());
        chainValue.setText(dbLeads.getChain().toString());
        subChainValue.setText(dbLeads.getSubChain().toString());
        contactFristNameValue.setText(dbLeads.getConFstName().toString());
        contactLastNameValue.setText(dbLeads.getConLastName().toString());
        jobTitleValue.setText(dbLeads.getJobTitle().toString());
        workPhoneValue.setText(dbLeads.getWorkPhNum().toString());
        mobilePhoneValue.setText(dbLeads.getCellPhNum().toString());
        emailValue.setText(dbLeads.getEmail().toString());
        customerValue.setText(dbLeads.getNameEn().toString());
        stageValue.setText(dbLeads.getStage().toString());
        estimateMonthValue.setText(dbLeads.getEstDate().toString());
        estimateSalesValue.setText(dbLeads.getEstSales().toString());
        customerDemandValue.setText(dbLeads.getDemand().toString());
        developerPlanValue.setText(dbLeads.getDevPlan().toString());
        customerTag1value.setText(dbLeads.getTag1().toString());
        customerTag2value.setText(dbLeads.getTag2().toString());
        customerTag3value.setText(dbLeads.getTag3().toString());
        customerTag4value.setText(dbLeads.getTag4().toString());
        customerTag5value.setText(dbLeads.getTag5().toString());
        dailycallValue.setText("");
    }

    private void setvalue() {
        salesNameValue.setText(leadsVo.getSaleName().toString());
        englishNameValue.setText(leadsVo.getNameEn().toString());
        chineseNameValue.setText(leadsVo.getNameCn().toString());
        ClassessValue.setText(leadsVo.getClasses().toString());
        statusValue.setText(leadsVo.getStatus().toString());
        reginValue.setText(leadsVo.getRegion().toString());
        provinceValue.setText(leadsVo.getProvince().toString());
        cityValue.setText(leadsVo.getCity().toString());
        addressValue.setText(leadsVo.getAddress().toString());
        channelValue.setText(leadsVo.getChannel().toString());
        subChannelValue.setText(leadsVo.getSubChannel().toString());
        chainValue.setText(leadsVo.getChain().toString());
        subChainValue.setText(leadsVo.getSubChain().toString());
        contactFristNameValue.setText(leadsVo.getConFstName().toString());
        contactLastNameValue.setText(leadsVo.getConLastName().toString());
        jobTitleValue.setText(leadsVo.getJobTitle().toString());
        workPhoneValue.setText(leadsVo.getWorkPhNum().toString());
        mobilePhoneValue.setText(leadsVo.getCellPhNum().toString());
        emailValue.setText(leadsVo.getEmail().toString());
        customerValue.setText(leadsVo.getAccountName().toString());
        stageValue.setText(leadsVo.getStage().toString());
        estimateMonthValue.setText(leadsVo.getEstDate().toString());
        /*if (!"".equals(estimateMonthValue.getText().toString())) {
            modifyEnble = true;
        } else {
            modifyEnble = false;
        }*/
        estimateSalesValue.setText(leadsVo.getEstSales().toString());
        customerDemandValue.setText(leadsVo.getDemand().toString());
        developerPlanValue.setText(leadsVo.getDevPlan().toString());
        customerTag1value.setText(leadsVo.getTag1().toString());
        customerTag2value.setText(leadsVo.getTag2().toString());
        customerTag3value.setText(leadsVo.getTag3().toString());
        customerTag4value.setText(leadsVo.getTag4().toString());
        customerTag5value.setText(leadsVo.getTag5().toString());
        dailycallValue.setText(leadsVo.getDcNum() + "");
    }

    private void initView() {
        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v.getId());
            }
        };
        salesNameValue = (TextView) findViewById(R.id.content_add_leads__sales_name_value);
        englishNameValue = (TextView) findViewById(R.id.content_add_leads__english_name_value);
        chineseNameValue = (TextView) findViewById(R.id.content_add_leads__chinese_name_value);
        ClassessValue = (TextView) findViewById(R.id.content_add_leads__classes_value);
        statusValue = (TextView) findViewById(R.id.content_add_leads__status_value);
        reginValue = (TextView) findViewById(R.id.content_add_leads__region_value);
        provinceValue = (TextView) findViewById(R.id.content_add_leads__province_value);
        cityValue = (TextView) findViewById(R.id.content_add_leads__city_value);
        addressValue = (TextView) findViewById(R.id.content_add_leads__address_value);
        channelValue = (TextView) findViewById(R.id.content_add_leads__channal_value);
        subChannelValue = (TextView) findViewById(R.id.content_add_leads__subchannal_value);
        chainValue = (TextView) findViewById(R.id.content_add_leads__chain_value);
        subChainValue = (TextView) findViewById(R.id.content_add_leads__sub_chain_value);
        contactFristNameValue = (TextView) findViewById(R.id.content_add_leads__constact_frist_value);
        contactLastNameValue = (TextView) findViewById(R.id.content_add_leads__constact_last_value);
        jobTitleValue = (TextView) findViewById(R.id.content_add_leads__jobtitle_value);
        workPhoneValue = (TextView) findViewById(R.id.content_add_leads__workphone_value);
        mobilePhoneValue = (TextView) findViewById(R.id.content_add_leads__mobilephone_value);
        emailValue = (TextView) findViewById(R.id.content_add_leads__email_value);
        customerValue = (TextView) findViewById(R.id.content_add_leads__target_customer_value);
        stageValue = (TextView) findViewById(R.id.content_add_leads__target_stage_value);
        estimateMonthValue = (TextView) findViewById(R.id.content_add_leads__estimate_month_value);
        estimateSalesValue = (TextView) findViewById(R.id.content_add_leads__estimate_sales_value);
        customerDemandValue = (TextView) findViewById(R.id.content_add_leads__customer_demand_value);
        developerPlanValue = (TextView) findViewById(R.id.content_add_leads__developement_plan_value);
        customerTag1value = (TextView) findViewById(R.id.content_add_leads__customer_tag_value);
        customerTag2value = (TextView) findViewById(R.id.content_add_leads__customer_tag2_value);
        customerTag3value = (TextView) findViewById(R.id.content_add_leads__customer_tag3_value);
        customerTag4value = (TextView) findViewById(R.id.content_add_leads__customer_tag4_value);
        customerTag5value = (TextView) findViewById(R.id.content_add_leads__customer_tag5_value);
        switchCheck = (TextView) findViewById(R.id.content_add_leads__Target_checked);
        dailycallValue = (TextView) findViewById(R.id.content_add_leads__daily_call_value);

        itemSalesName = findViewById(R.id.content_add_leads__sales_name);
        itemenglishName = findViewById(R.id.content_add_leads__english_name);
        itemChineseName = findViewById(R.id.content_add_leads__chinese_name);
        itemClasses = findViewById(R.id.content_add_leads__classes);
        itemStatus = findViewById(R.id.content_add_leads__status);
        itemRegion = findViewById(R.id.content_add_leads__region);
        itemProvince = findViewById(R.id.content_add_leads__province);
        itemCity = findViewById(R.id.content_add_leads__city);
        itemaddress = findViewById(R.id.content_add_leads__address);
        itemchannel = findViewById(R.id.content_add_leads__channal);
        itemsubChannel = findViewById(R.id.content_add_leads__subchannal);
        itemchain = findViewById(R.id.content_add_leads__chain);
        itemsubChain = findViewById(R.id.content_add_leads__sub_chain);
        itemcontactFristName = findViewById(R.id.content_add_leads__constact_frist);
        itemcontactLastName = findViewById(R.id.content_add_leads__constact_last);
        itemjobTitle = findViewById(R.id.content_add_leads__jobtitle);
        itemworkPhone = findViewById(R.id.content_add_leads__workphone);
        itemmobilePhone = findViewById(R.id.content_add_leads__mobilephone);
        itememail = findViewById(R.id.content_add_leads__email);
        itemcustomer = findViewById(R.id.content_add_leads__target_customer);
        itemstage = findViewById(R.id.content_add_leads__target_stage);
        itemestimateMonth = findViewById(R.id.content_add_leads__estimate_month);
        itemestimateSales = findViewById(R.id.content_add_leads__estimate_sales);
        itemcustomerDemand = findViewById(R.id.content_add_leads__customer_demand);
        itemdeveloperPlan = findViewById(R.id.content_add_leads__developement_plan);
        itemcustomerTag1 = findViewById(R.id.content_add_leads__customer_tag);
        itemcustomerTag2 = findViewById(R.id.content_add_leads__customer_tag2);
        itemcustomerTag3 = findViewById(R.id.content_add_leads__customer3_tag);
        itemcustomerTag4 = findViewById(R.id.content_add_leads__customer_tag4);
        itemcustomerTag5 = findViewById(R.id.content_add_leads__customer_tag5);
        itemDailyCall = findViewById(R.id.content_add_leads__daily_call);

        itemDailyCall.setOnClickListener(itemClickListener);
        itemSalesName.setOnClickListener(itemClickListener);
        itemenglishName.setOnClickListener(itemClickListener);
        itemChineseName.setOnClickListener(itemClickListener);
        itemClasses.setOnClickListener(itemClickListener);
        itemStatus.setOnClickListener(itemClickListener);
        itemRegion.setOnClickListener(itemClickListener);
        itemProvince.setOnClickListener(itemClickListener);
        itemCity.setOnClickListener(itemClickListener);
        itemaddress.setOnClickListener(itemClickListener);
        itemchannel.setOnClickListener(itemClickListener);
        itemsubChannel.setOnClickListener(itemClickListener);
        itemchain.setOnClickListener(itemClickListener);
        itemsubChain.setOnClickListener(itemClickListener);
        itemcontactFristName.setOnClickListener(itemClickListener);
        itemcontactLastName.setOnClickListener(itemClickListener);
        itemjobTitle.setOnClickListener(itemClickListener);
        itemworkPhone.setOnClickListener(itemClickListener);
        itemmobilePhone.setOnClickListener(itemClickListener);
        itememail.setOnClickListener(itemClickListener);
        itemcustomer.setOnClickListener(itemClickListener);
        itemstage.setOnClickListener(itemClickListener);
        itemestimateMonth.setOnClickListener(itemClickListener);
        itemestimateSales.setOnClickListener(itemClickListener);
        itemcustomerDemand.setOnClickListener(itemClickListener);
        itemdeveloperPlan.setOnClickListener(itemClickListener);
        itemcustomerTag1.setOnClickListener(itemClickListener);
        itemcustomerTag2.setOnClickListener(itemClickListener);
        itemcustomerTag3.setOnClickListener(itemClickListener);
        itemcustomerTag4.setOnClickListener(itemClickListener);
        itemcustomerTag5.setOnClickListener(itemClickListener);

        if (Application.getIsLeadSale() != 1) {
            //隐藏salesName
            itemSalesName.setVisibility(View.GONE);
        }
        switch1 = (Switch) findViewById(R.id.content_add_leads_switch);
        TargetShow = findViewById(R.id.content_add_leads_trun_off);
        switch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    TargetShow.setVisibility(View.VISIBLE);
                    if (leadsVo == null) {

                    } else {
                        itemDailyCall.setVisibility(View.VISIBLE);
                    }

                    switchCheck.setText(Application.getInstance().getString(R.string.leads_target_yes));
                } else {
                    TargetShow.setVisibility(View.GONE);
                    itemDailyCall.setVisibility(View.GONE);
                    switchCheck.setText(Application.getInstance().getString(R.string.leads_target_no));
                }
            }
        });
    }

    private void onItemClick(int id) {
        switch (id) {
            case R.id.content_add_leads__sales_name:
                Intent salesIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                salesIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SALES);
                startActivityForResult(salesIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SALES_SELECT);
                break;
            case R.id.content_add_leads__english_name:
                if (editOrAdd) {

                } else {
                    TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_ENGLISH_NAME, englishNameValue, Application.getInstance().getString(R.string.leads_english_names));
                }
                break;
            case R.id.content_add_leads__chinese_name:
                if (editOrAdd) {

                } else {
                    TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_CHINESE_NAME, chineseNameValue, Application.getInstance().getString(R.string.leads_chinese_name));
                }
                break;
            case R.id.content_add_leads__classes:
                Intent classIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                classIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CLASS);
                startActivityForResult(classIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CLASS_SELECT);
                break;
            case R.id.content_add_leads__status:
                Intent statusIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                statusIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_STATUS);
                startActivityForResult(statusIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_STATUS_SELECT);
                break;
            case R.id.content_add_leads__region:
                Intent regionIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                regionIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_REGION);
                startActivityForResult(regionIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_REGION_SELECT);
                break;
            case R.id.content_add_leads__province:
                Intent provinceIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                provinceIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_PROVINCE);
                startActivityForResult(provinceIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_PROVINCE_SELECT);
                break;
            case R.id.content_add_leads__city:
                if (!"".equals(provinceValue.getText().toString())) {
                    Intent cityIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                    cityIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CITY);
                    cityIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CITY_INTENT, provinceValue.getText().toString());
                    startActivityForResult(cityIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CITY_SELECT);
                } else {
                    Toast.makeText(this, Application.getInstance().getString(R.string.leads_city_enble), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.content_add_leads__address:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_ADDRESS, addressValue, Application.getInstance().getString(R.string.leads_address));
                break;
            case R.id.content_add_leads__email:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_EMAIL, emailValue, Application.getInstance().getString(R.string.leads_email));
                break;
            case R.id.content_add_leads__channal:
                Intent channelIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                channelIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CHANNEL);
                startActivityForResult(channelIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CHANNEL_SELECT);
                break;
            case R.id.content_add_leads__subchannal:
                if (!"".equals(channelValue.getText().toString())) {
                    Intent subchannelIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                    subchannelIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SUB_CHANNEL);
                    subchannelIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SUB_CHANNEL_INTENT, channelValue.getText().toString());
                    startActivityForResult(subchannelIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SUB_CHANNEL_SELECT);
                } else {
                    Toast.makeText(this, Application.getInstance().getString(R.string.leads_sub_channel_enble), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.content_add_leads__chain:
                Intent chainIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                chainIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CHAIN);
                startActivityForResult(chainIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CHAIN_SELECT);
                break;
            case R.id.content_add_leads__sub_chain:
                if (!"".equals(chainValue.getText().toString())) {
                    Intent subchainIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                    subchainIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SUB_CHAIN);
                    subchainIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SUB_CHAIN_INTENT, chainValue.getText().toString());
                    startActivityForResult(subchainIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SUB_CHAIN_SELECT);
                } else {
                    Toast.makeText(this, Application.getInstance().getString(R.string.leads_sub_chain_enble), Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.content_add_leads__constact_frist:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_FRISTNAME, contactFristNameValue, Application.getInstance().getString(R.string.leads_contact_frist_name));
                break;
            case R.id.content_add_leads__constact_last:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_LASTNAME, contactLastNameValue, Application.getInstance().getString(R.string.leads_contact_lastname));
                break;
            case R.id.content_add_leads__jobtitle:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_TITLE, jobTitleValue, Application.getInstance().getString(R.string.leads_job_title));
                break;
            case R.id.content_add_leads__workphone:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_WORK_PHONE, workPhoneValue, Application.getInstance().getString(R.string.leads_work_phone));
                break;
            case R.id.content_add_leads__mobilephone:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_MOBILE_PHONE, mobilePhoneValue, Application.getInstance().getString(R.string.leads_mobile_phone));
                break;
            case R.id.content_add_leads__target_customer:
                Intent intent = new Intent(getBaseContext(), CustomerSelectorActivity.class);
                intent.putExtra(CustomerSelectorActivity.TYPE, CustomerSelectorActivity.ALL);
                startActivityForResult(intent, CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT);
                break;
            case R.id.content_add_leads__target_stage:
                Intent stageIntent = new Intent(getBaseContext(), LeadsClassesAndStatusSelectActivity.class);
                stageIntent.putExtra(LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_STAGE);
                startActivityForResult(stageIntent, LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_STAGE_SELECT);
                break;
            case R.id.content_add_leads__estimate_month:
                showPopWindowDatePicker();
               /* if (modifyEnble) {

                } else {
                    showPopWindowDatePicker();
                }*/
                break;
            case R.id.content_add_leads__estimate_sales:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.leads_estimate_saless));
                textEditorData.setHint(context.getString(R.string.text_edit_number_hint));
                textEditorData.setSingleLine(true);
                textEditorData.setMaxLines(3);
                textEditorData.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                Application.getInstance().setTextEditorData(textEditorData);
                Intent decIntent = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(estimateSalesValue.getText().toString())) {
                    decIntent.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, estimateSalesValue.getText().toString());
                }
                startActivityForResult(decIntent, LeadsAddActivity.RESULT_SALES);
                break;
            case R.id.content_add_leads__developement_plan:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_DEVELOPE_PLAN, developerPlanValue, Application.getInstance().getString(R.string.leads_developement_plan));
                break;
            case R.id.content_add_leads__customer_tag:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG1, customerTag1value, Application.getInstance().getString(R.string.leads_customer_tag));
                break;
            case R.id.content_add_leads__customer_tag2:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG2, customerTag2value, Application.getInstance().getString(R.string.leads_customer_tag2));
                break;
            case R.id.content_add_leads__customer3_tag:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG3, customerTag3value, Application.getInstance().getString(R.string.leads_customer_tag3));
                break;
            case R.id.content_add_leads__customer_tag4:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG4, customerTag4value, Application.getInstance().getString(R.string.leads_customer_tag4));
                break;
            case R.id.content_add_leads__customer_tag5:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG5, customerTag5value, Application.getInstance().getString(R.string.leads_customer_tag5));
                break;
            case R.id.content_add_leads__customer_demand:
                TextEdit(TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_DEMAND, customerDemandValue, Application.getInstance().getString(R.string.leads_customer_demand));
                break;
            case R.id.content_add_leads__daily_call:
                Intent intent1 = new Intent(this, LeadsDailyCallListActivity.class);
                if ("true".equals(draftsOrNo)) {
                    intent1.putExtra(LeadsDailyCallListActivity.ROWID, dbLeads.getRowId());
                    intent1.putExtra(LeadsDailyCallListActivity.ACCOUNT_NAME, dbLeads.getNameCn() + dbLeads.getNameEn());
                } else {
                    intent1.putExtra(LeadsDailyCallListActivity.ROWID, leadsVo.getRowId());
                    intent1.putExtra(LeadsDailyCallListActivity.ACCOUNT_NAME, leadsVo.getNameCn() + leadsVo.getNameEn());
                }
                startActivity(intent1);
        }
    }

    private void TextEdit(int result, TextView textview, String title) {
        textEditorData = new TextEditorData();
        textEditorData.setTitle(title);
        textEditorData.setHint(context.getString(R.string.text_edit_hint));
        textEditorData.setSingleLine(false);
        Application.getInstance().setTextEditorData(textEditorData);
        Intent editeIntent2 = new Intent(getBaseContext(), TextEditorActivity.class);
        if (!"".equals(textview.getText())) {
            editeIntent2.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, textview.getText());
        }
        startActivityForResult(editeIntent2, result);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle;
        switch (requestCode) {
            case TextEditorActivity.ACTIVITY_ADD_LEADS_ENGLISH_NAME:
                resultBundle = data.getExtras();
                String englishtext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                englishNameValue.setText(englishtext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_CHINESE_NAME:
                resultBundle = data.getExtras();
                String chinesetext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                chineseNameValue.setText(chinesetext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_TITLE:
                resultBundle = data.getExtras();
                String titletext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                jobTitleValue.setText(titletext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_ADDRESS:
                resultBundle = data.getExtras();
                String addresstext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                addressValue.setText(addresstext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_FRISTNAME:
                resultBundle = data.getExtras();
                String fristNametext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                contactFristNameValue.setText(fristNametext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_LASTNAME:
                resultBundle = data.getExtras();
                String lasttext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                contactLastNameValue.setText(lasttext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_MOBILE_PHONE:
                resultBundle = data.getExtras();
                String mobiletext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                mobilePhoneValue.setText(mobiletext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_EMAIL:
                resultBundle = data.getExtras();
                String emailtext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                emailValue.setText(emailtext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_WORK_PHONE:
                resultBundle = data.getExtras();
                String worktext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                workPhoneValue.setText(worktext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_STATUS_SELECT:
                resultBundle = data.getExtras();
                String statustext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                statusValue.setText(statustext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_REGION_SELECT:
                resultBundle = data.getExtras();
                String regiontext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                reginValue.setText(regiontext);
                break;

            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_PROVINCE_SELECT:
                resultBundle = data.getExtras();
                String provincetext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                provinceValue.setText(provincetext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CHANNEL_SELECT:
                resultBundle = data.getExtras();
                String channeltext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                channelValue.setText(channeltext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CHAIN_SELECT:
                resultBundle = data.getExtras();
                String chaincetext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                chainValue.setText(chaincetext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CLASS_SELECT:
                resultBundle = data.getExtras();
                String classcetext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                ClassessValue.setText(classcetext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_CITY_SELECT:
                resultBundle = data.getExtras();
                String citytext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                cityValue.setText(citytext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SUB_CHAIN_SELECT:
                resultBundle = data.getExtras();
                String subchaintext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                subChainValue.setText(subchaintext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SUB_CHANNEL_SELECT:
                resultBundle = data.getExtras();
                String subchanneltext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                subChannelValue.setText(subchanneltext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_SALES_SELECT:
                resultBundle = data.getExtras();
                String salestext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                salesEid = resultBundle.getString(LeadsAddActivity.TYPE_OR_STATUS_SALE_EID);
                salesNameValue.setText(salestext);
                break;
            case LeadsClassesAndStatusSelectActivity.ACTIVITY_LEADS_STAGE_SELECT:
                resultBundle = data.getExtras();
                String stagetext = resultBundle
                        .getString(LeadsAddActivity.TYPE_OR_STATUS_NAME);
                stageValue.setText(stagetext);
                break;
            case CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT:
                resultBundle = data.getExtras();
                String customertext = resultBundle
                        .getString(CustomerSelectorActivity.KEY_NAME_EN);
                customerId = resultBundle.getString(CustomerSelectorActivity.KEY_ID);
                customerValue.setText(customertext);
                break;
            case LeadsAddActivity.RESULT_SALES:
                resultBundle = data.getExtras();
                String ressalestext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                estimateSalesValue.setText(ressalestext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_DEMAND:
                resultBundle = data.getExtras();
                String demandtext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                customerDemandValue.setText(demandtext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_DEVELOPE_PLAN:
                resultBundle = data.getExtras();
                String dptext = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                developerPlanValue.setText(dptext);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG1:
                resultBundle = data.getExtras();
                String tag1text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                customerTag1value.setText(tag1text);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG2:
                resultBundle = data.getExtras();
                String tag2text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                customerTag2value.setText(tag2text);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG3:
                resultBundle = data.getExtras();
                String tag3text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                customerTag3value.setText(tag3text);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG4:
                resultBundle = data.getExtras();
                String tag4text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                customerTag4value.setText(tag4text);
                break;
            case TextEditorActivity.ACTIVITY_ADD_LEADS_CUSTOMER_TAG5:
                resultBundle = data.getExtras();
                String tag5text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                customerTag5value.setText(tag5text);
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                // 编辑
                if (editOrAdd) {
                    //编辑的草稿箱
                    if ("true".equalsIgnoreCase(draftsOrNo)) {
                        judgeDraftsValue();
                    } else {
                        judgeEditValue();
                    }
                } else {//修改
                    //添加的草稿箱
                    if ("true".equalsIgnoreCase(draftsOrNo)) {
                        judgeDraftsValue();
                    } else {
                        judgeAddValue();
                    }
                }
                // finish();
                break;
            case R.id.action_task_add_confirm:
                //保存工作
                if (ValueNotNull()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setMessage(Application.getInstance().getString(R.string.Incomplete_information));
                    builder.setTitle("提示");
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    builder.create().show();
                } else {
                    //update leads
                    if (editOrAdd) {
                        final UpdateLeadsRO updateLeadsRo = new UpdateLeadsRO();
                        if ("true".equalsIgnoreCase(draftsOrNo)) {
                            updateLeadsRo.setRowId(dbLeads.getRowId());
                        } else {
                            updateLeadsRo.setRowId(leadsVo.getRowId());
                        }
                        updateLeadsRo.setNameEn(englishNameValue.getText().toString());
                        updateLeadsRo.setNameCn(chineseNameValue.getText().toString());
                        updateLeadsRo.setClasses(ClassessValue.getText().toString());
                        updateLeadsRo.setStatus(statusValue.getText().toString());
                        updateLeadsRo.setRegion(reginValue.getText().toString());
                        updateLeadsRo.setProvince(provinceValue.getText().toString());
                        updateLeadsRo.setCity(cityValue.getText().toString());
                        updateLeadsRo.setAddress(addressValue.getText().toString());
                        updateLeadsRo.setChannel(channelValue.getText().toString());
                        updateLeadsRo.setSubChannel(subChannelValue.getText().toString());
                        updateLeadsRo.setChain(chainValue.getText().toString());
                        updateLeadsRo.setSubChain(subChainValue.getText().toString());
                        updateLeadsRo.setConFstName(contactFristNameValue.getText().toString());
                        updateLeadsRo.setConLastName(contactLastNameValue.getText().toString());
                        updateLeadsRo.setJobTitle(jobTitleValue.getText().toString());
                        updateLeadsRo.setWorkPhNum(workPhoneValue.getText().toString());
                        updateLeadsRo.setCellPhNum(mobilePhoneValue.getText().toString());
                        updateLeadsRo.setEmail(emailValue.getText().toString());
                        updateLeadsRo.setSaleEid(salesEid);

                        if (switch1.isChecked()) {
                            updateLeadsRo.setTargetFlg("Y");
                            updateLeadsRo.setAccountId(customerId);
                            updateLeadsRo.setTag1(customerTag1value.getText().toString());
                            updateLeadsRo.setTag2(customerTag2value.getText().toString());
                            updateLeadsRo.setTag3(customerTag3value.getText().toString());
                            updateLeadsRo.setTag4(customerTag4value.getText().toString());
                            updateLeadsRo.setTag5(customerTag5value.getText().toString());
                            updateLeadsRo.setDevPlan(developerPlanValue.getText().toString());
                            updateLeadsRo.setDemand(customerDemandValue.getText().toString());
                            updateLeadsRo.setEstDate(estimateMonthValue.getText().toString());
                            updateLeadsRo.setEstSales(estimateSalesValue.getText().toString());
                            updateLeadsRo.setStage(stageValue.getText().toString());

                        } else {
                            updateLeadsRo.setTargetFlg("N");
                            updateLeadsRo.setAccountId("");
                            updateLeadsRo.setTag1("");
                            updateLeadsRo.setTag2("");
                            updateLeadsRo.setTag3("");
                            updateLeadsRo.setTag4("");
                            updateLeadsRo.setTag5("");
                            updateLeadsRo.setDevPlan("");
                            updateLeadsRo.setDemand("");
                            updateLeadsRo.setEstDate("");
                            updateLeadsRo.setEstSales("");
                            updateLeadsRo.setStage("");
                        }
                        service.updateLeads(new ICallback<ResultVO>() {
                            @Override
                            public void onDataReady(ResultVO data) {
                                Intent resultIntent = new Intent();
                                if ("true".equalsIgnoreCase(getIntent().getStringExtra(CUSTOMER_TARGET_BACK))) {
                                    resultIntent.putExtra(SimpleSelectorActivity.SELECTED_STRING, getIntent().getStringExtra(CUSTOMER_TYPE));
                                    setResult(RESULT_OK, resultIntent);
                                    finish();
                                } else {
                                    if ("true".equals(draftsOrNo)) {
                                        resultIntent.putExtra(DraftsLeadsListActivity.RESULT_TEXT_LEADS, "");
                                    } else {
                                        resultIntent.putExtra(SimpleSelectorActivity.SELECTED_STRING, getIntent().getStringExtra(CUSTOMER_TYPE));
                                    }
                                    setResult(RESULT_OK, resultIntent);
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "修改Leads成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String date = sDateFormat.format(new java.util.Date());
                                DBManger dbManger = new DBManger(LeadsAddActivity.this);
                                DBLeads dbLeads = new DBLeads("update", updateLeadsRo.getRowId(), updateLeadsRo.getTargetFlg(), updateLeadsRo.getNameEn(), updateLeadsRo.getNameCn(), updateLeadsRo.getSaleEid(),
                                        salesNameValue.getText().toString(), updateLeadsRo.getClasses(), updateLeadsRo.getStatus(), updateLeadsRo.getRegion(), updateLeadsRo.getProvince(), "", updateLeadsRo.getCity(), updateLeadsRo.getChannel(),
                                        "", updateLeadsRo.getAddress(), updateLeadsRo.getSubChannel(), updateLeadsRo.getChain(), "", updateLeadsRo.getSubChain(), updateLeadsRo.getConFstName(),
                                        updateLeadsRo.getConLastName(), updateLeadsRo.getJobTitle(), updateLeadsRo.getWorkPhNum(), updateLeadsRo.getCellPhNum(), updateLeadsRo.getEmail(), updateLeadsRo.getAccountId(), customerValue.getText().toString(),
                                        updateLeadsRo.getStage(), "", "", updateLeadsRo.getEstDate(), updateLeadsRo.getEstSales(), updateLeadsRo.getDemand(), updateLeadsRo.getDevPlan(), updateLeadsRo.getTag1(),
                                        updateLeadsRo.getTag2(), updateLeadsRo.getTag3(), updateLeadsRo.getTag4(), updateLeadsRo.getTag5(), date, "");
                                dbManger.InsertLeadsListTable(dbLeads);
                                Toast.makeText(LeadsAddActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                            }
                        }, updateLeadsRo);
                    } else {//add leads
                        final AddLeadsRO addLeadsRo = new AddLeadsRO();
                        Calendar time = Calendar.getInstance();
                        String hours = time.get(Calendar.HOUR_OF_DAY) + "";//获取小时
                        String min = time.get(Calendar.MINUTE) + "";//获取分钟
                        if (time.get(Calendar.HOUR_OF_DAY) < 10) {
                            hours = "0" + hours;
                        }
                        if (time.get(Calendar.MINUTE) < 10) {
                            min = "0" + min;
                        }
                        addLeadsRo.setNameEn(englishNameValue.getText().toString());
                        addLeadsRo.setNameCn(chineseNameValue.getText().toString());
                        addLeadsRo.setClasses(ClassessValue.getText().toString());
                        addLeadsRo.setStatus(statusValue.getText().toString());
                        addLeadsRo.setRegion(reginValue.getText().toString());
                        addLeadsRo.setProvince(provinceValue.getText().toString());
                        addLeadsRo.setCity(cityValue.getText().toString());
                        addLeadsRo.setAddress(addressValue.getText().toString());
                        addLeadsRo.setChannel(channelValue.getText().toString());
                        addLeadsRo.setSubChannel(subChannelValue.getText().toString());
                        addLeadsRo.setChain(chainValue.getText().toString());
                        addLeadsRo.setSubChain(subChainValue.getText().toString());
                        addLeadsRo.setConFstName(contactFristNameValue.getText().toString());
                        addLeadsRo.setConLastName(contactLastNameValue.getText().toString());
                        addLeadsRo.setJobTitle(jobTitleValue.getText().toString());
                        addLeadsRo.setWorkPhNum(workPhoneValue.getText().toString());
                        addLeadsRo.setCellPhNum(mobilePhoneValue.getText().toString());
                        addLeadsRo.setEmail(emailValue.getText().toString());


                        if (switch1.isChecked()) {
                            addLeadsRo.setTargetFlg("Y");
                            addLeadsRo.setSaleEid(salesEid);
                            addLeadsRo.setAccountId(customerId);
                            addLeadsRo.setTag1(customerTag1value.getText().toString());
                            addLeadsRo.setTag2(customerTag2value.getText().toString());
                            addLeadsRo.setTag3(customerTag3value.getText().toString());
                            addLeadsRo.setTag4(customerTag4value.getText().toString());
                            addLeadsRo.setTag5(customerTag5value.getText().toString());
                            addLeadsRo.setDevPlan(developerPlanValue.getText().toString());
                            addLeadsRo.setDemand(customerDemandValue.getText().toString());
                            addLeadsRo.setEstDate(estimateMonthValue.getText().toString());
                            addLeadsRo.setEstSales(estimateSalesValue.getText().toString());
                            addLeadsRo.setStage(stageValue.getText().toString());

                        } else {
                            addLeadsRo.setTargetFlg("N");
                            addLeadsRo.setSaleEid("");
                            addLeadsRo.setAccountId("");
                            addLeadsRo.setTag1("");
                            addLeadsRo.setTag2("");
                            addLeadsRo.setTag3("");
                            addLeadsRo.setTag4("");
                            addLeadsRo.setTag5("");
                            addLeadsRo.setDevPlan("");
                            addLeadsRo.setDemand("");
                            addLeadsRo.setEstDate("");
                            addLeadsRo.setEstSales("");
                            addLeadsRo.setStage("");
                        }
                        service.addLeads(new ICallback<ResultVO>() {
                            @Override
                            public void onDataReady(ResultVO data) {
                                if ("true".equals(draftsOrNo)) {
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra(DraftsLeadsListActivity.RESULT_TEXT_LEADS, "");
                                    setResult(RESULT_OK, resultIntent);
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "添加Leads成功", Toast.LENGTH_SHORT).show();
                                    initDailyCall();
                                    finish();
                                } else {
                                    LeadsListActivity.LListActivity.finish();
                                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), LeadsListActivity.class);
                                    startActivity(intent);
                                    initDailyCall();
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "添加Leads成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String date = sDateFormat.format(new java.util.Date());
                                DBManger dbManger = new DBManger(LeadsAddActivity.this);
                                DBLeads dbLeads = new DBLeads("new", "", addLeadsRo.getTargetFlg(), addLeadsRo.getNameEn(), addLeadsRo.getNameCn(), addLeadsRo.getSaleEid(),
                                        salesNameValue.getText().toString(), addLeadsRo.getClasses(), addLeadsRo.getStatus(), addLeadsRo.getRegion(), addLeadsRo.getProvince(), "", addLeadsRo.getCity(), addLeadsRo.getChannel(),
                                        "", addLeadsRo.getAddress(), addLeadsRo.getSubChannel(), addLeadsRo.getChain(), "", addLeadsRo.getSubChain(), addLeadsRo.getConFstName(),
                                        addLeadsRo.getConLastName(), addLeadsRo.getJobTitle(), addLeadsRo.getWorkPhNum(), addLeadsRo.getCellPhNum(), addLeadsRo.getEmail(), addLeadsRo.getAccountId(), "",
                                        addLeadsRo.getStage(), "", "", addLeadsRo.getEstDate(), addLeadsRo.getEstSales(), addLeadsRo.getDemand(), addLeadsRo.getDevPlan(), addLeadsRo.getTag1(),
                                        addLeadsRo.getTag2(), addLeadsRo.getTag3(), addLeadsRo.getTag4(), addLeadsRo.getTag5(), date, "");
                                dbManger.InsertLeadsListTable(dbLeads);
                                Toast.makeText(LeadsAddActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                            }
                        }, addLeadsRo);
                    }
                }
        }
        return super.onOptionsItemSelected(item);
    }

    private void judgeAddValue() {
        boolean result = false;
        if (!"".equals(salesNameValue.getText().toString())) {
            result = true;
        } else if (!"".equals(englishNameValue.getText().toString())) {
            result = true;
        } else if (!"".equals(chineseNameValue.getText().toString())) {
            result = true;
        } else if (!"".equals(ClassessValue.getText().toString())) {
            result = true;
        } else if (!"".equals(statusValue.getText().toString())) {
            result = true;
        } else if (!"".equals(reginValue.getText().toString())) {
            result = true;
        } else if (!"".equals(provinceValue.getText().toString())) {
            result = true;
        } else if (!"".equals(cityValue.getText().toString())) {
            result = true;
        } else if (!"".equals(addressValue.getText().toString())) {
            result = true;
        } else if (!"".equals(channelValue.getText().toString())) {
            result = true;
        } else if (!"".equals(subChannelValue.getText().toString())) {
            result = true;
        } else if (!"".equals(chainValue.getText().toString())) {
            result = true;
        } else if (!"".equals(subChainValue.getText().toString())) {
            result = true;
        } else if (!"".equals(contactFristNameValue.getText().toString())) {
            result = true;
        } else if (!"".equals(jobTitleValue.getText().toString())) {
            result = true;
        } else if (!"".equals(workPhoneValue.getText().toString())) {
            result = true;
        } else if (!"".equals(mobilePhoneValue.getText().toString())) {
            result = true;
        } else if (!"".equals(emailValue.getText().toString())) {
            result = true;
        } else if (!"".equals(customerValue.getText().toString())) {
            result = true;
        } else if (!"".equals(stageValue.getText().toString())) {
            result = true;
        } else if (!"".equals(estimateMonthValue.getText().toString())) {
            result = true;
        } else if (!"".equals(estimateSalesValue.getText().toString())) {
            result = true;
        } else if (!"".equals(customerDemandValue.getText().toString())) {
            result = true;
        } else if (!"".equals(developerPlanValue.getText().toString())) {
            result = true;
        } else if (!"".equals(customerTag1value.getText().toString())) {
            result = true;
        } else if (!"".equals(customerTag2value.getText().toString())) {
            result = true;
        } else if (!"".equals(customerTag3value.getText().toString())) {
            result = true;
        } else if (!"".equals(customerTag4value.getText().toString())) {
            result = true;
        } else if (!"".equals(customerTag5value.getText().toString())) {
            result = true;
        }

        if (result) {
            showDraftsWindow();
        } else {
            if (getIntent().getStringExtra(CUSTOMER_TYPE) != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(SimpleSelectorActivity.SELECTED_STRING, getIntent().getStringExtra(CUSTOMER_TYPE));
                setResult(RESULT_OK, resultIntent);
            }
            finish();
        }
    }

    private void judgeEditValue() {
        boolean result = false;
        if (!salesNameValue.getText().toString().equals(leadsVo.getSaleName().toString())) {
            result = true;
        } else if (!englishNameValue.getText().toString().equals(leadsVo.getNameEn().toString())) {
            result = true;
        } else if (!chineseNameValue.getText().toString().equals(leadsVo.getNameCn().toString())) {
            result = true;
        } else if (!ClassessValue.getText().toString().equals(leadsVo.getClasses().toString())) {
            result = true;
        } else if (!statusValue.getText().toString().equals(leadsVo.getStatus().toString())) {
            result = true;
        } else if (!reginValue.getText().toString().equals(leadsVo.getRegion().toString())) {
            result = true;
        } else if (!provinceValue.getText().toString().equals(leadsVo.getProvince().toString())) {
            result = true;
        } else if (!cityValue.getText().toString().equals(leadsVo.getCity().toString())) {
            result = true;
        } else if (!addressValue.getText().toString().equals(leadsVo.getAddress().toString())) {
            result = true;
        } else if (!channelValue.getText().toString().equals(leadsVo.getChannel().toString())) {
            result = true;
        } else if (!subChannelValue.getText().toString().equals(leadsVo.getSubChannel().toString())) {
            result = true;
        } else if (!chainValue.getText().toString().equals(leadsVo.getChain().toString())) {
            result = true;
        } else if (!subChainValue.getText().toString().equals(leadsVo.getSubChain().toString())) {
            result = true;
        } else if (!contactFristNameValue.getText().toString().equals(leadsVo.getConFstName().toString())) {
            result = true;
        } else if (!jobTitleValue.getText().toString().equals(leadsVo.getJobTitle().toString())) {
            result = true;
        } else if (!workPhoneValue.getText().toString().equals(leadsVo.getWorkPhNum().toString())) {
            result = true;
        } else if (!mobilePhoneValue.getText().toString().equals(leadsVo.getCellPhNum().toString())) {
            result = true;
        } else if (!emailValue.getText().toString().equals(leadsVo.getEmail().toString())) {
            result = true;
        } else if (!customerValue.getText().toString().equals(leadsVo.getNameEn().toString())) {
            result = true;
        } else if (!stageValue.getText().toString().equals(leadsVo.getStage().toString())) {
            result = true;
        } else if (!estimateMonthValue.getText().toString().equals(leadsVo.getEstDate().toString())) {
            result = true;
        } else if (!estimateSalesValue.getText().toString().equals(leadsVo.getEstSales().toString())) {
            result = true;
        } else if (!customerDemandValue.getText().toString().equals(leadsVo.getDemand().toString())) {
            result = true;
        } else if (!developerPlanValue.getText().toString().equals(leadsVo.getDevPlan().toString())) {
            result = true;
        } else if (!customerTag1value.getText().toString().equals(leadsVo.getTag1().toString())) {
            result = true;
        } else if (!customerTag2value.getText().toString().equals(leadsVo.getTag2().toString())) {
            result = true;
        } else if (!customerTag3value.getText().toString().equals(leadsVo.getTag3().toString())) {
            result = true;
        } else if (!customerTag4value.getText().toString().equals(leadsVo.getTag4().toString())) {
            result = true;
        } else if (!customerTag5value.getText().toString().equals(leadsVo.getTag5().toString())) {
            result = true;
        }

        if (result) {
            showDraftsWindow();
        } else {
            if (getIntent().getStringExtra(CUSTOMER_TYPE) != null) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra(SimpleSelectorActivity.SELECTED_STRING, getIntent().getStringExtra(CUSTOMER_TYPE));
                setResult(RESULT_OK, resultIntent);
            }
            finish();
        }
    }

    private void judgeDraftsValue() {
        boolean result = false;
        if (!salesNameValue.getText().toString().equals(dbLeads.getSaleName().toString())) {
            result = true;
        } else if (!englishNameValue.getText().toString().equals(dbLeads.getNameEn().toString())) {
            result = true;
        } else if (!chineseNameValue.getText().toString().equals(dbLeads.getNameCn().toString())) {
            result = true;
        } else if (!ClassessValue.getText().toString().equals(dbLeads.getClasses().toString())) {
            result = true;
        } else if (!statusValue.getText().toString().equals(dbLeads.getStatus().toString())) {
            result = true;
        } else if (!reginValue.getText().toString().equals(dbLeads.getRegion().toString())) {
            result = true;
        } else if (!provinceValue.getText().toString().equals(dbLeads.getProvince().toString())) {
            result = true;
        } else if (!cityValue.getText().toString().equals(dbLeads.getCity().toString())) {
            result = true;
        } else if (!addressValue.getText().toString().equals(dbLeads.getAddress().toString())) {
            result = true;
        } else if (!channelValue.getText().toString().equals(dbLeads.getChannel().toString())) {
            result = true;
        } else if (!subChannelValue.getText().toString().equals(dbLeads.getSubChannel().toString())) {
            result = true;
        } else if (!chainValue.getText().toString().equals(dbLeads.getChain().toString())) {
            result = true;
        } else if (!subChainValue.getText().toString().equals(dbLeads.getSubChain().toString())) {
            result = true;
        } else if (!contactFristNameValue.getText().toString().equals(dbLeads.getConFstName().toString())) {
            result = true;
        } else if (!jobTitleValue.getText().toString().equals(dbLeads.getJobTitle().toString())) {
            result = true;
        } else if (!workPhoneValue.getText().toString().equals(dbLeads.getWorkPhNum().toString())) {
            result = true;
        } else if (!mobilePhoneValue.getText().toString().equals(dbLeads.getCellPhNum().toString())) {
            result = true;
        } else if (!emailValue.getText().toString().equals(dbLeads.getEmail().toString())) {
            result = true;
        } else if (!customerValue.getText().toString().equals(dbLeads.getNameEn().toString())) {
            result = true;
        } else if (!stageValue.getText().toString().equals(dbLeads.getStage().toString())) {
            result = true;
        } else if (!estimateMonthValue.getText().toString().equals(dbLeads.getEstDate().toString())) {
            result = true;
        } else if (!estimateSalesValue.getText().toString().equals(dbLeads.getEstSales().toString())) {
            result = true;
        } else if (!customerDemandValue.getText().toString().equals(dbLeads.getDemand().toString())) {
            result = true;
        } else if (!developerPlanValue.getText().toString().equals(dbLeads.getDevPlan().toString())) {
            result = true;
        } else if (!customerTag1value.getText().toString().equals(dbLeads.getTag1().toString())) {
            result = true;
        } else if (!customerTag2value.getText().toString().equals(dbLeads.getTag2().toString())) {
            result = true;
        } else if (!customerTag3value.getText().toString().equals(dbLeads.getTag3().toString())) {
            result = true;
        } else if (!customerTag4value.getText().toString().equals(dbLeads.getTag4().toString())) {
            result = true;
        } else if (!customerTag5value.getText().toString().equals(dbLeads.getTag5().toString())) {
            result = true;
        }

        if (result) {
            showDraftsWindow();
        } else {
            finish();
        }
    }

    private void showDraftsWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_popup_drafts, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(findViewById(R.id.content_add_leads__sales_name_value),
                Gravity.BOTTOM, 0, 0);
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        window.setBackgroundDrawable(dw);
        Button saveButton = (Button) view.findViewById(R.id.content_popop_drafts_save);
        Button notSaveButton = (Button) view.findViewById(R.id.content_popop_drafts_not_save);
        Button currentButton = (Button) view.findViewById(R.id.content_popop_drafts_current_view);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存工作
                String rowId = "";
                if (editOrAdd) {
                    if ("true".equals(draftsOrNo)) {
                        rowId = dbLeads.getRowId();
                    } else {
                        rowId = leadsVo.getRowId();
                    }
                } else {
                    rowId = "";
                }
                String switchValue = "N";
                if (switch1.isChecked()) {
                    switchValue = "Y";
                } else {
                    switchValue = "N";
                }
                if ("true".equals(draftsOrNo)) {
                    if (editOrAdd) {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());
                        DBManger dbManger = new DBManger(LeadsAddActivity.this);
                        DBLeads dbLeadss = new DBLeads("update", rowId, switchValue, englishNameValue.getText().toString(), chineseNameValue.getText().toString(), salesEid,
                                salesNameValue.getText().toString(), ClassessValue.getText().toString(), statusValue.getText().toString(), reginValue.getText().toString(), provinceValue.getText().toString(), "", cityValue.getText().toString(), channelValue.getText().toString(),
                                "", addressValue.getText().toString(), subChannelValue.getText().toString(), chainValue.getText().toString(), "", subChainValue.getText().toString(), contactFristNameValue.getText().toString(),
                                contactLastNameValue.getText().toString(), jobTitleValue.getText().toString(), workPhoneValue.getText().toString(), mobilePhoneValue.getText().toString(), emailValue.getText().toString(), customerId, customerValue.getText().toString(),
                                stageValue.getText().toString(), "", "", estimateMonthValue.getText().toString(), estimateSalesValue.getText().toString(), customerDemandValue.getText().toString(), developerPlanValue.getText().toString(), customerTag1value.getText().toString(),
                                customerTag2value.getText().toString(), customerTag3value.getText().toString(), customerTag4value.getText().toString(), customerTag5value.getText().toString(), date, "");
                        dbManger.UpdateLeadsById(dbLeadss, dbLeads.getId());
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(DraftsLeadsListActivity.RESULT_TEXT_LEADS, "true");
                        setResult(RESULT_OK, resultIntent);
                    } else {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());
                        DBManger dbManger = new DBManger(LeadsAddActivity.this);
                        DBLeads dbLeadss = new DBLeads("new", rowId, switchValue, englishNameValue.getText().toString(), chineseNameValue.getText().toString(), salesEid,
                                salesNameValue.getText().toString(), ClassessValue.getText().toString(), statusValue.getText().toString(), reginValue.getText().toString(), provinceValue.getText().toString(), "", cityValue.getText().toString(), channelValue.getText().toString(),
                                "", addressValue.getText().toString(), subChannelValue.getText().toString(), chainValue.getText().toString(), "", subChainValue.getText().toString(), contactFristNameValue.getText().toString(),
                                contactLastNameValue.getText().toString(), jobTitleValue.getText().toString(), workPhoneValue.getText().toString(), mobilePhoneValue.getText().toString(), emailValue.getText().toString(), customerId, customerValue.getText().toString(),
                                stageValue.getText().toString(), "", "", estimateMonthValue.getText().toString(), estimateSalesValue.getText().toString(), customerDemandValue.getText().toString(), developerPlanValue.getText().toString(), customerTag1value.getText().toString(),
                                customerTag2value.getText().toString(), customerTag3value.getText().toString(), customerTag4value.getText().toString(), customerTag5value.getText().toString(), date, "");
                        dbManger.UpdateLeadsById(dbLeadss, dbLeads.getId());
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(DraftsLeadsListActivity.RESULT_TEXT_LEADS, "true");
                        setResult(RESULT_OK, resultIntent);
                    }
                    //  Toast.makeText(LeadsAddActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                } else {
                    if (editOrAdd) {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());
                        DBManger dbManger = new DBManger(LeadsAddActivity.this);
                        DBLeads dbLeads = new DBLeads("update", rowId, switchValue, englishNameValue.getText().toString(), chineseNameValue.getText().toString(), salesEid,
                                salesNameValue.getText().toString(), ClassessValue.getText().toString(), statusValue.getText().toString(), reginValue.getText().toString(), provinceValue.getText().toString(), "", cityValue.getText().toString(), channelValue.getText().toString(),
                                "", addressValue.getText().toString(), subChannelValue.getText().toString(), chainValue.getText().toString(), "", subChainValue.getText().toString(), contactFristNameValue.getText().toString(),
                                contactLastNameValue.getText().toString(), jobTitleValue.getText().toString(), workPhoneValue.getText().toString(), mobilePhoneValue.getText().toString(), emailValue.getText().toString(), customerId, customerValue.getText().toString(),
                                stageValue.getText().toString(), "", "", estimateMonthValue.getText().toString(), estimateSalesValue.getText().toString(), customerDemandValue.getText().toString(), developerPlanValue.getText().toString(), customerTag1value.getText().toString(),
                                customerTag2value.getText().toString(), customerTag3value.getText().toString(), customerTag4value.getText().toString(), customerTag5value.getText().toString(), date, "");
                        dbManger.InsertLeadsListTable(dbLeads);
                    } else {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());
                        DBManger dbManger = new DBManger(LeadsAddActivity.this);
                        DBLeads dbLeads = new DBLeads("new", rowId, switchValue, englishNameValue.getText().toString(), chineseNameValue.getText().toString(), salesEid,
                                salesNameValue.getText().toString(), ClassessValue.getText().toString(), statusValue.getText().toString(), reginValue.getText().toString(), provinceValue.getText().toString(), "", cityValue.getText().toString(), channelValue.getText().toString(),
                                "", addressValue.getText().toString(), subChannelValue.getText().toString(), chainValue.getText().toString(), "", subChainValue.getText().toString(), contactFristNameValue.getText().toString(),
                                contactLastNameValue.getText().toString(), jobTitleValue.getText().toString(), workPhoneValue.getText().toString(), mobilePhoneValue.getText().toString(), emailValue.getText().toString(), customerId, customerValue.getText().toString(),
                                stageValue.getText().toString(), "", "", estimateMonthValue.getText().toString(), estimateSalesValue.getText().toString(), customerDemandValue.getText().toString(), developerPlanValue.getText().toString(), customerTag1value.getText().toString(),
                                customerTag2value.getText().toString(), customerTag3value.getText().toString(), customerTag4value.getText().toString(), customerTag5value.getText().toString(), date, "");
                        dbManger.InsertLeadsListTable(dbLeads);
                        Toast.makeText(LeadsAddActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();

                    }
                }
                window.dismiss();
                finish();
            }
        });
        notSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
                if (getIntent().getStringExtra(CUSTOMER_TYPE) != null) {
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(SimpleSelectorActivity.SELECTED_STRING, getIntent().getStringExtra(CUSTOMER_TYPE));
                    setResult(RESULT_OK, resultIntent);
                }
                finish();
            }
        });
        currentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
    }

    private boolean ValueNotNull() {
        boolean result = false;
        if ("".equals(englishNameValue.getText().toString())) {
            result = true;
        }
        if ("".equals(statusValue.getText().toString())) {
            result = true;
        }

        if ("".equals(reginValue.getText().toString())) {
            result = true;
        }
        if ("".equals(provinceValue.getText().toString())) {
            result = true;
        }
        if ("".equals(cityValue.getText().toString())) {
            result = true;
        }
        if ("".equals(channelValue.getText().toString())) {
            result = true;
        }
        if ("".equals(subChannelValue.getText().toString())) {
            result = true;
        }
        if (switch1.isChecked()) {
            if ("".equals(stageValue.getText().toString())) {
                result = true;
            } else {
                if ("Quotation".equalsIgnoreCase(stageValue.getText().toString())) {
                    if ("".equals(estimateMonthValue.getText().toString())) {
                        result = true;
                    }
                    if ("".equals(estimateSalesValue.getText().toString())) {
                        result = true;
                    }
                } else {
                }
                if ("deal".equals(stageValue.getText().toString())) {
                    if ("".equals(customerValue.getText().toString())) {
                        result = true;
                    }
                }

            }

        }

        return result;
    }

    //popwindow显示
    private void showPopWindowDatePicker() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.content_popup_datepicker, null);
        final PopupWindow window = new PopupWindow(view,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        window.setFocusable(true);
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setAnimationStyle(R.style.mypopwindow_anim_style);
        window.showAtLocation(findViewById(R.id.content_add_leads__sales_name_value),
                Gravity.BOTTOM, 0, 0);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        if (!"".equals(estimateMonthValue.getText().toString())) {
            String times = estimateMonthValue.getText().toString();
            String years = times.substring(0, 4);
            String month = times.substring(5, 7);
            String day = times.substring(8, 10);
            if ("0".equals(month.substring(0, 1))) {
                month = month.substring(1, 2);
            }
            if ("0".equals(day.substring(0, 1))) {
                day = day.substring(1, 2);
            }
            year = Integer.parseInt(years);
            monthOfYear = Integer.parseInt(month) - 1;
            dayOfMonth = Integer.parseInt(day);
        } else {
            year = calendar.get(Calendar.YEAR);
            monthOfYear = calendar.get(Calendar.MONTH);
            dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        }

        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int years,
                                      int monthOfYears, int dayOfMonths) {
                year = years;
                monthOfYear = monthOfYears;
                dayOfMonth = dayOfMonths;
            }

        });
        TextView dateCancel = (TextView) view.findViewById(R.id.date_cancel);
        TextView datesubmit = (TextView) view.findViewById(R.id.date_submit);
        dateCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                window.dismiss();
            }
        });
        datesubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String month = "";
                String day = "";
                String date = "";
                if (dayOfMonth < 10) {
                    day = "0" + dayOfMonth;
                } else {
                    day = dayOfMonth + "";
                }
                if (monthOfYear < 9) {
                    month = "0" + (monthOfYear + 1);
                } else {
                    month = (monthOfYear + 1) + "";
                }
                date = year + "-" + month + "-" + day;
                estimateMonthValue.setText(date);
                window.dismiss();
            }
        });
    }
    //滑动到指定位置

    private void scrollToView(final ScrollView scrollViewParent, final View view) {
        // Get deepChild Offset
        Point childOffset = new Point();
        getDeepChildOffset(scrollViewParent, view.getParent(), view, childOffset);
        // Scroll to child.
        scrollViewParent.smoothScrollTo(0, childOffset.y);
    }

    private void getDeepChildOffset(final ViewGroup mainParent, final ViewParent parent, final View child, final Point accumulatedOffset) {
        ViewGroup parentGroup = (ViewGroup) parent;
        accumulatedOffset.x += child.getLeft();
        accumulatedOffset.y += child.getTop();
        if (parentGroup.equals(mainParent)) {
            return;
        }
        getDeepChildOffset(mainParent, parentGroup.getParent(), parentGroup, accumulatedOffset);
    }

    public void initDailyCall() {
        dailyCallService.getSummary(new ICallback<CPSummaryResultVO>() {
            @Override
            public void onDataReady(CPSummaryResultVO result) {
                summary = result.getData();
                Application.getInstance().setSummary(summary);
                mHandler.msgContent = "返回的数据";
                mHandler.sendEmptyMessage(0x123);
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            // 编辑
            if (editOrAdd) {
                //编辑的草稿箱
                if ("true".equalsIgnoreCase(draftsOrNo)) {
                    judgeDraftsValue();
                } else {
                    judgeEditValue();
                }
            } else {//修改
                //添加的草稿箱
                if ("true".equalsIgnoreCase(draftsOrNo)) {
                    judgeDraftsValue();
                } else {
                    judgeAddValue();
                }
            }
        }
        return false;
    }
}
