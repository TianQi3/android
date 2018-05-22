package com.humming.asc.sales.activity.settings;

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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.postn.PositionInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.postn.PositionInfoVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.LoginActivity;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.model.PositionCoditionItem;
import com.humming.asc.sales.model.PositionRequest;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.VersionService;

import java.util.ArrayList;
import java.util.List;

public class PositionListActivity extends AbstractActivity {

    private ListView positionListView;
    private Application app = Application.getInstance();
    private Toolbar toolbar;
    private VersionService versionService;
    private List<PositionCoditionItem> pCIS;
    private String rowId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_position_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        versionService = Application.getVersionService();
        pCIS = new ArrayList<PositionCoditionItem>();
        positionListView = (ListView) findViewById(R.id.content_position__listViews);
        positionListView.setVisibility(View.VISIBLE);
        toolbar.setTitle(app.getString(R.string.position));
        initData();

    }

    private void initData() {
        versionService.queryAllPostnInfo(new ICallback<PositionInfoResultVO>() {
            @Override
            public void onDataReady(final PositionInfoResultVO data) {
                final MyArrayAdapter arrayAdapter = new MyArrayAdapter(Application.getInstance().getCurrentActivity(),
                        R.layout.list_item_position, data.getData());
                positionListView.setAdapter(arrayAdapter);
                positionListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        for (PositionCoditionItem poinitem : pCIS) {
                            poinitem.setSelect(false);
                        }
                        pCIS.get(position).setSelect(true);
                        arrayAdapter.notifyDataSetChanged();
                        rowId = data.getData().get(position).getRowId();
                    }
                });
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_position_go, menu);
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
            case R.id.action_position_confirm:
                if (!"".equals(rowId)) {
                    PositionRequest positionRequest = new PositionRequest();
                    positionRequest.setPostnRowId(rowId);
                    versionService.setDefaultPostn(new ICallback<ResultVO>() {
                        @Override
                        public void onDataReady(ResultVO data) {
                            MainActivity.activity.finish();
                            Intent intent = new Intent(Application.getInstance().getCurrentActivity(), LoginActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onError(Throwable throwable) {

                        }
                    }, positionRequest);
                } else {

                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MyArrayAdapter extends ArrayAdapter<PositionInfoVO> {
        private Context context;
        private int resource;
        private List<PositionInfoVO> items;

        public MyArrayAdapter(Context context, int resource, List<PositionInfoVO> objects) {
            super(context, resource, objects);
            this.context = context;
            this.resource = resource;
            this.items = objects;
            pCIS.clear();
            for (PositionInfoVO positionInfo : objects) {
                PositionCoditionItem pci = new PositionCoditionItem();
                pci.setPositionName(positionInfo.getPositionName());
                if (positionInfo.getAppDefault() == 1) {
                    pci.setSelect(true);
                } else {
                    pci.setSelect(false);
                }
                pci.setRowId(positionInfo.getRowId());
                pCIS.add(pci);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            MyArrayAdapter.ItemHolder holder = null;
            if (row == null) {
                LayoutInflater inflater = ((Activity) context)
                        .getLayoutInflater();
                row = inflater.inflate(resource, parent, false);

                holder = new MyArrayAdapter.ItemHolder();
                holder.positionNam = (TextView) row
                        .findViewById(R.id.list_item_position_name);
                row.setTag(holder);
            } else {
                holder = (ItemHolder) row.getTag();
            }
            if (pCIS.get(position).isSelect()) {
                row.setBackgroundResource(R.color.transparentCoditionright);
            } else {
                row.setBackgroundResource(R.color.colors);
            }
            PositionInfoVO po = items.get(position);
            holder.positionNam.setText(po.getPositionName());

            return row;
        }

        class ItemHolder {
            TextView positionNam;
        }

    }

}

