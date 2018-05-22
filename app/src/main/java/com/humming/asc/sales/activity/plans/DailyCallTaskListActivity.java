package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.cp.task.TaskListResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskListVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.AbstractArrayAdapter;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.Taskservice;

import java.util.List;

public class DailyCallTaskListActivity extends AbstractActivity {

    private ListView taskListView;
    public static final int ACTIVITY_TASK_RESULT = 10011 ;
    public static final String ACTIVITY_TASK_RESULT_NAME = "value" ;
    public static final String ACTIVITY_TASK_RESULT_CUSTOMER = "customer" ;
    public static final String ACTIVITY_TASK_RESULT_ID = "id" ;
    private Application app = Application.getInstance();
    private Taskservice taskservice;
    private Toolbar toolbar;
    private List<TaskListVO> lists;
    public static String CUSTOMERROWID ="customer_row_id";
    private String rowId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.content_task__listView).setVisibility(View.GONE);
        taskListView = (ListView) findViewById(R.id.content_task__listViews);
        taskListView.setVisibility(View.VISIBLE);
        mLoading.show();
        taskservice = Application.getTaskservice();
        if(!"".equals(getIntent().getStringExtra(DailyCallTaskListActivity.CUSTOMERROWID))){
            taskservice.queryByCustomerRowId(new ICallback<TaskListResultVO>() {

                @Override
                public void onDataReady(TaskListResultVO data) {
                    lists = data.getData();
                    toolbar.setTitle(app.getString(R.string.title_activity_task_list) + "(" + lists.size() + ")");
                    mLoading.hide();
                    TaskListArrayAdapter taskListArrayAdapter = new TaskListArrayAdapter(app.getCurrentActivity(),
                            R.layout.list_item_daily_call_task, lists);
                    taskListView.setAdapter(taskListArrayAdapter);

                }
                @Override
                public void onError(Throwable throwable) {

                }
            }, getIntent().getStringExtra(DailyCallTaskListActivity.CUSTOMERROWID),null);
        }else{
            taskservice.query(new ICallback<TaskListResultVO>() {

                @Override
                public void onDataReady(TaskListResultVO data) {
                    lists = data.getData();
                    toolbar.setTitle(app.getString(R.string.title_activity_task_list) + "(" + lists.size() + ")");
                    mLoading.hide();
                    TaskListArrayAdapter taskListArrayAdapter = new TaskListArrayAdapter(app.getCurrentActivity(),
                            R.layout.list_item_daily_call_task, lists);
                    taskListView.setAdapter(taskListArrayAdapter);

                }
                @Override
                public void onError(Throwable throwable) {

                }
            }, null);
        }
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle resultBundle = new Bundle();
                resultBundle.putString(
                        DailyCallTaskListActivity.ACTIVITY_TASK_RESULT_NAME,
                        lists.get(position).getTaskName());
                resultBundle.putString(
                        DailyCallTaskListActivity.ACTIVITY_TASK_RESULT_CUSTOMER,
                        lists.get(position).getAccountName());
                resultBundle.putString(
                        DailyCallTaskListActivity.ACTIVITY_TASK_RESULT_ID,
                        lists.get(position).getTaskId());
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        DailyCallTaskListActivity.ACTIVITY_TASK_RESULT,
                        resultIntent);
                finish();
            }
        });
    }

    private class  TaskListArrayAdapter extends AbstractArrayAdapter<ViewHolder, TaskListVO> {
        public TaskListArrayAdapter(Context context, int resource, List<TaskListVO> list) {
            super(context, resource, list);
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(int position, ViewHolder viewHolder, View rowView) {
            viewHolder.taskName = (TextView) rowView
                    .findViewById(R.id.list_item_task__name);
            viewHolder.taskAccountName = (TextView) rowView
                    .findViewById(R.id.list_item_task__account_name);
            viewHolder.taskState = (TextView) rowView
                    .findViewById(R.id.list_item_task__state);
            viewHolder.taskSubject = (TextView) rowView
                    .findViewById(R.id.list_item_task__subject);
            viewHolder.taskDate = (TextView) rowView
                    .findViewById(R.id.list_item_task__date);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, TaskListVO itemData) {
            viewHolder.taskDate.setText(itemData.getTaskRange().toString());
            viewHolder.taskSubject.setText(itemData.getTaskType().toString());
            viewHolder.taskState.setText(itemData.getTaskState().toString());
            viewHolder.taskAccountName.setText(itemData.getTaskName().toString());
            viewHolder.taskName.setText(itemData.getAccountName().toString());
        }
    }

    class ViewHolder {
        TextView taskAccountName;
        TextView taskName;
        TextView taskDate;
        TextView taskSubject;
        TextView taskState;
        View v;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
