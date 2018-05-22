package com.humming.asc.sales.activity.Stocks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.InventoryCity;
import com.humming.asc.dp.presentation.vo.InventoryDetail;
import com.humming.asc.dp.presentation.vo.InventoryVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.ICallback;

import java.util.ArrayList;
import java.util.List;

public class InventoryDetailActivity extends AbstractActivity implements
        ICallback<InventoryVO> {
    private ExpandableListView listView;
    private Button btnWOTS;
    private Button btnBooking;
    private String itemcode;
    private String vintage;
    private List<InventoryCity> citys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_inventory_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        btnWOTS = (Button) findViewById(R.id.activity_inventory_detail__button_wots);
        btnBooking = (Button) findViewById(R.id.activity_inventory_detail__button_booking);
        listView = (ExpandableListView) findViewById(R.id.activity_inventory_detail__list_view);

        btnWOTS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), WOTSListActivity.class);
                Bundle b = new Bundle();
                b.putString(Config.ITEM_CODE, itemcode);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        btnBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), BookingListActivity.class);
                Bundle b = new Bundle();
                b.putString(Config.ITEM_CODE, itemcode);
                b.putString(Config.VINTAGE, vintage);
                intent.putExtras(b);
                startActivity(intent);
            }
        });

        Bundle b = getIntent().getExtras();
        itemcode = b.getString(Config.ITEM_CODE);
        vintage = b.getString(Config.VINTAGE);
        setTitle(itemcode);
        mLoading.show();
        Application.getInventoryService().getInventoryDetail(this, itemcode, vintage);
    }

    @Override
    public void onDataReady(InventoryVO data) {
        btnWOTS.setText(data.getWots());
        mLoading.hide();
        btnBooking.setText(data.getBooking());
        citys = data.getCitys();
        citys = data.getCitys();
        List<InventoryCity> list = new ArrayList<InventoryCity>();
        if (citys != null) {
            List<InventoryDetail> details = null;
            for (InventoryCity city : citys) {
                details = city.getDetails();
                if (details != null && details.size() > 0) {
                    list.add(city);
                }
            }
        }
        MyExpandableListAdapter adapter = new MyExpandableListAdapter(this,
                list);
        listView.setAdapter(adapter);
        if (list != null) {
            int size = list.size();
            for (int i = 0; i < size; i++) {
                listView.expandGroup(i);
            }
        }

    }

    @Override
    public void onError(Throwable throwable) {

    }

    public class MyExpandableListAdapter extends BaseExpandableListAdapter {

        private final List<InventoryCity> groups;
        public LayoutInflater inflater;
        public Activity activity;
        private String right = "";
        private String down = "";

        public MyExpandableListAdapter(Activity act, List<InventoryCity> groups) {
            activity = act;
            this.groups = groups;
            inflater = act.getLayoutInflater();
            right = act.getResources().getString(R.string.icon_char_right);
            down = act.getResources().getString(R.string.icon_char_down);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return groups.get(groupPosition).getDetails().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public View getChildView(int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final InventoryDetail cityDetail = (InventoryDetail) getChild(groupPosition,
                    childPosition);
            View row = convertView;
            CityDetailHolder holder = null;
            if (row == null) {

                row = inflater.inflate(
                        R.layout.list_item_inventory_detail_group_item, parent,
                        false);

                holder = new CityDetailHolder();
                holder.warehouse = (TextView) row
                        .findViewById(R.id.list_item_inventory_detail__warehouse);
                holder.quantity = (TextView) row
                        .findViewById(R.id.list_item_inventory_detail__quantity);
                row.setTag(holder);
            } else {
                holder = (CityDetailHolder) row.getTag();
            }

            holder.warehouse.setText(cityDetail.getName());
            holder.quantity.setText(cityDetail.getNum());

            // convertView.setOnClickListener(new OnClickListener() {
            // @Override
            // public void onClick(View v) {
            // Toast.makeText(activity, children, Toast.LENGTH_SHORT)
            // .show();
            // }
            // });
            return row;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return groups.get(groupPosition).getDetails().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return groups.size();
        }

        @Override
        public void onGroupCollapsed(int groupPosition) {
            super.onGroupCollapsed(groupPosition);
        }

        @Override
        public void onGroupExpanded(int groupPosition) {
            super.onGroupExpanded(groupPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            View row = convertView;
            CityHolder holder;
            if (row == null) {
                row = inflater.inflate(
                        R.layout.list_item_inventory_detail_group_header, null);
                holder = new CityHolder();
                holder.text = (TextView) row
                        .findViewById(R.id.list_item_inventory_detail_group_header__text);
                holder.icon = (TextView) row
                        .findViewById(R.id.list_item_inventory_detail_group_header__icon);
                row.setTag(holder);
            } else {
                holder = (CityHolder) row.getTag();
            }
            InventoryCity city = (InventoryCity) getGroup(groupPosition);
            holder.text.setText(city.getCity());

            // holder.icon.setText(isExpanded ? down : right);

            return row;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        class CityDetailHolder {
            TextView warehouse;
            TextView quantity;
        }

        class CityHolder {
            TextView text;
            TextView icon;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
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
}