package com.humming.asc.sales.activity.customer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.AccountOrderVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.CustomerService;
import com.humming.asc.sales.service.ICallback;

import java.util.List;

/**
 * Created by Zhtq on 16/1/18.
 */
public class CustomerOrderActivity extends AbstractActivity {
    private ListView listView;
    private MyArrayAdapter arrayAdapter;
    private String customerCode;
    public static final String CUS_CODE = "cus_code";
    private View vNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order_list);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.activity_client_order__list_view);
        vNoData = findViewById(R.id.activity__no_data);
        customerCode = getIntent().getStringExtra(CustomerOrderActivity.CUS_CODE);
        CustomerService service = Application.getCustomerService();
        mLoading.show();
        service.queryCusOrderList(new ICallback<List<AccountOrderVO>>() {

            @Override
            public void onDataReady(List<AccountOrderVO> data) {
                if (data == null || data.size() == 0) {
                    vNoData.setVisibility(View.VISIBLE);
                } else {
                    vNoData.setVisibility(View.GONE);
                }
                mLoading.hide();
                arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_customer_order, data);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, customerCode);
        final Context context = this.getBaseContext();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(context, CustomerOrderDetailActivity.class);
                AccountOrderVO order = arrayAdapter.getItem(position);
                Bundle b = new Bundle();
                b.putString(CustomerOrderDetailActivity.ORDER_ID, order.getNumber());
                b.putString(CustomerOrderDetailActivity.CUSTOMER_CODE, customerCode);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
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

    private class MyArrayAdapter extends ArrayAdapter<AccountOrderVO> {
        private Context context;
        private int resource;
        private List<AccountOrderVO> items;

        public MyArrayAdapter(Context context, int resource, List<AccountOrderVO> objects) {
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
                holder.number = (TextView) row
                        .findViewById(R.id.list_item_client_order__no);
                holder.date = (TextView) row
                        .findViewById(R.id.list_item_client_order__date);
                holder.total = (TextView) row
                        .findViewById(R.id.list_item_client_order__total);
                holder.state = (TextView) row
                        .findViewById(R.id.list_item_client_order__state);
                holder.type = (TextView) row
                        .findViewById(R.id.list_item_client_order__type);

                row.setTag(holder);
            } else {
                holder = (ItemHolder) row.getTag();
            }

            AccountOrderVO rowData = items.get(position);
            holder.number.setText(rowData.getNumber());
            holder.date.setText(rowData.getDate());
            holder.total.setText(rowData.getTotal());
            holder.state.setText(rowData.getState());
            holder.type.setText(rowData.getType());

            return row;
        }

        class ItemHolder {
            TextView number;
            TextView date;
            TextView total;
            TextView state;
            TextView type;
        }

    }
}
