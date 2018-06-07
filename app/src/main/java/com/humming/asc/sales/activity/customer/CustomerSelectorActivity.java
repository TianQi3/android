package com.humming.asc.sales.activity.customer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.ILoadingLayout;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.humming.asc.dp.presentation.vo.cp.CustomerVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.plans.DailyCallEditorActivity;
import com.humming.asc.sales.service.CustomerService;
import com.humming.asc.sales.service.ICallback;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class CustomerSelectorActivity extends AbstractActivity {
    public static final int ACTIVITY_CUSTOMER_SELECTOR_RESULT = 20001;
    public static final String TYPE = "type";
    public static final String KEY_NAME_EN = "name_end";
    public static final String KEY_ID = "id";
    public static final String ASSOC_TYPE = "Assoc Type";
    public static final String KEY_ACCOUNT = "Key Account";//
    public static final String ALL = "all";//
    public static final String OTHER_ACCOUNT = "Other Account";//
    public final static String LEADS = "Leads";//
    public final static String TARGET_LEADS = "Target Leads";//
    private PullToRefreshListView listView;
    private ArrayList<CustomerVO> currentlist;
    private ArrayList<CustomerVO> lists;
    CustomerService customerService;
    private MenuItem seacherFilterMenu;
    private SearchView msearchView;
    private int[] itemPageResArray;
    private int CuspageNo = 1, keyPageNo = 1;
    private String flag = KEY_ACCOUNT;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_selector);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (PullToRefreshListView) findViewById(R.id.content_customer__listView);
        itemPageResArray = new int[]{R.layout.list_item_customer, R.layout.list_item_view_page_right};
        init();
        lists = new ArrayList<CustomerVO>();
        customerService = Application.getCustomerService();
        mLoading.show();
        CuspageNo = 1;
        flag = getIntent().getStringExtra(CustomerSelectorActivity.TYPE);
        if (ALL.equalsIgnoreCase(flag)) {
            queryCustomer(null, String.valueOf(CuspageNo));
        } else {
            queryCustomer(flag, String.valueOf(CuspageNo));
        }

    }

    //查询方法
    public void queryCustomer(final String flag, final String pageNos) {
        customerService.query(new ICallback<List<CustomerVO>>() {

            @Override
            public void onDataReady(List<CustomerVO> data) {
                currentlist = (ArrayList<CustomerVO>) data;
                int scorllCurrentItem =0;
                mLoading.hide();
                if ("1".equals(pageNos)) {
                    lists = currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if(currentSize>=0){
                        scorllCurrentItem = lists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        lists.add(currentlist.get(i));
                    }
                }
                MyArrayAdapters arrayAdapter = new MyArrayAdapters(Application.getInstance().getCurrentActivity(),
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

    public void queryBykeyWords(String keyWords, final String flag, final String pageNo) {
        customerService.queryByKeyWord(new ICallback<List<CustomerVO>>() {
            @Override
            public void onDataReady(List<CustomerVO> data) {
                currentlist = (ArrayList<CustomerVO>) data;
                mLoading.hide();
                int scorllCurrentItem =0;
                if ("1".equals(pageNo)) {
                    lists = currentlist;
                } else {
                    int currentSize = currentlist.size();
                    if(currentSize>=0){
                        scorllCurrentItem = lists.size();
                    }
                    for (int i = 0; i < currentSize; i++) {
                        lists.add(currentlist.get(i));
                    }
                }
                MyArrayAdapters arrayAdapter = new MyArrayAdapters(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_view_pager_delete, lists, itemPageResArray, 0, flag);
                listView.setAdapter(arrayAdapter);
                listView.getRefreshableView().setSelection(scorllCurrentItem);
                listView.onRefreshComplete();
                listView.setMode(PullToRefreshBase.Mode.PULL_FROM_END);
                listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
                    @Override
                    public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                        keyPageNo++;
                        queryCustomer(flag, keyPageNo + "");
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, keyWords, flag, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_customer_selector, menu);
        seacherFilterMenu = menu.findItem(R.id.action_customer_search);
        msearchView = (SearchView) MenuItemCompat.getActionView(seacherFilterMenu);
        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyPageNo = 1;
                lists.clear();
                mLoading.show();
                queryBykeyWords(query, flag, String.valueOf(keyPageNo));
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
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

    private class MyArrayAdapters extends AbstractItemPagerArrayAdapter<CustomerVO, ViewHolder> {
        private String types;
        private View.OnClickListener onItemClickListener;

        public MyArrayAdapters(Context context, int list_item_customer1, final List<CustomerVO> list, int[] itemPageResArray, int i, final String types) {
            super(context, list_item_customer1, list, itemPageResArray, i, types);
            this.types = types;
            onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    CustomerVO customer = lists.get(position);
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(
                            CustomerSelectorActivity.KEY_NAME_EN,
                            customer.getAccountNameEn());
                    resultBundle.putString(
                            CustomerSelectorActivity.KEY_ID,
                            customer.getCustomerRowId());
                    resultBundle.putString(
                            CustomerSelectorActivity.ASSOC_TYPE,
                            customer.getCustomerType());
                    resultBundle.putString(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL_TYPE, flag);
                    Intent resultIntent = new Intent()
                            .putExtras(resultBundle);
                    setResult(
                            CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT,
                            resultIntent);
                    finish();
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
            viewHolder.v.setOnClickListener(onItemClickListener);
        }

        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    if (position == 1) {
                        return 0f;
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
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getChannel() + "   " + itemData.getStage());
                viewHolder.nameEn.setText(itemData.getProvince() + "   " + itemData.getCity());
                viewHolder.leadsRowId.setText(name + nameen);
                viewHolder.leadsSales.setText(itemData.getSales());
            } else if ("untarget leads".equalsIgnoreCase(type)) {
                assocTypeImgRes = R.drawable.icon_l;
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getChannel() + "   " + itemData.getStage());
                viewHolder.nameEn.setText(itemData.getProvince() + "   " + itemData.getCity());
                viewHolder.leadsRowId.setText(name + nameen);
                viewHolder.leadsSales.setText(itemData.getSales());

            } else if ("other account".equalsIgnoreCase(type)) {
                assocTypeImgRes = R.drawable.icon_o;
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getAccountNameEn());
                viewHolder.nameEn.setText(name);
                viewHolder.leadsRowId.setText(itemData.getAccountCode());
                viewHolder.leadsSales.setText(itemData.getSales());
            } else if ("key account".equalsIgnoreCase(type)) {
                assocTypeImgRes = R.drawable.icon_k;
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getAccountNameEn());
                viewHolder.nameEn.setText(name);
                viewHolder.leadsRowId.setText(itemData.getAccountCode());
                viewHolder.leadsSales.setText(itemData.getSales());
            } else {
                assocTypeImgRes = R.drawable.icon_k;
                viewHolder.assocType.setImageResource(assocTypeImgRes);
                viewHolder.name.setText(itemData.getAccountNameEn());
                viewHolder.nameEn.setText(name);
                viewHolder.leadsRowId.setText(itemData.getAccountCode());
                viewHolder.leadsSales.setText(itemData.getSales());
            }
        }
    }

    class ViewHolder {
        View v;
        ImageView assocType;
        TextView name;
        TextView nameEn;
        TextView leadsSales;
        TextView leadsRowId;

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
