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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.cp.CPSummaryResultVO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.activity.plans.DailyCallEditorActivity;
import com.humming.asc.sales.datebase.DBManger;
import com.humming.asc.sales.model.DBDailycall;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class DraftsDailyCallListActivity extends AbstractActivity {

    private ListView listView;
    private Application app = Application.getInstance();
    private DailyCallService dailyCallService;
    private ArrayList<DBDailycall> dclists;
    private int[] itemPageResArray;
    private CPSummaryVO summary;
    private Application mAPP;
    private MainActivity.MyHandler mHandler;
    private DBManger dbm;
    public static final int DRAFTS_DC_RESULT_CODE = 12133;
    public static final String RESULT_TEXT_DAILY_CALL = "result_text_daily_call";
    private int index;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_call_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mAPP = (Application) getApplication();
        // 获得共享变量实例
        mHandler = mAPP.getHandler();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.content_daily_call_list__listView).setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.content_daily_call_list__listViews);
        listView.setVisibility(View.VISIBLE);
        dailyCallService = Application.getDailyCallService();
        itemPageResArray = new int[]{R.layout.list_item_daily_call, R.layout.list_item_view_page_right};
        dbm = new DBManger(this);
        dclists = (ArrayList) dbm.SelectDailyCallList();
        int sum = dclists.size();
        toolbar.setTitle(app.getString(R.string.customer_dailycall) + "(" + sum + ")");
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getBaseContext(),
                R.layout.list_item_view_pager_delete, dclists, itemPageResArray, 0, "");
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
     //   getMenuInflater().inflate(R.menu.menu_task_product, menu);
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


    class ViewHolder {
        View type;
        ImageView assocType;
        TextView name;
        TextView subject;
        TextView meetcount;
        ImageView state;
        TextView upd;
        Button btnComment;
        View view;
        TextView commentsCount;
    }

    class MyArrayAdapter extends AbstractItemPagerArrayAdapter<DBDailycall, ViewHolder> {
        private View.OnClickListener onbtnDeleteClickListener;
        private View.OnClickListener onbtnItemClickListener;

        public MyArrayAdapter(Context context, int resource, final List<DBDailycall> items, int[] itemPageResArray, int defaultPageIndex, final String types) {
            super(context, resource, items, itemPageResArray, defaultPageIndex, types);
            onbtnDeleteClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    dbm.DeleteDailyCallById(items.get(position).getId());
                    items.remove(position);
                    updateViewPagerState(position, 0, true);
                    int sum = items.size();
                    getSupportActionBar().setTitle(app.getString(R.string.customer_dailycall) + "(" + sum + ")");
                    notifyDataSetChanged();
                }
            };
            onbtnItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final int position = Integer.parseInt(v.getTag().toString());
                    index = position;
                    Application.getInstance().setDbDailycall(items.get(position));
                    Intent intent = new Intent(DraftsDailyCallListActivity.this, DailyCallEditorActivity.class);
                    intent.putExtra(DailyCallEditorActivity.DRAFTS_DC, "true");
                    startActivityForResult(intent, DRAFTS_DC_RESULT_CODE);
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
            viewHolder.view = centerView;
            viewHolder.btnComment = (Button) rightView.findViewById(R.id.right_task);
            viewHolder.commentsCount = (TextView) centerView.findViewById(R.id.list_item_daily_call_comments_count);
            viewHolder.assocType = (ImageView) centerView
                    .findViewById(R.id.list_item_daily_call_assoc_type);
            viewHolder.name = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_account_name);
            viewHolder.subject = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_subject);
            viewHolder.meetcount = (TextView) centerView
                    .findViewById(R.id.list_item_daily_call_meetcount);
            viewHolder.state = (ImageView) centerView
                    .findViewById(R.id.list_item_daily_call_state);
            viewHolder.type =centerView
                    .findViewById(R.id.list_item_daily_call_state_type);
            viewHolder.upd = (TextView) centerView.findViewById(R.id.list_item_daily_call_up_date);
            viewHolder.btnComment.setOnClickListener(onbtnDeleteClickListener);
            viewHolder.view.setOnClickListener(onbtnItemClickListener);
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

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, DBDailycall itemData) {
            String assocType = itemData.getAssocType().toLowerCase();
            String name = itemData.getAccountName();
            viewHolder.view.setTag(position);
            int assocTypeImgRes = R.drawable.icon_l;
            viewHolder.btnComment.setVisibility(View.VISIBLE);
            viewHolder.btnComment.setText((Application.getInstance().getString(R.string.delete)));
            viewHolder.btnComment.setTag(position);
            String state = itemData.getStatus();
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

            String type = itemData.getType().toLowerCase();
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
            String subject = itemData.getSubject();
            viewHolder.assocType.setImageResource(assocTypeImgRes);
            viewHolder.name.setText(name);
            viewHolder.subject.setText(subject);
            viewHolder.meetcount.setText(itemData.getMeetingContent());
            viewHolder.state.setImageResource(stateImgRes);
            viewHolder.state.setBackgroundResource(stateBgRes);
            viewHolder.type.setBackgroundResource(typeImgRes);
            viewHolder.upd.setText(itemData.getCreateTime());
            viewHolder.commentsCount.setText("");
            viewHolder.name.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.meetcount.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.upd.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.subject.setTextColor(getResources().getColor(R.color.colorAccent));
        }
    }

    //筛选返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle;
        switch (requestCode) {
            case DraftsDailyCallListActivity.DRAFTS_DC_RESULT_CODE:
                String text =data.getStringExtra(DraftsDailyCallListActivity.RESULT_TEXT_DAILY_CALL);
                if("true".equals(text)){
                }else{
                    dbm.DeleteDailyCallById(dclists.get(index).getId());
                }
                dclists = (ArrayList) dbm.SelectDailyCallList();
                int sum = dclists.size();
                getSupportActionBar().setTitle(app.getString(R.string.customer_dailycall) + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getBaseContext(),
                        R.layout.list_item_view_pager_delete, dclists, itemPageResArray, 0, "");
                listView.setAdapter(arrayAdapter);
                break;
        }
    }

    public void initDailyCall() {
        dailyCallService.getSummary(new ICallback<CPSummaryResultVO>() {
            @Override
            public void onDataReady(CPSummaryResultVO result) {
                summary = result.getData();
                Application.getInstance().setSummary(summary);
                mHandler.msgContent = "返回的数据";
                mHandler.sendEmptyMessage(0x123);
            }

            @Override
            public void onError(Throwable throwable) {
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            initDailyCall();
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
