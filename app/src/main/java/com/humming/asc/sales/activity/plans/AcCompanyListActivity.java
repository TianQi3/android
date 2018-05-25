package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.cp.DCSummaryInfoVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.QueryAllEmployeeResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.QueryAllEmployeeVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.AbstractArrayAdapter;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 16/1/27.
 */
public class AcCompanyListActivity extends AbstractActivity {
    private ListView listView;
    private SearchView msearchView;
    public static final int ACTIVITY_ACCOMPANY_CODE = 13011;
    public static String ACTIVITY_ACCOMPANY = "daily_call_accompany";
    public static String ACTIVITY_ACCOMPANY_ID = "daily_call_accompany_id";
    public static final int ACTIVITY_DAILY_CALL_ACCOMPANY_SELECT = 10111;
    private MenuItem seacherFilterMenu;


    private List<QueryAllEmployeeVO> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_status_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(Application.getInstance().getString(R.string.activity_label_accompany));
        listView = (ListView) findViewById(R.id.content_class_status_list);
        DailyCallService dailyCallservice = Application.getDailyCallService();
        mLoading.show();
        dailyCallservice.queryAllEmployee(new ICallback<QueryAllEmployeeResultVO>() {

            @Override
            public void onDataReady(QueryAllEmployeeResultVO data) {
                lists = data.getData();
                mLoading.hide();
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(), R.layout.list_item_textview, lists);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle resultBundle = new Bundle();
                resultBundle.putString(
                        AcCompanyListActivity.ACTIVITY_ACCOMPANY, lists.get(position).getShowName());
                resultBundle.putString(
                        AcCompanyListActivity.ACTIVITY_ACCOMPANY_ID, lists.get(position).getRowId());
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        ACTIVITY_DAILY_CALL_ACCOMPANY_SELECT,
                        resultIntent);
                finish();
            }
        });
    }

    class ViewHolder {
        TextView name;
    }

    class MyArrayAdapter extends AbstractArrayAdapter<ViewHolder, QueryAllEmployeeVO> {

        public MyArrayAdapter(Context context, int resource, List<QueryAllEmployeeVO> items) {
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
        protected void setItemData(int position, ViewHolder viewHolder, QueryAllEmployeeVO itemData) {
            viewHolder.name.setText(itemData.getShowName());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        seacherFilterMenu = menu.findItem(R.id.action_search);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        DCSummaryInfoVO info;
        switch (id) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //关键字查询
    private void setQueryDataByKeyWord(String query) {
        List<QueryAllEmployeeVO> queryLists = new ArrayList<>();
        queryLists.clear();
        for (QueryAllEmployeeVO queryAllEmployeeVO : lists) {
            if (queryAllEmployeeVO.getShowName().contains(query)) {
                Log.v("xxxx", queryAllEmployeeVO.getShowName());
                queryLists.add(queryAllEmployeeVO);
            }
        }
        lists = queryLists;
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(), R.layout.list_item_textview, lists);
        listView.setAdapter(arrayAdapter);
    }
}
