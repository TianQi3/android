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
import com.humming.asc.dp.presentation.vo.cp.baseinfo.MenuDetailVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.MenuResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.MenuValuesVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.model.ConditionItem;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.Taskservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Zhtq on 16/1/22.
 */
public class DailyCallConditionActivity extends AbstractActivity implements ImageLoader.ImageCache {
    private ListView leftListView, rightListView;
    private HashMap<String, List<MenuValuesVO>> menuValuesMap;
    private List<String> menuKeys;
    public static final String CONDITION_MENU = "condition_menu";
    public static final String CONDITION_MENU_VALUE = "condition_menu_value";
    public static final int ACTIVITY_CONDITION_RESULT = 20001;
    private String currentMenu, currentMenuValue;
    private ArrayList<ConditionItem> mSelectNames;
    private rightConditionTaskAdapter rightAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_and_dailycall_condition);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        leftListView = (ListView) findViewById(R.id.content_task_and_dailycall_condition__left_listView);
        rightListView = (ListView) findViewById(R.id.content_task_and_dailycall_condition__right_listView);
        mSelectNames = new ArrayList<ConditionItem>();
        initQueryDate();
    }

    private void initQueryDate() {
        menuKeys = new ArrayList<String>();
        menuValuesMap = new HashMap<String, List<MenuValuesVO>>();
        Taskservice taskService = Application.getTaskservice();
        taskService.queryTaskCondition(new ICallback<MenuResultVO>() {
            @Override
            public void onDataReady(MenuResultVO data) {
                final List<MenuDetailVO> menuDetailVoList = data.getData().getDetails();
                for (int i = 0; i < menuDetailVoList.size(); i++) {
                    menuValuesMap.put(menuDetailVoList.get(i).getMenuName(), menuDetailVoList.get(i).getMenuValues());
                    menuKeys.add(menuDetailVoList.get(i).getMenuName());
                }
                final leftConditionTaskAdapter leftAdapter = new leftConditionTaskAdapter(Application.getInstance().getCurrentActivity(), menuKeys);
                leftListView.setAdapter(leftAdapter);
                rightAdapter = new rightConditionTaskAdapter(Application.getInstance().getCurrentActivity(), menuValuesMap, menuDetailVoList.get(0).getMenuName());
                currentMenu = menuDetailVoList.get(0).getMenuName();
                rightListView.setAdapter(rightAdapter);
                leftListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        rightAdapter = new rightConditionTaskAdapter(Application.getInstance().getCurrentActivity(), menuValuesMap, menuDetailVoList.get(position).getMenuName());
                        rightListView.setAdapter(rightAdapter);
                        currentMenu = menuDetailVoList.get(position).getMenuName();
                        for (int i = 0; i < parent.getCount(); i++) {
                            View v = parent.getChildAt(parent.getCount() - 1 - (parent.getCount() - 1 - i));
                            if (position == i) {
                                v.setBackgroundResource(R.color.transparentCoditionright);
                            } else {
                                v.setBackgroundResource(R.color.transparentCoditionLeft);
                            }
                        }

                    }
                });
                rightListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (ConditionItem conditionItem : mSelectNames) {
                            conditionItem.setSelect(false);
                        }
                        mSelectNames.get(position).setSelect(true);
                        rightAdapter.notifyDataSetChanged();
                        currentMenuValue = menuValuesMap.get(currentMenu).get(position).getMenuConditionValue();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        }, "daily");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_task_condition, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_task_condition_confirm:
                if (currentMenuValue != null) {
                    Bundle resultBundle = new Bundle();
                    resultBundle.putString(
                            DailyCallConditionActivity.CONDITION_MENU,
                            currentMenu);
                    resultBundle.putString(
                            DailyCallConditionActivity.CONDITION_MENU_VALUE,
                            currentMenuValue);
                    Intent resultIntent = new Intent()
                            .putExtras(resultBundle);
                    setResult(
                            DailyCallConditionActivity.ACTIVITY_CONDITION_RESULT,
                            resultIntent);
                    finish();
                }
                break;
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Bitmap getBitmap(String url) {
        return null;
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {

    }

    class rightConditionTaskAdapter extends BaseAdapter {

        private Context context;
        private String index;
        private HashMap<String, List<MenuValuesVO>> menuValuesMap;
        private List<MenuValuesVO> menuResultList;

        public rightConditionTaskAdapter(Context context, HashMap<String, List<MenuValuesVO>> menuValuesMap, String index) {
            this.context = context;
            this.menuValuesMap = menuValuesMap;
            this.index = index;
            this.menuResultList = this.menuValuesMap.get(index);
            mSelectNames.clear();
            for (MenuValuesVO menuValuesVO : menuResultList) {
                ConditionItem conditionItem = new ConditionItem();
                conditionItem.setName(menuValuesVO.getMenuShowValue());
                conditionItem.setSelect(false);
                mSelectNames.add(conditionItem);
            }
        }

        @Override
        public int getCount() {
            return menuResultList.size();
        }

        @Override
        public Object getItem(int position) {
            return menuResultList.get(position);
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
                holder.rightName = (TextView) convertView.findViewById(R.id.list_item_task__and_dailycallcondition_right__content);
                holder.assocType = (ImageView) convertView.findViewById(R.id.list_item_task_and_dailycall_condition_right__img);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.rightName.setText(menuResultList.get(position).getMenuShowValue());
            if (mSelectNames.get(position).getSelect()) {
                convertView.setBackgroundResource(R.color.transparentCoditionLeft);
            } else {
                convertView.setBackgroundResource(R.color.transparentCoditionright);
            }
            if (menuResultList.get(position).getIsHaveImage()) {
                holder.assocType.setVisibility(View.VISIBLE);
                ImageLoader imageLoader = new ImageLoader(Application.getInstance().getRequestQueue(), new BitmapCache());
                ImageLoader.ImageListener listener = ImageLoader.getImageListener(holder.assocType, R.drawable.ic_add, R.drawable.ic_add);
                imageLoader.get(menuResultList.get(position).getImageUrl(), listener);
            } else {
                holder.assocType.setVisibility(View.GONE);
            }
            return convertView;
        }

    }

    class leftConditionTaskAdapter extends BaseAdapter {

        private List<String> menuKey;
        private Context con;

        public leftConditionTaskAdapter(Context content, List<String> menuKey) {
            this.menuKey = menuKey;
            this.con = content;
        }

        @Override
        public int getCount() {
            return menuKey.size();
        }

        @Override
        public Object getItem(int position) {
            return menuKey.get(position);
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
                convertView = LayoutInflater.from(con).inflate(R.layout.list_item_task_and_dailycall_condition_left, null);
                holder.leftName = (TextView) convertView.findViewById(R.id.list_item_task_and_dailycall_condition_left__content);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (position == 0) {
                convertView.setBackgroundResource(R.color.transparentCoditionright);
            } else {
                convertView.setBackgroundResource(R.color.transparentCoditionLeft);
            }
            holder.leftName.setText(menuKeys.get(position));
            return convertView;
        }
    }

    class ViewHolder {
        ImageView assocType;
        TextView leftName;
        TextView rightName;
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
}
