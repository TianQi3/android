package com.humming.asc.sales.activity.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.AccountARDetailVO;
import com.humming.asc.dp.presentation.vo.AccountARVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.service.CustomerService;
import com.humming.asc.sales.service.ICallback;

import java.util.List;

/**
 * Created by Zhtq on 16/1/18.
 */
public class CustomerARActivity extends AbstractActivity {
    private TextView credit, pt, total;
    private String customerCode;
    private ListView arListview;
    public static final String CUS_CODE = "cus_code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_ar);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        credit = (TextView) findViewById(R.id.content_customer_ar_1);
        pt = (TextView) findViewById(R.id.content_customer_ar_2);
        total = (TextView) findViewById(R.id.content_customer_ar_3);
        arListview = (ListView) findViewById(R.id.content_customer_ar__listView);
        customerCode = getIntent().getStringExtra(CustomerARActivity.CUS_CODE);
        CustomerService service = Application.getCustomerService();
        service.queryCusAr(new ICallback<AccountARVO>() {
            @Override
            public void onDataReady(AccountARVO data) {
                credit.setText(data.getCredit().toString());
                pt.setText(data.getPt().toString());
                total.setText(data.getTotal().toString());
                List<AccountARDetailVO> list = data.getArlist();
                ArListAdapter arListAdapter = new ArListAdapter(Application.getInstance().getBaseContext(), list);
                arListview.setAdapter(arListAdapter);
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

    class ArListAdapter extends BaseAdapter {

        private Context context;
        private List<AccountARDetailVO> dates;

        public ArListAdapter(Context context, List<AccountARDetailVO> datas) {
            this.context = context;
            this.dates = datas;
        }

        @Override
        public int getCount() {
            return dates.size();
        }

        @Override
        public Object getItem(int position) {
            return dates.get(position);
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
                convertView = LayoutInflater.from(context).inflate(R.layout.list_textview, null);
                holder.name = (TextView) convertView.findViewById(R.id.list__textview);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setBackgroundResource(R.color.colorAccents);
            holder.name.setTextColor(getResources().getColor(R.color.WhiteBG));
            holder.name.setText(dates.get(position).getCompany()+"  " + dates.get(position).getType()+":"+dates.get(position).getAmt());

            return convertView;
        }
    }

    class ViewHolder {
        TextView name;
    }
}
