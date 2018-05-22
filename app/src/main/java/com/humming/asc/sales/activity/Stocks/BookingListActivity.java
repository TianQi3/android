package com.humming.asc.sales.activity.Stocks;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.InventoryBookingVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.ICallback;

import java.util.List;

public class BookingListActivity extends AbstractActivity implements
        ICallback<List<InventoryBookingVO>> {
    private ListView listView;
    private String itemcode;
    private String vintage;
    private View vNoData;
    private LinearLayout heardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_wots_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.activity_inventory_wots_list__list_view);
        heardView = (LinearLayout) findViewById(R.id.activity_inventory_list__heard);
        heardView.setVisibility(View.GONE);

        Bundle b = getIntent().getExtras();
        itemcode = b.getString(Config.ITEM_CODE);
        vintage = b.getString(Config.VINTAGE);
        vNoData = findViewById(R.id.activity__no_data);
        Application.getInventoryService().getBookingList(this, itemcode, vintage);
        setTitle(R.string.title_booking);
    }

    @Override
    public void onDataReady(List<InventoryBookingVO> data) {
        if (data == null || data.size() == 0) {
            vNoData.setVisibility(View.VISIBLE);
        } else {
            vNoData.setVisibility(View.GONE);
        }
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(this,
                R.layout.list_item_booking, data);
        listView.setAdapter(arrayAdapter);

    }

    @Override
    public void onError(Throwable throwable) {

    }

    private class MyArrayAdapter extends ArrayAdapter<InventoryBookingVO> {
        private Context context;
        private int resource;
        private List<InventoryBookingVO> items;

        public MyArrayAdapter(Context context, int resource,
                              List<InventoryBookingVO> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.items = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            ItemHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = ((Activity) context)
                        .getLayoutInflater();
                row = inflater.inflate(resource, parent, false);

                holder = new ItemHolder();
                holder.client = (TextView) row
                        .findViewById(R.id.list_item_booking__client);
                holder.sales = (TextView) row
                        .findViewById(R.id.list_item_booking__sales);
                holder.date = (TextView) row
                        .findViewById(R.id.list_item_booking__date);
                holder.quantity = (TextView) row
                        .findViewById(R.id.list_item_booking__quantity);
                holder.warehouse = (TextView) row.findViewById(R.id.list_item_booking__warehouse);

                row.setTag(holder);
            } else {
                holder = (ItemHolder) row.getTag();
            }

            InventoryBookingVO rowData = items.get(position);
            holder.date.setText(rowData.getDate());
            holder.client.setText(rowData.getAccount());
            holder.sales.setText(rowData.getSales());
            holder.quantity.setText(rowData.getNum());
            holder.warehouse.setText(rowData.getWhouseName());
            return row;
        }

        class ItemHolder {
            TextView date;
            TextView client;
            TextView sales;
            TextView quantity;
            TextView warehouse;
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
