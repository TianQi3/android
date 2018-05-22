package com.humming.asc.sales.activity.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.humming.asc.dp.presentation.vo.cp.DCSummaryInfoVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskListResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskListVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.plans.DailyCallEditorActivity;
import com.humming.asc.sales.activity.plans.TaskAddActivity;
import com.humming.asc.sales.activity.plans.TaskConditionActivity;
import com.humming.asc.sales.activity.plans.TaskEditActivity;
import com.humming.asc.sales.activity.plans.TaskProductListActivity;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.Taskservice;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomerTaskListActivity extends AbstractActivity {

    private SearchView msearchView;
    private PullToRefreshListView taskListView;
    public static final String ROW_ID = "row_id";
    public static final String CUS_NAME = "cus_namess";
    private int[] itemPageResArray;
    private Application app = Application.getInstance();
    private MenuItem seacherFilterMenu;
    private Taskservice taskservice;
    private Toolbar toolbar;
    String rowId = "";
    String cusName = "";
    private List<TaskListVO> currentlist;
    private ArrayList<TaskListVO> tlists;
    public static Activity TListActivity;
    public static final String CUSTOMER_DETAIL_ADD_TASK = "customer_detail_add_task";
    private int PageNo = 1, keyWordPageNo = 1, conditionPageNo = 1;
    public static final String CUSTOMER_ADD_TASK_ROWID = "customer_detail_rowid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TListActivity = this;
        rowId = getIntent().getStringExtra(CustomerTaskListActivity.ROW_ID);
        cusName = getIntent().getStringExtra(CustomerTaskListActivity.CUS_NAME);
        itemPageResArray = new int[]{R.layout.list_item_task, R.layout.list_item_view_page_right};
        taskListView = (PullToRefreshListView) findViewById(R.id.content_task__listView);
        init();
        mLoading.show();
        taskservice = Application.getTaskservice();
        PageNo = 1;
        tlists = new ArrayList<TaskListVO>();
        queryTask(rowId,String.valueOf(PageNo));
    }

    //查询Task
    public void queryTask(final String rowId,final String pageNos) {
        taskservice.queryByCustomerRowId(new ICallback<TaskListResultVO>() {

            @Override
            public void onDataReady(TaskListResultVO data) {
                currentlist = data.getData();
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    tlists = (ArrayList<TaskListVO>) currentlist;
                    TaskListArrayAdapter taskListArrayAdapter = new TaskListArrayAdapter(Application.getInstance().getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, currentlist, itemPageResArray, 0);
                    taskListView.setAdapter(taskListArrayAdapter);
                } else {
                    int currentSize = currentlist.size();
                    int scorllCurrentItem =0;
                    if(currentSize>=0){
                        scorllCurrentItem = tlists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        tlists.add(currentlist.get(i));
                    }
                    TaskListArrayAdapter taskListArrayAdapter = new TaskListArrayAdapter(Application.getInstance().getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, tlists, itemPageResArray, 0);
                    taskListView.setAdapter(taskListArrayAdapter);
                    taskListView.getRefreshableView().setSelection(scorllCurrentItem);
                }
                int sum = tlists.size();
                toolbar.setTitle(app.getString(R.string.title_activity_task_list) + "(" + sum + ")");


                taskListView.onRefreshComplete();
                taskListView.setMode(PullToRefreshBase.Mode.BOTH);
                taskListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        PageNo=1;
                        queryTask(rowId,"1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        PageNo++;
                        queryTask(rowId,PageNo + "");

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        },rowId, pageNos);
    }

    //关键字查询
    public void queryBykeyWord(final String keyWords, final String pageNos) {
        taskservice.queryByNameCus(new ICallback<TaskListResultVO>() {
            @Override
            public void onDataReady(TaskListResultVO data) {
                currentlist = data.getData();
                int scorllCurrentItem =0;
                int sum = currentlist.size();
                toolbar.setTitle(app.getString(R.string.title_activity_task_list) + "(" + sum + ")");
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    tlists = (ArrayList<TaskListVO>) currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if(currentSize>=0){
                        scorllCurrentItem = tlists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        tlists.add(currentlist.get(i));
                    }
                }
                TaskListArrayAdapter taskListArrayAdapter = new TaskListArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, tlists, itemPageResArray, 0);
                taskListView.setAdapter(taskListArrayAdapter);
                taskListView.getRefreshableView().setSelection(scorllCurrentItem);
                taskListView.onRefreshComplete();
                taskListView.setMode(PullToRefreshBase.Mode.BOTH);
                taskListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        keyWordPageNo=1;
                        queryBykeyWord(keyWords, "1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        keyWordPageNo++;
                        queryBykeyWord(keyWords, keyWordPageNo + "");

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, rowId,keyWords, pageNos);
    }

    //condition 查询
    public void queryByCondition(final String conditionMenu, final String menuValue, final String PageNos) {
        taskservice.queryByCOnditionValueCus(new ICallback<TaskListResultVO>() {

            @Override
            public void onDataReady(TaskListResultVO data) {
                currentlist = data.getData();
                int scorllCurrentItem =0;
                mLoading.hide();
                toolbar.setTitle(app.getString(R.string.title_activity_task_list) + "(" + currentlist.size() + ")");
                if ("1".equals(PageNos)) {
                    tlists = (ArrayList<TaskListVO>) currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if(currentSize>=0){
                        scorllCurrentItem = tlists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        tlists.add(currentlist.get(i));
                    }
                }
                TaskListArrayAdapter taskListArrayAdapter = new TaskListArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, tlists, itemPageResArray, 0);
                taskListView.setAdapter(taskListArrayAdapter);
                taskListView.getRefreshableView().setSelection(scorllCurrentItem);
                taskListView.onRefreshComplete();
                taskListView.setMode(PullToRefreshBase.Mode.BOTH);
                taskListView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        conditionPageNo = 1;
                        queryByCondition(conditionMenu, menuValue, "1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        conditionPageNo++;
                        queryByCondition(conditionMenu, menuValue, conditionPageNo + "");
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        },rowId, conditionMenu, menuValue, PageNos);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_list, menu);
        seacherFilterMenu = menu.findItem(R.id.action_task_search);
        msearchView = (SearchView) MenuItemCompat.getActionView(seacherFilterMenu);
        MenuItemCompat.setOnActionExpandListener(seacherFilterMenu, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                //点击返回按钮
                setQueryDataByKeyWord("");
                return true;
            }
        });
        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                setQueryDataByKeyWord(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return true;
    }

    //关键字查询
    private void setQueryDataByKeyWord(String query) {
        keyWordPageNo = 1;
        tlists.clear();
        queryBykeyWord(query, String.valueOf(keyWordPageNo));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        DCSummaryInfoVO info;
        switch (id) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_task_condition:
                Intent intent = new Intent(getBaseContext(), TaskConditionActivity.class);
                startActivityForResult(intent, TaskConditionActivity.ACTIVITY_CONDITION_RESULT);
                break;
            case R.id.action_task_add:
                Intent intent1 = new Intent(app.getBaseContext(), TaskAddActivity.class);
                intent1.putExtra(TaskAddActivity.CUSTOMER_ADD_TASK_NAMEEN,cusName);
                intent1.putExtra(TaskAddActivity.CUSTOMER_ADD_TASK_ROWID,rowId);
                intent1.putExtra(TaskAddActivity.CUSTOMER_ADD_TASK,"true");
                intent1.putExtra(CustomerTaskListActivity.CUSTOMER_DETAIL_ADD_TASK,"true");
                app.getCurrentActivity().startActivity(intent1);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class TaskListArrayAdapter extends AbstractItemPagerArrayAdapter<TaskListVO, ViewHolder> {
        private View.OnClickListener onbtnDailyCallClickListener;
        private View.OnClickListener onbtnProductClickListener;

        public TaskListArrayAdapter(Context context, int list_item_customer1, final List<TaskListVO> list, int[] itemPageResArray, int i) {
            super(context, list_item_customer1, list, itemPageResArray, i, "");
            onbtnDailyCallClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Intent addDailyCallIntent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
                    Application.getInstance().setDailyCallDetail4Edit(null);
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_ID, list.get(position).getTaskId());
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.FOLLOW_UP, "true");
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_ADD_DAILY_CALL, "true");
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME, list.get(position).getAccountName());
                    addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_NAME, list.get(position).getTaskName());
                    startActivity(addDailyCallIntent);
                    updateViewPagerState(position, 0,true);
                    notifyDataSetChanged();
                }
            };
            onbtnProductClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Intent intent = new Intent(getBaseContext(), TaskProductListActivity.class);
                    intent.putExtra(TaskProductListActivity.TASK_ID, list.get(position).getTaskId());
                    intent.putExtra(TaskProductListActivity.CUSTOMER_TASK_PRODUCT,"true");
                    startActivity(intent);
                    updateViewPagerState(position, 0,true);
                    notifyDataSetChanged();
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
            viewHolder.btnDailycall = (Button) rightView.findViewById(R.id.right_dailycall);
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
            viewHolder.btnProduct = (Button) rightView.findViewById(R.id.right_product);
            viewHolder.content = findViewById(R.id.list_item_task__big);
            centerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String taskId = tlists.get(position).getTaskId();
                    Intent intent = new Intent(app.getBaseContext(), TaskEditActivity.class);
                    intent.putExtra(TaskEditActivity.TASK_ID, taskId);
                    intent.putExtra(CustomerTaskListActivity.CUSTOMER_DETAIL_ADD_TASK,"true");
                    intent.putExtra(CustomerTaskListActivity.CUSTOMER_ADD_TASK_ROWID,rowId);
                    app.getCurrentActivity().startActivity(intent);
                }
            });
            viewHolder.btnDailycall.setOnClickListener(onbtnDailyCallClickListener);
            viewHolder.btnProduct.setOnClickListener(onbtnProductClickListener);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, TaskListVO itemData) {
            viewHolder.btnProduct.setVisibility(View.VISIBLE);
            viewHolder.btnDailycall.setVisibility(View.VISIBLE);
            viewHolder.taskDate.setText(itemData.getTaskRange());
            viewHolder.taskSubject.setText(itemData.getTaskType());
            viewHolder.taskState.setText(itemData.getTaskState());
            viewHolder.taskAccountName.setText(itemData.getTaskName());
            viewHolder.taskName.setText(itemData.getAccountName());
            viewHolder.saleName.setText(itemData.getSaleName());
            viewHolder.btnProduct.setTag(position);
            viewHolder.btnDailycall.setTag(position);
        }

        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    if (position == 1) {
                        return 0.48f;
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
        Button btnDailycall;
        Button btnProduct;
        View content;
    }

    //筛选返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle;
        switch (requestCode) {
            case TaskConditionActivity.ACTIVITY_CONDITION_RESULT:
                mLoading.show();
                resultBundle = data.getExtras();
                String conditionMenu = resultBundle
                        .getString(TaskConditionActivity.CONDITION_MENU);
                String menuValue = resultBundle.getString(TaskConditionActivity.CONDITION_MENU_VALUE);
                if ("date".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "dateType";
                } else if ("status".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "taskStatus";
                } else if ("type".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "taskType";
                }
                conditionPageNo = 1;
                tlists.clear();

                queryByCondition(conditionMenu, menuValue, String.valueOf(conditionPageNo));
        }
    }

    private void init() {
        ILoadingLayout startLabels = taskListView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = taskListView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

    }
}
