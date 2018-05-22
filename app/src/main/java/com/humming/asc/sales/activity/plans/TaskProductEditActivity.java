package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.ro.cp.task.AddTaskProductRO;
import com.humming.asc.dp.presentation.ro.cp.task.UpdateTaskProductRO;
import com.humming.asc.dp.presentation.vo.cp.ItemInfoReusltVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.TextEditorActivity;
import com.humming.asc.sales.activity.TextEditorData;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.InventoryService;
import com.humming.asc.sales.service.Taskservice;

import java.util.ArrayList;
import java.util.Calendar;

public class TaskProductEditActivity extends AbstractActivity {
    public static final int RESULT_SALES = 10010;
    public static final int RESULT_QTY = 10011;

    public static final String EDIT_OR_ADD = "edit_or_add";
    public static final String PRODUCT_COME = "product_come";
    public static final String EDIT_ITEM_CODE = "edit_item_code";
    public static final String EDIT_PRODUCT_NAME = "edit_product_name";
    public static final String EDIT_FORECAST_SALES = "edit_forecast_sales";
    public static final String EDIT_FORECAST_QTY = "edit_forecast_qty";
    public static final String EDIT_FORECAST_DATE = "edit_forecast_date";
    public static final String EDIT_ACTUAL_QTY = "edit_actual_qty";
    public static final String EDIT_ACTUAL_SALES = "edit_actual_sales";
    public static final String EDIT_ACTUAL_DATE = "edit_actual_date";
    public static final String EDIT_PRO_ID = "edit_pro_id";
    public static final String EDIT_ROW_ID = "edit_row_id";

    public static final String EDIT = "edit";
    public static final String ADD = "add";
    public static final String TASK_ID = "task_id";

    private TextEditorData textEditorData;
    private Context context = Application.getInstance().getBaseContext();
    private String editOrAdd;
    private int year, monthOfYear, dayOfMonth;
    private View itemCode, ForecastSales, forecastQty, forecastDate;
    private TextView itemCodeValue, productName, forecastSalesValue, forecastQtyValue, forecastDateValue, actualSalesValue, actualQtyValue, actualDateValue;
    private Taskservice taskservice;
    private AddTaskProductRO addTaskProductRO;
    private UpdateTaskProductRO updateTaskProductRO;
    private ArrayList<String> codeList = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_taskproduct);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        taskservice = Application.getTaskservice();
        addTaskProductRO = new AddTaskProductRO();
        updateTaskProductRO = new UpdateTaskProductRO();
        editOrAdd = getIntent().getStringExtra(TaskProductEditActivity.EDIT_OR_ADD);
        View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(v.getId());
            }
        };
        itemCode = findViewById(R.id.content_edit_taskproduct__select_code);
        itemCode.setOnClickListener(itemClickListener);
        itemCodeValue = (TextView) findViewById(R.id.content_edit_taskproduct__select_code_value);
        productName = (TextView) findViewById(R.id.content_edit_taskproduct__product_name);
        ForecastSales = findViewById(R.id.content_edit_taskproduct__forecast_sales);
        ForecastSales.setOnClickListener(itemClickListener);
        forecastSalesValue = (TextView) findViewById(R.id.content_edit_taskproduct__forecast_sales_value);
        forecastQty = findViewById(R.id.content_edit_taskproduct__forecast_qty);
        forecastQty.setOnClickListener(itemClickListener);
        forecastQtyValue = (TextView) findViewById(R.id.content_edit_taskproduct__forecast_qty_value);
        forecastDate = findViewById(R.id.content_edit_taskproduct__forecast_date);
        forecastDate.setOnClickListener(itemClickListener);
        forecastDateValue = (TextView) findViewById(R.id.content_edit_taskproduct__forecast_date_value);
        actualSalesValue = (TextView) findViewById(R.id.content_edit_taskproduct__actual_sales_value);
        actualQtyValue = (TextView) findViewById(R.id.content_edit_taskproduct__actual_qty_value);
        actualDateValue = (TextView) findViewById(R.id.content_edit_taskproduct__actual_date_value);
        //编辑
        if (EDIT.equals(editOrAdd)) {
            itemCode.setEnabled(false);
            itemCodeValue.setText(getIntent().getStringExtra(TaskProductEditActivity.EDIT_ITEM_CODE));
            productName.setText(getIntent().getStringExtra(TaskProductEditActivity.EDIT_PRODUCT_NAME));
            forecastSalesValue.setText(getIntent().getStringExtra(TaskProductEditActivity.EDIT_FORECAST_SALES));
            forecastQtyValue.setText(getIntent().getStringExtra(TaskProductEditActivity.EDIT_FORECAST_QTY));
            forecastDateValue.setText(getIntent().getStringExtra(TaskProductEditActivity.EDIT_FORECAST_DATE));
            String sales = getIntent().getStringExtra(TaskProductEditActivity.EDIT_ACTUAL_SALES);
            actualSalesValue.setText(sales);
            String qty = getIntent().getStringExtra(TaskProductEditActivity.EDIT_ACTUAL_QTY);
            actualQtyValue.setText(qty);
            actualDateValue.setText(getIntent().getStringExtra(TaskProductEditActivity.EDIT_ACTUAL_DATE));
        } else {
            itemCode.setEnabled(true);

        }
        if (Application.getInstance().getItemCodeLists() != null) {
            codeList = Application.getInstance().getItemCodeLists();
        } else {
            InventoryService service = Application.getInventoryService();
            service.queryItemCodes(new ICallback<ItemInfoReusltVO>() {
                @Override
                public void onDataReady(ItemInfoReusltVO data) {
                    codeList = (ArrayList<String>) data.getData();
                    Application.getInstance().setItemCodeLists(codeList);
                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
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
        window.showAtLocation(findViewById(R.id.content_edit_taskproduct__select_code_value),
                Gravity.BOTTOM, 0, 0);

        DatePicker datePicker = (DatePicker) view.findViewById(R.id.content_popup_datepicker);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        monthOfYear = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
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
                forecastDateValue.setText(date);
                window.dismiss();
            }
        });
    }

    private void onItemClick(int id) {
        switch (id) {
            case R.id.content_edit_taskproduct__forecast_sales:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.forecast_sales));
                textEditorData.setHint(context.getString(R.string.text_edit_number_hint));
                textEditorData.setSingleLine(true);
                textEditorData.setMaxLines(3);
                textEditorData.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                Application.getInstance().setTextEditorData(textEditorData);
                Intent editeIntent1 = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(forecastSalesValue.getText().toString())) {
                    editeIntent1.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, forecastSalesValue.getText().toString());
                }
                startActivityForResult(editeIntent1, TaskProductEditActivity.RESULT_SALES);
                break;
            case R.id.content_edit_taskproduct__forecast_qty:
                textEditorData = new TextEditorData();
                textEditorData.setTitle(context.getString(R.string.forecast_qty));
                textEditorData.setHint(context.getString(R.string.text_edit_number_hint));
                textEditorData.setSingleLine(true);
                textEditorData.setMaxLines(3);
                textEditorData.setInputType(EditorInfo.TYPE_CLASS_NUMBER);
                Application.getInstance().setTextEditorData(textEditorData);
                Intent decIntent = new Intent(getBaseContext(), TextEditorActivity.class);
                if (!"".equals(forecastQtyValue.getText().toString())) {
                    decIntent.putExtra(TextEditorActivity.CURRENT_TEXT_VALUE, forecastQtyValue.getText().toString());
                }
                startActivityForResult(decIntent, TaskProductEditActivity.RESULT_QTY);
                break;
            case R.id.content_edit_taskproduct__forecast_date:
                showPopWindowDatePicker();
                break;
            case R.id.content_edit_taskproduct__select_code:

                Intent intent = new Intent(getBaseContext(), TaskProductItemCodeListActivity.class);
                Bundle b = new Bundle();
                b.putStringArrayList(TaskProductItemCodeListActivity.STRING_LIST, codeList);
                //   b.putString(TaskProductItemCodeListActivity.ITEM_CODE,getIntent().getStringExtra(TaskProductEditActivity.EDIT_ITEM_CODE));
                intent.putExtras(b);
                startActivityForResult(intent, TaskProductItemCodeListActivity.ITEM_CODE_REQUEST_CODE);
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
                finish();
                break;
            case R.id.action_task_add_confirm:
                //编辑(update)
                if (EDIT.equals(editOrAdd)) {
                    updateTaskProductRO.setForecastQuantity(forecastQtyValue.getText().toString());
                    updateTaskProductRO.setForecastSales(forecastSalesValue.getText().toString());
                    updateTaskProductRO.setProdId(getIntent().getStringExtra(TaskProductEditActivity.EDIT_PRO_ID));
                    updateTaskProductRO.setRowId(getIntent().getStringExtra(TaskProductEditActivity.EDIT_ROW_ID));
                    updateTaskProductRO.setSaleDate(forecastDateValue.getText().toString());
                    taskservice.updateTaskProduct(new ICallback<ResultVO>() {

                        @Override
                        public void onDataReady(ResultVO data) {
                            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), TaskProductListActivity.class);
                            intent.putExtra(TaskProductListActivity.TASK_ID, TaskProductListActivity.taskId);
                            startActivity(intent);
                            Toast.makeText(Application.getInstance().getCurrentActivity(), "修改product成功", Toast.LENGTH_SHORT).show();
                            finish();
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, updateTaskProductRO);

                } else {//新增(add)
                    if (ValueNotNull()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setMessage  (Application.getInstance().getString(R.string.Incomplete_information));
                        builder.setTitle("提示");
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        builder.create().show();
                    } else {
                        addTaskProductRO.setForecastQuantity(forecastQtyValue.getText().toString());
                        addTaskProductRO.setForecastSales(forecastSalesValue.getText().toString());
                        addTaskProductRO.setProdId(itemCodeValue.getText().toString());
                        addTaskProductRO.setSaleDate(forecastDateValue.getText().toString());
                        addTaskProductRO.setTaskRowId(getIntent().getStringExtra(TASK_ID));
                        taskservice.addTaskProduct(new ICallback<ResultVO>() {

                            @Override
                            public void onDataReady(ResultVO data) {
                                if ("true".equalsIgnoreCase(getIntent().getStringExtra(TaskProductEditActivity.PRODUCT_COME))) {
                                    finish();
                                } else {
                                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), TaskProductListActivity.class);
                                    intent.putExtra(TaskProductListActivity.TASK_ID, TaskProductListActivity.taskId);
                                    intent.putExtra(TaskProductListActivity.PRODUCT_COUNT,
                                            "1");
                                    startActivity(intent);
                                    Toast.makeText(Application.getInstance().getCurrentActivity(), "添加product成功", Toast.LENGTH_SHORT).show();
                                    finish();
                                }

                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }
                        }, addTaskProductRO);
                    }
                }

        }
        return super.onOptionsItemSelected(item);
    }

    //判断信息是否填写完整
    private boolean ValueNotNull() {
        boolean result = false;
        if ("".equals(forecastQtyValue.getText().toString())) {
            result = true;
        } else if ("".equals(forecastSalesValue.getText().toString())) {
            result = true;
        } else if ("".equals(itemCodeValue.getText().toString())) {
            result = true;
        } else if ("".equals(forecastDateValue.getText().toString())) {
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
            case TaskProductEditActivity.RESULT_SALES:
                resultBundle = data.getExtras();
                String text = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                forecastSalesValue.setText(text);
                break;
            case TaskProductEditActivity.RESULT_QTY:
                resultBundle = data.getExtras();
                String text2 = resultBundle
                        .getString(TextEditorActivity.KEY_TEXT);
                forecastQtyValue.setText(text2);
                break;
            case TaskProductItemCodeListActivity.ITEM_CODE_REQUEST_CODE:
                resultBundle = data.getExtras();
                String text3 = resultBundle
                        .getString(TaskProductItemCodeListActivity.ITEM_CODE);
                String codeName = resultBundle.getString(TaskProductItemCodeListActivity.ITEM_CODE_NAME);
                itemCodeValue.setText(text3);
                productName.setText(codeName);
                break;
        }
    }
}
