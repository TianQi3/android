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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallQueryResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.plans.DailyCallCommentsListActivity;
import com.humming.asc.sales.activity.plans.DailyCallConditionActivity;
import com.humming.asc.sales.activity.plans.DailyCallEditorActivity;
import com.humming.asc.sales.activity.plans.TaskConditionActivity;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomerDailyCallListActivity extends AbstractActivity {

    private PullToRefreshListView listView;
    private Application app = Application.getInstance();
    private DailyCallService dailyCallService;
    private MenuItem seacherFilterMenu;
    private SearchView msearchView;
    private ArrayList<DailyCallVO> currentlist;
    private ArrayList<DailyCallVO> dclists;
    private int[] itemPageResArray;
    public static final String ROW_ID = "dc_row_id";
    public static final String OTHER_OR_KEY = "other_or_key";
    public static final String CUS_NAME = "dc_cus_name";
    private String dcId = "";
    private String cusName = "";
    public static Activity DCListActivity;
    private String types = "Other Account";
    private int PageNo = 1, keyWordPageNo = 1, conditionPageNo = 1;
    public static final String CUSTOMER_DETAIL_ADD_DAILY_CALL = "customer_detail_add_daily_call";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_call_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mLoading.show();
        DCListActivity = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cusName = getIntent().getStringExtra(CustomerDailyCallListActivity.CUS_NAME);
        dcId = getIntent().getStringExtra(CustomerDailyCallListActivity.ROW_ID);
        listView = (PullToRefreshListView) findViewById(R.id.content_daily_call_list__listView);
        init();
        itemPageResArray = new int[]{R.layout.list_item_daily_call, R.layout.list_item_view_page_right};
        dailyCallService = Application.getDailyCallService();
        PageNo = 1;
        dclists = new ArrayList<DailyCallVO>();
        if ("true".equals(getIntent().getStringExtra(OTHER_OR_KEY))) {
            queryDailyCallByKeyAccount(String.valueOf(PageNo));
            types = "Key Account";
        } else {
            queryDailyCall(String.valueOf(PageNo));
            types="Other Account";
        }

    }

    //dailyCall 查询
    public void queryDailyCallByKeyAccount(final String pageNos) {
        dailyCallService.queryByKeyAccount(new ICallback<DailyCallQueryResultVO>() {
            @Override
            public void onDataReady(DailyCallQueryResultVO result) {
                currentlist = (ArrayList<DailyCallVO>) result.getData();
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    dclists = currentlist;
                    int sum = currentlist.size();
                    getSupportActionBar().setTitle(app.getString(R.string.edit_task_daily_call) + "(" + sum + ")");
                    MyArrayAdapter arrayAdapter = new MyArrayAdapter(app.getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, currentlist, itemPageResArray, 0, "");
                    listView.setAdapter(arrayAdapter);
                } else {
                    int currentSize = currentlist.size();
                    int scorllCurrentItem = 0;
                    if (currentSize >= 0) {
                        scorllCurrentItem = dclists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        dclists.add(currentlist.get(i));
                    }
                    int sum = dclists.size();
                    getSupportActionBar().setTitle(app.getString(R.string.edit_task_daily_call) + "(" + sum + ")");
                    MyArrayAdapter arrayAdapter = new MyArrayAdapter(app.getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, dclists, itemPageResArray, 0, "");
                    listView.setAdapter(arrayAdapter);
                    listView.getRefreshableView().setSelection(scorllCurrentItem);
                }
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        PageNo = 1;
                        queryDailyCallByKeyAccount("1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        PageNo++;
                        queryDailyCallByKeyAccount(PageNo + "");

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, dcId, pageNos);
    }

    //dailyCall 查询
    public void queryDailyCall(final String pageNos) {
        dailyCallService.queryLeadsDailyCall(new ICallback<DailyCallQueryResultVO>() {
            @Override
            public void onDataReady(DailyCallQueryResultVO result) {
                currentlist = (ArrayList<DailyCallVO>) result.getData();
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    dclists = currentlist;
                    int sum = currentlist.size();
                    getSupportActionBar().setTitle(app.getString(R.string.edit_task_daily_call) + "(" + sum + ")");
                    MyArrayAdapter arrayAdapter = new MyArrayAdapter(app.getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, currentlist, itemPageResArray, 0, "");
                    listView.setAdapter(arrayAdapter);
                } else {
                    int currentSize = currentlist.size();
                    int scorllCurrentItem = 0;
                    if (currentSize >= 0) {
                        scorllCurrentItem = dclists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        dclists.add(currentlist.get(i));
                    }
                    int sum = dclists.size();
                    getSupportActionBar().setTitle(app.getString(R.string.edit_task_daily_call) + "(" + sum + ")");
                    MyArrayAdapter arrayAdapter = new MyArrayAdapter(app.getCurrentActivity(),
                            R.layout.list_item_view_pager_delete, dclists, itemPageResArray, 0, "");
                    listView.setAdapter(arrayAdapter);
                    listView.getRefreshableView().setSelection(scorllCurrentItem);
                }
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        PageNo = 1;
                        queryDailyCall("1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        PageNo++;
                        queryDailyCall(PageNo + "");

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, dcId, types, pageNos);
    }

    //关键字查询
    public void QueryByName(final String keywords, final String pageNos) {
        dailyCallService.queryByNameCus(new ICallback<DailyCallQueryResultVO>() {

            @Override
            public void onDataReady(DailyCallQueryResultVO data) {
                currentlist = (ArrayList<DailyCallVO>) data.getData();
                int scorllCurrentItem = 0;
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    dclists = currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = dclists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        dclists.add(currentlist.get(i));
                    }
                }
                int sum = dclists.size();
                getSupportActionBar().setTitle(app.getString(R.string.edit_task_daily_call) + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(app.getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, dclists, itemPageResArray, 0, "");
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        keyWordPageNo = 1;
                        QueryByName(keywords, "1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        keyWordPageNo++;
                        QueryByName(keywords, keyWordPageNo + "");

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, dcId, types, keywords, pageNos);
    }

    //筛选查询
    public void queryByCOndition(final String conditionMenu, final String menuValue, final String pageNos) {
        dailyCallService.queryByCOnditionValueCus(new ICallback<DailyCallQueryResultVO>() {

            @Override
            public void onDataReady(DailyCallQueryResultVO data) {
                currentlist = (ArrayList<DailyCallVO>) data.getData();
                int scorllCurrentItem = 0;
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    dclists = currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = dclists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        dclists.add(currentlist.get(i));
                    }
                }
                int sum = dclists.size();
                getSupportActionBar().setTitle(app.getString(R.string.edit_task_daily_call) + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(app.getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, dclists, itemPageResArray, 0, "");
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        conditionPageNo = 1;
                        queryByCOndition(conditionMenu, menuValue, "1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        conditionPageNo++;
                        queryByCOndition(conditionMenu, menuValue, conditionPageNo + "");

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, dcId, types, conditionMenu, menuValue);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_daily_call, menu);
        seacherFilterMenu = menu.findItem(R.id.action_daily_call_search);
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
                mLoading.show();
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

    //keyword search
    private void setQueryDataByKeyWord(String query) {
        keyWordPageNo = 1;
        dclists.clear();
        QueryByName(query, String.valueOf(keyWordPageNo));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        //add dailyCall
        if (id == R.id.action_daily_call_add) {
            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallEditorActivity.class);
            Application.getInstance().setDailyCallDetail4Edit(null);
            intent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME, cusName);
            intent.putExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL, "true");
            intent.putExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL_TYPE, types);
            intent.putExtra(CustomerDailyCallListActivity.CUSTOMER_DETAIL_ADD_DAILY_CALL, "true");
            intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, dcId);
            //intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, "Other Account");
            if ("true".equals(getIntent().getStringExtra(OTHER_OR_KEY))) {
                intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, "Key Account");
            } else {
                intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, "Other Account");
            }
            Application.getInstance().getCurrentActivity().startActivity(intent);
            return true;
        }
        //condition
        if (id == R.id.action_daily_call_condition) {
            Intent intent1 = new Intent(app.getBaseContext(), DailyCallConditionActivity.class);
            startActivityForResult(intent1, DailyCallConditionActivity.ACTIVITY_CONDITION_RESULT);
        }
        if (id == android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    class ViewHolder {
        ImageView type;
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

    class MyArrayAdapter extends AbstractItemPagerArrayAdapter<DailyCallVO, ViewHolder> {
        private View.OnClickListener onbtnCommentsClickListener;
        private View.OnClickListener onbtnItemClickListener;

        public MyArrayAdapter(Context context, int resource, final List<DailyCallVO> items, int[] itemPageResArray, int defaultPageIndex, String types) {
            super(context, resource, items, itemPageResArray, defaultPageIndex, types);
            onbtnCommentsClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Intent intentComment = new Intent(getBaseContext(), DailyCallCommentsListActivity.class);
                    intentComment.putExtra(DailyCallCommentsListActivity.ACCOUNT_NAME, items.get(position).getAccountName());
                    intentComment.putExtra(DailyCallCommentsListActivity.SUBJECT, items.get(position).getSubject());
                    intentComment.putExtra(DailyCallCommentsListActivity.CALL_PLAN, items.get(position).getMeetingContent());
                    intentComment.putExtra(DailyCallCommentsListActivity.DAILY_CALL_ID, items.get(position).getRowId());
                    startActivity(intentComment);
                    updateViewPagerState(position, 0, true);
                    notifyDataSetChanged();
                }
            };
            onbtnItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    dailyCallService.queryByRowId(new ICallback<DailyCallDetailResultVO>() {

                        @Override
                        public void onDataReady(DailyCallDetailResultVO data) {
                            DailyCallDetailVO data1 = data.getData();
                            Application.getInstance().setDailyCallDetail4Edit(data1);
                            Intent intent = new Intent(getBaseContext(), DailyCallEditorActivity.class);
                            intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, data1.getAssocType());
                            intent.putExtra(DailyCallEditorActivity.TASK_ID, data1.getTaskId());
                            intent.putExtra(CustomerDailyCallListActivity.CUSTOMER_DETAIL_ADD_DAILY_CALL, "true");
                            intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, data1.getOtherAccountId());
                            startActivity(intent);
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, items.get(position).getRowId());
                    updateViewPagerState(position, 0, true);
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
            viewHolder.type = (ImageView) centerView
                    .findViewById(R.id.list_item_daily_call_state_type);
            viewHolder.upd = (TextView) centerView.findViewById(R.id.list_item_daily_call_up_date);
            viewHolder.btnComment.setOnClickListener(onbtnCommentsClickListener);
            viewHolder.view.setOnClickListener(onbtnItemClickListener);
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
        protected void setItemData(int position, ViewHolder viewHolder, DailyCallVO dailyCallVO) {
            String assocType = dailyCallVO.getAssocType().toLowerCase();
            String name = dailyCallVO.getAccountName();
            viewHolder.view.setTag(position);
            int assocTypeImgRes = R.drawable.icon_l;
            viewHolder.btnComment.setVisibility(View.VISIBLE);
            viewHolder.btnComment.setText("Comments");
            viewHolder.btnComment.setTag(position);
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
            viewHolder.commentsCount.setText(dailyCallVO.getCommentsCount() + "");
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
            case TaskConditionActivity.ACTIVITY_CONDITION_RESULT:
                mLoading.show();
                resultBundle = data.getExtras();
                String conditionMenu = resultBundle
                        .getString(TaskConditionActivity.CONDITION_MENU);
                String menuValue = resultBundle.getString(TaskConditionActivity.CONDITION_MENU_VALUE);
                if ("date".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "dateType";
                } else if ("status".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "status";
                } else if ("type".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "dcvaluetype";
                } else if ("customer type".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "assoctype";
                } else if ("sales".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "sale";
                }
                conditionPageNo = 1;
                dclists.clear();
                queryByCOndition(conditionMenu, menuValue, String.valueOf(conditionPageNo));

        }
    }

    private void init() {
        ILoadingLayout startLabels = listView
                .getLoadingLayoutProxy(true, false);
        startLabels.setPullLabel("下拉刷新...");// 刚下拉时，显示的提示
        startLabels.setRefreshingLabel("正在载入...");// 刷新时
        startLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

        ILoadingLayout endLabels = listView.getLoadingLayoutProxy(
                false, true);
        endLabels.setPullLabel("上拉刷新...");// 刚下拉时，显示的提示
        endLabels.setRefreshingLabel("正在载入...");// 刷新时
        endLabels.setReleaseLabel("放开刷新...");// 下来达到一定距离时，显示的提示

    }
}
