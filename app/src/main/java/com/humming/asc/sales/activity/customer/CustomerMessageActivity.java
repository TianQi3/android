package com.humming.asc.sales.activity.customer;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.AccountInfoVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.CustomerService;
import com.humming.asc.sales.service.ICallback;

/**
 * Created by Zhtq on 16/1/18.
 */
public class CustomerMessageActivity extends AbstractActivity {
    private TextView title, cYtd, cMtd, lYtd, lMtd, lYear, lMonth, bYear, bMonth, status;
    private String customerCode;
    public static final String CUS_CODE = "cus_code";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_message);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.activity_customer_message__title);
        cYtd = (TextView) findViewById(R.id.activity_customer_message__ytd_count);
        cMtd = (TextView) findViewById(R.id.activity_customer_message__mtd_count);
        lYtd = (TextView) findViewById(R.id.activity_customer_message__last_ytd_count);
        lMtd = (TextView) findViewById(R.id.activity_customer_message__last_mtd_count);
        lYear = (TextView) findViewById(R.id.activity_customer_message__full_year);
        lMonth = (TextView) findViewById(R.id.activity_customer_message__full_month);
        bYear = (TextView) findViewById(R.id.activity_customer_message__budget_year);
        bMonth = (TextView) findViewById(R.id.activity_customer_message__budget_month);
        status = (TextView) findViewById(R.id.activity_customer_message__status);
        customerCode = getIntent().getStringExtra(CustomerMessageActivity.CUS_CODE);
        getSupportActionBar().setTitle(customerCode);
        CustomerService service = Application.getCustomerService();
        service.queryCusMessage(new ICallback<AccountInfoVO>() {
            @Override
            public void onDataReady(AccountInfoVO data) {
                title.setText(data.getName_cn());
                cYtd.setText(data.getYtd());
                cMtd.setText(data.getMtd());
                lYtd.setText(data.getYtd_ly_sp());
                lMtd.setText(data.getMtd_ly_sp());
                lYear.setText(data.getYtd_ly());
                lMonth.setText(data.getMtd_ly());
                bYear.setText(data.getBudget_year());
                bMonth.setText(data.getBudget_month());
                status.setText(data.getState());
            }

            @Override
            public void onError(Throwable throwable) {

            }
        },customerCode);
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
