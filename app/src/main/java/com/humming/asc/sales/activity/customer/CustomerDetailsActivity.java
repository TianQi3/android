package com.humming.asc.sales.activity.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.component.DetailsItem;

/**
 * Created by Zhtq on 16/1/18.
 */
public class CustomerDetailsActivity extends AbstractActivity {
    public static final String CUSTOMER_TYPE = "customer_type";
    private String type = "Key Account";
    private String cus_code = "";
    private String row_id = "";
    private String cus_name = "";
    public static final String CUSTOMER_CODE = "customer_code";
    public static final String CUSTOMER_ROW_ID = "row_ids";
    public static final String CUSTOMER_NAME = "cus_names";
    private DetailsItem cusMessage, cusAr, cusOrder, cusCg, cusTask, cusDailyCall, cusContact, keyDailyCall, mfRequest, nullItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.title_activity_customer_detials));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        cus_code = getIntent().getStringExtra(CustomerDetailsActivity.CUSTOMER_CODE);
        row_id = getIntent().getStringExtra(CustomerDetailsActivity.CUSTOMER_ROW_ID);
        cus_name = getIntent().getStringExtra(CustomerDetailsActivity.CUSTOMER_NAME);
        type = getIntent().getStringExtra(CustomerDetailsActivity.CUSTOMER_TYPE);
        cusMessage = (DetailsItem) findViewById(R.id.content_customer_details__customer_message);
        cusAr = (DetailsItem) findViewById(R.id.content_customer_details__ar);
        cusOrder = (DetailsItem) findViewById(R.id.content_customer_details__customer_order);
        cusCg = (DetailsItem) findViewById(R.id.content_customer_details__gc);
        cusTask = (DetailsItem) findViewById(R.id.content_customer_details__task);
        cusDailyCall = (DetailsItem) findViewById(R.id.content_customer_details__daily_call);
        cusContact = (DetailsItem) findViewById(R.id.content_customer_details__contact);
        keyDailyCall = (DetailsItem) findViewById(R.id.content_customer_details__keyaccount_daily_call);
        nullItem = (DetailsItem) findViewById(R.id.content_customer_details__null);
        mfRequest = findViewById(R.id.content_customer_details__mf_request);
        mfRequest.setOnClickListener(itemClickListener);
        cusMessage.setOnClickListener(itemClickListener);
        keyDailyCall.setOnClickListener(itemClickListener);
        cusAr.setOnClickListener(itemClickListener);
        cusOrder.setOnClickListener(itemClickListener);
        cusTask.setOnClickListener(itemClickListener);
        cusDailyCall.setOnClickListener(itemClickListener);
        cusCg.setOnClickListener(itemClickListener);
        cusContact.setOnClickListener(itemClickListener);
        if ("Key Account".equalsIgnoreCase(type)) {
            cusDailyCall.setVisibility(View.GONE);
            cusTask.setVisibility(View.VISIBLE);
            keyDailyCall.setVisibility(View.VISIBLE);
            nullItem.setVisibility(View.GONE);
        } else {
            cusDailyCall.setVisibility(View.VISIBLE);
            cusTask.setVisibility(View.GONE);
            keyDailyCall.setVisibility(View.GONE);
            nullItem.setVisibility(View.VISIBLE);
        }
    }

    View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onItemClick(v.getId());
        }
    };

    private void onItemClick(int id) {
        switch (id) {
            case R.id.content_customer_details__customer_message:
                Intent intent = new Intent(this, CustomerMessageActivity.class);
                intent.putExtra(CustomerMessageActivity.CUS_CODE, cus_code);
                startActivity(intent);
                break;
            case R.id.content_customer_details__ar:
                Intent intent1 = new Intent(this, CustomerARActivity.class);
                intent1.putExtra(CustomerARActivity.CUS_CODE, cus_code);
                startActivity(intent1);
                break;
            case R.id.content_customer_details__customer_order:
                Intent intentOrder = new Intent(this, CustomerOrderActivity.class);
                intentOrder.putExtra(CustomerOrderActivity.CUS_CODE, cus_code);
                startActivity(intentOrder);
                break;
            case R.id.content_customer_details__gc:
                Intent intent2 = new Intent(this, CustomerCGActivity.class);
                intent2.putExtra(CustomerCGActivity.CUS_CODE, cus_code);
                startActivity(intent2);
                break;
            case R.id.content_customer_details__task:
                Intent intent3 = new Intent(this, CustomerTaskListActivity.class);
                intent3.putExtra(CustomerTaskListActivity.ROW_ID, row_id);
                intent3.putExtra(CustomerTaskListActivity.CUS_NAME, cus_name);
                startActivity(intent3);
                break;
            case R.id.content_customer_details__daily_call:
                Intent intent4 = new Intent(this, CustomerDailyCallListActivity.class);
                intent4.putExtra(CustomerDailyCallListActivity.CUS_NAME, cus_name);
                intent4.putExtra(CustomerDailyCallListActivity.ROW_ID, row_id);
                intent4.putExtra(CustomerDailyCallListActivity.OTHER_OR_KEY, "false");
                startActivity(intent4);
                break;
            case R.id.content_customer_details__contact:
                Intent intentContact = new Intent(this, CustomerContactsInfoActivity.class);
                intentContact.putExtra(CustomerContactsInfoActivity.CUS_CODE, cus_code);
                startActivity(intentContact);
                break;
            case R.id.content_customer_details__keyaccount_daily_call:
                Intent intentKeyDailyCall = new Intent(this, CustomerDailyCallListActivity.class);
                intentKeyDailyCall.putExtra(CustomerDailyCallListActivity.CUS_NAME, cus_name);
                intentKeyDailyCall.putExtra(CustomerDailyCallListActivity.ROW_ID, row_id);
                intentKeyDailyCall.putExtra(CustomerDailyCallListActivity.OTHER_OR_KEY, "true");
                startActivity(intentKeyDailyCall);
                break;
            case R.id.content_customer_details__mf_request:
                Intent intent5 = new Intent(this, CustomerMFListActivity.class);
                intent5.putExtra("cus_code", cus_code);
                intent5.putExtra("cusrowId", row_id);
                startActivity(intent5);
                break;
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
}
