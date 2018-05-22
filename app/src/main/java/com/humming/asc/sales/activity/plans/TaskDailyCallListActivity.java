package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallQueryResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.AbstractArrayAdapter;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;

import java.util.List;

public class TaskDailyCallListActivity extends AbstractActivity implements ICallback<DailyCallQueryResultVO> {
    public static final String TASK_CUSTOMER = "task_customer";
    public static final String TASK_NAME = "task_name";
    public static final String TASK_ID = "task_id";
    private TextView taskName, taskCustomer;
    private ListView dailycallListview;
    private List<DailyCallVO> lists;
    private DailyCallService dailyCallService;
    private Toolbar toolbar;
    private View back;
    private String getIntentTaskName, getIntentTaskId, getIntentCus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_daily_call_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        back = findViewById(R.id.content_task_daily_call_list__back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getIntentTaskName = getIntent().getStringExtra(TASK_NAME);
        getIntentTaskId = getIntent().getStringExtra(TASK_ID);
        getIntentCus = getIntent().getStringExtra(TASK_CUSTOMER);
        taskName = (TextView) findViewById(R.id.content_daily_call_list__task);
        taskCustomer = (TextView) findViewById(R.id.content_daily_call_list__customer);
        taskName.setText(getIntentTaskName);
        taskCustomer.setText(getIntentCus);
        dailyCallService = Application.getDailyCallService();
        dailycallListview = (ListView) findViewById(R.id.content_task_product__listView);
        dailyCallService.queryDailyCallByTaskId(this, getIntentTaskId, "1");
        dailycallListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dailyCallService.queryByRowId(new ICallback<DailyCallDetailResultVO>() {

                    @Override
                    public void onDataReady(DailyCallDetailResultVO data) {
                        DailyCallDetailVO data1 = data.getData();
                        Application.getInstance().setDailyCallDetail4Edit(data1);
                        Intent intent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
                        intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, data1.getAssocType());
                        intent.putExtra(DailyCallEditorActivity.FOLLOW_UP, "true");
                        intent.putExtra(DailyCallEditorActivity.TASK_ID, data1.getTaskId());
                        intent.putExtra(DailyCallEditorActivity.TASK_COME, "true");
                        startActivity(intent);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                }, lists.get(position).getRowId());
            }
        });
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
            case android.R.id.home:
                TaskEditActivity.taskEditActivity.finish();
                Intent intents = new Intent(Application.getInstance().getBaseContext(), TaskEditActivity.class);
                intents.putExtra(TaskEditActivity.TASK_ID, getIntentTaskId);
                startActivity(intents);
                finish();
                break;
            case R.id.action_product_add:
                Intent addDailyCallIntent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
                Application.getInstance().setDailyCallDetail4Edit(null);
                addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_ID, getIntentTaskId);
                addDailyCallIntent.putExtra(DailyCallEditorActivity.FOLLOW_UP, "true");
                addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_ADD_DAILY_CALL, "true");
                addDailyCallIntent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME, getIntentCus);
                addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_NAME, getIntentTaskName);
                addDailyCallIntent.putExtra(DailyCallEditorActivity.TASK_COME, "true");
                addDailyCallIntent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, "Key Account");
                startActivity(addDailyCallIntent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataReady(DailyCallQueryResultVO data) {
        lists = data.getData();
        getSupportActionBar().setTitle(Application.getInstance().getString(R.string.title_activity_my_daily_call) + "(" + lists.size() + ")");
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                R.layout.list_item_task_daily_call, lists);
        dailycallListview.setAdapter(arrayAdapter);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    class ViewHolder {
        View type;
        ImageView assocType;
        TextView name;
        TextView subject;
        TextView meetcount;
        ImageView state;
        TextView upd;
        TextView salesName;
    }

    class MyArrayAdapter extends AbstractArrayAdapter<ViewHolder, DailyCallVO> {

        public MyArrayAdapter(Context context, int resource, List<DailyCallVO> items) {
            super(context, resource, items);
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(int position, ViewHolder viewHolder, View rowView) {
            viewHolder.assocType = (ImageView) rowView
                    .findViewById(R.id.list_item_daily_call_assoc_type);
            viewHolder.name = (TextView) rowView
                    .findViewById(R.id.list_item_daily_call_account_name);
            viewHolder.subject = (TextView) rowView
                    .findViewById(R.id.list_item_daily_call_subject);
            viewHolder.meetcount = (TextView) rowView
                    .findViewById(R.id.list_item_daily_call_meetcount);
            viewHolder.state = (ImageView) rowView
                    .findViewById(R.id.list_item_daily_call_state);
            viewHolder.type = (View) rowView
                    .findViewById(R.id.list_item_daily_call_state_type);
            viewHolder.upd = (TextView) rowView.findViewById(R.id.list_item_daily_call_up_date);
            viewHolder.salesName = (TextView) rowView.findViewById(R.id.list_item_daily_call_sales_name);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, DailyCallVO dailyCallVO) {
            String assocType = dailyCallVO.getAssocType().toLowerCase();
            String name = dailyCallVO.getAccountName();
            int assocTypeImgRes = R.drawable.icon_l;

            String state = dailyCallVO.getStatus();
            int stateImgRes = R.drawable.ic_state_progress;
            int stateBgRes = R.drawable.bg_oval_y;
            if ("planned".equalsIgnoreCase(state)) {

            } else if ("completed".equalsIgnoreCase(state)) {
                stateImgRes = R.drawable.ic_state_completed;
                stateBgRes = R.drawable.bg_oval_g;
            } else {
                stateImgRes = R.drawable.ic_state_cancel;
                stateBgRes = R.drawable.bg_oval_r;
            }

            String type = dailyCallVO.getType().toLowerCase();
            int typeImgRes = R.drawable.ic_type_visit;
            if ("email/sms".equalsIgnoreCase(type)) {
                typeImgRes = R.drawable.ic_type_mail;
            } else if ("key account".equalsIgnoreCase(type)) {
                typeImgRes = R.drawable.ic_type_phone;
            }
            if ("key account".equalsIgnoreCase(assocType)) {
                assocTypeImgRes = R.drawable.icon_k;
            } else if ("other account".equalsIgnoreCase(assocType)) {
                assocTypeImgRes = R.drawable.icon_o;
            } else if ("leads".equalsIgnoreCase(assocType)) {
                assocTypeImgRes = R.drawable.icon_l;
            } else {
                assocTypeImgRes = R.drawable.icon_l;
            }
            String subject = dailyCallVO.getSubject();
            viewHolder.assocType.setImageResource(assocTypeImgRes);
            viewHolder.name.setText(name);
            viewHolder.subject.setText(subject);
            viewHolder.meetcount.setText(dailyCallVO.getMeetingContent());
            viewHolder.state.setImageResource(stateImgRes);
            viewHolder.state.setBackgroundResource(stateBgRes);
            viewHolder.type.setBackgroundResource(typeImgRes);
            viewHolder.upd.setText(dailyCallVO.getLastUpd());

            viewHolder.salesName.setText(dailyCallVO.getSaleName());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            TaskEditActivity.taskEditActivity.finish();
            Intent intents = new Intent(Application.getInstance().getBaseContext(), TaskEditActivity.class);
            intents.putExtra(TaskEditActivity.TASK_ID, getIntentTaskId);
            startActivity(intents);
            finish();
        }
        return false;

    }
}
