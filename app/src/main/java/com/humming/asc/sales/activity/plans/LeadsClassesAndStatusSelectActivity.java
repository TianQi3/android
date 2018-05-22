package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.humming.asc.dp.presentation.vo.cp.SaleInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.SaleInfoVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.LinkageResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.LinkageVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.AbstractArrayAdapter;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.LeadsService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 16/1/27.
 */
public class LeadsClassesAndStatusSelectActivity extends AbstractActivity implements ICallback<LinkageResultVO>  {
    private ListView listView;
    public static final int ACTIVITY_LEADS_STATUS_SELECT = 10022;
    public static final int ACTIVITY_LEADS_STAGE_SELECT = 10023;
    public static final int ACTIVITY_LEADS_CLASS_SELECT = 10024;
    public static final int ACTIVITY_LEADS_REGION_SELECT = 10025;
    public static final int ACTIVITY_LEADS_PROVINCE_SELECT = 10026;
    public static final int ACTIVITY_LEADS_CHANNEL_SELECT = 10027;
    public static final int ACTIVITY_LEADS_CHAIN_SELECT = 10028;
    public static final int ACTIVITY_LEADS_CITY_SELECT = 10029;
    public static final int ACTIVITY_ADD_STATUS_SELECT = 10021;
    public static final int ACTIVITY_LEADS_SUB_CHANNEL_SELECT = 10030;
    public static final int ACTIVITY_LEADS_SUB_CHAIN_SELECT = 10031;
    public static final int ACTIVITY_LEADS_SALES_SELECT = 10032;
    public static String ACTIVITY_LEADS = "activity_leads";
    public static String ACTIVITY_LEADS_STATUS = "activity_leads_status";
    public static String ACTIVITY_LEADS_SALES = "activity_leads_sales";
    public static String ACTIVITY_LEADS_STAGE = "activity_leads_stage";

    public static String ACTIVITY_LEADS_CLASS = "activity_leads_class";
    public static String ACTIVITY_LEADS_REGION = "activity_leads_region";
    public static String ACTIVITY_LEADS_PROVINCE = "activity_leads_province";
    public static String ACTIVITY_LEADS_CITY = "activity_leads_city";
    public static String ACTIVITY_LEADS_SUB_CHANNEL = "activity_leads_sub_channel";
    public static String ACTIVITY_LEADS_CHANNEL = "activity_leads_channel";
    public static String ACTIVITY_LEADS_CHAIN = "activity_leads_chain";
    public static String ACTIVITY_LEADS_SUB_CHAIN = "activity_leads_sub_chain";

    public static String ACTIVITY_LEADS_CITY_INTENT = "province_city";
    public static String ACTIVITY_LEADS_SUB_CHANNEL_INTENT = "sub_channel";
    public static String ACTIVITY_LEADS_SUB_CHAIN_INTENT = "sub_chain";
    private List<String> lists;
    private List<LinkageVO> linksgeVoLists;
    private List<SaleInfoVO> datas;
    private String currentName;
    public static int positions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_status_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        LeadsService leadsService = Application.getLeadsService();
        currentName = getIntent().getStringExtra(ACTIVITY_LEADS);
        listView = (ListView) findViewById(R.id.content_class_status_list);
        lists = new ArrayList<String>();
        if (ACTIVITY_LEADS_STATUS.equals(currentName)) {
            getSupportActionBar().setTitle(R.string.status);
            leadsService.getLeadsStatus(this);
        } else if (ACTIVITY_LEADS_STAGE.equals(currentName)) {
            getSupportActionBar().setTitle(R.string.stage);
            leadsService.getLeadsStage(this);
        } else if (ACTIVITY_LEADS_CLASS.equals(currentName)) {
            getSupportActionBar().setTitle(R.string.classes);
            leadsService.getLeadsClass(this);
        }else if (ACTIVITY_LEADS_REGION.equals(currentName)) {
            getSupportActionBar().setTitle(R.string.region);
            leadsService.getLeadsRegion(this);
        }else if (ACTIVITY_LEADS_PROVINCE.equals(currentName)) {
            getSupportActionBar().setTitle(R.string.province);
            leadsService.getLeadsProvince(this,null);
        } else if (ACTIVITY_LEADS_CHANNEL.equals(currentName)) {
            getSupportActionBar().setTitle(R.string.channel);
            leadsService.getLeadsChannel(this);
        }else if (ACTIVITY_LEADS_CHAIN.equals(currentName)) {
            getSupportActionBar().setTitle(R.string.leads_chain);
            leadsService.getLeadsChain(this);
        }else if(ACTIVITY_LEADS_CITY.equals(currentName)){
            getSupportActionBar().setTitle(R.string.city);
            leadsService.getLeadsCity(this,getIntent().getStringExtra(ACTIVITY_LEADS_CITY_INTENT));

        }else if(ACTIVITY_LEADS_SUB_CHAIN.equals(currentName)){
            getSupportActionBar().setTitle(R.string.leads_sub_chain);
            leadsService.getLeadsSubChain(this,getIntent().getStringExtra(ACTIVITY_LEADS_SUB_CHAIN_INTENT));
        }else if(ACTIVITY_LEADS_SUB_CHANNEL.equals(currentName)){
            getSupportActionBar().setTitle(R.string.sub_channel);
            leadsService.getLeadsSubChannel(this,getIntent().getStringExtra(ACTIVITY_LEADS_SUB_CHANNEL_INTENT));
        }else if(ACTIVITY_LEADS_SALES.equals(currentName)){
            getSupportActionBar().setTitle(R.string.sales);
            leadsService.getLeadsSales(new ICallback<SaleInfoResultVO>() {

                @Override
                public void onDataReady(SaleInfoResultVO data) {
                    datas = data.getData();
                    rightConditionTaskAdapter rightAdapter = new rightConditionTaskAdapter(Application.getInstance().getCurrentActivity(),datas);
                    listView.setAdapter(rightAdapter);
                }

                @Override
                public void onError(Throwable throwable) {

                }
            });
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle resultBundle = new Bundle();
                positions = position;
                if(ACTIVITY_LEADS_SALES.equals(currentName)){
                    resultBundle.putString(
                            LeadsAddActivity.TYPE_OR_STATUS_SALE_EID, datas.get(position).getEid());
                    resultBundle.putString(
                            LeadsAddActivity.TYPE_OR_STATUS_NAME, datas.get(position).getFirstName()+datas.get(position).getLastName());
                }else{
                    resultBundle.putString(
                            LeadsAddActivity.TYPE_OR_STATUS_NAME, lists.get(position));
                }
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        ACTIVITY_ADD_STATUS_SELECT,
                        resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onDataReady(LinkageResultVO data) {
        linksgeVoLists = data.getData();
        for(int i=0;i<linksgeVoLists.size();i++){
            lists.add(linksgeVoLists.get(i).getValue());
        }
        MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(), R.layout.list_item_textview, lists);
        listView.setAdapter(arrayAdapter);
    }

    @Override
    public void onError(Throwable throwable) {

    }

    class ViewHolder {
        TextView name;
        ImageView assocType;
    }

    class MyArrayAdapter extends AbstractArrayAdapter<ViewHolder, String> {

        public MyArrayAdapter(Context context, int resource, List<String> items) {
            super(context, resource, items);
        }

        @Override
        protected ViewHolder createViewHolder() {
            return new ViewHolder();
        }

        @Override
        protected void initViewHolder(int position, ViewHolder viewHolder, View rowView) {
            viewHolder.name = (TextView) rowView.findViewById(R.id.list_item__textview);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, String itemData) {
            viewHolder.name.setText(itemData);
        }
    }
    class rightConditionTaskAdapter extends BaseAdapter {

        private Context context;
        private List<SaleInfoVO> dates;

        public rightConditionTaskAdapter(Context context, List<SaleInfoVO> datas) {
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
                convertView = LayoutInflater.from(context).inflate(R.layout.list_item_task_and_dailycall_condition_right, null);
                holder.name = (TextView) convertView.findViewById(R.id.list_item_task__and_dailycallcondition_right__content);
                holder.assocType = (ImageView) convertView.findViewById(R.id.list_item_task_and_dailycall_condition_right__img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.name.setText(dates.get(position).getFirstName()+dates.get(position).getLastName());
            if (dates.get(position).getHeadImg() != null) {
                holder.assocType.setVisibility(View.VISIBLE);
                ImageLoader imageLoader = new ImageLoader(Application.getInstance().getRequestQueue(), new BitmapCache());
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.assocType, R.drawable.ic_add, R.drawable.ic_add);
                imageLoader.get(dates.get(position).getHeadImg(), listener);
            } else {
                holder.assocType.setVisibility(View.GONE);
            }
            return convertView;
        }
    }
    private class BitmapCache implements ImageLoader.ImageCache {
        private LruCache<String, Bitmap> cache;

        public BitmapCache() {
            cache = new LruCache<String, Bitmap>(8 * 1024 * 1024) {
                @Override
                protected int sizeOf(String key, Bitmap bitmap) {
                    return bitmap.getRowBytes() * bitmap.getHeight();
                }
            };
        }

        @Override
        public Bitmap getBitmap(String url) {
            return cache.get(url);
        }

        @Override
        public void putBitmap(String url, Bitmap bitmap) {
            cache.put(url, bitmap);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
