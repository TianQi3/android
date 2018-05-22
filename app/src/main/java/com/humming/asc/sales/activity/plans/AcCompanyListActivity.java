package com.humming.asc.sales.activity.plans;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.cp.baseinfo.DCSubjectResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.AbstractArrayAdapter;
import com.humming.asc.sales.service.DailyCallService;
import com.humming.asc.sales.service.ICallback;

import java.util.List;

/**
 * Created by Zhtq on 16/1/27.
 */
public class AcCompanyListActivity extends AbstractActivity {
    private ListView listView;
    public static final int ACTIVITY_ACCOMPANY_CODE = 13011;
    public static final int ACTIVITY_DAILY_CALL_ACCOMPANY_SELECT = 10050;
    public static String ACTIVITY_ACCOMPANY = "daily_call_accompany";

    public static String ACTIVITY_DAILY_CALL_ACCOMPANY = "activity_daily_call_accompany";
    private List<String> lists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_status_list);
        listView = (ListView) findViewById(R.id.content_class_status_list);
        DailyCallService dailyCallservice = Application.getDailyCallService();
        mLoading.show();
        dailyCallservice.getDCSubject(new ICallback<DCSubjectResultVO>() {

            @Override
            public void onDataReady(DCSubjectResultVO data) {

                MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(), R.layout.list_item_textview, lists);
                listView.setAdapter(arrayAdapter);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Bundle resultBundle = new Bundle();
                resultBundle.putString(
                        TaskAddActivity.TYPE_OR_STATUS_NAME, lists.get(position));
                Intent resultIntent = new Intent()
                        .putExtras(resultBundle);
                setResult(
                        ACTIVITY_DAILY_CALL_ACCOMPANY_SELECT,
                        resultIntent);
                finish();
            }
        });
    }

    class ViewHolder {
        TextView name;
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
            viewHolder.name = (TextView) rowView
                    .findViewById(R.id.list_item__textview);
        }

        @Override
        protected void setItemData(int position, ViewHolder viewHolder, String itemData) {
            viewHolder.name.setText(itemData);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
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
