package com.humming.asc.sales.activity.plans;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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

import com.humming.asc.dp.presentation.ro.cp.task.UpdateTaskRO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskDetailResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskDetailVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.TextEditorActivity;
import com.humming.asc.sales.activity.TextEditorData;
import com.humming.asc.sales.activity.customer.CustomerTaskListActivity;
import com.humming.asc.sales.activity.settings.DraftsTaskListActivity;
import com.humming.asc.sales.datebase.DBManger;
import com.humming.asc.sales.model.DBTask;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.Taskservice;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Zhtq on 16/1/21.
 */
public class TaskEditActivity extends AbstractActivity implements ICallback<TaskDetailResultVO> {
    private View editTaskCustomer, editTaskEdit, editTaskDesc, editTaskType, editTaskStatus, editTaskStartDate, editTaskEndDate, editTaskProduct, editTaskDailyCall;
    private TextView customerLabel, customerValue, taskValue, note, typeValue, statusValue, startDateValue, endDateValue, productCount, DailyCallCount, TaskUserTitle;
    private Context context = Application.getInstance().getBaseContext();
    private TextEditorData textEditorData;
    public static final String TYPE_OR_STATUS_NAME = "name";
    private int year, monthOfYear, dayOfMonth;
    private boolean startOrEndDate = true;
    private String taskId = "";
    public static final String TASK_ID = "task_id";
    public static Activity taskEditActivity;
    private TaskHandler mHandler;
    private Application mAPP;
    public static final String DRAFTS_TASK = "draft_task_edit";
    private DBTask dbTask;
    private DBManger dbManger;
    private TaskDetailVO taskDetailVO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_task);
        Toolbar toolbar = (Toolbar) findViewById(R.id.edit_task_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        taskId = getIntent().getStringExtra(TaskEditActivity.TASK_ID);
        taskEditActivity = this;
        mAPP = (Application) getApplication();
        mHandler = new TaskHandler();
        mAPP.setTaskHandler(mHandler);
        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v.getId());
            }
        };
        editTaskCustomer = findViewById(R.id.content_edit_task__select_customer);
        TaskUserTitle = (TextView) findViewById(R.id.content_edit_task__customer_title);
        customerValue = (TextView) findViewById(R.id.content_edit_task__select_customer_value);
        customerLabel = (TextView) findViewById(R.id.content_edit_task__select_customer_label);
        customerLabel.setTextColor(this.getResources().getColor(R.color.textGray));
        customerValue.setTextColor(this.getResources().getColor(R.color.textGray));
        editTaskEdit = findViewById(R.id.content_edit_task__edit_task);
        editTaskEdit.setOnClickListener(itemClickListener);
        taskValue = (TextView) findViewById(R.id.content_edit_task__task_value);
        editTaskDesc = findViewById(R.id.content_edit_task__description);
        editTaskDesc.setOnClickListener(itemClickListener);
        note = (TextView) findViewById(R.id.content_edit_task__note);
        editTaskType = findViewById(R.id.content_edit_task__type);
        editTaskType.setOnClickListener(itemClickListener);
        typeValue = (TextView) findViewById(R.id.content_edit_task__type_value);
        editTaskStatus = findViewById(R.id.content_edit_task__status);
        editTaskStatus.setOnClickListener(itemClickListener);
        statusValue = (TextView) findViewById(R.id.content_edit_task__status_value);
        editTaskStartDate = findViewById(R.id.content_edit_task__start_date);
        editTaskStartDate.setOnClickListener(itemClickListener);
        startDateValue = (TextView) findViewById(R.id.content_edit_task__start_date_value);
        editTaskEndDate = findViewById(R.id.content_edit_task__end_date);
        editTaskEndDate.setOnClickListener(itemClickListener);
        endDateValue = (TextView) findViewById(R.id.content_edit_task__end_date_value);
        productCount = (TextView) findViewById(R.id.content_edit_task__product_count);
        DailyCallCount = (TextView) findViewById(R.id.content_edit_task__daily_call_count);
        editTaskDailyCall = findViewById(R.id.content_edit_task__daily_call);
        editTaskProduct = findViewById(R.id.content_edit_task__product);
        editTaskProduct.setOnClickListener(itemClickListener);
        editTaskDailyCall.setOnClickListener(itemClickListener);
        if ("true".equals(getIntent().getStringExtra(DRAFTS_TASK))) {
            dbTask = Application.getInstance().getDbTask();
            dbManger = new DBManger(TaskEditActivity.this);
            setDraftsTaskValue();
            taskId = dbTask.getTaskId();
        } else {
            Taskservice taskservice = Application.getTaskservice();
            taskservice.queryTaskById(this, taskId);
        }
    }

    private void setDraftsTaskValue() {
        TaskUserTitle.setText(dbTask.getSaleName());
       // TaskUserTitle.setVisibility(View.GONE);
        taskValue.setText(dbTask.getTaskName());
        customerValue.setText(dbTask.getAccountName());
        typeValue.setText(dbTask.getType().toString());
        startDateValue.setText(dbTask.getStartDate());
        endDateValue.setText(dbTask.getEndDate() + "");
        if (dbTask.getStatus() != null) {
            statusValue.setText(dbTask.getStatus());
        }
        if (dbTask.getDescription() != null) {
            note.setText(dbTask.getDescription());
        }
        //productCount.setText(taskDetailVO.getProductNum() + "");
        //  DailyCallCount.setText(taskDetailVO.getDcNum() + "");
    }

    @Override
    public void onDataReady(TaskDetailResultVO data) {
        taskDetailVO = data.getData();
        TaskUserTitle.setText(taskDetailVO.getSaleName());
        taskValue.setText(taskDetailVO.getTaskName());
        customerValue.setText(taskDetailVO.getAccountName());
        typeValue.setText(taskDetailVO.getType().toString());
        startDateValue.setText(taskDetailVO.getStartDate());
        endDateValue.setText(taskDetailVO.getEndDate() + "");
        if (taskDetailVO.getStatus() != null) {
            statusValue.setText(taskDetailVO.getStatus());
        }
        if (taskDetailVO.getDescription() != null) {
            note.setText(taskDetailVO.getDescription());
        }
        productCount.setText(taskDetailVO.getProductNum() + "");
        DailyCallCount.setText(taskDetailVO.getDcNum() + "");
    }

    @Override
    public void onError(Throwable throwable) {

    }

    private void onItemClick(int id) {
        switch (id) {
            case R.id.content_edit_task__select_customer:
                //   Intent intent = new Intent(getBaseContext(), CustomerSelectorActivity.class);
                //  startActivityForResult(intent, CustomerSelectorActivity.ACTIVITY_TASK_EDIT_RESULT);
                break;
            case R.id.content_edit_task__edit_task:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.title_activity_task_list));
                textEditorData.setHint(context.getString(R.string.text_edit_hint));
                Application.getInstance().setTextEditorData(textEditorData);
                Intent editeIntent1 = new Intent(getBaseContext(), TextEditorActivity.class);
                editeIntent1.putExtra(TextEditorActivity.CURRENT_CLASS, TextEditorActivity.CURRENT_CLASS_TASK_TASK_EDIT);
                if (!"".equals(taskValue.getText())) {
                    editeIntent1.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, taskValue.getText());
                }
                startActivityForResult(editeIntent1, TextEditorActivity.ACTIVITY_EDIT_TASK_RESULT);

                break;
            case R.id.content_edit_task__description:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.add_task_description));
                textEditorData.setHint(context.getString(R.string.text_edit_hint));
                Application.getInstance().setTextEditorData(textEditorData);
                Intent decIntent = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(note.getText())) {
                    decIntent.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, note.getText());
                }
                decIntent.putExtra(TextEditorActivity.CURRENT_CLASS, TextEditorActivity.CURRENT_CLASS_TASK_DESC_EDIT);
                startActivityForResult(decIntent, TextEditorActivity.ACTIVITY_EDIT_DESC_RESULT);
                break;
            case R.id.content_edit_task__type:
                Intent typeIntent = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                typeIntent.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_TASK_TYPE);
                startActivityForResult(typeIntent, TypeAndStatusSelectActivity.ACTIVITY_ADD_TASK_SELECT);
                break;
            case R.id.content_edit_task__status:
                Intent statusIntent = new Intent(getBaseContext(), TypeAndStatusSelectActivity.class);
                statusIntent.putExtra(TypeAndStatusSelectActivity.ACTIVITY_TASK, TypeAndStatusSelectActivity.ACTIVITY_TASK_STATUS);
                startActivityForResult(statusIntent, TypeAndStatusSelectActivity.ACTIVITY_ADD_STATUS_SELECT);
                break;
            case R.id.content_edit_task__start_date:
                startOrEndDate = true;
                showPopWindowDatePicker();
                break;
            case R.id.content_edit_task__end_date:
                startOrEndDate = false;
                showPopWindowDatePicker();
                break;
            case R.id.content_edit_task__product:
                Intent intent = new Intent(getBaseContext(), TaskProductListActivity.class);
                intent.putExtra(TaskProductListActivity.TASK_ID, taskId);
                startActivityForResult(intent, TaskProductListActivity.RESULT_CODE);
                // startActivity(intent);
                break;
            case R.id.content_edit_task__daily_call:
                Intent intent1 = new Intent(getBaseContext(), TaskDailyCallListActivity.class);
                intent1.putExtra(TaskDailyCallListActivity.TASK_NAME, taskValue.getText().toString());
                intent1.putExtra(TaskDailyCallListActivity.TASK_CUSTOMER, customerValue.getText().toString());
                intent1.putExtra(TaskDailyCallListActivity.TASK_ID, taskId);
                startActivity(intent1);
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
        window.showAtLocation(findViewById(R.id.content_edit_task__select_customer),
                Gravity.BOTTOM, 0, 0);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        datePicker.setCalendarViewShown(false);
        Calendar calendar = Calendar.getInstance();
        if (startOrEndDate) {
            if (!"".equals(startDateValue.getText().toString())) {
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
                monthOfYear = Integer.parseInt(month) - 1;
                dayOfMonth = Integer.parseInt(day);
            } else {
                year = calendar.get(Calendar.YEAR);
                monthOfYear = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }
        } else {
            if (!"".equals(endDateValue.getText().toString())) {
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
                monthOfYear = (Integer.parseInt(month) - 1);
                dayOfMonth = Integer.parseInt(day);
            } else {
                year = calendar.get(Calendar.YEAR);
                monthOfYear = calendar.get(Calendar.MONTH);
                dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            }
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
                    if ("Generate sales".equalsIgnoreCase(typeValue.getText().toString())) {
                        if ("0".equalsIgnoreCase(productCount.getText().toString()) || "".equalsIgnoreCase(productCount.getText().toString())) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(this);
                            builder.setMessage(Application.getInstance().getString(R.string.select_product_change_type));
                            builder.setTitle("提示");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                            builder.create().show();
                        }
                    } else {
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
                    }
                } else {
                    //update task
                    UpdateTaskRO updateTaskRO = new UpdateTaskRO();
                    updateTaskRO.setTaskRowId(taskId);
                    updateTaskRO.setTaskStatus(statusValue.getText().toString());
                    updateTaskRO.setTaskType(typeValue.getText().toString());
                    updateTaskRO.setTaskName(taskValue.getText().toString());
                    updateTaskRO.setTaskDes(note.getText().toString());
                    updateTaskRO.setTaskStartTime(startDateValue.getText().toString());
                    updateTaskRO.setTaskEndTime(endDateValue.getText().toString());
                    Taskservice taskservice = Application.getTaskservice();
                    taskservice.updateTask(new ICallback<ResultVO>() {

                        @Override
                        public void onDataReady(ResultVO data) {
                            //customer 进入
                            if ("true".equalsIgnoreCase(getIntent().getStringExtra(CustomerTaskListActivity.CUSTOMER_DETAIL_ADD_TASK))) {
                                CustomerTaskListActivity.TListActivity.finish();
                                Intent intent = new Intent(Application.getInstance().getBaseContext(), CustomerTaskListActivity.class);
                                intent.putExtra(CustomerTaskListActivity.ROW_ID, getIntent().getStringExtra(CustomerTaskListActivity.CUSTOMER_ADD_TASK_ROWID));
                                startActivity(intent);
                                finish();
                                //草稿箱进入
                            } else if ("true".equals(getIntent().getStringExtra(DRAFTS_TASK))) {
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra(DraftsTaskListActivity.RESULT_TEXT_TASK, "");
                                setResult(RESULT_OK, resultIntent);
                                Toast.makeText(Application.getInstance().getCurrentActivity(), "修改task成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                //taskList进入
                                TaskListActivity.TListActivity.finish();
                                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), TaskListActivity.class);
                                startActivity(intent);
                                Toast.makeText(Application.getInstance().getCurrentActivity(), "修改task成功", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            String date = sDateFormat.format(new java.util.Date());
                            DBManger dbManger = new DBManger(TaskEditActivity.this);
                            DBTask dbTask = new DBTask(taskId, "", taskValue.getText().toString(), customerValue.getText().toString(), "", endDateValue.getText().toString(), startDateValue.getText().toString(),
                                    typeValue.getText().toString(), statusValue.getText().toString(), "update", note.getText().toString(), TaskUserTitle.getText().toString(), date,"");
                            dbManger.InsertTaskListTable(dbTask);
                            Toast.makeText(TaskEditActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();

                        }
                    }, updateTaskRO);
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
        } else if (!statusValue.getText().toString().equals(dbTask.getStatus().toString())) {
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
        if (!customerValue.getText().toString().equals(taskDetailVO.getAccountName().toString())) {
            result = true;
        } else if (!taskValue.getText().toString().equals(taskDetailVO.getTaskName().toString())) {
            result = true;
        } else if (!startDateValue.getText().toString().equals(taskDetailVO.getStartDate().toString())) {
            result = true;
        } else if (!endDateValue.getText().toString().equals(taskDetailVO.getEndDate().toString())) {
            result = true;
        } else if (!typeValue.getText().toString().equals(taskDetailVO.getType().toString())) {
            result = true;
        } else if (!note.getText().toString().equals(taskDetailVO.getDescription().toString())) {
            result = true;
        }else if (!statusValue.getText().toString().equals(taskDetailVO.getStatus().toString())) {
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
        window.showAtLocation(findViewById(R.id.content_edit_task__select_customer),
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
                if("true".equals(getIntent().getStringExtra(DRAFTS_TASK))){
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    DBManger dbManger = new DBManger(TaskEditActivity.this);
                    DBTask dbTasks = new DBTask(taskId, "", taskValue.getText().toString(), customerValue.getText().toString(), "", endDateValue.getText().toString(), startDateValue.getText().toString(),
                            typeValue.getText().toString(), statusValue.getText().toString(), "update", note.getText().toString(), TaskUserTitle.getText().toString(), date,"");
                    dbManger.UpdateTaskById(dbTasks,dbTask.getId());
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra(DraftsTaskListActivity.RESULT_TEXT_TASK, "true");
                    setResult(RESULT_OK, resultIntent);
                  //  Toast.makeText(TaskEditActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
                }else{
                    SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String date = sDateFormat.format(new java.util.Date());
                    DBManger dbManger = new DBManger(TaskEditActivity.this);
                    DBTask dbTask = new DBTask(taskId, "", taskValue.getText().toString(), customerValue.getText().toString(), "", endDateValue.getText().toString(), startDateValue.getText().toString(),
                            typeValue.getText().toString(), statusValue.getText().toString(), "update", note.getText().toString(), TaskUserTitle.getText().toString(), date,"");
                    dbManger.InsertTaskListTable(dbTask);
                    Toast.makeText(TaskEditActivity.this, "已添加到草稿箱", Toast.LENGTH_SHORT).show();
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
        }
        if ("".equals(typeValue.getText().toString())) {
            result = true;
        } else {
            if ("Generate sales".equalsIgnoreCase(typeValue.getText().toString())) {
                if ("0".equalsIgnoreCase(productCount.getText().toString()) || "".equalsIgnoreCase(productCount.getText().toString())) {
                    result = true;
                }
            }
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
            case TextEditorActivity.ACTIVITY_EDIT_TASK_RESULT:
                resultBundle = data.getExtras();
                String text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                taskValue.setText(text);
                break;
            case TextEditorActivity.ACTIVITY_EDIT_DESC_RESULT:
                resultBundle = data.getExtras();
                String text2 = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                note.setText(text2);
                break;
            case TypeAndStatusSelectActivity.ACTIVITY_ADD_TASK_SELECT:
                resultBundle = data.getExtras();
                String text3 = resultBundle
                        .getString(TaskEditActivity.TYPE_OR_STATUS_NAME);
                typeValue.setText(text3);
                if ("Generate sales".equalsIgnoreCase(text3)) {
                    if ("0".equalsIgnoreCase(productCount.getText().toString()) || "".equalsIgnoreCase(productCount.getText().toString())) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage(Application.getInstance().getString(R.string.select_product_change_type));
                        builder.setTitle("提示");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    }
                }
                break;
            case TypeAndStatusSelectActivity.ACTIVITY_ADD_STATUS_SELECT:
                resultBundle = data.getExtras();
                String text4 = resultBundle
                        .getString(TaskEditActivity.TYPE_OR_STATUS_NAME);
                statusValue.setText(text4);
                break;
            case TaskProductListActivity.RESULT_CODE:
                resultBundle = data.getExtras();
                String nameEn = resultBundle
                        .getString(TaskProductListActivity.PRODUCT_COUNT);

                productCount.setText(nameEn);
                break;
        }
    }

    public class TaskHandler extends Handler {

        public String msgContent;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            productCount.setText(msgContent);
        }

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
