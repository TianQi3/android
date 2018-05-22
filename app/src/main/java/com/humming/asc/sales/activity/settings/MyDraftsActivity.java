package com.humming.asc.sales.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.datebase.DBManger;

public class MyDraftsActivity extends AbstractActivity {
    private TextView TaskCount, DailyCount, LeadsCount;
    public static final int TASK_RESULT_CODE = 12210;
    public static final int DC_RESULT_CODE = 12211;
    public static final int LEADS_RESULT_CODE = 12212;
    public static final String RESULT_TEXT = "result_text";
    private DBManger dbManger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drafts_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TaskCount = (TextView) findViewById(R.id.activity_drafts__task);
        DailyCount = (TextView) findViewById(R.id.activity_drafts__daily_call);
        LeadsCount = (TextView) findViewById(R.id.activity_drafts__leads);
        //查询数据库:
        dbManger = new DBManger(this);
        int taskCount = dbManger.SelectTaskList().size();
        int dailycallCount = dbManger.SelectDailyCallList().size();
        int leadsCount = dbManger.SelectLeadsList().size();
        TaskCount.setText(Application.getInstance().getString(R.string.customer_task) + "(" + taskCount + ")");
        DailyCount.setText(Application.getInstance().getString(R.string.customer_dailycall) + "(" + dailycallCount + ")");
        LeadsCount.setText(Application.getInstance().getString(R.string.label_leads) + "(" + leadsCount + ")");
        TaskCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getBaseContext(), DraftsTaskListActivity.class);
                startActivityForResult(intent, TASK_RESULT_CODE);
            }
        });
        DailyCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getBaseContext(), DraftsDailyCallListActivity.class);
                startActivityForResult(intent, DC_RESULT_CODE);
            }
        });
        LeadsCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getBaseContext(), DraftsLeadsListActivity.class);
                startActivityForResult(intent, LEADS_RESULT_CODE);
            }
        });
        if (taskCount == 0) {
            TaskCount.setEnabled(false);
        }
        if (dailycallCount == 0) {
            DailyCount.setClickable(false);
        }
        if (leadsCount == 0) {
            LeadsCount.setClickable(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_customer_selector, menu);
        return true;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle = data.getExtras();
        String nameEn = resultBundle
                .getString(MyDraftsActivity.RESULT_TEXT);
        switch (requestCode) {
            case MyDraftsActivity.TASK_RESULT_CODE:
                int taskCount = dbManger.SelectTaskList().size();
                TaskCount.setText(Application.getInstance().getString(R.string.customer_task) + "(" + taskCount + ")");
                break;
            case MyDraftsActivity.DC_RESULT_CODE:
                int dcCount =dbManger.SelectDailyCallList().size();
                DailyCount.setText(Application.getInstance().getString(R.string.customer_dailycall) + "(" + dcCount + ")");
                break;
            case MyDraftsActivity.LEADS_RESULT_CODE:
                int leadsCount = dbManger.SelectLeadsList().size();
                LeadsCount.setText(Application.getInstance().getString(R.string.label_leads) + "(" + leadsCount + ")");
                break;
        }
    }
}
