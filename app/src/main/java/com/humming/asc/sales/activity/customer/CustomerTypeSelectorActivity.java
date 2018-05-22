package com.humming.asc.sales.activity.customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.plans.DailyCallEditorActivity;

public class CustomerTypeSelectorActivity extends AbstractActivity {

    private TextView keyAccount, otherAccount, leads;
    public static int judge = 0;
    public static final int ACTIVITY_CUSTOMER_SELECTOR_RESULT = 20008;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_type_select);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        keyAccount = (TextView) findViewById(R.id.activity_customer_type_select__keyaccount);
        otherAccount = (TextView) findViewById(R.id.activity_customer_type_select__otheraccount);
        leads = (TextView) findViewById(R.id.activity_customer_type_select__leads);
        keyAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge = 1;
                Intent intent = new Intent(getBaseContext(), CustomerSelectorActivity.class);
                intent.putExtra(CustomerSelectorActivity.TYPE, CustomerSelectorActivity.KEY_ACCOUNT);
                startActivityForResult(intent, CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT);
            }
        });
        otherAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge = 2;
                Intent intent = new Intent(getBaseContext(), CustomerSelectorActivity.class);
                intent.putExtra(CustomerSelectorActivity.TYPE, CustomerSelectorActivity.OTHER_ACCOUNT);
                startActivityForResult(intent, CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT);
            }
        });
        leads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                judge = 3;
                Intent intent = new Intent(getBaseContext(), CustomerSelectorActivity.class);
                intent.putExtra(CustomerSelectorActivity.TYPE, CustomerSelectorActivity.TARGET_LEADS);
                startActivityForResult(intent, CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        // getMenuInflater().inflate(R.menu.menu_customer_selector, menu);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        Bundle resultBundle;
        switch (requestCode) {
            case CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT:
                resultBundle = data.getExtras();
                String nameEn = resultBundle
                        .getString(CustomerSelectorActivity.KEY_NAME_EN);
                String id = resultBundle.getString(CustomerSelectorActivity.KEY_ID);
                String assoc_type = resultBundle.getString(CustomerSelectorActivity.ASSOC_TYPE);
                String type = resultBundle.getString(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL_TYPE);
                //发送
                Bundle resultBundles = new Bundle();
                resultBundles.putString(
                        CustomerSelectorActivity.KEY_NAME_EN,
                        nameEn);
                resultBundles.putString(
                        CustomerSelectorActivity.KEY_ID,
                        id);
                resultBundles.putString(
                        CustomerSelectorActivity.ASSOC_TYPE,
                        assoc_type);
                resultBundles.putString(DailyCallEditorActivity.CUSTOMER_ADD_DAILY_CALL_TYPE, type);
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        CustomerSelectorActivity.ACTIVITY_CUSTOMER_SELECTOR_RESULT,
                        resultIntent);
                finish();
                break;
        }
    }
}
