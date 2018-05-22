package com.humming.asc.sales.activity.customer;

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

import com.humming.asc.dp.presentation.vo.AccountCGOrderVO;
import com.humming.asc.dp.presentation.vo.AccountCGVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.CustomerService;
import com.humming.asc.sales.service.ICallback;

import java.util.List;

/**
 * Created by Zhtq on 16/1/18.
 */
public class CustomerCGActivity extends AbstractActivity {
    private ListView listView;
    private TextView tvCustomerName;
    private TextView tvTotalQuantity;
    private TextView tvCredit;
    private String customerCode;
    private List<AccountCGOrderVO> lists;
    public static final String CUS_CODE = "cus_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_cg);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        customerCode = getIntent().getStringExtra(CustomerCGActivity.CUS_CODE);
        tvCustomerName = (TextView) findViewById(R.id.activity_client_cg__customer_name);
        tvTotalQuantity = (TextView) findViewById(R.id.activity_client_cg__total_quantity);
        tvCredit = (TextView) findViewById(R.id.activity_client_cg__credit);

        listView = (ListView) findViewById(R.id.activity_client_cg__list_view);
        CustomerService service = Application.getCustomerService();
        service.queryCusCg(new ICallback<AccountCGVO>() {
            @Override
            public void onDataReady(AccountCGVO data) {
                lists = data.getOrders();
                tvCustomerName.setText(data.getCustomerName());
                tvTotalQuantity.setText(data.getQty());
                tvCredit.setText(data.getCredit());

                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_customer_cg, lists);
                listView.setAdapter(arrayAdapter);

            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, customerCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // getMenuInflater().inflate(R.menu.menu_task_add, menu);
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

    private class MyArrayAdapter extends ArrayAdapter<AccountCGOrderVO> {
        private Context context;
        private int resource;
        private List<AccountCGOrderVO> items;

        public MyArrayAdapter(Context context, int resource,
                              List<AccountCGOrderVO> objects) {
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
                holder.itemCode = (TextView) row
                        .findViewById(R.id.list_item_client_cg__code);

                holder.vintage = (TextView) row
                        .findViewById(R.id.list_item_client_cg__vintage);
                holder.date = (TextView) row
                        .findViewById(R.id.list_item_client_cg__date);
                holder.qty = (TextView) row
                        .findViewById(R.id.list_item_client_cg__qty);
                row.setTag(holder);
            } else {
                holder = (ItemHolder) row.getTag();
            }

            AccountCGOrderVO rowData = items.get(position);
            holder.itemCode.setText(rowData.getItemcode());
            holder.vintage.setText(getResources().getString(R.string.cg_year)+rowData.getVintage());
            holder.date.setText(getResources().getString(R.string.list_item_wots__date)+rowData.getDate());
            holder.qty.setText(getResources().getString(R.string.activity_inventory_list__label_inventory)+rowData.getQty());
            return row;
        }

        class ItemHolder {
            TextView qty;
            TextView itemCode;
            TextView vintage;
            TextView date;
        }

    }
}
