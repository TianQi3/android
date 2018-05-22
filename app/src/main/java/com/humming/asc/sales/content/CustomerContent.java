package com.humming.asc.sales.content;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.humming.asc.dp.presentation.ro.cp.leads.UpdateTargetFlagRO;
import com.humming.asc.dp.presentation.vo.cp.CustomerVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.LinkageResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.LinkageVO;
import com.humming.asc.dp.presentation.vo.cp.leads.LeadsDetailResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.activity.customer.CustomerDetailsActivity;
import com.humming.asc.sales.activity.plans.DailyCallEditorActivity;
import com.humming.asc.sales.activity.plans.LeadsAddActivity;
import com.humming.asc.sales.activity.plans.TaskAddActivity;
import com.humming.asc.sales.adapter.AbstractArrayAdapter;
import com.humming.asc.sales.component.Loading;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.LeadsService;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PuTi(编程即菩提) on 1/7/16.
 */
public class CustomerContent extends LinearLayout {

    private PullToRefreshListView listView;
    private ListView chainListview;
    private ArrayList<CustomerVO> currentlist;
    private ArrayList<CustomerVO> lists;
    private final View view;
    public final String KEY_ACCOUNT = "Key Account";
    public final String OTHER_ACCOUNT = "Other Account";
    public final String TARGET_LEADS = "Target Leads";
    public final String UNTARGET_LEADS = "Untarget Leads";
    private LeadsService leadsService;
    private List<LinkageVO> linksgeVoLists;
    private int[] itemPageResArray;
    private int CuspageNo = 1, keyPageNo = 1;
    public static String Title = "";
    public static final int RESULT_CODE = 9999;
    private Loading mLoading;
    private ArrayList<String> chainLists;
    private MyArrayAdapters myArrayAdapters;
    private ArrayList<String> itemCodeQueryList;
    public boolean searchChainCus = false;
    private String currentChainValue;

    public CustomerContent(Context context) {
        this(context, null);
    }

    public CustomerContent(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = inflate(context, R.layout.fragment_customer, this);
        itemPageResArray = new int[]{R.layout.list_item_customer, R.layout.list_item_view_page_right};
        listView = (PullToRefreshListView) findViewById(R.id.content_customer__listView);
        chainListview = (ListView) findViewById(R.id.content_customer__listViews);
        init();
        leadsService = Application.getLeadsService();
        CuspageNo = 1;
        lists = new ArrayList<CustomerVO>();
        itemCodeQueryList = new ArrayList<String>();
        chainLists = new ArrayList<String>();
        mLoading = (Loading) findViewById(R.id.activity_loading);
        mLoading.show();
        queryCustomer(KEY_ACCOUNT, String.valueOf(CuspageNo));
        chainListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                lists.clear();
                CuspageNo = 1;
                mLoading.show();
                currentChainValue = myArrayAdapters.getItem(position);
                queryChainCustomer(myArrayAdapters.getItem(position), String.valueOf(CuspageNo));
            }
        });
    }

    //查询方法
    public void queryCustomer(final String flag, final String pageNos) {
        chainListview.setVisibility(GONE);
        listView.setVisibility(VISIBLE);
        leadsService.queryCustomerLeads(new ICallback<List<CustomerVO>>() {
            @Override
            public void onDataReady(List<CustomerVO> data) {
                mLoading.hide();
                currentlist = (ArrayList<CustomerVO>) data;
                int scorllCurrentItem = 0;
                //   mLoading.hide();
                if ("1".equals(pageNos)) {
                    lists = currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = lists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        lists.add(currentlist.get(i));
                    }
                }
                int sum = lists.size();
                Title = flag + "(" + sum + ")";
                MainActivity.toolbar.setTitle(flag + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, lists, itemPageResArray, 0, flag);
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        CuspageNo++;
                        queryCustomer(flag, CuspageNo + "");
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, flag, pageNos);
    }

    //根据关键字查询chain Customer
    public void queryChainCustomerByKeyWords(final String keywords, final String chain, final String pageNos) {
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);
        chainListview.setVisibility(GONE);
        listView.setVisibility(VISIBLE);
        leadsService.queryChainByKeywords(new ICallback<List<CustomerVO>>() {

            @Override
            public void onDataReady(List<CustomerVO> data) {
                mLoading.hide();
                currentlist = (ArrayList<CustomerVO>) data;
                int scorllCurrentItem = 0;
                if ("1".equals(pageNos)) {
                    lists = currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = lists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        lists.add(currentlist.get(i));
                    }
                }
                int sum = lists.size();
                Title = chain + "(" + sum + ")";
                Application.getInstance().getCurrentActivity().invalidateOptionsMenu();
                MainActivity.toolbar.setTitle(chain + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, lists, itemPageResArray, 0, "chain");
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        CuspageNo++;
                        queryChainCustomerByKeyWords(keywords, chain, CuspageNo + "");
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, keywords, chain, pageNos);
    }

    //查询chain方法
    public void queryChainCustomer(final String flag, final String pageNos) {
        searchChainCus = true;
        MainActivity.chainOrOther = false;
        MainActivity.actionBar.setDisplayHomeAsUpEnabled(true);
        chainListview.setVisibility(GONE);
        listView.setVisibility(VISIBLE);
        leadsService.queryChain(new ICallback<List<CustomerVO>>() {

            @Override
            public void onDataReady(List<CustomerVO> data) {
                mLoading.hide();
                currentlist = (ArrayList<CustomerVO>) data;
                int scorllCurrentItem = 0;
                if ("1".equals(pageNos)) {
                    lists = currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = lists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        lists.add(currentlist.get(i));
                    }
                }
                int sum = lists.size();
                Title = flag + "(" + sum + ")";
                MainActivity.chainMenu.setChecked(true);
                //   Application.getInstance().getCurrentActivity().invalidateOptionsMenu();
                MainActivity.toolbar.setTitle(flag + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, lists, itemPageResArray, 0, "chain");
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        CuspageNo++;
                        queryChainCustomer(flag, CuspageNo + "");
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, flag, pageNos);
    }

    //关键字查询
    public void queryBykeyWords(final String keyWords, final String flag, final String pageNo) {
        leadsService.queryByKeyWord(new ICallback<List<CustomerVO>>() {
            @Override
            public void onDataReady(List<CustomerVO> data) {
                mLoading.hide();
                currentlist = (ArrayList<CustomerVO>) data;
                int scorllCurrentItem = 0;
                if ("1".equals(pageNo)) {
                    lists = currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if (currentSize >= 0) {
                        scorllCurrentItem = lists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        lists.add(currentlist.get(i));
                    }
                }
                int sum = lists.size();
                Title = flag + "(" + sum + ")";
                MainActivity.toolbar.setTitle(flag + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, lists, itemPageResArray, 0, flag);
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        keyPageNo++;
                        queryBykeyWords(keyWords, flag, keyPageNo + "");
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, keyWords, flag, pageNo);
    }

    public void setQueryDataByKeyWord(String keyword, final String type) {
        keyPageNo = 1;
        lists.clear();
        mLoading.show();
        queryBykeyWords(keyword, type, String.valueOf(keyPageNo));
    }

    public void setQueryDataByChainKeyWord(String keyword) {
        lists.clear();
        CuspageNo = 1;
        mLoading.show();
        queryChainCustomerByKeyWords(keyword, currentChainValue, String.valueOf(keyPageNo));
    }

    //关键字查询
    public void setQueryChainByKeyWord(String keyWords) {
        itemCodeQueryList.clear();
        for (String value : chainLists) {
            if (value.toLowerCase().indexOf(keyWords.toLowerCase()) != -1) {
                itemCodeQueryList.add(value);
            }
        }
        myArrayAdapters = new MyArrayAdapters(Application.getInstance().getCurrentActivity(), R.layout.list_item_textview, itemCodeQueryList);
        chainListview.setAdapter(myArrayAdapters);
    }

    //筛选数据
    public void setScreenData(String msg) {
        switch (msg) {
            case KEY_ACCOUNT:
                lists.clear();
                CuspageNo = 1;
                MainActivity.toolbar.setTitle(KEY_ACCOUNT);
                mLoading.show();
                queryCustomer(KEY_ACCOUNT, String.valueOf(CuspageNo));
                break;
            case TARGET_LEADS:
                lists.clear();
                CuspageNo = 1;
                MainActivity.toolbar.setTitle(TARGET_LEADS);
                mLoading.show();
                queryCustomer(TARGET_LEADS, String.valueOf(CuspageNo));
                break;
            case UNTARGET_LEADS:
                lists.clear();
                CuspageNo = 1;
                MainActivity.toolbar.setTitle(UNTARGET_LEADS);
                mLoading.show();
                queryCustomer(UNTARGET_LEADS, String.valueOf(CuspageNo));
                break;
            case OTHER_ACCOUNT:
                lists.clear();
                CuspageNo = 1;
                MainActivity.toolbar.setTitle(OTHER_ACCOUNT);
                mLoading.show();
                queryCustomer(OTHER_ACCOUNT, String.valueOf(CuspageNo));
                break;
        }
    }

    //筛选数据
    public void setChainData(String type) {
        chainListview.setVisibility(VISIBLE);
        listView.setVisibility(GONE);
        switch (type) {
            case "chain":
                searchChainCus = false;
                mLoading.show();
                MainActivity.toolbar.setTitle(getResources().getString(R.string.leads_chain));
                leadsService.getLeadsChain(new ICallback<LinkageResultVO>() {
                    @Override
                    public void onDataReady(LinkageResultVO data) {
                        linksgeVoLists = data.getData();
                        chainLists.clear();
                        mLoading.hide();
                        for (int i = 0; i < linksgeVoLists.size(); i++) {
                            chainLists.add(linksgeVoLists.get(i).getValue());
                        }
                        myArrayAdapters = new MyArrayAdapters(Application.getInstance().getCurrentActivity(), R.layout.list_item_textview, chainLists);
                        chainListview.setAdapter(myArrayAdapters);
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                });
                break;
        }

    }

    private class MyArrayAdapter extends AbstractItemPagerArrayAdapter<CustomerVO, ViewHolder> {
        private OnClickListener onbtnDailyCallClickListener;
        private OnClickListener onItemClickListener;
        private OnClickListener onbtnTaskClickListener;
        private View.OnClickListener onbtnTargetClickListener;
        private View.OnClickListener onbtnUntargetClickListener;
        private String typess;

        public MyArrayAdapter(Context context, int list_item_customer1, final List<CustomerVO> list, int[] itemPageResArray, int i, final String types) {
            super(context, list_item_customer1, list, itemPageResArray, i, types);
            this.typess = types;
            onbtnDailyCallClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), DailyCallEditorActivity.class);
                    Application.getInstance().setDailyCallDetail4Edit(null);
                    intent.putExtra(DailyCallEditorActivity.CUSTOMER_NAME, list.get(position).getAccountName()+list.get(position).getAccountNameEn());
                    intent.putExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL, "true");
                    intent.putExtra(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL_TYPE, types);
                    if ("Target Leads".equals(types)) {
                        typess = "Leads";
                    }
                    intent.putExtra(DailyCallEditorActivity.ASSOC_TYPE, typess);
                    intent.putExtra(DailyCallEditorActivity.CUSTOMER_ROWID, list.get(position).getCustomerRowId());
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    updateViewPagerState(position, 0, true);
                    notifyDataSetChanged();
                }
            };
            onbtnTaskClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), TaskAddActivity.class);
                    intent.putExtra(TaskAddActivity.CUSTOMER_ADD_TASK_NAMEEN, list.get(position).getAccountNameEn());
                    intent.putExtra(TaskAddActivity.CUSTOMER_ADD_TASK_ROWID, list.get(position).getCustomerRowId());
                    intent.putExtra(TaskAddActivity.CUSTOMER_ADD_TASK, "true");
                    Application.getInstance().getCurrentActivity().startActivity(intent);
                    updateViewPagerState(position, 0, true);
                    notifyDataSetChanged();
                }
            };
            onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    if ("Key Account".equalsIgnoreCase(types) || "Other Account".equalsIgnoreCase(types) || "chain".equalsIgnoreCase(types)) {
                        Intent intent = new Intent(Application.getInstance().getBaseContext(), CustomerDetailsActivity.class);
                        intent.putExtra(CustomerDetailsActivity.CUSTOMER_TYPE, types);
                        intent.putExtra(CustomerDetailsActivity.CUSTOMER_CODE, list.get(position).getAccountCode());
                        intent.putExtra(CustomerDetailsActivity.CUSTOMER_ROW_ID, list.get(position).getCustomerRowId());
                        intent.putExtra(CustomerDetailsActivity.CUSTOMER_NAME, list.get(position).getAccountName());
                        Application.getInstance().getCurrentActivity().startActivity(intent);
                    } else {
                        leadsService.queryById(new ICallback<LeadsDetailResultVO>() {
                            @Override
                            public void onDataReady(LeadsDetailResultVO data) {
                                Application.getInstance().setLeadsDetail4Edit(data.getData());
                                Intent intent = new Intent(Application.getInstance().getBaseContext(), LeadsAddActivity.class);
                                intent.putExtra(LeadsAddActivity.CUSTOMER_TARGET_BACK, "true");
                                intent.putExtra(LeadsAddActivity.CUSTOMER_TYPE, types);
                                if ("Target Leads".equals(types)) {
                                    intent.putExtra(LeadsAddActivity.TARGET_OPEN_CLOSE, "YES");
                                }
                                Application.getInstance().getCurrentActivity().startActivityForResult(intent, CustomerContent.RESULT_CODE);
                            }

                            @Override
                            public void onError(Throwable throwable) {

                            }
                        }, list.get(position).getCustomerRowId());

                    }
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
                            lists.clear();
                            CuspageNo = 1;
                            MainActivity.toolbar.setTitle(TARGET_LEADS);
                            mLoading.show();
                            queryCustomer(TARGET_LEADS, String.valueOf(CuspageNo));
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
                    mLoading.show();
                    leadsService.queryById(new ICallback<LeadsDetailResultVO>() {
                        @Override
                        public void onDataReady(LeadsDetailResultVO data) {
                            mLoading.hide();
                            Application.getInstance().setLeadsDetail4Edit(data.getData());
                            Intent intent = new Intent(Application.getInstance().getBaseContext(), LeadsAddActivity.class);
                            intent.putExtra(LeadsAddActivity.TARGET_OPEN_CLOSE, "YES");
                            intent.putExtra(LeadsAddActivity.TARGET_SCROLL, "true");
                            intent.putExtra(LeadsAddActivity.CUSTOMER_TARGET_BACK, "true");
                            intent.putExtra(LeadsAddActivity.CUSTOMER_TYPE, types);
                            Application.getInstance().getCurrentActivity().startActivityForResult(intent, CustomerContent.RESULT_CODE);
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, list.get(position).getCustomerRowId());
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
            viewHolder.btnCompetitor = (Button) rightView.findViewById(R.id.right_competitor);
            viewHolder.btnDailyCall = (Button) rightView.findViewById(R.id.right_dailycall);
            viewHolder.btnDelete = (Button) rightView.findViewById(R.id.right_task);
            viewHolder.btnUntarget = (Button) rightView.findViewById(R.id.right_untarget);
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
            viewHolder.btnDelete.setOnClickListener(onbtnTaskClickListener);
            viewHolder.btnDailyCall.setOnClickListener(onbtnDailyCallClickListener);
            viewHolder.btnUntarget.setOnClickListener(onbtnUntargetClickListener);
            viewHolder.btnCompetitor.setOnClickListener(onbtnTargetClickListener);
            viewHolder.v.setOnClickListener(onItemClickListener);
        }


        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    if (position == 1) {
                        //  return 0.5f;
                        if ("other account".equalsIgnoreCase(typess) || "untarget leads".equalsIgnoreCase(typess)) {
                            return 0.24f;
                        } else {
                            return 0.48f;
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
            viewHolder.v.setTag(position);
            String name = itemData.getAccountName();
            String nameen = itemData.getAccountNameEn();
            int assocTypeImgRes;
            int stateBgRes = R.drawable.bg_oval_y;
            String type = itemData.getCustomerType().toLowerCase();
            if ("target leads".equalsIgnoreCase(type)) {
                assocTypeImgRes = R.drawable.icon_l;
                showOrGone(viewHolder);
                viewHolder.btnUntarget.setVisibility(View.VISIBLE);
                viewHolder.btnDailyCall.setVisibility(View.VISIBLE);
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getChannel() + "   " + itemData.getStage());
                viewHolder.nameEn.setText(itemData.getProvince() + "   " + itemData.getCity());
                viewHolder.leadsRowId.setText(name + nameen);
                viewHolder.leadsSales.setText(itemData.getSales());
            } else if ("untarget leads".equalsIgnoreCase(type)) {
                assocTypeImgRes = R.drawable.icon_l;
                showOrGone(viewHolder);
                viewHolder.btnCompetitor.setVisibility(View.VISIBLE);
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getChannel() + "   " + itemData.getStage());
                viewHolder.nameEn.setText(itemData.getProvince() + "   " + itemData.getCity());
                viewHolder.leadsRowId.setText(name + nameen);
                viewHolder.leadsSales.setText(itemData.getSales());
            } else if ("other account".equalsIgnoreCase(type)) {
                assocTypeImgRes = R.drawable.icon_o;
                showOrGone(viewHolder);
                viewHolder.btnDailyCall.setVisibility(View.VISIBLE);
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getAccountNameEn());
                viewHolder.nameEn.setText(name);
                viewHolder.leadsRowId.setText(itemData.getAccountCode());
                viewHolder.leadsSales.setText(itemData.getSales());
            } else if ("key account".equalsIgnoreCase(type)) {
                assocTypeImgRes = R.drawable.icon_k;
                showOrGone(viewHolder);
                viewHolder.btnDelete.setVisibility(View.VISIBLE);
                viewHolder.btnDailyCall.setVisibility(View.VISIBLE);
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getAccountNameEn());
                viewHolder.nameEn.setText(name);
                viewHolder.leadsRowId.setText(itemData.getAccountCode());
                viewHolder.leadsSales.setText(itemData.getSales());
            } else {
                assocTypeImgRes = R.drawable.icon_k;
                showOrGone(viewHolder);
                viewHolder.btnCompetitor.setVisibility(View.VISIBLE);
                viewHolder.btnDailyCall.setVisibility(View.VISIBLE);
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getAccountNameEn());
                viewHolder.nameEn.setText(name);
                viewHolder.leadsRowId.setText(itemData.getAccountCode());
                viewHolder.leadsSales.setText(itemData.getSales());
            }
            viewHolder.btnDelete.setTag(position);
            viewHolder.btnUntarget.setTag(position);
            viewHolder.btnCompetitor.setTag(position);
            viewHolder.btnDailyCall.setTag(position);
        }

        public void showOrGone(ViewHolder viewHolder) {
            viewHolder.btnUntarget.setVisibility(View.GONE);
            viewHolder.btnDelete.setVisibility(View.GONE);
            viewHolder.btnDailyCall.setVisibility(View.GONE);
            viewHolder.btnCompetitor.setVisibility(View.GONE);
        }
    }

    class ViewHolder {
        View v;
        ImageView assocType;
        TextView name;
        TextView nameEn;
        TextView leadsSales;
        TextView leadsRowId;
        Button btnCompetitor;
        Button btnUntarget;
        Button btnDelete;
        Button btnDailyCall;

    }

    class ViewHolders {
        TextView name;
    }

    class MyArrayAdapters extends AbstractArrayAdapter<ViewHolders, String> {
        private List<String> items;

        public MyArrayAdapters(Context context, int resource, List<String> items) {
            super(context, resource, items);
            this.items = items;
        }

        @Override
        protected ViewHolders createViewHolder() {
            return new ViewHolders();
        }

        @Override
        public String getItem(int position) {
            return items.get(position);
        }

        @Override
        protected void initViewHolder(int position, ViewHolders viewHolder, View rowView) {
            viewHolder.name = (TextView) rowView.findViewById(R.id.list_item__textview);
        }

        @Override
        protected void setItemData(int position, ViewHolders viewHolder, String itemData) {
            viewHolder.name.setText(itemData);
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

    public void updateData(String type) {
        lists.clear();
        CuspageNo = 1;
        MainActivity.toolbar.setTitle(type);
        mLoading.show();
        queryCustomer(type, String.valueOf(CuspageNo));
    }
}
