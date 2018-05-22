package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.vo.ItemVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.ItemResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskProductResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskProductVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.Taskservice;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.List;

public class TaskProductListActivity extends AbstractActivity implements ICallback<TaskProductResultVO> {
    public static final String TASK_ID = "task_id";
    public static final String CUSTOMER_TASK_PRODUCT = "customer_task_product";
    public static final String PRODUCT_COUNT = "0";
    public static final int RESULT_CODE = 11111;
    public static String taskId;
    private ListView productListview;
    private List<TaskProductVO> lists;
    private Taskservice taskservice;
    private ItemVO itemVO;
    private String productName;
    private int position;
    private int[] itemPageResArray;
    private Toolbar toolbar;
    private String qtyProduct = "0";
    private TaskEditActivity.TaskHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_product_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        taskservice = Application.getTaskservice();
        mHandler = Application.getInstance().getTaskHandler();
        itemPageResArray = new int[]{R.layout.list_item_task_product, R.layout.list_item_view_page_right};
        taskId = getIntent().getStringExtra(TaskProductListActivity.TASK_ID);
        qtyProduct = getIntent().getStringExtra(TaskProductListActivity.PRODUCT_COUNT);
        productListview = (ListView) findViewById(R.id.content_task_product__listView);
        taskservice.queryProductByTaskId(this, taskId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_product_add:
                Intent intent = new Intent(this, TaskProductEditActivity.class);
                intent.putExtra(TaskProductEditActivity.EDIT_OR_ADD, TaskProductEditActivity.ADD);
                intent.putExtra(TaskProductEditActivity.TASK_ID, taskId);
                startActivity(intent);
                finish();
                break;
            case android.R.id.home:
                if ("true".equalsIgnoreCase(getIntent().getStringExtra(CUSTOMER_TASK_PRODUCT))) {
                    finish();
                } else {
                    mHandler.msgContent = lists.size() + "";
                    mHandler.sendEmptyMessage(0x123);
                    finish();
                    /*TaskEditActivity.taskEditActivity.finish();
                    Intent intents = new Intent(Application.getInstance().getBaseContext(), TaskEditActivity.class);
                    intents.putExtra(TaskEditActivity.TASK_ID, taskId);
                    startActivity(intents);
                    finish();*/
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onDataReady(TaskProductResultVO data) {
        lists = data.getData();
        toolbar.setTitle(Application.getInstance().getString(R.string.edit_task_product));
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                R.layout.list_item_view_pager_delete, lists, itemPageResArray, 0, "");
        productListview.setAdapter(arrayAdapter);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    class ViewHolder {
        TextView actualQuantity;
        TextView actualSales;
        TextView forecastQuantity;
        TextView saleDate;
        TextView forecastSales;
        TextView proId;
        Button btnDelete;
        View v;
    }

    private class MyArrayAdapter extends AbstractItemPagerArrayAdapter<TaskProductVO, ViewHolder> {
        private View.OnClickListener onbtnDeleteClickListener;
        private View.OnClickListener onbtnItemClickListener;

        public MyArrayAdapter(Context context, int resource, final List<TaskProductVO> items, int[] itemPageResArray, int defaultPageIndex, String types) {
            super(context, resource, items, itemPageResArray, defaultPageIndex, types);
            onbtnDeleteClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除product
                    final int position = Integer.parseInt(v.getTag().toString());
                    taskservice.deleteTaskProduct(new ICallback<ResultVO>() {
                        @Override
                        public void onDataReady(ResultVO data) {
                            Toast.makeText(Application.getInstance().getCurrentActivity(), Application.getInstance().getString(R.string.delete_product_success), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), TaskProductListActivity.class);
                            intent.putExtra(TaskProductListActivity.TASK_ID, taskId);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, items.get(position).getRowId());
                }
            };
            onbtnItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int positions = Integer.parseInt(v.getTag().toString());
                    position = positions;
                    taskservice.getItemCode(new ICallback<ItemResultVO>() {

                        @Override
                        public void onDataReady(ItemResultVO data) {
                            itemVO = data.getData();
                            mLoading.hide();
                            productName = itemVO.getNameEN() + "  " + itemVO.getNameCN();
                            Intent intent = new Intent(getBaseContext(), TaskProductEditActivity.class);
                            intent.putExtra(TaskProductEditActivity.EDIT_OR_ADD, TaskProductEditActivity.EDIT);
                            intent.putExtra(TaskProductEditActivity.EDIT_ITEM_CODE, lists.get(position).getProdId());
                            //   intent.putExtra(TaskProductEditActivity.EDIT_PRODUCT_NAME,lists.get(position).get());
                            intent.putExtra(TaskProductEditActivity.EDIT_FORECAST_SALES, lists.get(position).getForecastSales() + "");
                            intent.putExtra(TaskProductEditActivity.EDIT_FORECAST_DATE, lists.get(position).getSaleDate());
                            intent.putExtra(TaskProductEditActivity.EDIT_FORECAST_QTY, lists.get(position).getForecastQuantity() + "");
                            intent.putExtra(TaskProductEditActivity.EDIT_ACTUAL_QTY, lists.get(position).getActualQuantity() + "");
                            intent.putExtra(TaskProductEditActivity.EDIT_ACTUAL_SALES, lists.get(position).getActualSales() + "");
                            intent.putExtra(TaskProductEditActivity.EDIT_ACTUAL_DATE, lists.get(position).getActualDate() + "");
                            intent.putExtra(TaskProductEditActivity.EDIT_PRODUCT_NAME, productName);
                            intent.putExtra(TaskProductEditActivity.EDIT_PRO_ID, lists.get(position).getProdId());
                            intent.putExtra(TaskProductEditActivity.EDIT_ROW_ID, lists.get(position).getRowId());
                            startActivity(intent);
                            //  startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, lists.get(position).getRowId());
                }
            };
        }

        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    if (position == 1) {
                        return 0.24f;
                    } else {
                        return 1f;
                    }
                }
            };
            return adapter;
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(int position, ViewHolder viewHolder, List<View> itemPages) {
            View rightView = itemPages.get(1);
            View centerView = itemPages.get(0);
            viewHolder.v = centerView;
            viewHolder.proId = (TextView) centerView
                    .findViewById(R.id.list_item_task_product__proid);
            viewHolder.forecastSales = (TextView) centerView
                    .findViewById(R.id.list_item_task_product__forecastsale);
            viewHolder.forecastQuantity = (TextView) centerView
                    .findViewById(R.id.list_item_task_product__forecastqty);
            viewHolder.saleDate = (TextView) centerView
                    .findViewById(R.id.list_item_task_product__saledate);
            viewHolder.actualQuantity = (TextView) centerView
                    .findViewById(R.id.list_item_task_product__actualqty);
            viewHolder.actualSales = (TextView) centerView
                    .findViewById(R.id.list_item_task_product__actualsales);
            viewHolder.btnDelete = (Button) rightView
                    .findViewById(R.id.right_task);
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.btnDelete.setText(Application.getInstance().getString(R.string.delete));
            viewHolder.btnDelete.setOnClickListener(onbtnDeleteClickListener);
            viewHolder.v.setOnClickListener(onbtnItemClickListener);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, TaskProductVO itemData) {
            viewHolder.btnDelete.setTag(position);
            viewHolder.v.setTag(position);
            viewHolder.proId.setText(itemData.getProdId());
            viewHolder.saleDate.setText(itemData.getSaleDate());
            viewHolder.forecastSales.setText(itemData.getForecastSales() + "");
            viewHolder.forecastQuantity.setText(itemData.getForecastQuantity() + "");
            viewHolder.actualSales.setText(itemData.getActualSales() + "");
            viewHolder.actualQuantity.setText(itemData.getActualQuantity() + "");
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ("true".equalsIgnoreCase(getIntent().getStringExtra(CUSTOMER_TASK_PRODUCT))) {
                finish();
            } else {
                mHandler.msgContent = lists.size() + "";
                mHandler.sendEmptyMessage(0x123);
                finish();
            }

        }

        return false;

    }

}
