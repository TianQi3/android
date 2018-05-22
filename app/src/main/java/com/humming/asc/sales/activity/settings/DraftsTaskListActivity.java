package com.humming.asc.sales.activity.settings;

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

import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.plans.TaskAddActivity;
import com.humming.asc.sales.activity.plans.TaskEditActivity;
import com.humming.asc.sales.datebase.DBManger;
import com.humming.asc.sales.model.DBTask;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DraftsTaskListActivity extends AbstractActivity {

    private ListView taskListView;
    private int[] itemPageResArray;
    private Application app = Application.getInstance();
    private Toolbar toolbar;
    private ArrayList<DBTask> tlists;
    private DBManger dbm;
    public static final int DRAFTS_TASK_RESULT_CODE = 12132;
    public static final String RESULT_TEXT_TASK = "result_text_task";
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemPageResArray = new int[]{R.layout.list_item_task, R.layout.list_item_view_page_right};
        findViewById(R.id.content_task__listView).setVisibility(View.GONE);
        taskListView = (ListView) findViewById(R.id.content_task__listViews);
        taskListView.setVisibility(View.VISIBLE);
        dbm = new DBManger(this);
        tlists = (ArrayList) dbm.SelectTaskList();
        int sum = tlists.size();
        toolbar.setTitle(app.getString(R.string.title_activity_task_list) + "(" + sum + ")");
        TaskListArrayAdapter arrayAdapter = new TaskListArrayAdapter(Application.getInstance().getBaseContext(),
                R.layout.list_item_view_pager_delete, tlists, itemPageResArray, 0, "");
        taskListView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      //  getMenuInflater().inflate(R.menu.menu_task_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Bundle resultBundles = new Bundle();
                resultBundles.putString(MyDraftsActivity.RESULT_TEXT, "");
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(
                        RESULT_OK,
                        resultIntent);
                finish();
                break;
            case R.id.action_product_add:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TaskListArrayAdapter extends AbstractItemPagerArrayAdapter<DBTask, ViewHolder> {
        private View.OnClickListener onbtnDeleteClickListener;
        private View.OnClickListener onbtnItemClickListener;

        public TaskListArrayAdapter(Context context, int list_item_customer1, final List<DBTask> list, int[] itemPageResArray, int i, String types) {
            super(context, list_item_customer1, list, itemPageResArray, i, types);
            onbtnDeleteClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //删除数据库:
                    final int position = Integer.parseInt(v.getTag().toString());
                    dbm.DeleteTaskById(tlists.get(position).getId());
                    list.remove(position);
                    updateViewPagerState(position, 0, true);
                    int sum = list.size();
                    toolbar.setTitle(app.getString(R.string.title_activity_task_list) + "(" + sum + ")");
                    notifyDataSetChanged();

                }
            };
            onbtnItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    index = position;
                    Application.getInstance().setDbTask(list.get(position));
                    if ("new".equalsIgnoreCase(list.get(position).getRemark())) {
                        Intent intent = new Intent(DraftsTaskListActivity.this, TaskAddActivity.class);
                        intent.putExtra(TaskAddActivity.DRAFTS_TASK, "true");
                        startActivityForResult(intent, DRAFTS_TASK_RESULT_CODE);
                    } else {
                        Intent intent = new Intent(DraftsTaskListActivity.this, TaskEditActivity.class);
                        intent.putExtra(TaskEditActivity.DRAFTS_TASK, "true");
                        startActivityForResult(intent, DRAFTS_TASK_RESULT_CODE);
                    }
                }
            };
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(final int position, ViewHolder viewHolder, List<View> itemPages) {
            View rightView = itemPages.get(1);
            View centerView = itemPages.get(0);
            viewHolder.v = centerView;
            viewHolder.taskName = (TextView) centerView
                    .findViewById(R.id.list_item_task__name);
            viewHolder.taskAccountName = (TextView) centerView
                    .findViewById(R.id.list_item_task__account_name);
            viewHolder.taskState = (TextView) centerView
                    .findViewById(R.id.list_item_task__state);
            viewHolder.taskSubject = (TextView) centerView
                    .findViewById(R.id.list_item_task__subject);
            viewHolder.taskDate = (TextView) centerView
                    .findViewById(R.id.list_item_task__date);
            viewHolder.saleName = (TextView) centerView.findViewById(R.id.list_item_task__sale_name);
            viewHolder.btnDelete = (Button) rightView
                    .findViewById(R.id.right_task);
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.btnDelete.setText(Application.getInstance().getString(R.string.delete));
            viewHolder.btnDelete.setOnClickListener(onbtnDeleteClickListener);
            viewHolder.v.setOnClickListener(onbtnItemClickListener);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, DBTask itemData) {
            viewHolder.btnDelete.setTag(position);
            viewHolder.v.setTag(position);
            viewHolder.taskDate.setText(itemData.getStartDate() + "-" + itemData.getEndDate());
            viewHolder.taskSubject.setText(itemData.getType());
            viewHolder.taskState.setText(itemData.getStatus());
            viewHolder.taskAccountName.setText(itemData.getTaskName());
            viewHolder.taskName.setText(itemData.getAccountName());
            viewHolder.saleName.setText(itemData.getSaleName());
            viewHolder.taskDate.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.taskSubject.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.taskState.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.taskAccountName.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.taskName.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.saleName.setTextColor(getResources().getColor(R.color.colorAccent));
        }

        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    if (position == 1) {
                        return 0.18f;
                    } else {
                        return 1f;
                    }
                }
            };
            return adapter;
        }

    }

    class ViewHolder {
        TextView taskAccountName;
        TextView taskName;
        TextView taskDate;
        TextView taskSubject;
        TextView taskState;
        TextView saleName;
        Button btnDelete;
        View v;
    }

    //跳转返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case DraftsTaskListActivity.DRAFTS_TASK_RESULT_CODE:
                String text =data.getStringExtra(DraftsTaskListActivity.RESULT_TEXT_TASK);
                if("true".equals(text)){

                }else{
                    dbm.DeleteTaskById(tlists.get(index).getId());
                }
                tlists = (ArrayList) dbm.SelectTaskList();
                int sum = tlists.size();
                toolbar.setTitle(app.getString(R.string.title_activity_task_list) + "(" + sum + ")");
                TaskListArrayAdapter arrayAdapter = new TaskListArrayAdapter(Application.getInstance().getBaseContext(),
                        R.layout.list_item_view_pager_delete, tlists, itemPageResArray, 0, "");
                taskListView.setAdapter(arrayAdapter);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bundle resultBundles = new Bundle();
            resultBundles.putString(MyDraftsActivity.RESULT_TEXT, "");
            Intent resultIntent = new Intent()
                    .putExtras(resultBundles);
            setResult(
                    RESULT_OK,
                    resultIntent);
            finish();
        }

        return false;

    }
}

