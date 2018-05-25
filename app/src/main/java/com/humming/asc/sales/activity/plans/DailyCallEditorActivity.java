package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.humming.asc.dp.presentation.ro.cp.dailycall.AddDailyCallRO;
import com.humming.asc.dp.presentation.ro.cp.dailycall.UpdateDailyCallRO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryResultVO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.activity.TextEditorActivity;
import com.humming.asc.sales.activity.TextEditorData;
import com.humming.asc.sales.activity.customer.CustomerDailyCallListActivity;
import com.humming.asc.sales.activity.customer.CustomerSelectorActivity;
import com.humming.asc.sales.activity.customer.CustomerTypeSelectorActivity;
import com.humming.asc.sales.activity.settings.DraftsDailyCallListActivity;
import com.humming.asc.sales.content.PlansContent;
import com.humming.asc.sales.datebase.DBManger;
import com.humming.asc.sales.model.DBDailycall;
import com.humming.asc.sales.model.ImageItem;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DailyCallEditorActivity extends AbstractActivity {
    private DailyCallDetailVO dailyCallDetail;
    public static final int ACTIVITY_FOLLOW_UP_RESULT = 10110;
    private TextView salesTitleValue, customerValue, taskValue, subjectValue, typeValue, statusValue, startTimeValue, subjectAddValue, typeAddValue, startTimeAddValue, endTimeAddValue,
            endTimeValue, locationValue, callplanValue, noteVale, commentsValue, followUpValue, resultValut, photoValue, customerLabel, taskLabel, acCompanyValue;
    private View itemCustomer, itemTask, itemSubject, itemType, itemStatus, itemStartTime, itemAddSubject, itemAddType, itemAddStartTime, itemAddEndTime,
            itemEndTime, itemLocation, itemCallPlan, itemNote, itemComments, itemFollowUp, itemResult, itemPhoto, itemAccompany;
    private View visibleOne, visibleTwo, visiblewOne1;
    private int year, monthOfYear, dayOfMonth, hour, minute;
    private boolean startOrEndDate = true;
    private boolean editOrAdd = false;
    private TextEditorData textEditorData;
    private Context context = Application.getInstance().getBaseContext();
    private String customerRowId = "";
    private String acRowId = "";
    private boolean taskShow = true;
    private DailyCallService service;
    public static final String ASSOC_TYPE = "assoc_type";
    public static final String TASK_ID = "task_id";
    private String assoc_type = "";
    private String task_id = "";
    private String ifTrueDCList = "false";
    public static final String TASK_COME = "task_come";
    public static final String TASK_COME2 = "task_come2";
    public static final String IFDAILY_CALL_LIST = "if_daily_call";
    public static final String CUSTOMER_NAME = "customer_name";
    public static final String CUSTOMER_ROWID = "customer_rowid";
    public static final String TASK_NAME = "task_name";
    public static final String SUBJECT_NAME = "subject_name";
    public static final String CUSTOMER_ADD_DAILY_CALL = "falses";
    public static final String CUSTOMER_ADD_DAILY_CALL_TYPE = "daily_call_type";
    public static final String FOLLOW_UP = "false";
    public static final String TASK_ADD_DAILY_CALL = "task_add";
    public static final String DAILY_CALL_DATE = "daily_call_date";
    public static String followUp = "false";
    public static String customer_add = "false";
    public static final String ACTIVITY_FOLLOW_UP_VALUE = "follow_up_done";
    private PlansContent plansContent;
    private String cusType = "";
    private Application mAPP;
    private MainActivity.MyHandler mHandler;
    private DailyCallService dailyCallService;
    private CPSummaryVO summary;
    private int backOfPhoto = 0;
    private String photoName = "";
    public static final String DRAFTS_DC = "drafts_dc";
    private DBDailycall dbDailycall;
    private String draftCome = "";
    String subjectName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_call_editor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        followUp = "false";
        dailyCallDetail = Application.getInstance().getDailyCallDetail4Edit();
        service = Application.getDailyCallService();
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        plansContent = new PlansContent(context);
        mAPP = (Application) getApplication();
        // 获得共享变量实例
        mHandler = mAPP.getHandler();
        dailyCallService = Application.getDailyCallService();
        assoc_type = getIntent().getStringExtra(DailyCallEditorActivity.ASSOC_TYPE);
        String customerName = getIntent().getStringExtra(DailyCallEditorActivity.CUSTOMER_NAME);
        String taskName = getIntent().getStringExtra(DailyCallEditorActivity.TASK_NAME);
        subjectName = getIntent().getStringExtra(DailyCallEditorActivity.SUBJECT_NAME);
        followUp = getIntent().getStringExtra(DailyCallEditorActivity.FOLLOW_UP);
        customer_add = getIntent().getStringExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL);
        task_id = getIntent().getStringExtra(DailyCallEditorActivity.TASK_ID);
        cusType = getIntent().getStringExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL_TYPE);
        ifTrueDCList = getIntent().getStringExtra(DailyCallEditorActivity.IFDAILY_CALL_LIST);
        customerRowId = getIntent().getStringExtra(CUSTOMER_ROWID);
        draftCome = getIntent().getStringExtra(DRAFTS_DC);
        //草稿箱操作
        if ("true".equals(draftCome)) {
            dbDailycall = Application.getInstance().getDbDailycall();
            customerRowId = dbDailycall.getCustomerRowId();
            task_id = dbDailycall.getTaskId();
            acRowId = dbDailycall.getAcRowId();
            assoc_type = dbDailycall.getAssocType();
            if ("new".equals(dbDailycall.getRemark())) {
                //草稿箱添加
                editOrAdd = false;
                actionBar.setTitle(R.string.title_activity_add_daily_call);
                initValue();
                setViewVisibleAdd();
                setDraftsAddValue();
            } else {
                //草稿箱编辑
                editOrAdd = true;
                actionBar.setTitle(R.string.title_activity_edit_daily_call);
                initValue();
                setViewVisibleEdit();
                setDraftsEditValue();
            }
            if (!"Key Account".equals(assoc_type)) {
                taskLabel.setTextColor(this.getResources().getColor(R.color.textGray));
                taskValue.setTextColor(this.getResources().getColor(R.color.textGray));
                itemTask.setClickable(false);
            } else {
                taskLabel.setTextColor(this.getResources().getColor(R.color.colorAccent));
                itemTask.setClickable(true);
            }
        } else {
            if (dailyCallDetail == null) {//添加
                dailyCallDetail = new DailyCallDetailVO();
                editOrAdd = false;
                actionBar.setTitle(R.string.title_activity_add_daily_call);
                initValue();
                setViewVisibleAdd();
                //dailyCall 设置时间
                if (getIntent().getStringExtra(DAILY_CALL_DATE) != null) {
                    startTimeAddValue.setText(getIntent().getStringExtra(DAILY_CALL_DATE));
                } else {

                }
                if ("true".equals(followUp)) {
                    actionBar.setTitle(R.string.title_activity_follow_up);
                    customerValue.setText(customerName);
                    taskValue.setText(taskName);
                    subjectAddValue.setText(subjectName);
                    customerLabel.setTextColor(this.getResources().getColor(R.color.textGray));
                    taskLabel.setTextColor(this.getResources().getColor(R.color.textGray));
                    customerValue.setTextColor(this.getResources().getColor(R.color.textGray));
                    taskValue.setTextColor(this.getResources().getColor(R.color.textGray));
                    if ("true".equals(getIntent().getStringExtra(TASK_ADD_DAILY_CALL))) {
                        actionBar.setTitle(R.string.title_activity_add_daily_call);
                    }
                }
                if ("true".equals(customer_add)) {//customer  add
                    if ("Key Account".equalsIgnoreCase(cusType)) {
                        taskLabel.setTextColor(this.getResources().getColor(R.color.colorAccent));
                        itemTask.setClickable(true);
                    } else {
                        taskLabel.setTextColor(this.getResources().getColor(R.color.textGray));
                        itemTask.setClickable(false);
                    }
                    customerValue.setText(customerName);
                    customerLabel.setTextColor(this.getResources().getColor(R.color.textGray));
                    customerValue.setTextColor(this.getResources().getColor(R.color.textGray));
                    customerRowId = getIntent().getStringExtra(CUSTOMER_ROWID);
                }

            } else {//修改
                editOrAdd = true;
                actionBar.setTitle(R.string.title_activity_edit_daily_call);
                initValue();
                setViewVisibleEdit();
                setValue(dailyCallDetail);
                if (!"Key Account".equals(assoc_type)) {
                    taskLabel.setTextColor(this.getResources().getColor(R.color.textGray));
                    taskValue.setTextColor(this.getResources().getColor(R.color.textGray));
                    itemTask.setClickable(false);
                } else {
                    taskLabel.setTextColor(this.getResources().getColor(R.color.colorAccent));
                    itemTask.setClickable(true);
                }
            }
        }
    }

    private void setDraftsAddValue() {
        customerValue.setText(dbDailycall.getAccountName());
        taskValue.setText(dbDailycall.getAccountName());
        statusValue.setText(dbDailycall.getStatus());
        subjectAddValue.setText(dbDailycall.getSubject());
        typeAddValue.setText(dbDailycall.getType());
        startTimeAddValue.setText(dbDailycall.getStartTime());
        endTimeAddValue.setText(dbDailycall.getEndTime());
        locationValue.setText(dbDailycall.getLocation());
        noteVale.setText(dbDailycall.getNote());
        callplanValue.setText(dbDailycall.getMeetingContent());
        acCompanyValue.setText(dbDailycall.getAcName());
        // commentsValue.setText(dbDailycall.getCommentsCount() + "");
        resultValut.setText(dbDailycall.getResult());
        //  photoValue.setText(dbDailycall.getPicList().size() + "");
    }

    private void setDraftsEditValue() {
        salesTitleValue.setText(dbDailycall.getSaleName());
        //salesTitleValue.setVisibility(View.GONE);
        customerValue.setText(dbDailycall.getAccountName());
        taskValue.setText(dbDailycall.getTaskName());
        subjectValue.setText(dbDailycall.getSubject());
        typeValue.setText(dbDailycall.getType());
        statusValue.setText(dbDailycall.getStatus());
        startTimeValue.setText(dbDailycall.getStartTime());
        endTimeValue.setText(dbDailycall.getEndTime());
        locationValue.setText(dbDailycall.getLocation());
        noteVale.setText(dbDailycall.getNote());
        callplanValue.setText(dbDailycall.getMeetingContent());
        acCompanyValue.setText(dbDailycall.getAcName());
        // commentsValue.setText(dbDailycall.getCommentsCount() + "");
        resultValut.setText(dbDailycall.getResult());
        //  photoValue.setText(dbDailycall.getPicList().size() + "");
    }

    private void setViewVisibleAdd() {
        visibleOne.setVisibility(View.VISIBLE);
        visibleTwo.setVisibility(View.VISIBLE);
        visiblewOne1.setVisibility(View.GONE);
        itemComments.setVisibility(View.GONE);
        itemFollowUp.setVisibility(View.GONE);
        itemResult.setVisibility(View.GONE);
    }

    private void setViewVisibleEdit() {
        salesTitleValue.setVisibility(View.VISIBLE);
        visibleOne.setVisibility(View.GONE);
        visibleTwo.setVisibility(View.GONE);
        visiblewOne1.setVisibility(View.VISIBLE);
        itemComments.setVisibility(View.VISIBLE);
        itemFollowUp.setVisibility(View.VISIBLE);
        itemResult.setVisibility(View.VISIBLE);
    }

    private void setValue(DailyCallDetailVO dailyCallDetail) {
        salesTitleValue.setText(dailyCallDetail.getSaleName());
        customerValue.setText(dailyCallDetail.getAccountName());
        taskValue.setText(dailyCallDetail.getTaskName());
        subjectValue.setText(dailyCallDetail.getSubject());
        typeValue.setText(dailyCallDetail.getType());
        statusValue.setText(dailyCallDetail.getStatus());
        startTimeValue.setText(dailyCallDetail.getStartTime());
        endTimeValue.setText(dailyCallDetail.getEndTime());
        locationValue.setText(dailyCallDetail.getLocation());
        noteVale.setText(dailyCallDetail.getNote());
        callplanValue.setText(dailyCallDetail.getMeetingContent());
        commentsValue.setText(dailyCallDetail.getCommentsCount() + "");
        resultValut.setText(dailyCallDetail.getResult());
        photoValue.setText(dailyCallDetail.getPicList().size() + "");
        acCompanyValue.setText(dailyCallDetail.getAcmpUserName());
        if ("true".equals(followUp)) {
            customerValue.setTextColor(this.getResources().getColor(R.color.textGray));
            taskValue.setTextColor(this.getResources().getColor(R.color.textGray));
        }
    }

    private void initValue() {
        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v.getId());
            }
        };
        salesTitleValue = (TextView) findViewById(R.id.activity_daily_call_editor__sales_title);
        itemCustomer = findViewById(R.id.activity_daily_call_editor__item_customer);
        itemCustomer.setOnClickListener(itemClickListener);
        customerValue = (TextView) findViewById(R.id.activity_daily_call_editor__customer_value);
        taskValue = (TextView) findViewById(R.id.activity_daily_call_editor__task_value);
        subjectValue = (TextView) findViewById(R.id.activity_daily_call_editor__subject_value);
        typeValue = (TextView) findViewById(R.id.activity_daily_call_editor__type_value);
        statusValue = (TextView) findViewById(R.id.activity_daily_call_editor__status_value);
        startTimeValue = (TextView) findViewById(R.id.activity_daily_call_editor__start_time_value);
        endTimeValue = (TextView) findViewById(R.id.activity_daily_call_editor__end_time_value);
        locationValue = (TextView) findViewById(R.id.activity_daily_call_editor__location_value);
        callplanValue = (TextView) findViewById(R.id.activity_daily_call_editor__call_plan_value);
        noteVale = (TextView) findViewById(R.id.activity_daily_call_editor__note_value);
        commentsValue = (TextView) findViewById(R.id.activity_daily_call_editor__comment_value);
        followUpValue = (TextView) findViewById(R.id.activity_daily_call_editor__follow_up_value);
        resultValut = (TextView) findViewById(R.id.activity_daily_call_editor__result_value);
        photoValue = (TextView) findViewById(R.id.activity_daily_call_editor__photo_value);
        subjectAddValue = (TextView) findViewById(R.id.activity_daily_call_editor__subject_value_add);
        typeAddValue = (TextView) findViewById(R.id.activity_daily_call_editor__type_value_add);
        startTimeAddValue = (TextView) findViewById(R.id.activity_daily_call_editor__start_time_value_add);
        endTimeAddValue = (TextView) findViewById(R.id.activity_daily_call_editor__end_time_value_add);
        customerLabel = (TextView) findViewById(R.id.activity_daily_call_editor__customer_label);
        taskLabel = (TextView) findViewById(R.id.activity_daily_call_editor__task_label);
        acCompanyValue = (TextView) findViewById(R.id.activity_daily_call_editor__accompany_value);

        visibleOne = findViewById(R.id.activity_daily_call_editor__one);
        visibleTwo = findViewById(R.id.activity_daily_call_editor__two);
        visiblewOne1 = findViewById(R.id.activity_daily_call_editor__one1);


        itemTask = findViewById(R.id.activity_daily_call_editor__task);
        itemAccompany = findViewById(R.id.activity_daily_call_editor__accompany);
        itemSubject = findViewById(R.id.activity_daily_call_editor__subject);
        itemType = findViewById(R.id.activity_daily_call_editor__type);
        itemStatus = findViewById(R.id.activity_daily_call_editor__status);
        itemStartTime = findViewById(R.id.activity_daily_call_editor__start_time);
        itemEndTime = findViewById(R.id.activity_daily_call_editor__end_time);
        itemLocation = findViewById(R.id.activity_daily_call_editor__location);
        itemCallPlan = findViewById(R.id.activity_daily_call_editor__call_plan);
        itemNote = findViewById(R.id.activity_daily_call_editor__note);
        itemComments = findViewById(R.id.activity_daily_call_editor__comment);
        itemFollowUp = findViewById(R.id.activity_daily_call_editor__follow_up);
        itemResult = findViewById(R.id.activity_daily_call_editor__result);
        itemPhoto = findViewById(R.id.activity_daily_call_editor__photo);
        itemAddSubject = findViewById(R.id.activity_daily_call_editor__subject_add);
        itemAddType = findViewById(R.id.activity_daily_call_editor__type_add);
        itemAddStartTime = findViewById(R.id.activity_daily_call_editor__start_time_add);
        itemAddEndTime = findViewById(R.id.activity_daily_call_editor__end_time_add);

        itemTask.setOnClickListener(itemClickListener);
        itemAccompany.setOnClickListener(itemClickListener);
        itemSubject.setOnClickListener(itemClickListener);
        itemType.setOnClickListener(itemClickListener);
        itemStatus.setOnClickListener(itemClickListener);
        itemStartTime.setOnClickListener(itemClickListener);
        itemEndTime.setOnClickListener(itemClickListener);
        itemLocation.setOnClickListener(itemClickListener);
        itemCallPlan.setOnClickListener(itemClickListener);
        itemNote.setOnClickListener(itemClickListener);
        itemComments.setOnClickListener(itemClickListener);
        itemFollowUp.setOnClickListener(itemClickListener);
        itemResult.setOnClickListener(itemClickListener);
        itemPhoto.setOnClickListener(itemClickListener);
        itemAddSubject.setOnClickListener(itemClickListener);
        itemAddType.setOnClickListener(itemClickListener);
        itemAddStartTime.setOnClickListener(itemClickListener);
        itemAddEndTime.setOnClickListener(itemClickListener);

    }

    private void onItemClick(int id) {
        switch (id) {
            case R.id.activity_daily_call_editor__item_customer:
                if ("true".equals(followUp) || "true".equals(customer_add)) {

                } else {
                    Intent intent = new Intent(getBaseContext(), CustomerTypeSelectorActivity.class);
                    startActivityForResult(intent, CustomerTypeSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT);
                }

                break;
            case R.id.activity_daily_call_editor__accompany://监护人
                Intent intents = new Intent(getBaseContext(), AcCompanyListActivity.class);
                startActivityForResult(intents, AcCompanyListActivity.ACTIVITY_ACCOMPANY_CODE);
                break;
            case R.id.activity_daily_call_editor__task:
                if (taskShow) {
                    if ("true".equals(followUp)) {
                    }
                    if ("true".equals(customer_add)) {

                        if (!"Key Account".equals(cusType)) {
                        } else {
                            Intent intentTask = new Intent(getBaseContext(), DailyCallTaskListActivity.class);
                            intentTask.putExtra(DailyCallTaskListActivity.CUSTOMERROWID, customerRowId);
                            startActivityForResult(intentTask, DailyCallTaskListActivity.ACTIVITY_TASK_RESULT);
                        }
                    } else {
                        if (!"Key Account".equals(assoc_type)) {
                        } else {
                            Intent intentTask = new Intent(getBaseContext(), DailyCallTaskListActivity.class);
                            intentTask.putExtra(DailyCallTaskListActivity.CUSTOMERROWID, "");
                            startActivityForResult(intentTask, DailyCallTaskListActivity.ACTIVITY_TASK_RESULT);
                        }
                    }
                } else {
                }

                break;
            case R.id.activity_daily_call_editor__subject:
                Intent subjectIntent = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                subjectIntent.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_DAILY_CALL_SUBJECT);
                startActivityForResult(subjectIntent, TypeAndStatusSelectActivity.ACTIVITY_ADD_UPDATE_DAILY_CALL_SUBJECT_SELECT);
                break;
            case R.id.activity_daily_call_editor__type:
                Intent typeIntent = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                typeIntent.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_DAILY_CALL_TYPE);
                startActivityForResult(typeIntent, TypeAndStatusSelectActivity.ACTIVITY_ADD_UPDATE_DAILY_CALL_TYPE_SELECT);
                break;
            case R.id.activity_daily_call_editor__status:
                Intent statusIntent = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                statusIntent.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_DAILY_CALL_STATUS);
                startActivityForResult(statusIntent, TypeAndStatusSelectActivity.ACTIVITY_ADD_UPDATE_DAILY_CALL_STATUS_SELECT);
                break;
            case R.id.activity_daily_call_editor__start_time:
                startOrEndDate = true;
                showPopWindowDatePicker();
                break;
            case R.id.activity_daily_call_editor__end_time:
                startOrEndDate = false;
                showPopWindowDatePicker();
                break;
            case R.id.activity_daily_call_editor__location:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.prompt_location));
                textEditorData.setHint(context.getString(R.string.text_edit_hint));
                textEditorData.setSingleLine(false);
                Application.getInstance().setTextEditorData(textEditorData);
                Intent editeIntent1 = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(locationValue.getText())) {
                    editeIntent1.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, locationValue.getText());
                }
                startActivityForResult(editeIntent1, TextEditorActivity.ACTIVITY_EDIT_DAILY_CALL_LOCATION_RESULT);
                break;
            case R.id.activity_daily_call_editor__call_plan:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.prompt_call_plans));
                textEditorData.setHint(context.getString(R.string.text_edit_hint));
                textEditorData.setSingleLine(false);
                Application.getInstance().setTextEditorData(textEditorData);
                Intent editeIntent2 = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(callplanValue.getText())) {
                    editeIntent2.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, callplanValue.getText());
                }
                startActivityForResult(editeIntent2, TextEditorActivity.ACTIVITY_EDIT_DAILY_CALL_CALLPLAN_RESULT);
                break;
            case R.id.activity_daily_call_editor__note:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.prompt_note));
                textEditorData.setHint(context.getString(R.string.text_edit_hint));
                textEditorData.setSingleLine(false);
                Application.getInstance().setTextEditorData(textEditorData);
                Intent editeIntent3 = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(noteVale.getText())) {
                    editeIntent3.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, noteVale.getText());
                }
                startActivityForResult(editeIntent3, TextEditorActivity.ACTIVITY_EDIT_DAILY_CALL_NOTE_RESULT);
                break;
            case R.id.activity_daily_call_editor__comment:
                Intent intentComment = new Intent(getBaseContext(), DailyCallCommentsListActivity.class);
                if ("true".equals(draftCome)) {
                    intentComment.putExtra(DailyCallCommentsListActivity.ACCOUNT_NAME, dbDailycall.getAccountName());
                    intentComment.putExtra(DailyCallCommentsListActivity.SUBJECT, dbDailycall.getSubject());
                    intentComment.putExtra(DailyCallCommentsListActivity.CALL_PLAN, dbDailycall.getMeetingContent());
                    intentComment.putExtra(DailyCallCommentsListActivity.DAILY_CALL_ID, dbDailycall.getRowId());
                } else {
                    intentComment.putExtra(DailyCallCommentsListActivity.ACCOUNT_NAME, dailyCallDetail.getAccountName());
                    intentComment.putExtra(DailyCallCommentsListActivity.SUBJECT, dailyCallDetail.getSubject());
                    intentComment.putExtra(DailyCallCommentsListActivity.CALL_PLAN, dailyCallDetail.getMeetingContent());
                    intentComment.putExtra(DailyCallCommentsListActivity.DAILY_CALL_ID, dailyCallDetail.getRowId());
                }
                startActivityForResult(intentComment, DailyCallCommentsListActivity.RESULT_CODE);
                break;
            case R.id.activity_daily_call_editor__follow_up:
                Intent addDailyCallIntent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
                Application.getInstance().setDailyCallDetail4Edit(null);
                addDailyCallIntent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, assoc_type);
                addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_ID, task_id);
                if ("Key Account".equals(assoc_type)) {
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_ID, task_id);
                } else if ("Other Account".equals(assoc_type)) {
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, customerRowId);
                } else {
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, customerRowId);
                }
                if ("true".equals(draftCome)) {
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME, dbDailycall.getAccountName());
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_NAME, dbDailycall.getTaskName());
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.SUBJECT_NAME, dbDailycall.getSubject());
                } else {
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME, dailyCallDetail.getAccountName());
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_NAME, dailyCallDetail.getTaskName());
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.SUBJECT_NAME, dailyCallDetail.getSubject());
                }
                addDailyCallIntent.putExtra(DailyCallEditorActivity.FOLLOW_UP, "true");
                startActivityForResult(addDailyCallIntent, DailyCallEditorActivity.ACTIVITY_FOLLOW_UP_RESULT);
                break;
            case R.id.activity_daily_call_editor__result:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.prompt_result));
                textEditorData.setHint(context.getString(R.string.text_edit_hint));
                textEditorData.setSingleLine(false);
                Application.getInstance().setTextEditorData(textEditorData);
                Intent editeIntent4 = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(resultValut.getText())) {
                    editeIntent4.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, resultValut.getText());
                }
                startActivityForResult(editeIntent4, TextEditorActivity.ACTIVITY_EDIT_DAILY_CALL_RESULT_RESULT);
                break;
            case R.id.activity_daily_call_editor__photo:
                Intent intent = new Intent(getBaseContext(), DailyCallPhotoActivity.class);
                if (editOrAdd) {
                    if (backOfPhoto == 1) {

                    } else {
                        if (dailyCallDetail.getPicList() != null) {
                            ArrayList<ImageItem> lists = new ArrayList<ImageItem>();
                            for (String s : dailyCallDetail.getPicList()) {
                                ImageItem item = new ImageItem();
                                item.setSelect(false);
                                item.setPath(s);
                                lists.add(item);
                            }
                            Application.getInstance().setImageList(lists);
                        }
                    }
                } else {
                }
                startActivityForResult(intent, DailyCallPhotoActivity.ACTIVITY_PHOTO_RESULT);
                break;
            case R.id.activity_daily_call_editor__subject_add:
                Intent subjectIntent2 = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                subjectIntent2.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_DAILY_CALL_SUBJECT);
                startActivityForResult(subjectIntent2, TypeAndStatusSelectActivity.ACTIVITY_ADD_UPDATE_DAILY_CALL_SUBJECT_SELECT);
                break;
            case R.id.activity_daily_call_editor__type_add:
                Intent typeIntent2 = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                typeIntent2.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_DAILY_CALL_TYPE);
                startActivityForResult(typeIntent2, TypeAndStatusSelectActivity.ACTIVITY_ADD_UPDATE_DAILY_CALL_TYPE_SELECT);
                break;
            case R.id.activity_daily_call_editor__start_time_add:
                startOrEndDate = true;
                showPopWindowDatePicker();
                break;
            case R.id.activity_daily_call_editor__end_time_add:
                startOrEndDate = false;
                showPopWindowDatePicker();
                break;


        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle;
        switch (requestCode) {
            case TextEditorActivity.ACTIVITY_TEXT_EDITOR_RESULT:
                resultBundle = data.getExtras();
                String text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                break;
            case AcCompanyListActivity.ACTIVITY_ACCOMPANY_CODE:
                resultBundle = data.getExtras();
                String AcName = resultBundle
                        .getString(AcCompanyListActivity.ACTIVITY_ACCOMPANY);
                acRowId = resultBundle.getString(AcCompanyListActivity.ACTIVITY_ACCOMPANY_ID);
                acCompanyValue.setText(AcName);
                break;
            case CustomerTypeSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT:
                resultBundle = data.getExtras();
                String nameEn = resultBundle
                        .getString(CustomerSelectorActivity.KEY_NAME_EN);
                customerRowId = resultBundle.getString(CustomerSelectorActivity.KEY_ID);
                assoc_type = resultBundle.getString(CustomerSelectorActivity.ASSOC_TYPE);
                if ("Target Leads".equals(assoc_type)) {
                    assoc_type = "Leads";
                }
                if (!"Key Account".equals(assoc_type)) {
                    taskLabel.setTextColor(this.getResources().getColor(R.color.textGray));
                    taskValue.setTextColor(this.getResources().getColor(R.color.textGray));
                    itemTask.setClickable(false);
                } else {
                    taskLabel.setTextColor(this.getResources().getColor(R.color.colorAccent));
                    itemTask.setClickable(true);
                }
                customerValue.setText(nameEn);
                taskValue.setText(null);
                if (editOrAdd) {
                    subjectValue.setText(null);
                } else {
                    subjectAddValue.setText(null);
                }
                if (CustomerTypeSelectorActivity.judge != 1) {
                    taskShow = false;
                    // taskValue.setTextColor(getApplication().getColor(R.color.transparentCoditionLeft));
                    // taskValue.setTextColor(R.color.transparentCoditionLeft);
                    taskLabel.setTextColor(this.getResources().getColor(R.color.textGray));
                    itemTask.setClickable(false);
                } else {
                    taskShow = true;
                    taskLabel.setTextColor(this.getResources().getColor(R.color.colorAccent));

                    itemTask.setClickable(true);
                }
                break;
            case TypeAndStatusSelectActivity.ACTIVITY_ADD_UPDATE_DAILY_CALL_STATUS_SELECT:
                resultBundle = data.getExtras();
                String textStatus = resultBundle
                        .getString(TaskEditActivity.TYPE_OR_STATUS_NAME);
                statusValue.setText(textStatus);
                break;
            case TypeAndStatusSelectActivity.ACTIVITY_ADD_UPDATE_DAILY_CALL_SUBJECT_SELECT:
                resultBundle = data.getExtras();
                String textSubject = resultBundle
                        .getString(TaskEditActivity.TYPE_OR_STATUS_NAME);
                if (editOrAdd) {
                    subjectValue.setText(textSubject);
                } else {
                    subjectAddValue.setText(textSubject);
                }
                if (TypeAndStatusSelectActivity.listsDate.get(TypeAndStatusSelectActivity.positions).getFlag() == 1) {
                    customerValue.setText(null);
                    taskValue.setText(null);
                    taskShow = true;
                    itemTask.setBackground(null);
                }
                break;
            case TypeAndStatusSelectActivity.ACTIVITY_ADD_UPDATE_DAILY_CALL_TYPE_SELECT:
                resultBundle = data.getExtras();
                String texttype = resultBundle
                        .getString(TaskEditActivity.TYPE_OR_STATUS_NAME);
                if (editOrAdd) {
                    typeValue.setText(texttype);
                } else {
                    typeAddValue.setText(texttype);
                }
                break;
            case TextEditorActivity.ACTIVITY_EDIT_DAILY_CALL_CALLPLAN_RESULT:
                resultBundle = data.getExtras();
                String textCallPlan = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                callplanValue.setText(textCallPlan);
                break;
            case TextEditorActivity.ACTIVITY_EDIT_DAILY_CALL_LOCATION_RESULT:
                resultBundle = data.getExtras();
                String textLocation = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                locationValue.setText(textLocation);
                break;
            case TextEditorActivity.ACTIVITY_EDIT_DAILY_CALL_NOTE_RESULT:
                resultBundle = data.getExtras();
                String textNote = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                noteVale.setText(textNote);
                break;
            case TextEditorActivity.ACTIVITY_EDIT_DAILY_CALL_RESULT_RESULT:
                resultBundle = data.getExtras();
                String textResult = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                resultValut.setText(textResult);
                break;
            case DailyCallTaskListActivity.ACTIVITY_TASK_RESULT:
                resultBundle = data.getExtras();
                String textTask = resultBundle
                        .getString(DailyCallTaskListActivity.ACTIVITY_TASK_RESULT_NAME);
                String textTaskAccount = resultBundle
                        .getString(DailyCallTaskListActivity.ACTIVITY_TASK_RESULT_CUSTOMER);
                task_id = resultBundle.getString(DailyCallTaskListActivity.ACTIVITY_TASK_RESULT_ID);
                taskValue.setText(textTask);
                if ("".equals(customerValue.getText())) {
                    customerValue.setText(textTaskAccount);
                }
                break;
            case DailyCallEditorActivity.ACTIVITY_FOLLOW_UP_RESULT:
                resultBundle = data.getExtras();
                String texts = resultBundle
                        .getString(DailyCallEditorActivity.ACTIVITY_FOLLOW_UP_VALUE);
                followUpValue.setText(texts);
                followUpValue.setTextColor(this.getResources().getColor(R.color.textGray));
                itemFollowUp.setClickable(false);
                //  followUpValue.setTextColor(this.getResources().getColor());
                break;
            case DailyCallCommentsListActivity.RESULT_CODE:
                resultBundle = data.getExtras();
                String textss = resultBundle
                        .getString(DailyCallCommentsListActivity.ACTIVITY_COMMENTS_VALUE);
                commentsValue.setText(textss);
                //  followUpValue.setTextColor(this.getResources().getColor());
                break;
            case DailyCallPhotoActivity.ACTIVITY_PHOTO_RESULT:
                resultBundle = data.getExtras();
                String photoCount = resultBundle
                        .getString(DailyCallPhotoActivity.PHOTO_LISTS);
                photoValue.setText(photoCount);
                backOfPhoto = 1;
                //  followUpValue.setTextColor(this.getResources().getColor());
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
                Application.getInstance().setImageList(null);
                initDailyCall();
                if (editOrAdd) {
                    //编辑的草稿箱
                    if ("true".equalsIgnoreCase(draftCome)) {
                        judgeDraftsValue();
                    } else {
                        judgeEditValue();
                    }
                } else {//修改
                    //添加的草稿箱
                    if ("true".equalsIgnoreCase(draftCome)) {
                        judgeDraftsValue();
                    } else {
                        judgeAddValue();
                    }
                }
                //  finish();
                break;
            case R.id.action_task_add_confirm:
                //保存工作(先验证信息是否填写准确)
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
                    //update daily call
                    if (editOrAdd) {
                        //   Log.v("xxxx",customerRowId+"--"+dailyCallDetail.getAssocType()+"--"+dailyCallDetail.getTaskId()+"--"+dailyCallDetail.getRowId());
                        UpdateDailyCallRO updateDailyCallRO = new UpdateDailyCallRO();
                        updateDailyCallRO.setCustomerRowId(customerRowId);
                        updateDailyCallRO.setStatus(statusValue.getText().toString());
                        if (Application.getInstance().getImageList() != null) {
                            int sum = Application.getInstance().getImageList().size();
                            StringBuffer sb = new StringBuffer();
                            for (int i = 0; i < sum; i++) {
                                sb.append(Application.getInstance().getImageList().get(i).getPath());
                                if (i != sum) {
                                    sb.append(",");
                                }
                            }
                            photoName = sb.toString();
                        }
                        updateDailyCallRO.setPicNames(photoName);
                        updateDailyCallRO.setResult(resultValut.getText().toString());
                        if ("true".equals(draftCome)) {
                            updateDailyCallRO.setAssocType(assoc_type);
                            updateDailyCallRO.setTaskId(task_id);
                            updateDailyCallRO.setRowId(dbDailycall.getRowId());
                        } else {
                            updateDailyCallRO.setAssocType(dailyCallDetail.getAssocType());
                            updateDailyCallRO.setTaskId(dailyCallDetail.getTaskId());
                            updateDailyCallRO.setRowId(dailyCallDetail.getRowId());
                        }
                        updateDailyCallRO.setEndTime(endTimeValue.getText().toString());
                        updateDailyCallRO.setStartTime(startTimeValue.getText().toString());
                        updateDailyCallRO.setLocation(locationValue.getText().toString());
                        updateDailyCallRO.setMeetingContent(callplanValue.getText().toString());
                        updateDailyCallRO.setNote(noteVale.getText().toString());
                        updateDailyCallRO.setType(typeValue.getText().toString());
                        updateDailyCallRO.setSubject(subjectValue.getText().toString());
                        updateDailyCallRO.setAcmpUserId(acRowId);
                        service.updateDailyCall(new ICallback<ResultVO>() {
                            @Override
                            public void onDataReady(ResultVO data) {
                                if ("true".equals(customer_add)) {//customer  add
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "修改Daily Call成功", Toast.LENGTH_SHORT).show();
                                    initDailyCall();
                                    finish();
                                } else if ("true".equals(draftCome)) {
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra(DraftsDailyCallListActivity.RESULT_TEXT_DAILY_CALL, "");
                                    setResult(RESULT_OK, resultIntent);
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "修改Daily Call成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    if ("true".equalsIgnoreCase(ifTrueDCList)) {
                                        DailyCallListActivity.DCListActivity.finish();
                                    } else {
                                    }
                                    if (getIntent().getStringExtra(TASK_COME) != null) {
                                        Intent intent1 = new Intent(getBaseContext(), TaskDailyCallListActivity.class);
                                        intent1.putExtra(TaskDailyCallListActivity.TASK_NAME, taskValue.getText().toString());
                                        intent1.putExtra(TaskDailyCallListActivity.TASK_CUSTOMER, customerValue.getText().toString());
                                        intent1.putExtra(TaskDailyCallListActivity.TASK_ID, task_id);
                                        startActivity(intent1);
                                        initDailyCall();
                                        finish();
                                    } else {
                                        if ("true".equals(getIntent().getStringExtra(CustomerDailyCallListActivity.CUSTOMER_DETAIL_ADD_DAILY_CALL))) {
                                            CustomerDailyCallListActivity.DCListActivity.finish();
                                            Intent intent = new Intent(getBaseContext(), CustomerDailyCallListActivity.class);
                                            intent.putExtra(CustomerDailyCallListActivity.ROW_ID, customerRowId);
                                            startActivity(intent);
                                            finish();
                                        } else {
                                            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallListActivity.class);
                                            startActivity(intent);
                                            Toast.makeText(Application.getInstance().getCurrentActivity(), "修改Daily Call成功", Toast.LENGTH_SHORT).show();
                                            initDailyCall();
                                            finish();
                                        }

                                    }
                                }
                            }

                            @Override
                            public void onError(Throwable throwable) {
                                SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                String date = sDateFormat.format(new java.util.Date());
                                DBManger dbManger = new DBManger(DailyCallEditorActivity.this);
                                DBDailycall dbDailycall = new DBDailycall("update", dailyCallDetail.getRowId(), dailyCallDetail.getTaskId(), customerValue.getText().toString(), customerRowId, dailyCallDetail.getAssocType(),
                                        callplanValue.getText().toString(), resultValut.getText().toString(), noteVale.getText().toString(), "", startTimeValue.getText().toString(), endTimeValue.getText().toString(), typeValue.getText().toString(),
                                        statusValue.getText().toString(), locationValue.getText().toString(), subjectValue.getText().toString(), "", photoName, date, salesTitleValue.getText().toString(), "", acRowId, acCompanyValue.getText().toString());
                                dbManger.InsertDailyCallListTable(dbDailycall);
                                Toast.makeText(DailyCallEditorActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                            }
                        }, updateDailyCallRO);
                    } else {//add daily call
                        AddDailyCallRO addDailyCallRO = new AddDailyCallRO();
                        addDailyCallRO.setCustomerRowId(customerRowId);
                        addDailyCallRO.setStartTime(startTimeAddValue.getText().toString());
                        addDailyCallRO.setEndTime(endTimeAddValue.getText().toString());
                        addDailyCallRO.setLocation(locationValue.getText().toString());
                        addDailyCallRO.setNote(noteVale.getText().toString());
                        addDailyCallRO.setSubject(subjectAddValue.getText().toString());
                        if (task_id == null) {
                            task_id = "";
                        }
                        addDailyCallRO.setTaskId(task_id);
                        addDailyCallRO.setAssocType(assoc_type);
                        addDailyCallRO.setType(typeAddValue.getText().toString());
                        addDailyCallRO.setMeetingContent(callplanValue.getText().toString());
                        addDailyCallRO.setResult("");
                        addDailyCallRO.setAcmpUserId(acRowId);
                        if (Application.getInstance().getImageList() != null) {
                            int sum = Application.getInstance().getImageList().size();
                            StringBuffer sb = new StringBuffer();
                            for (int i = 0; i < sum; i++) {
                                sb.append(Application.getInstance().getImageList().get(i).getPath());
                                if (i != sum) {
                                    sb.append(",");
                                }
                            }
                            photoName = sb.toString();
                        }
                        addDailyCallRO.setPicNames(photoName);
                        addDailyCallRO.setStatus("Planned");
                        if ("true".equals(followUp)) {
                            service.addDailyCall(new ICallback<ResultVO>() {
                                @Override
                                public void onDataReady(ResultVO data) {
                                    // Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallListActivity.class);
                                    // startActivity(intent);
                                    // Toast.makeText(Application.getInstance().getCurrentActivity(), "添加Daily Call成功", Toast.LENGTH_SHORT).show();
                                    if (getIntent().getStringExtra(TASK_COME) != null) {
                                        Intent intent1 = new Intent(getBaseContext(), TaskDailyCallListActivity.class);
                                        intent1.putExtra(TaskDailyCallListActivity.TASK_NAME, taskValue.getText().toString());
                                        intent1.putExtra(TaskDailyCallListActivity.TASK_CUSTOMER, customerValue.getText().toString());
                                        intent1.putExtra(TaskDailyCallListActivity.TASK_ID, task_id);
                                        startActivity(intent1);
                                        initDailyCall();
                                        finish();
                                    } else if (getIntent().getStringExtra(TASK_COME2) != null) {
                                        //  Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallListActivity.class);
                                        //  startActivity(intent);
                                        Toast.makeText(Application.getInstance().getCurrentActivity(), "添加Daily Call成功", Toast.LENGTH_SHORT).show();
                                        initDailyCall();
                                        finish();
                                    } else {
                                        initDailyCall();
                                        Bundle resultBundle = new Bundle();
                                        resultBundle.putString(
                                                DailyCallEditorActivity.ACTIVITY_FOLLOW_UP_VALUE,
                                                "Done");
                                        Intent resultIntent = new Intent()
                                                .putExtras(resultBundle);
                                        setResult(
                                                DailyCallEditorActivity.ACTIVITY_FOLLOW_UP_RESULT,
                                                resultIntent);
                                        finish();
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String date = sDateFormat.format(new java.util.Date());
                                    DBManger dbManger = new DBManger(DailyCallEditorActivity.this);
                                    DBDailycall dbDailycall = new DBDailycall("new", "", task_id, customerValue.getText().toString(), customerRowId, assoc_type, callplanValue.getText().toString(), "",
                                            noteVale.getText().toString(), "", startTimeAddValue.getText().toString(), endTimeAddValue.getText().toString(),
                                            typeAddValue.getText().toString(), "Planned", locationValue.getText().toString(), subjectAddValue.getText().toString(), "", photoName, date, "", "", acRowId, acCompanyValue.getText().toString());
                                    dbManger.InsertDailyCallListTable(dbDailycall);
                                    Toast.makeText(DailyCallEditorActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                                }
                            }, addDailyCallRO);
                        } else {
                            service.addDailyCall(new ICallback<ResultVO>() {
                                @Override
                                public void onDataReady(ResultVO data) {
                                    if ("true".equals(ifTrueDCList)) {
                                        DailyCallListActivity.DCListActivity.finish();
                                        Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallListActivity.class);
                                        startActivity(intent);
                                        Toast.makeText(Application.getInstance().getCurrentActivity(), "添加Daily Call成功", Toast.LENGTH_SHORT).show();
                                        initDailyCall();
                                        finish();
                                    } else {
                                        if ("true".equals(getIntent().getStringExtra(CustomerDailyCallListActivity.CUSTOMER_DETAIL_ADD_DAILY_CALL))) {
                                            CustomerDailyCallListActivity.DCListActivity.finish();
                                            Intent intent = new Intent(getBaseContext(), CustomerDailyCallListActivity.class);
                                            intent.putExtra(CustomerDailyCallListActivity.ROW_ID, customerRowId);
                                            startActivity(intent);
                                            finish();
                                        } else if ("true".equals(draftCome)) {
                                            Intent resultIntent = new Intent();
                                            resultIntent.putExtra(DraftsDailyCallListActivity.RESULT_TEXT_DAILY_CALL, "");
                                            setResult(RESULT_OK, resultIntent);
                                            Toast.makeText(Application.getInstance().getCurrentActivity(), "修改Daily Call成功", Toast.LENGTH_SHORT).show();
                                            finish();
                                        } else {
                                            Toast.makeText(Application.getInstance().getCurrentActivity(), "添加Daily Call成功", Toast.LENGTH_SHORT).show();
                                            initDailyCall();
                                            finish();
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                                    String date = sDateFormat.format(new java.util.Date());
                                    DBManger dbManger = new DBManger(DailyCallEditorActivity.this);
                                    DBDailycall dbDailycall = new DBDailycall("new", "", task_id, customerValue.getText().toString(), customerRowId, assoc_type, callplanValue.getText().toString(), "",
                                            noteVale.getText().toString(), "", startTimeAddValue.getText().toString(), endTimeAddValue.getText().toString(),
                                            typeAddValue.getText().toString(), "Planned", locationValue.getText().toString(), subjectAddValue.getText().toString(), "", photoName, date, "", "", acRowId, acCompanyValue.getText().toString());
                                    dbManger.InsertDailyCallListTable(dbDailycall);
                                    Toast.makeText(DailyCallEditorActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                                }
                            }, addDailyCallRO);
                        }
                    }
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void judgeAddValue() {
        boolean result = false;
        if (!"".equals(customerValue.getText().toString())) {
            result = true;
        } else if (!"".equals(taskValue.getText().toString())) {
            result = true;
        } else if (!"".equals(resultValut.getText().toString())) {
            result = true;
        } else if (!"".equals(noteVale.getText().toString())) {
            result = true;
        } else if (!"".equals(callplanValue.getText().toString())) {
            result = true;
        } else if (!"".equals(acCompanyValue.getText().toString())) {
            result = true;
        } else if (!"".equals(locationValue.getText().toString())) {
            result = true;
        } else if (!"".equals(subjectAddValue.getText().toString())) {
            result = true;
        } else if (!"".equals(typeAddValue.getText().toString())) {
            result = true;
        } else if (!"".equals(startTimeAddValue.getText().toString())) {
            result = true;
        } else if (!"".equals(endTimeAddValue.getText().toString())) {
            result = true;
        }

        if (result) {
            showDraftsWindow();
        } else {
            finish();
        }
    }

    private void judgeEditValue() {
        boolean result = false;
        if (!customerValue.getText().toString().equals(dailyCallDetail.getAccountName().toString())) {
            result = true;
        } else if (!taskValue.getText().toString().equals(dailyCallDetail.getTaskName().toString())) {
            result = true;
        } else if (!resultValut.getText().toString().equals(dailyCallDetail.getResult().toString())) {
            result = true;
        } else if (!noteVale.getText().toString().equals(dailyCallDetail.getNote().toString())) {
            result = true;
        } else if (!callplanValue.getText().toString().equals(dailyCallDetail.getMeetingContent().toString())) {
            result = true;
        } else if (!locationValue.getText().toString().equals(dailyCallDetail.getLocation().toString())) {
            result = true;
        }else if (!acCompanyValue.getText().toString().equals(dailyCallDetail.getAcmpUserName().toString())) {
            result = true;
        } else if (!subjectValue.getText().toString().equals(dailyCallDetail.getSubject().toString())) {
            result = true;
        } else if (!typeValue.getText().toString().equals(dailyCallDetail.getType().toString())) {
            result = true;
        } else if (!startTimeValue.getText().toString().equals(dailyCallDetail.getStartTime().toString())) {
            result = true;
        } else if (!endTimeValue.getText().toString().equals(dailyCallDetail.getEndTime().toString())) {
            result = true;
        } else if (!statusValue.getText().toString().equals(dailyCallDetail.getStatus().toString())) {
            result = true;
        }

        if (result) {
            showDraftsWindow();
        } else {
            finish();
        }
    }

    private void judgeDraftsValue() {
        boolean result = false;
        if (!customerValue.getText().toString().equals(dbDailycall.getAccountName().toString())) {
            result = true;
        } else if (!taskValue.getText().toString().equals(dbDailycall.getTaskName().toString())) {
            result = true;
        } else if (!resultValut.getText().toString().equals(dbDailycall.getResult().toString())) {
            result = true;
        } else if (!noteVale.getText().toString().equals(dbDailycall.getNote().toString())) {
            result = true;
        } else if (!callplanValue.getText().toString().equals(dbDailycall.getMeetingContent().toString())) {
            result = true;
        } else if (!locationValue.getText().toString().equals(dbDailycall.getLocation().toString())) {
            result = true;
        } else if (!acCompanyValue.getText().toString().equals(dbDailycall.getAcName().toString())) {
            result = true;
        }
        if ("update".equals(dbDailycall.getRemark())) {
            if (!subjectValue.getText().toString().equals(dbDailycall.getSubject().toString())) {
                result = true;
            } else if (!typeValue.getText().toString().equals(dbDailycall.getType().toString())) {
                result = true;
            } else if (!startTimeValue.getText().toString().equals(dbDailycall.getStartTime().toString())) {
                result = true;
            } else if (!endTimeValue.getText().toString().equals(dbDailycall.getEndTime().toString())) {
                result = true;
            }
        } else {
            if (!subjectAddValue.getText().toString().equals(dbDailycall.getSubject().toString())) {
                result = true;
            } else if (!typeAddValue.getText().toString().equals(dbDailycall.getType().toString())) {
                result = true;
            } else if (!startTimeAddValue.getText().toString().equals(dbDailycall.getStartTime().toString())) {
                result = true;
            } else if (!endTimeAddValue.getText().toString().equals(dbDailycall.getEndTime().toString())) {
                result = true;
            }
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
        window.showAtLocation(findViewById(R.id.activity_daily_call_editor__item_customer),
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

                if (editOrAdd) {
                    if ("true".equals(draftCome)) {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());
                        DBManger dbManger = new DBManger(DailyCallEditorActivity.this);
                        DBDailycall dbDailycalls = new DBDailycall("update", dbDailycall.getRowId(), dbDailycall.getTaskId(), customerValue.getText().toString(), customerRowId, dbDailycall.getAssocType(),
                                callplanValue.getText().toString(), resultValut.getText().toString(), noteVale.getText().toString(), "", startTimeValue.getText().toString(), endTimeValue.getText().toString(), typeValue.getText().toString(),
                                statusValue.getText().toString(), locationValue.getText().toString(), subjectValue.getText().toString(), "", photoName, date, salesTitleValue.getText().toString(), "", acRowId, acCompanyValue.getText().toString());
                        dbManger.UpdateDailyCallById(dbDailycalls, dbDailycall.getId());
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(DraftsDailyCallListActivity.RESULT_TEXT_DAILY_CALL, "true");
                        setResult(RESULT_OK, resultIntent);
                    } else {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());
                        DBManger dbManger = new DBManger(DailyCallEditorActivity.this);
                        DBDailycall dbDailycall = new DBDailycall("update", dailyCallDetail.getRowId(), dailyCallDetail.getTaskId(), customerValue.getText().toString(), customerRowId, dailyCallDetail.getAssocType(),
                                callplanValue.getText().toString(), resultValut.getText().toString(), noteVale.getText().toString(), "", startTimeValue.getText().toString(), endTimeValue.getText().toString(), typeValue.getText().toString(),
                                statusValue.getText().toString(), locationValue.getText().toString(), subjectValue.getText().toString(), "", photoName, date, salesTitleValue.getText().toString(), "", acRowId, acCompanyValue.getText().toString());
                        dbManger.InsertDailyCallListTable(dbDailycall);
                    }
                } else {
                    if ("true".equals(draftCome)) {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());
                        DBManger dbManger = new DBManger(DailyCallEditorActivity.this);
                        DBDailycall dbDailycalls = new DBDailycall("new", "", task_id, customerValue.getText().toString(), customerRowId, assoc_type, callplanValue.getText().toString(), "",
                                noteVale.getText().toString(), "", startTimeAddValue.getText().toString(), endTimeAddValue.getText().toString(),
                                typeAddValue.getText().toString(), "Planned", locationValue.getText().toString(), subjectAddValue.getText().toString(), "", photoName, date, "", "", acRowId, acCompanyValue.getText().toString());
                        dbManger.UpdateDailyCallById(dbDailycalls, dbDailycall.getId());
                        // Toast.makeText(DailyCallEditorActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra(DraftsDailyCallListActivity.RESULT_TEXT_DAILY_CALL, "true");
                        setResult(RESULT_OK, resultIntent);
                        //  Toast.makeText(Application.getInstance().getCurrentActivity(), "修改Leads成功", Toast.LENGTH_SHORT).show();
                    } else {
                        SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String date = sDateFormat.format(new java.util.Date());
                        DBManger dbManger = new DBManger(DailyCallEditorActivity.this);
                        DBDailycall dbDailycall = new DBDailycall("new", "", task_id, customerValue.getText().toString(), customerRowId, assoc_type, callplanValue.getText().toString(), "",
                                noteVale.getText().toString(), "", startTimeAddValue.getText().toString(), endTimeAddValue.getText().toString(),
                                typeAddValue.getText().toString(), "Planned", locationValue.getText().toString(), subjectAddValue.getText().toString(), "", photoName, date, "", "", acRowId, acCompanyValue.getText().toString());
                        dbManger.InsertDailyCallListTable(dbDailycall);
                        Toast.makeText(DailyCallEditorActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                        //   Toast.makeText(Application.getInstance().getCurrentActivity(), "修改Leads成功", Toast.LENGTH_SHORT).show();
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
        boolean subject_type = false;
        if (editOrAdd) {
            if ("".equals(subjectValue.getText().toString())) {
                result = true;
            } else {
                if ("true".equals(draftCome)) {
                    if ("On leave".equalsIgnoreCase(dbDailycall.getSubject()) || "Internal meeting/At office".equalsIgnoreCase(dbDailycall.getSubject())) {

                    } else {
                        if ("".equals(customerValue.getText().toString())) {
                            result = true;
                        }
                        if ("".equals(typeValue.getText().toString())) {
                            result = true;
                        }
                        if (!"Key Account".equals(assoc_type)) {
                        } else {
                            if ("".equals(taskValue.getText().toString())) {
                                Toast.makeText(Application.getInstance().getBaseContext(), "请选择Task", Toast.LENGTH_SHORT).show();
                                result = true;
                            }
                        }
                    }
                } else {
                    if ("On leave".equalsIgnoreCase(subjectValue.getText().toString()) || "Internal meeting/At office".equalsIgnoreCase(subjectValue.getText().toString())) {

                    } else {
                        if ("".equals(customerValue.getText().toString())) {
                            result = true;
                        }
                        if ("".equals(typeValue.getText().toString())) {
                            result = true;
                        }
                        if (!"Key Account".equals(assoc_type)) {
                        } else {
                            if ("".equals(taskValue.getText().toString())) {
                                Toast.makeText(Application.getInstance().getBaseContext(), "请选择Task", Toast.LENGTH_SHORT).show();
                                result = true;
                            }
                        }
                    }
                }
            }
            if ("".equals(callplanValue.getText().toString())) {
                http:
//www.itlanbao.com/code/20151030/10000/100603.html
                result = true;
            }

            if ("".equals(startTimeValue.getText().toString())) {
                result = true;
            }
            if ("".equals(endTimeValue.getText().toString())) {
                result = true;
            }
        } else {
            if ("".equals(subjectAddValue.getText().toString())) {
                result = true;
            } else {
                //flow up
                if (subjectName != null) {
                } else {
                    //选择最后两个
                    if (TypeAndStatusSelectActivity.listsDate.get(TypeAndStatusSelectActivity.positions).getFlag() == 1) {
                        subject_type = true;
                    } else {
                        if ("".equals(customerValue.getText().toString())) {
                            result = true;
                        } else if ("".equals(typeAddValue.getText().toString())) {
                            result = true;
                        }
                    }
                }
            }

            if ("".equals(callplanValue.getText().toString())) {
                result = true;
            }
            if (subject_type) {

            } else {
                if ("".equals(typeAddValue.getText().toString())) {
                    result = true;
                }
            }
            if ("".equals(startTimeAddValue.getText().toString())) {
                result = true;
            }
            if ("".equals(endTimeAddValue.getText().toString())) {
                result = true;
            }
            if (!"Key Account".equals(assoc_type)) {
            } else {
                if ("".equals(taskValue.getText().toString())) {
                    Toast.makeText(Application.getInstance().getBaseContext(), "请选择Task", Toast.LENGTH_SHORT).show();
                    result = true;
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
        window.showAtLocation(findViewById(R.id.activity_daily_call_editor__item_customer),
                Gravity.BOTTOM, 0, 0);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        TimePicker timePicker = (TimePicker) view.findViewById(R.id.content_popup_timepicker);
        timePicker.setVisibility(View.VISIBLE);
        timePicker.setIs24HourView(true);
        Calendar calendar = Calendar.getInstance();
        if (!editOrAdd) {
            if (startOrEndDate) {
                /*if (!"".equals(endTimeAddValue.getText().toString())) {
                    String times = endTimeAddValue.getText().toString();
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
                } else {*/
                if (!"".equals(startTimeAddValue.getText().toString())) {
                    String times = startTimeAddValue.getText().toString();
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
                //  }
            } else {
                /*if (!"".equals(startTimeAddValue.getText().toString())) {
                    String times = startTimeAddValue.getText().toString();
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
                    monthOfYear = (Integer.parseInt(month) - 1);
                    dayOfMonth = Integer.parseInt(day);
                } else {*/
                if (!"".equals(endTimeAddValue.getText().toString())) {
                    String times = endTimeAddValue.getText().toString();
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
                    monthOfYear = (Integer.parseInt(month) - 1);
                    dayOfMonth = Integer.parseInt(day);
                } else {
                    year = calendar.get(Calendar.YEAR);
                    monthOfYear = calendar.get(Calendar.MONTH);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                }
            }
            //     }
        } else {
            if (startOrEndDate) {
                if (!"".equals(startTimeValue.getText().toString())) {
                    String times = startTimeValue.getText().toString();
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
            } else {
                if (!"".equals(endTimeValue.getText().toString())) {
                    String times = endTimeValue.getText().toString();
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
                    monthOfYear = (Integer.parseInt(month) - 1);
                    dayOfMonth = Integer.parseInt(day);
                } else {
                    year = calendar.get(Calendar.YEAR);
                    monthOfYear = calendar.get(Calendar.MONTH);
                    dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                }
            }
        }
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);
        datePicker.init(year, monthOfYear, dayOfMonth, new DatePicker.OnDateChangedListener() {

            public void onDateChanged(DatePicker view, int years,
                                      int monthOfYears, int dayOfMonths) {
                DailyCallEditorActivity.this.year = years;
                DailyCallEditorActivity.this.monthOfYear = monthOfYears;
                DailyCallEditorActivity.this.dayOfMonth = dayOfMonths;
            }

        });
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker arg0, int hours, int minutes) {
                DailyCallEditorActivity.this.hour = hours;
                DailyCallEditorActivity.this.minute = minutes;
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
                String hours = "";
                String minutes = "";
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
                if (hour < 10) {
                    hours = "0" + hour;
                } else {
                    hours = hour + "";
                }
                if (minute < 10) {
                    minutes = "0" + minute;
                } else {
                    minutes = minute + "";
                }
                date = year + "-" + month + "-" + day + " " + hours + ":" + minutes;
                if (editOrAdd) {
                    if (startOrEndDate) {
                        startTimeValue.setText(date);
                        if (!"".equals(endTimeValue.getText())) {
                            if (startTimeValue.getText().toString().compareTo(endTimeValue.getText().toString()) > 0) {
                                endTimeValue.setText("");
                                Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.error_time), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        endTimeValue.setText(date);
                        if (!"".equals(endTimeValue.getText())) {
                            if (startTimeValue.getText().toString().compareTo(endTimeValue.getText().toString()) > 0) {
                                endTimeValue.setText("");
                                Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.error_time), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                } else {
                    if (startOrEndDate) {
                        startTimeAddValue.setText(date);
                        if (!"".equals(endTimeAddValue.getText())) {
                            if (startTimeAddValue.getText().toString().compareTo(endTimeAddValue.getText().toString()) > 0) {
                                endTimeAddValue.setText("");
                                Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.error_time), Toast.LENGTH_SHORT).show();
                            }
                        }
                    } else {
                        endTimeAddValue.setText(date);
                        if (!"".equals(endTimeAddValue.getText())) {
                            if (startTimeAddValue.getText().toString().compareTo(endTimeAddValue.getText().toString()) > 0) {
                                endTimeAddValue.setText("");
                                Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.error_time), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }

                window.dismiss();
            }
        });
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
            Application.getInstance().setImageList(null);
            initDailyCall();
            if (editOrAdd) {
                //编辑的草稿箱
                if ("true".equalsIgnoreCase(draftCome)) {
                    judgeDraftsValue();
                } else {
                    judgeEditValue();
                }
            } else {//修改
                //添加的草稿箱
                if ("true".equalsIgnoreCase(draftCome)) {
                    judgeDraftsValue();
                } else {
                    judgeAddValue();
                }
            }
        }

        return false;

    }
}
