package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.ItemVO;
import com.humming.asc.dp.presentation.vo.cp.task.ItemResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.Taskservice;

import java.util.ArrayList;
import java.util.List;

public class TaskProductItemCodeListActivity extends AbstractActivity {

    private SearchView msearchView;
    private ListView itemCodeListView;
    private MenuItem seacherFilterMenu;
    public static final String STRING_LIST = "string_array_list";
    private Toolbar toolbar;
    public static final String ITEM_CODE = "item_code";
    public static final String ITEM_CODE_NAME = "item_code_name";
    public static final int ITEM_CODE_REQUEST_CODE = 10000;
    private ArrayList<String> itemCodeList;
    private ArrayList<String> itemCodeQueryList;
    private Taskservice taskservice;
    private ItemVO itemVO;
    private String productName;
    private int positions;
    ItemCodeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_product_itemcode);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        itemCodeListView = (ListView) findViewById(R.id.content_task_product_itemcode__listView);
        itemCodeList = new ArrayList<String>();
        itemCodeQueryList = new ArrayList<String>();
        Bundle bound = getIntent().getExtras();
        itemCodeList = bound.getStringArrayList(TaskProductItemCodeListActivity.STRING_LIST);
        adapter = new ItemCodeAdapter(Application.getInstance().getCurrentActivity(),itemCodeList);
        itemCodeListView.setAdapter(adapter);
        taskservice = Application.getTaskservice();
        itemCodeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                positions = position;
                taskservice.getItemCode(new ICallback<ItemResultVO>() {
                    @Override
                    public void onDataReady(ItemResultVO data) {
                        itemVO = data.getData();
                        productName = itemVO.getNameEN()+"  "+itemVO.getNameCN();
                        Bundle resultBundle = new Bundle();
                        resultBundle.putString(
                                TaskProductItemCodeListActivity.ITEM_CODE,
                                adapter.getItem(positions));
                        resultBundle.putString(TaskProductItemCodeListActivity.ITEM_CODE_NAME,productName);
                        Intent resultIntent = new Intent()
                                .putExtras(resultBundle);
                        setResult(
                                TaskProductItemCodeListActivity.ITEM_CODE_REQUEST_CODE,
                                resultIntent);
                        finish();
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }
                },adapter.getItem(positions));

            }
        });
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
                queryListCode("");
                return true;
            }
        });
        msearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                queryListCode(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                queryListCode(newText);
                return false;
            }
        });
        return true;
    }

    //关键字查询
    public void queryListCode(String keyWords){
        itemCodeQueryList.clear();
        for(String value:itemCodeList){
            if(value.toLowerCase().indexOf(keyWords.toLowerCase())!=-1){
                itemCodeQueryList.add(value);
            }
        }
        adapter = new ItemCodeAdapter(Application.getInstance().getCurrentActivity(),itemCodeQueryList);
        itemCodeListView.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class ItemCodeAdapter extends BaseAdapter {

        private List<String> itemCodeList;
        private Context con;

        public ItemCodeAdapter(Context content, List<String> itemCodeList) {
            this.itemCodeList = itemCodeList;
            this.con = content;
        }

        @Override
        public int getCount() {
            return itemCodeList.size();
        }

        @Override
        public String getItem(int position) {
            return itemCodeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(con).inflate(R.layout.list_item_textview, null);
                holder.itemCode = (TextView) convertView.findViewById(R.id.list_item__textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.itemCode.setText(itemCodeList.get(position));
            return convertView;
        }
    }
    class ViewHolder{
        private TextView itemCode;
    }
}
