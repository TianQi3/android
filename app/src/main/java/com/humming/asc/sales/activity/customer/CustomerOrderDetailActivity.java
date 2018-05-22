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
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.AccountOrderDetailVO;
import com.humming.asc.dp.presentation.vo.AccountOrderDetalList;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.Stocks.ProductDetailActivity;
import com.humming.asc.sales.service.ICallback;

import java.util.List;


public class CustomerOrderDetailActivity extends AbstractActivity {
	private TextView tvTotal;
	private TextView tvAddress;
	private TextView tvNeedWJD;
	private TextView tvNeedInvoice;
	private TextView tvRemark;
	private ListView listView;
	private MyArrayAdapter arrayAdapter;
	public static final String CUSTOMER_CODE = "customercode";
	public static final String ORDER_ID = "orderid";
	private List<AccountOrderDetalList> orderlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_customer_order_detail);
		final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		tvTotal = (TextView) findViewById(R.id.activity_client_order_detail__total);
		tvNeedInvoice = (TextView) findViewById(R.id.activity_client_order_detail__need_invoice);
		tvNeedWJD = (TextView) findViewById(R.id.activity_client_order_detail__need_wjd);
		
		tvAddress = (TextView) findViewById(R.id.activity_client_order_detail__address);
		tvRemark = (TextView) findViewById(R.id.activity_client_order_detail__remark);

		listView = (ListView) findViewById(R.id.activity_client_order_detail__list_view);

		Bundle bound = getIntent().getExtras();
		String customercode = bound.getString(CustomerOrderDetailActivity.CUSTOMER_CODE);
		String orderid = bound.getString(CustomerOrderDetailActivity.ORDER_ID);
		setTitle(getString(R.string.title_order_no) + orderid);
		mLoading.show();
		Application.getCustomerService().queryCusOrderDetail(new ICallback<AccountOrderDetailVO>() {

			@Override
			public void onDataReady(AccountOrderDetailVO data) {
				mLoading.hide();
				orderlist = data.getOrderlist();
				tvTotal.setText(data.getTotal());
				tvAddress.setText(data.getAddress());
				tvNeedWJD.setText(data.getNeedwjd());
				tvNeedInvoice.setText(data.getNeedinvoice());
				tvRemark.setText(data.getRemark());

				arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
						R.layout.list_item_customer_order_detail, orderlist);
				listView.setAdapter(arrayAdapter);
			}

			@Override
			public void onError(Throwable throwable) {

			}
		}, customercode, orderid);

		final Context context = this.getBaseContext();
		listView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(context, ProductDetailActivity.class);
				AccountOrderDetalList item = arrayAdapter.getItem(position);
				Bundle bound = new Bundle();
				bound.putString(Config.ITEM_CODE, item.getItemcode());
				bound.putString(Config.NAME, "");
				bound.putString(Config.BRAND, "");
				bound.putString(Config.CATEGORY, "");
				intent.putExtras(bound);
				startActivity(intent);
			}
		});
	}


	private class MyArrayAdapter extends ArrayAdapter<AccountOrderDetalList> {
		private Context context;
		private int resource;
		private List<AccountOrderDetalList> items;

		public MyArrayAdapter(Context context, int resource,
				List<AccountOrderDetalList> objects) {
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
						.findViewById(R.id.list_item_client_order_detail__item_code);

				holder.priceNumber = (TextView) row
						.findViewById(R.id.list_item_client_order_detail__price_x_number);
				holder.total = (TextView) row
						.findViewById(R.id.list_item_client_order_detail__total);
				row.setTag(holder);
			} else {
				holder = (ItemHolder) row.getTag();
			}

			AccountOrderDetalList rowData = items.get(position);
			holder.itemCode.setText(rowData.getItemcode());
			holder.priceNumber.setText(rowData.getPrice() + "x"
					+ rowData.getQty());
			holder.total.setText(rowData.getTotal());
			return row;
		}

		class ItemHolder {
			TextView itemCode;
			TextView priceNumber;
			TextView total;
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
