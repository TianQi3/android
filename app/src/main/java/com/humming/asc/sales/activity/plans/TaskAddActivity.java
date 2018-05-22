package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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
import android.widget.Toast;

import com.humming.asc.dp.presentation.ro.cp.task.AddTaskRO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryResultVO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.activity.TextEditorActivity;
import com.humming.asc.sales.activity.TextEditorData;
import com.humming.asc.sales.activity.customer.CustomerSelectorActivity;
import com.humming.asc.sales.activity.customer.CustomerTaskListActivity;
import com.humming.asc.sales.activity.settings.DraftsTaskListActivity;
import com.humming.asc.sales.datebase.DBManger;
import com.humming.asc.sales.model.DBTask;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.Taskservice;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Zhtq on 16/1/21.
 */
public class TaskAddActivity extends AbstractActivity {
    public static final String CUSTOMER_ADD_TASK_NAMEEN = "cnameen";
    public static final String CUSTOMER_ADD_TASK_ROWID = "crowid";
    public static final String CUSTOMER_ADD_TASK = "false";
    private View addTaskCustomer, addTaskEdit, addTaskDesc, addTaskType, addTaskStatus, addTaskStartDate, addTaskEndDate;
    private TextView customerValue, taskValue, note, typeValue, statusValue, startDateValue, endDateValue;
    private Context context = Application.getInstance().getBaseContext();
    private TextEditorData textEditorData;
    public static final String TYPE_OR_STATUS_NAME = "name";
    private int year, monthOfYear, dayOfMonth;
    private boolean startOrEndDate = true;
    private String CustomerRowId = "";
    private Application mAPP;
    private MainActivity.MyHandler mHandler;
    private DailyCallService dailyCallService;
    private CPSummaryVO summary;
    public static final String DRAFTS_TASK = "draft_task_add";
    private DBTask dbTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.add_task_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mAPP = (Application) getApplication();
        dailyCallService = Application.getDailyCallService();
        // 获得共享变量实例
        mHandler = mAPP.getHandler();
        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v.getId());
            }
        };
        addTaskCustomer = findViewById(R.id.content_add_task__select_customer);
        addTaskCustomer.setOnClickListener(itemClickListener);
        customerValue = (TextView) findViewById(R.id.content_add_task__select_customer_value);
        addTaskEdit = findViewById(R.id.content_add_task__edit_task);
        addTaskEdit.setOnClickListener(itemClickListener);
        taskValue = (TextView) findViewById(R.id.content_add_task__task_value);
        addTaskDesc = findViewById(R.id.content_add_task__description);
        addTaskDesc.setOnClickListener(itemClickListener);
        note = (TextView) findViewById(R.id.content_add_task__note);
        addTaskType = findViewById(R.id.content_add_task__type);
        addTaskType.setOnClickListener(itemClickListener);
        typeValue = (TextView) findViewById(R.id.content_add_task__type_value);
        addTaskStatus = findViewById(R.id.content_add_task__status);
        addTaskStatus.setOnClickListener(itemClickListener);
        statusValue = (TextView) findViewById(R.id.content_add_task__status_value);
        addTaskStartDate = findViewById(R.id.content_add_task__start_date);
        addTaskStartDate.setOnClickListener(itemClickListener);
        startDateValue = (TextView) findViewById(R.id.content_add_task__start_date_value);
        addTaskEndDate = findViewById(R.id.content_add_task__end_date);
        addTaskEndDate.setOnClickListener(itemClickListener);
        endDateValue = (TextView) findViewById(R.id.content_add_task__end_date_value);
        if ("true".equals(getIntent().getStringExtra(CUSTOMER_ADD_TASK))) {
            //在costomer中进入
            customerValue.setText(getIntent().getStringExtra(CUSTOMER_ADD_TASK_NAMEEN));
            customerValue.setTextColor(this.getResources().getColor(R.color.textGray));
            CustomerRowId = getIntent().getStringExtra(CUSTOMER_ADD_TASK_ROWID);
        } else if ("true".equals(getIntent().getStringExtra(DRAFTS_TASK))) {
            dbTask = Application.getInstance().getDbTask();
            CustomerRowId = dbTask.getCustomerRowId();
            setDraftsValue();
        }

    }

    private void setDraftsValue() {
        customerValue.setText(dbTask.getAccountName().toString());
        taskValue.setText(dbTask.getTaskName().toString());
        note.setText(dbTask.getDescription().toString());
        typeValue.setText(dbTask.getType().toString());
        statusValue.setText(dbTask.getStatus().toString());
        startDateValue.setText(dbTask.getStartDate().toString());
        endDateValue.setText(dbTask.getEndDate().toString());
    }

    private void onItemClick(int id) {
        switch (id) {
            case R.id.content_add_task__select_customer:
                if ("true".equals(getIntent().getStringExtra(CUSTOMER_ADD_TASK))) {

                } else {
                    Intent intent = new Intent(getBaseContext(), CustomerSelectorActivity.class);
                    intent.putExtra(CustomerSelectorActivity.TYPE, CustomerSelectorActivity.KEY_ACCOUNT);
                    startActivityForResult(intent, CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT);
                }
                break;
            case R.id.content_add_task__edit_task:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.title_activity_task_list));
                textEditorData.setHint(context.getString(R.string.text_edit_hint));
                Application.getInstance().setTextEditorData(textEditorData);
                Intent editeIntent1 = new Intent(getBaseContext(), TextEditorActivity.class);
                editeIntent1.putExtra(TextEditorActivity.CURRENT_CLASS, TextEditorActivity.CURRENT_CLASS_TASK_TASK);
                if (!"".equals(taskValue.getText())) {
                    editeIntent1.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, taskValue.getText());
                }
                startActivityForResult(editeIntent1, TextEditorActivity.ACTIVITY_TEXT_EDITOR_RESULT);

                break;
            case R.id.content_add_task__description:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.add_task_description));
                textEditorData.setHint(context.getString(R.string.text_edit_hint));

                Application.getInstance().setTextEditorData(textEditorData);
                Intent decIntent = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(note.getText())) {
                    decIntent.putExtra(TextEditorActivity.CURRENT_CLASS_NAME, note.getText());
                }
                decIntent.putExtra(TextEditorActivity.CURRENT_CLASS, TextEditorActivity.CURRENT_CLASS_TASK_DESC);
                startActivityForResult(decIntent, TextEditorActivity.ACTIVITY_DESCRIPTION_RESULT);
                break;
            case R.id.content_add_task__type:
                Intent typeIntent = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                typeIntent.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_TASK_TYPE);
                startActivityForResult(typeIntent, TypeAndStatusSelectActivity.ACTIVITY_ADD_TASK_SELECT);
                break;
            case R.id.content_add_task__status:
                Intent statusIntent = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                statusIntent.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_TASK_STATUS);
                startActivityForResult(statusIntent, TypeAndStatusSelectActivity.ACTIVITY_ADD_STATUS_SELECT);
                break;
            case R.id.content_add_task__start_date:
                startOrEndDate = true;
                showPopWindowDatePicker();
                break;
            case R.id.content_add_task__end_date:
                startOrEndDate = false;
                showPopWindowDatePicker();
                break;
        }
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
        window.showAtLocation(findViewById(R.id.content_add_task__select_customer),
                Gravity.BOTTOM, 0, 0);
        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        if (startOrEndDate) {
            /*if (!"".equals(endDateValue.getText().toString())) {
                String times = endDateValue.getText().toString();
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
                if (!"".equals(startDateValue.getText().toString())) {
                    String times = endDateValue.getText().toString();
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
         //   }
        } else {
            /*if (!"".equals(startDateValue.getText().toString())) {
                String times = startDateValue.getText().toString();
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
                if (!"".equals(endDateValue.getText().toString())) {
                    String times = startDateValue.getText().toString();
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
         //   }
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
                if (startOrEndDate) {
                    startDateValue.setText(date);
                    if (!"".equals(endDateValue.getText())) {
                        if (startDateValue.getText().toString().compareTo(endDateValue.getText().toString()) > 0) {
                            endDateValue.setText("");
                            Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.error_time), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    endDateValue.setText(date);
                    if (!"".equals(startDateValue)) {
                        if (startDateValue.getText().toString().compareTo(endDateValue.getText().toString()) > 0) {
                            endDateValue.setText("");
                            Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.error_time), Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                window.dismiss();
            }
        });
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
                if ("true".equals(getIntent().getStringExtra(DRAFTS_TASK))) {
                    judgeDraftsValue();
                } else {
                    judgeValue();
                }
                //finish();
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
                    //add task
                    AddTaskRO addTaskRo = new AddTaskRO();
                    addTaskRo.setCustomerRowId(CustomerRowId);
                    addTaskRo.setTaskStatus(statusValue.getText().toString());
                    addTaskRo.setTaskType(typeValue.getText().toString());
                    addTaskRo.setTaskName(taskValue.getText().toString());
                    addTaskRo.setTaskDes(note.getText().toString());
                    addTaskRo.setTaskStartTime(startDateValue.getText().toString());
                    addTaskRo.setTaskEndTime(endDateValue.getText().toString());
                    Taskservice taskservice = Application.getTaskservice();
                    taskservice.addTask(new ICallback<ResultVO>() {

                        @Override
                        public void onDataReady(ResultVO data) {
                            if ("true".equals(getIntent().getStringExtra(CUSTOMER_ADD_TASK))) {
                                if ("true".equalsIgnoreCase(getIntent().getStringExtra(CustomerTaskListActivity.CUSTOMER_DETAIL_ADD_TASK))) {
                                    CustomerTaskListActivity.TListActivity.finish();
                                    Intent intent = new Intent(Application.getInstance().getBaseContext(), CustomerTaskListActivity.class);
                                    intent.putExtra(CustomerTaskListActivity.ROW_ID, CustomerRowId);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "添加task成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            } else {
                                if ("true".equals(getIntent().getStringExtra(DRAFTS_TASK))) {
                                    Intent resultIntent = new Intent();
                                    resultIntent.putExtra(DraftsTaskListActivity.RESULT_TEXT_TASK, "");
                                    setResult(RESULT_OK, resultIntent);
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "添加task成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    TaskListActivity.TListActivity.finish();
                                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), TaskListActivity.class);
                                    startActivity(intent);
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "添加task成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }
                            initDailyCall();
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // 保存到本地数据库
                            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date = sDateFormat.format(new java.util.Date());
                            DBManger dbManger = new DBManger(TaskAddActivity.this);
                            DBTask dbTask = new DBTask("", CustomerRowId, taskValue.getText().toString(), customerValue.getText().toString(), "", endDateValue.getText().toString(), startDateValue.getText().toString(), typeValue.getText().toString(), statusValue.getText().toString(), "new", note.getText().toString(), "", date,"");
                            dbManger.InsertTaskListTable(dbTask);
                            Toast.makeText(TaskAddActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                        }
                    }, addTaskRo);
                }

        }
        return super.onOptionsItemSelected(item);
    }

    private void judgeDraftsValue() {
        boolean result = false;
        if (!customerValue.getText().toString().equals(dbTask.getAccountName().toString())) {
            result = true;
        } else if (!taskValue.getText().toString().equals(dbTask.getTaskName().toString())) {
            result = true;
        } else if (!startDateValue.getText().toString().equals(dbTask.getStartDate().toString())) {
            result = true;
        } else if (!endDateValue.getText().toString().equals(dbTask.getEndDate().toString())) {
            result = true;
        } else if (!typeValue.getText().toString().equals(dbTask.getType().toString())) {
            result = true;
        } else if (!note.getText().toString().equals(dbTask.getDescription().toString())) {
            result = true;
        }
        if (result) {
            showDraftsWindow();
        } else {
            finish();
        }
    }

    private void judgeValue() {
        boolean result = false;
        if (!"".equals(customerValue.getText().toString())) {
            result = true;
        } else if (!"".equals(taskValue.getText().toString())) {
            result = true;
        } else if (!"".equals(startDateValue.getText().toString())) {
            result = true;
        } else if (!"".equals(endDateValue.getText().toString())) {
            result = true;
        } else if (!"".equals(typeValue.getText().toString())) {
            result = true;
        } else if (!"".equals(note.getText().toString())) {
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
        window.showAtLocation(findViewById(R.id.content_add_task__select_customer),
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
                if ("true".equals(getIntent().getStringExtra(DRAFTS_TASK))) {
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    DBManger dbManger = new DBManger(TaskAddActivity.this);
                    DBTask dbTasks = new DBTask("", CustomerRowId, taskValue.getText().toString(), customerValue.getText().toString(), "", endDateValue.getText().toString(), startDateValue.getText().toString(), typeValue.getText().toString(), statusValue.getText().toString(), "new", note.getText().toString(), "", date,"");
                    dbManger.UpdateTaskById(dbTasks, dbTask.getId());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(DraftsTaskListActivity.RESULT_TEXT_TASK, "true");
                    setResult(RESULT_OK, resultIntent);
                    // Toast.makeText(TaskAddActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    DBManger dbManger = new DBManger(TaskAddActivity.this);
                    DBTask dbTask = new DBTask("", CustomerRowId, taskValue.getText().toString(), customerValue.getText().toString(), "", endDateValue.getText().toString(), startDateValue.getText().toString(), typeValue.getText().toString(), statusValue.getText().toString(), "new", note.getText().toString(), "", date,"");
                    dbManger.InsertTaskListTable(dbTask);
                    Toast.makeText(TaskAddActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
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

    //判断信息是否填写完整
    private boolean ValueNotNull() {
        boolean result = false;
        if ("".equals(customerValue.getText().toString())) {
            result = true;
        } else if ("".equals(taskValue.getText().toString())) {
            result = true;
        } else if ("".equals(startDateValue.getText().toString())) {
            result = true;
        } else if ("".equals(endDateValue.getText().toString())) {
            result = true;
        } else if ("".equals(typeValue.getText().toString())) {
            result = true;
        }
        return result;
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
                taskValue.setText(text);
                break;
            case TextEditorActivity.ACTIVITY_DESCRIPTION_RESULT:
                resultBundle = data.getExtras();
                String text2 = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                note.setText(text2);
                break;
            case TypeAndStatusSelectActivity.ACTIVITY_ADD_TASK_SELECT:
                resultBundle = data.getExtras();
                String text3 = resultBundle
                        .getString(TaskAddActivity.TYPE_OR_STATUS_NAME);
                typeValue.setText(text3);
                break;
            case TypeAndStatusSelectActivity.ACTIVITY_ADD_STATUS_SELECT:
                resultBundle = data.getExtras();
                String text4 = resultBundle
                        .getString(TaskAddActivity.TYPE_OR_STATUS_NAME);
                statusValue.setText(text4);
                break;
            case CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT:
                resultBundle = data.getExtras();
                String nameEn = resultBundle
                        .getString(CustomerSelectorActivity.KEY_NAME_EN);
                String id = resultBundle.getString(CustomerSelectorActivity.KEY_ID);
                CustomerRowId = id;
                customerValue.setText(nameEn);
                break;
        }
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
            if ("true".equals(getIntent().getStringExtra(DRAFTS_TASK))) {
                judgeDraftsValue();
            } else {
                judgeValue();
            }
        }
        return false;
    }
}
