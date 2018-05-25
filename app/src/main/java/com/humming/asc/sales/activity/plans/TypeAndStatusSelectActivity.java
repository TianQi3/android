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

import com.humming.asc.dp.presentation.vo.cp.baseinfo.BaseInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.DCSubjectResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.DCSubjectVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.AbstractArrayAdapter;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.Taskservice;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 16/1/27.
 */
public class TypeAndStatusSelectActivity extends AbstractActivity implements ICallback<BaseInfoResultVO> {
    private ListView listView;
    public static final int ACTIVITY_ADD_TASK_SELECT = 10010;
    public static final int ACTIVITY_ADD_UPDATE_DAILY_CALL_TYPE_SELECT = 10012;
    public static final int ACTIVITY_ADD_UPDATE_DAILY_CALL_SUBJECT_SELECT = 10013;
    public static final int ACTIVITY_ADD_UPDATE_DAILY_CALL_STATUS_SELECT = 10014;
    public static final int ACTIVITY_ADD_STATUS_SELECT = 10011;
    public static String ACTIVITY_TASK = "activity_task";
    public static String ACTIVITY_TASK_TYPE = "activity_task_type";
    public static String ACTIVITY_TASK_STATUS = "activity_task_status";

    public static String ACTIVITY_DAILY_CALL_STATUS = "activity_daily_call_status";
    public static String ACTIVITY_DAILY_CALL_TYPE = "activity_daily_call_type";
    public static String ACTIVITY_DAILY_CALL_SUBJECT = "activity_daily_call_subject";
    private List<String> lists;
    private String currentName;
    public static List<DCSubjectVO> listsDate;
    public static int positions = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_status_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        currentName = getIntent().getStringExtra(ACTIVITY_TASK);
        listView = (ListView) findViewById(R.id.content_class_status_list);
        Taskservice taskservice = Application.getTaskservice();
        DailyCallService dailyCallservice = Application.getDailyCallService();
        mLoading.show();
        if (ACTIVITY_TASK_STATUS.equals(currentName)) { //ts status
            getSupportActionBar().setTitle(Application.getInstance().getString(R.string.status));
            taskservice.getTaskStatus(this);
        } else if (ACTIVITY_TASK_TYPE.equals(currentName)) { //task's type
            getSupportActionBar().setTitle(Application.getInstance().getString(R.string.type));
            taskservice.getTaskType(this);
        } else if (ACTIVITY_DAILY_CALL_STATUS.equals(currentName)) {//daily call's status
            getSupportActionBar().setTitle(Application.getInstance().getString(R.string.status));
            dailyCallservice.getDCStatus(this);

        } else if (ACTIVITY_DAILY_CALL_SUBJECT.equals(currentName)) {
            getSupportActionBar().setTitle(Application.getInstance().getString(R.string.subject));
            dailyCallservice.getDCSubject(new ICallback<DCSubjectResultVO>() {

                @Override
                public void onDataReady(DCSubjectResultVO data) {
                    listsDate = data.getData();
                    mLoading.hide();
                    lists = new ArrayList<String>();
                    if (listsDate != null && listsDate.size() > 0) {
                        for (int i = 0; i < listsDate.size(); i++) {
                            lists.add(listsDate.get(i).getSubjcet());
                        }
                        if ("true".equals(DailyCallEditorActivity.followUp)) {
                            for (int i = 1; i < 3; i++) {
                                lists.remove(lists.size() - i);
                            }
                        }
                    }
                    MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(), R.layout.list_item_textview, lists);
                    listView.setAdapter(arrayAdapter);
                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        } else if (ACTIVITY_DAILY_CALL_TYPE.equals(currentName)) {
            getSupportActionBar().setTitle(Application.getInstance().getString(R.string.type));
            dailyCallservice.getDCType(this);
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle resultBundle = new Bundle();
                positions = position;
                resultBundle.putString(
                        TaskAddActivity.TYPE_OR_STATUS_NAME, lists.get(position));
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        ACTIVITY_ADD_STATUS_SELECT,
                        resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onDataReady(BaseInfoResultVO data) {
        lists = data.getData();
        mLoading.hide();
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(), R.layout.list_item_textview, lists);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    class ViewHolder {
        TextView name;
    }

    class MyArrayAdapter extends AbstractArrayAdapter<ViewHolder, String> {

        public MyArrayAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(int position, ViewHolder viewHolder, View rowView) {
            viewHolder.name = (TextView) rowView
                    .findViewById(R.id.list_item__textview);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, String itemData) {
            viewHolder.name.setText(itemData);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
