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
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.WOTSDetailResult;
import com.humming.asc.dp.presentation.vo.WOTSDetailVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.ICallback;

import java.util.List;

public class WOTSListActivity extends AbstractActivity implements
        ICallback<WOTSDetailResult> {
    private ListView listView;
    private String itemcode;
    private View vNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_wots_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.activity_inventory_wots_list__list_view);

        Bundle b = getIntent().getExtras();
        itemcode = b.getString(Config.ITEM_CODE);
        //Application.getInventoryService().getWOTSList(this,itemcode);
        Application.getInventoryService().getWOTSListNew(this, itemcode);
        vNoData = findViewById(R.id.activity__no_data);
        setTitle(R.string.title_wots);
    }


    /*@Override
    public void onDataReady(List<InventoryWotsVO> data) {
        if (data == null || data.size() == 0) {
            vNoData.setVisibility(View.VISIBLE);
        } else {
            vNoData.setVisibility(View.GONE);
        }
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(this,
                R.layout.list_item_wots, data);
        listView.setAdapter(arrayAdapter);
    }*/
    public void onDataReady(WOTSDetailResult data) {
        if (data.getData() == null || data.getData().size() == 0) {
            vNoData.setVisibility(View.VISIBLE);
        } else {
            vNoData.setVisibility(View.GONE);
        }
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(this,
                R.layout.list_item_wots, data.getData());
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    private class MyArrayAdapter extends ArrayAdapter<WOTSDetailVO> {
        private Context context;
        private int resource;
        private List<WOTSDetailVO> items;

        public MyArrayAdapter(Context context, int resource, List<WOTSDetailVO> objects) {
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
                holder.date = (TextView) row
                        .findViewById(R.id.list_item_wots__date);
                holder.hkQuantity = (TextView) row
                        .findViewById(R.id.list_item_wots__num_hk);
                holder.mainQuantity = (TextView) row
                        .findViewById(R.id.list_item_wots__num_main);

                row.setTag(holder);
            } else {
                holder = (ItemHolder) row.getTag();
            }

            WOTSDetailVO rowData = items.get(position);
            String date = rowData.getDate();
            if ("0".equals(date)) {
                date = getString(R.string.unknow);
            }
            holder.date.setText(date);
            holder.hkQuantity.setText(new Double(rowData.getHkQty()).intValue() + "");
            holder.mainQuantity.setText(new Double(rowData.getDlQty()).intValue() + "");

            return row;
        }

        class ItemHolder {
            TextView date;
            TextView hkQuantity;
            TextView mainQuantity;
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
