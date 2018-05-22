package com.humming.asc.sales.activity.Stocks;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.ItemListVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.AbstractArrayAdapter;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.InventoryService;

import java.util.List;

public class InventoryListActivity extends AbstractActivity {
    private ListView listView;
    private List<ItemListVO> list;
    private View vNoData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        listView = (ListView) findViewById(R.id.activity_inventory_list__list_view);
        setTitle(R.string.title_inventory_query_result);
        vNoData = findViewById(R.id.activity__no_data);
        Bundle b = getIntent().getExtras();
        String itemcode = b.getString(Config.ITEM_CODE);
        String name = b.getString(Config.NAME);
        String brand = b.getString(Config.BRAND);
        String category = b.getString(Config.CATEGORY);
        String citycode = b.getString(Config.CITYCODE);
        InventoryService service = Application.getInventoryService();
        mLoading.show();
        service.getInventoryList(
                new ICallback<List<ItemListVO>>() {

                    @Override
                    public void onDataReady(List<ItemListVO> data) {
                        list = data;
                        if (list == null || list.size() == 0) {
                            vNoData.setVisibility(View.VISIBLE);
                        } else {
                            vNoData.setVisibility(View.GONE);
                        }
                        mLoading.hide();
                        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                                R.layout.list_item_inventory, list);
                        StringBuffer sb = new StringBuffer();
                        sb.append(getString(R.string.title_inventory_query_result));
                        sb.append(":(").append(list.size()).append(")");
                        setTitle(sb.toString());
                        listView.setAdapter(arrayAdapter);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        vNoData.setVisibility(View.VISIBLE);
                    }
                }, itemcode, name, brand,
                category,citycode);

        final Context context = this.getBaseContext();
        listView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                	Intent intent = new Intent(context,
                			InventoryDetailActivity.class);
                		Bundle b = new Bundle();
                	b.putString(Config.ITEM_CODE, list.get(position).getItemcode());
                		b.putString(Config.VINTAGE, list.get(position).getVintage());
                		intent.putExtras(b);
                	startActivity(intent);
            }
        });
    }
    class MyArrayAdapter extends AbstractArrayAdapter<ViewHolder, ItemListVO> {

        public MyArrayAdapter(Context context, int resource, List<ItemListVO> items) {
            super(context, resource, items);
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(int position, ViewHolder viewHolder, View rowView) {
            viewHolder.info = (ImageView) rowView
                    .findViewById(R.id.list_item_inventory_list__info);
            viewHolder.code = (TextView) rowView
                    .findViewById(R.id.list_item_inventory_list__code);
            viewHolder.inventory = (TextView) rowView
                    .findViewById(R.id.list_item_inventory_list__inventory);
            viewHolder.cn = (TextView) rowView
                    .findViewById(R.id.list_item_inventory_list__cn);
            viewHolder.en = (TextView) rowView
                    .findViewById(R.id.list_item_inventory_list__en);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, final ItemListVO itemData) {
            StringBuffer sb = new StringBuffer();
            sb.append(itemData.getItemcode()).append("(")
                    .append(itemData.getVintage()).append(")");
            viewHolder.code.setText(sb.toString());
            viewHolder.inventory.setText(itemData.getInventory());
            viewHolder.en.setText(itemData.getName_en());
            viewHolder.cn.setText(itemData.getName_cn());

            viewHolder.info.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Application.getInstance().getCurrentActivity(), ProductDetailActivity.class);
                    Bundle bound = new Bundle();
                    bound.putString(Config.ITEM_CODE, itemData.getItemcode());
                    intent.putExtras(bound);
                    startActivity(intent);
                }
            });
        }

    }
    class ViewHolder {
        ImageView info;
        TextView code;
        TextView inventory;
        TextView cn;
        TextView en;
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
