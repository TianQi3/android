package com.humming.asc.sales.activity.plans;

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
import com.humming.asc.dp.presentation.ro.cp.leads.UpdateTargetFlagRO;
import com.humming.asc.dp.presentation.vo.cp.CustomerVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.leads.LeadsDetailResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.Stocks.SimpleSelectorActivity;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.LeadsService;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 16/1/15.
 */
public class LeadsListActivity extends AbstractActivity {

    private PullToRefreshListView listView;

    private ArrayList<CustomerVO> currentslist;
    private ArrayList<CustomerVO> list;
    private String TARGET_LEADS = "Target Leads";
    private String UNTARGET_LEADS = "Untarget Leads";
    private String target = "Target";
    private String untarget = "Untarget";
    private MenuItem seacherFilterMenu, targetMenu;
    private SearchView msearchView;
    private LeadsService leadsService;
    private String flag = "Target Leads";
    private int[] itemPageResArray;
    private TextView title;
    public static Activity LListActivity;
    public static String LEADS_TYPE = "leads_type";
    private int targetPageNo = 1, unTargetPageNo = 1, keyWordPageNo = 1, conditionPageNo = 1;
    public static final int LEADS_RESULT = 12342;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        LListActivity = this;
        title = (TextView) findViewById(R.id.activity_leads_list_title);
        itemPageResArray = new int[]{R.layout.list_item_leads, R.layout.list_item_view_page_right};
        listView = (PullToRefreshListView) findViewById(R.id.content_customer__listView);
        init();
        mLoading.show();
        leadsService = Application.getLeadsService();
        targetPageNo = 1;
        list = new ArrayList<CustomerVO>();
        queryLeads(flag, targetPageNo + "");
        title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag.equalsIgnoreCase(UNTARGET_LEADS)) {
                    flag = TARGET_LEADS;
                    targetPageNo = 1;
                    list.clear();
                    queryLeads(flag, targetPageNo + "");
                } else {
                    flag = UNTARGET_LEADS;
                    unTargetPageNo = 1;
                    list.clear();
                    queryLeads(flag, unTargetPageNo + "");
                }
            }
        });
    }

    public void queryLeads(final String flag, final String pageNo) {
        leadsService.query(new ICallback<List<CustomerVO>>() {

            @Override
            public void onDataReady(List<CustomerVO> data) {
                currentslist = (ArrayList<CustomerVO>) data;
                int scorllCurrentItem = 0;
                mLoading.hide();
                if ("1".equals(pageNo)) {
                    list = currentslist;
                } else {
                    int currentSize = currentslist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = list.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        list.add(currentslist.get(i));
                    }
                }
                int sum = list.size();
                if (flag.equalsIgnoreCase(UNTARGET_LEADS)) {
                    title.setText(untarget + "(" + sum + ")");
                } else {
                    title.setText(target + "(" + sum + ")");
                }

                // getSupportActionBar().setTitle(flag + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, list, itemPageResArray, 0, flag);
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        if (TARGET_LEADS.equals(flag)) {
                            targetPageNo = 1;
                        } else {
                            unTargetPageNo = 1;
                        }
                        queryLeads(flag, "1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        if (TARGET_LEADS.equals(flag)) {
                            targetPageNo++;
                            queryLeads(flag, targetPageNo + "");
                        } else {
                            unTargetPageNo++;
                            queryLeads(flag, unTargetPageNo + "");
                        }


                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, flag, pageNo);
    }

    public void queryLeadsByKeyWords(final String keywords, final String flag, final String pageNo) {
        leadsService.queryByKeyWord(new ICallback<List<CustomerVO>>() {

            @Override
            public void onDataReady(List<CustomerVO> data) {
                currentslist = (ArrayList<CustomerVO>) data;
                int scorllCurrentItem = 0;

                mLoading.hide();
                if ("1".equals(pageNo)) {
                    list = currentslist;
                } else {
                    int currentSize = currentslist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = list.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        list.add(currentslist.get(i));
                    }
                }
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, list, itemPageResArray, 0, flag);
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                int sum = list.size();
                title.setText(flag + "(" + sum + ")");
                //  getSupportActionBar().setTitle(flag + "(" + sum + ")");
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        keyWordPageNo = 1;
                        queryLeadsByKeyWords(keywords, flag, "1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        keyWordPageNo++;
                        queryLeadsByKeyWords(keywords, flag, keyWordPageNo + "");
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, keywords, flag, pageNo);
    }

    public void queryLeadsByCondition(final String conditionMenu, final String menuValue, final String pageNo) {
        leadsService.queryByConditionValue(new ICallback<List<CustomerVO>>() {

            @Override
            public void onDataReady(List<CustomerVO> data) {
                currentslist = (ArrayList<CustomerVO>) data;
                int scorllCurrentItem = 0;

                mLoading.hide();
                if ("1".equals(pageNo)) {
                    list = currentslist;
                } else {
                    int currentSize = currentslist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = list.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        list.add(currentslist.get(i));
                    }
                }
                int sum = list.size();
                title.setText(flag + "(" + sum + ")");
                //  getSupportActionBar().setTitle(flag + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, list, itemPageResArray, 0, flag);
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.BOTH);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
                    @Override
                    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                        conditionPageNo = 1;
                        queryLeadsByCondition(conditionMenu, menuValue, "1");
                    }

                    @Override
                    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                        conditionPageNo++;
                        queryLeadsByCondition(conditionMenu, menuValue, conditionPageNo + "");

                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, conditionMenu, menuValue, pageNo);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_leads_list, menu);
        // targetMenu = menu.findItem(R.id.menu_leads);
        seacherFilterMenu = menu.findItem(R.id.action_leads_search);
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

    //关键字查询
    private void setQueryDataByKeyWord(String query) {
        keyWordPageNo = 1;
        list.clear();
        queryLeadsByKeyWords(query, flag, keyWordPageNo + "");

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            finish();
        }
        if (id == R.id.action_leads_add) {
            Application.getInstance().setLeadsDetail4Edit(null);
            Intent intent = new Intent(getBaseContext(), LeadsAddActivity.class);
            startActivity(intent);
            return true;
        }
        //筛选
        if (id == R.id.action_leads_condition) {
            Intent intent1 = new Intent(Application.getInstance().getCurrentActivity(), LeadsConditionActivity.class);
            intent1.putExtra(LeadsConditionActivity.FLAG, flag);
            startActivityForResult(intent1, LeadsConditionActivity.ACTIVITY_CONDITION_RESULT);
            return true;
        }
       /* if (id == R.id.menu_target_leads) {
            item.setCheckable(true);
            flag = TARGET_LEADS;
            targetMenu.setTitle(Application.getInstance().getString(R.string.menu_target));
            targetPageNo = 1;
            list.clear();
            queryLeads(flag, targetPageNo + "");
        }
        if (id == R.id.menu_untarget_leads) {
            item.setCheckable(true);
            flag = UNTARGET_LEADS;
            targetMenu.setTitle(Application.getInstance().getString(R.string.menu_untarget));
            unTargetPageNo = 1;
            list.clear();
            queryLeads(flag, unTargetPageNo + "");
        }*/
        return super.onOptionsItemSelected(item);
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

    class ViewHolder {
        ImageView assocType;
        TextView name;
        TextView nameEn;
        TextView leadsSales;
        TextView leadsRowId;
        Button btnTargetLeads;
        Button btnUntarget;
        Button btnDailyCall;
        View v;
    }

    private class MyArrayAdapter extends AbstractItemPagerArrayAdapter<CustomerVO, ViewHolder> {
        private String type;
        private List<CustomerVO> itemss;
        private View.OnClickListener onbtnDailyCallClickListener;
        private View.OnClickListener onbtnTargetClickListener;
        private View.OnClickListener onbtnUntargetClickListener;
        private View.OnClickListener onItemClickListener;

        public MyArrayAdapter(Context context, int resource, final List<CustomerVO> items, int[] itemPageResArray, int defaultPageIndex, String types) {
            super(context, resource, items, itemPageResArray, defaultPageIndex, types);
            this.type = types;
            this.itemss = items;
            onbtnDailyCallClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallEditorActivity.class);
                    Application.getInstance().setDailyCallDetail4Edit(null);
                    intent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME, items.get(position).getAccountNameEn() + items.get(position).getAccountName());
                    intent.putExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL, "true");
                    intent.putExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL_TYPE, type);
                    intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, "Leads");
                    intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, items.get(position).getCustomerRowId());
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    updateViewPagerState(position, 0, true);
                    notifyDataSetChanged();
                }
            };
            onbtnUntargetClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    UpdateTargetFlagRO updateTargetFlagRO = new UpdateTargetFlagRO();
                    updateTargetFlagRO.setRowId(list.get(position).getCustomerRowId());
                    updateTargetFlagRO.setTargetFlag("N");
                    leadsService.updateLeadsType(new ICallback<ResultVO>() {
                        @Override
                        public void onDataReady(ResultVO data) {
                            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), LeadsListActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, updateTargetFlagRO);
                    updateViewPagerState(position, 0, true);
                    notifyDataSetChanged();
                }
            };
            onbtnTargetClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    leadsService.queryById(new ICallback<LeadsDetailResultVO>() {
                        @Override
                        public void onDataReady(LeadsDetailResultVO data) {
                            Application.getInstance().setLeadsDetail4Edit(data.getData());
                            Intent intent = new Intent(Application.getInstance().getBaseContext(), LeadsAddActivity.class);
                            //    intent.putExtra(LeadsAddActivity.CUSTOMER_ROWID, items.get(position).getCustomerRowId());
                            intent.putExtra(LeadsAddActivity.TARGET_OPEN_CLOSE, "YES");
                            intent.putExtra(LeadsAddActivity.TARGET_SCROLL, "true");
                            intent.putExtra(LeadsAddActivity.CUSTOMER_TYPE, type);
                            startActivityForResult(intent, LeadsListActivity.LEADS_RESULT);
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, items.get(position).getCustomerRowId());

                    updateViewPagerState(position, 0, true);
                    notifyDataSetChanged();
                }
            };
            onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    leadsService.queryById(new ICallback<LeadsDetailResultVO>() {
                        @Override
                        public void onDataReady(LeadsDetailResultVO data) {
                            Application.getInstance().setLeadsDetail4Edit(data.getData());
                            Intent intent = new Intent(Application.getInstance().getBaseContext(), LeadsAddActivity.class);
                            intent.putExtra(LeadsAddActivity.CUSTOMER_TYPE, type);
                            if ("Target Leads".equals(flag)) {
                                intent.putExtra(LeadsAddActivity.TARGET_OPEN_CLOSE, "YES");
                            }
                            startActivityForResult(intent, LeadsListActivity.LEADS_RESULT);
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, itemss.get(position).getCustomerRowId());
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
        protected void initViewHolder(int position, ViewHolder viewHolder, List<View> itemPages) {
            View rightView = itemPages.get(1);
            View centerView = itemPages.get(0);
            viewHolder.v = centerView;
            viewHolder.assocType = (ImageView) centerView
                    .findViewById(R.id.leads_cus_type);
            viewHolder.name = (TextView) centerView
                    .findViewById(R.id.leads_account_name);
            viewHolder.nameEn = (TextView) centerView
                    .findViewById(R.id.leads_account_nameEn);
            viewHolder.leadsSales = (TextView) centerView
                    .findViewById(R.id.leads_sales);
            viewHolder.leadsRowId = (TextView) centerView
                    .findViewById(R.id.leads_rowid);
            viewHolder.btnTargetLeads = (Button) rightView.findViewById(R.id.right_competitor);
            viewHolder.btnDailyCall = (Button) rightView.findViewById(R.id.right_dailycall);
            viewHolder.btnUntarget = (Button) rightView.findViewById(R.id.right_untarget);
            viewHolder.btnTargetLeads.setOnClickListener(onbtnTargetClickListener);
            viewHolder.btnUntarget.setOnClickListener(onbtnUntargetClickListener);
            viewHolder.btnDailyCall.setOnClickListener(onbtnDailyCallClickListener);
            viewHolder.v.setOnClickListener(onItemClickListener);
        }

        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    if (position == 1) {
                        //  return 0.5f;
                        if (TARGET_LEADS.equalsIgnoreCase(type)) {
                            return 0.48f;
                        } else {
                            return 0.24f;
                        }
                    } else {
                        return 1f;
                    }
                }
            };
            return adapter;
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, CustomerVO itemData) {
            String name = itemData.getAccountName();
            String nameen = itemData.getAccountNameEn();
            int assocTypeImgRes = R.drawable.icon_l;
            viewHolder.v.setTag(position);
            if (TARGET_LEADS.equalsIgnoreCase(type)) {
                showOrGone(viewHolder);
                viewHolder.btnUntarget.setVisibility(View.VISIBLE);
                viewHolder.btnDailyCall.setVisibility(View.VISIBLE);
                viewHolder.btnUntarget.setTag(position);
                viewHolder.btnDailyCall.setTag(position);
            } else {
                showOrGone(viewHolder);
                viewHolder.btnTargetLeads.setVisibility(View.VISIBLE);
                viewHolder.btnTargetLeads.setTag(position);

            }
            viewHolder.assocType.setImageResource(assocTypeImgRes);
            viewHolder.name.setText(itemData.getChannel() + "   " + itemData.getStage());
            viewHolder.nameEn.setText(itemData.getProvince() + "   " + itemData.getCity());
            viewHolder.leadsRowId.setText(name + nameen);
            viewHolder.leadsSales.setText(itemData.getSales());
        }

        public void showOrGone(ViewHolder viewHolder) {
            viewHolder.btnUntarget.setVisibility(View.GONE);
            viewHolder.btnDailyCall.setVisibility(View.GONE);
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
            case LeadsConditionActivity.ACTIVITY_CONDITION_RESULT:
                mLoading.show();
                resultBundle = data.getExtras();
                String conditionMenu = resultBundle
                        .getString(LeadsConditionActivity.CONDITION_MENU);
                String menuValue = resultBundle.getString(LeadsConditionActivity.CONDITION_MENU_VALUE);
                if ("channel".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "channel";
                } else if ("provice".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "provice";
                } else if ("type".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "type";
                } else if ("stage".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "stage";
                } else if ("sales".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "sale";
                }else if ("status".equalsIgnoreCase(conditionMenu)) {
                    conditionMenu = "status";
                }
                conditionPageNo = 1;
                list.clear();
                queryLeadsByCondition(conditionMenu, menuValue, conditionPageNo + "");
                break;
            case LeadsListActivity.LEADS_RESULT:
                String text = data.getStringExtra(SimpleSelectorActivity.SELECTED_STRING);
                queryLeads(text, 1 + "");
                break;
        }
    }
}
