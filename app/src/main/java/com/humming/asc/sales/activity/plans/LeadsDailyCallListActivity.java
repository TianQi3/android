package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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

public class LeadsDailyCallListActivity extends AbstractActivity implements ICallback<DailyCallQueryResultVO> {
    public static final String ROWID = "row_id";
    public static final String ACCOUNT_NAME = "account_name";
    private ListView dailycallListview;
    private List<DailyCallVO> lists;
    private DailyCallService dailyCallService;
    private Toolbar toolbar;
    private View back;
    private String getIntentRowId;
    private String accountName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_daily_call_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        back = findViewById(R.id.content_task_daily_call_list__back);
        back.setVisibility(View.GONE);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        getIntentRowId = getIntent().getStringExtra(ROWID);
        accountName = getIntent().getStringExtra(ACCOUNT_NAME);
        dailyCallService = Application.getDailyCallService();
        dailycallListview = (ListView) findViewById(R.id.content_task_product__listView);
        dailyCallService.queryLeadsDailyCall(this, getIntentRowId,"Leads","");
        dailycallListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                dailyCallService.queryByRowId(new ICallback<DailyCallDetailResultVO>() {

                    @Override
                    public void onDataReady(DailyCallDetailResultVO data) {
                        DailyCallDetailVO data1 = data.getData();
                        Application.getInstance().setDailyCallDetail4Edit(data1);
                        Intent intent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
                        intent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME,accountName);
                        intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, getIntentRowId);
                        intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, data1.getAssocType());
                        intent.putExtra(DailyCallEditorActivity.FOLLOW_UP, "true");
                        intent.putExtra(DailyCallEditorActivity.TASK_ID, data1.getTaskId());
                        startActivity(intent);
                        finish();
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
                finish();
                break;
            case R.id.action_product_add:
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallEditorActivity.class);
                Application.getInstance().setDailyCallDetail4Edit(null);
                intent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME,accountName);
                intent.putExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL, "true");
                intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, "Leads");
                intent.putExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL_TYPE, "Leads");
                intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, getIntentRowId);
                Application.getInstance().getCurrentActivity().startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDataReady(DailyCallQueryResultVO data) {
        lists = data.getData();
        toolbar.setTitle(Application.getInstance().getString(R.string.title_activity_my_daily_call) + "(" + lists.size() + ")");
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                R.layout.list_item_task_daily_call, lists);
        dailycallListview.setAdapter(arrayAdapter);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    class ViewHolder {
        ImageView type;
        ImageView assocType;
        TextView name;
        TextView subject;
        TextView meetcount;
        ImageView state;
        TextView upd;
    }

    private class MyArrayAdapter extends AbstractArrayAdapter<ViewHolder, DailyCallVO> {

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
            viewHolder.type = (ImageView) rowView
                    .findViewById(R.id.list_item_daily_call_state_type);
            viewHolder.upd = (TextView) rowView.findViewById(R.id.list_item_daily_call_up_date);
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
            viewHolder.type.setImageResource(typeImgRes);
            viewHolder.upd.setText(dailyCallVO.getLastUpd());
        }
    }
}
