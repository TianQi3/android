package com.humming.asc.sales.activity.settings;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.plans.LeadsAddActivity;
import com.humming.asc.sales.datebase.DBManger;
import com.humming.asc.sales.model.DBLeads;
import com.puti.view.list.AbstractItemPagerArrayAdapter;
import com.puti.view.list.ItemViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 16/1/15.
 */
public class DraftsLeadsListActivity extends AbstractActivity {

    private ListView listView;

    private ArrayList<DBLeads> list;
    private int[] itemPageResArray;
    private TextView title;
    private DBManger dbm;
    public static final int LEADS_EDIT_RESULT_CODE = 12321;
    public static final String RESULT_TEXT_LEADS = "result_text_leads";
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leads_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title = (TextView) findViewById(R.id.activity_leads_list_title);
        title.setVisibility(View.GONE);
        itemPageResArray = new int[]{R.layout.list_item_leads, R.layout.list_item_view_page_right};
        findViewById(R.id.content_customer__listView).setVisibility(View.GONE);
        listView = (ListView) findViewById(R.id.content_customer__listViews);
        listView.setVisibility(View.VISIBLE);
        dbm = new DBManger(this);
        list = (ArrayList) dbm.SelectLeadsList();
        int sum = list.size();
        getSupportActionBar().setTitle(getResources().getString(R.string.label_leads) + "(" + sum + ")");
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getBaseContext(),
                R.layout.list_item_view_pager_delete, list, itemPageResArray, 0, "");
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
       // getMenuInflater().inflate(R.menu.menu_task_product, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                Bundle resultBundles = new Bundle();
                resultBundles.putString(MyDraftsActivity.RESULT_TEXT, "");
                Intent resultIntent = new Intent()
                        .putExtras(resultBundles);
                setResult(
                        RESULT_OK,
                        resultIntent);
                finish();
                break;
            case R.id.action_product_add:

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    class ViewHolder {
        ImageView assocType;
        TextView name;
        TextView nameEn;
        TextView leadsSales;
        TextView leadsRowId;
        Button btnDelete;
        View v;
    }

    private class MyArrayAdapter extends AbstractItemPagerArrayAdapter<DBLeads, ViewHolder> {
        private List<DBLeads> itemss;
        private View.OnClickListener onbtnDeleteClickListener;
        private View.OnClickListener onItemClickListener;

        public MyArrayAdapter(Context context, int resource, final List<DBLeads> items, int[] itemPageResArray, int defaultPageIndex, String types) {
            super(context, resource, items, itemPageResArray, defaultPageIndex, types);
            this.itemss = items;
            onbtnDeleteClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    dbm.DeleteLeads(list.get(position).getId());
                    itemss.remove(position);
                    updateViewPagerState(position, 0, true);
                    int sum = itemss.size();
                    getSupportActionBar().setTitle(getResources().getString(R.string.label_leads) + "(" + sum + ")");
                    notifyDataSetChanged();
                }
            };
            onItemClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = Integer.parseInt(v.getTag().toString());
                    index = position;
                    Application.getInstance().setDbLeads(itemss.get(position));
                    Intent intent = new Intent(DraftsLeadsListActivity.this, LeadsAddActivity.class);
                    intent.putExtra(LeadsAddActivity.DRAFTS_LEADS,"true");
                    startActivityForResult(intent,LEADS_EDIT_RESULT_CODE);

                }
            };
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(int position, ViewHolder viewHolder, List<View> itemPages) {
            View rightView = itemPages.get(1);
            View centerView = itemPages.get(0);
            viewHolder.v = centerView;
            viewHolder.assocType = (ImageView) centerView
                    .findViewById(R.id.leads_cus_type);
            viewHolder.name = (TextView) centerView
                    .findViewById(R.id.leads_account_name);
            viewHolder.nameEn = (TextView) centerView
                    .findViewById(R.id.leads_account_nameEn);
            viewHolder.leadsSales = (TextView) centerView
                    .findViewById(R.id.leads_sales);
            viewHolder.leadsRowId = (TextView) centerView
                    .findViewById(R.id.leads_rowid);
            viewHolder.btnDelete = (Button) rightView
                    .findViewById(R.id.right_task);
            viewHolder.btnDelete.setVisibility(View.VISIBLE);
            viewHolder.btnDelete.setText(Application.getInstance().getString(R.string.delete));
            viewHolder.btnDelete.setOnClickListener(onbtnDeleteClickListener);
            viewHolder.v.setOnClickListener(onItemClickListener);
        }

        @Override
        protected ItemViewPagerAdapter createItemViewPagerAdapter(List<View> list) {
            ItemViewPagerAdapter adapter = new ItemViewPagerAdapter(list) {
                @Override
                public float getPageWidth(int position) {
                    if (position == 1) {
                        return 0.18f;
                    } else {
                        return 1f;
                    }
                }
            };
            return adapter;
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, DBLeads itemData) {
            String name = itemData.getNameCn();
            String nameen = itemData.getNameEn();
            int assocTypeImgRes = R.drawable.icon_l;
            viewHolder.v.setTag(position);
            viewHolder.btnDelete.setTag(position);
            viewHolder.assocType.setImageResource(assocTypeImgRes);
            viewHolder.name.setText(itemData.getChannel() + "   " + itemData.getStage());
            viewHolder.nameEn.setText(itemData.getProvince() + "   " + itemData.getCity());
            viewHolder.leadsRowId.setText(name + nameen);
            viewHolder.leadsSales.setText(itemData.getSaleName());
            viewHolder.name.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.nameEn.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.leadsRowId.setTextColor(getResources().getColor(R.color.colorAccent));
            viewHolder.leadsSales.setTextColor(getResources().getColor(R.color.colorAccent));
        }

    }

    //跳转返回结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data == null) {
            return;
        }
        switch (requestCode) {
            case DraftsLeadsListActivity.LEADS_EDIT_RESULT_CODE:
                String text = data.getStringExtra(RESULT_TEXT_LEADS);
                if("true".equals(text)){

                }else{
                    dbm.DeleteLeads(list.get(index).getId());
                }
                list = (ArrayList) dbm.SelectLeadsList();
                int sum = list.size();
                getSupportActionBar().setTitle(getResources().getString(R.string.label_leads) + "(" + sum + ")");
                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getBaseContext(),
                        R.layout.list_item_view_pager_delete, list, itemPageResArray, 0, "");
                listView.setAdapter(arrayAdapter);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Bundle resultBundles = new Bundle();
            resultBundles.putString(MyDraftsActivity.RESULT_TEXT, "");
            Intent resultIntent = new Intent()
                    .putExtras(resultBundles);
            setResult(
                    RESULT_OK,
                    resultIntent);
            finish();
        }
        return false;
    }
}
