package com.humming.asc.sales.activity.customer;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.vo.AccountAddressVO;
import com.humming.asc.dp.presentation.vo.AccountContactVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.ICallback;

import java.util.List;

public class CustomerContactsInfoActivity extends AbstractActivity {
    private ListView addresslistView, infoListView;
    public static final String CUS_CODE = "cus_id";
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 3;
    private String number = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_contact);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        addresslistView = (ListView) findViewById(R.id.activity_contact__list_view);
        infoListView = (ListView) findViewById(R.id.activity_contact__infolist_view);
        Bundle bound = getIntent().getExtras();
        String customercode = bound.getString(CustomerContactsInfoActivity.CUS_CODE);
        Application.getCustomerService().queryCusContactDetail(new ICallback<List<AccountContactVO>>() {
            @Override
            public void onDataReady(List<AccountContactVO> data) {
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_customer_contacts, data);
                infoListView.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, customercode);
        Application.getCustomerService().queryCusAddressDetail(new ICallback<List<AccountAddressVO>>() {

            @Override
            public void onDataReady(List<AccountAddressVO> data) {
                AddressArrayAdapter arrayAdapter = new AddressArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_customer_address, data);
                addresslistView.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, customercode);
    }

    private class MyArrayAdapter extends ArrayAdapter<AccountContactVO> {
        private Context context;
        private int resource;
        private List<AccountContactVO> items;

        public MyArrayAdapter(Context context, int resource,
                              List<AccountContactVO> objects) {
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
                holder.name = (TextView) row
                        .findViewById(R.id.list_item_client_contacts__name);
                holder.title = (TextView) row
                        .findViewById(R.id.list_item_client_contacts__type);
                holder.phone = (TextView) row
                        .findViewById(R.id.list_item_client_contacts__phone);
                holder.callPhone = (ImageView) row.findViewById(R.id.list_item_client_contact_call);

                row.setTag(holder);
            } else {
                holder = (ItemHolder) row.getTag();
            }

            final AccountContactVO rowData = items.get(position);
            holder.name.setText(rowData.getName());
            holder.title.setText(rowData.getType());
            holder.phone.setText(rowData.getPhone());
            if ("".equals(rowData.getPhone())) {
                holder.callPhone.setVisibility(View.GONE);
            }
            final ItemHolder finalHolder = holder;
            holder.callPhone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    number = finalHolder.phone.getText().toString();
                    if (!"".equals(number)) {
                        TelephonyManager mTelephonyManager = (TelephonyManager) context.getSystemService(Service.TELEPHONY_SERVICE);
                        int absent = mTelephonyManager.getSimState();
                        if (absent == TelephonyManager.SIM_STATE_ABSENT) {
                            Toast.makeText(Application.getInstance().getCurrentActivity(), "请确认sim卡是否插入或者sim卡暂时不可用！",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            if (ContextCompat.checkSelfPermission(CustomerContactsInfoActivity.this,
                                    Manifest.permission.CALL_PHONE)
                                    != PackageManager.PERMISSION_GRANTED) {

                                ActivityCompat.requestPermissions(CustomerContactsInfoActivity.this,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
                            } else {
                                Intent phoneIntent = new Intent(
                                        "android.intent.action.CALL", Uri.parse("tel:"
                                        + number));
                                startActivity(phoneIntent);
                            }
                        }
                    } else {
                        Toast.makeText(Application.getInstance().getCurrentActivity(), "电话为空",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });
            return row;
        }

        class ItemHolder {
            TextView name;
            TextView title;
            TextView phone;
            ImageView callPhone;
        }

    }

    private class AddressArrayAdapter extends ArrayAdapter<AccountAddressVO> {
        private Context context;
        private int resource;
        private List<AccountAddressVO> items;

        public AddressArrayAdapter(Context context, int resource,
                                   List<AccountAddressVO> objects) {
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
                holder.address = (TextView) row
                        .findViewById(R.id.list_item_client_address__address);

                row.setTag(holder);
            } else {
                holder = (ItemHolder) row.getTag();
            }

            AccountAddressVO rowData = items.get(position);
            holder.address.setText(rowData.getAddress());

            return row;
        }

        class ItemHolder {
            TextView address;
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent phoneIntent = new Intent(
                        "android.intent.action.CALL", Uri.parse("tel:"
                        + number));
                startActivity(phoneIntent);
            } else {
                // Permission Denied
                //  Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
