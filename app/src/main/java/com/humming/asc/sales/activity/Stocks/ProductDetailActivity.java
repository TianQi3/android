
package com.humming.asc.sales.activity.Stocks;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.humming.asc.dp.presentation.vo.ItemVO;
import com.humming.asc.dp.presentation.vo.cp.task.ItemResultVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.plans.ProductCalculatorActivity;
import com.humming.asc.sales.service.ICallback;

public class ProductDetailActivity extends AbstractActivity implements
        ICallback<ItemResultVO> {
    private TextView tvCN;
    private TextView tvEN;
    private TextView tvVolume;
    private TextView tvSpe;
    private Button btnDetail, btnCalculator;
    private TextView tvInventory;
    private TextView tvPrice;
    private TextView tvChannel;
    private TextView tvChannel2;
    private TextView tvChannel3;
    private TextView tvItemGroup;
    private String itemcode;
    private String name;
    private String brand;
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tvCN = (TextView) findViewById(R.id.activity_client_product__name_cn);
        tvEN = (TextView) findViewById(R.id.activity_client_product__name_en);
        tvVolume = (TextView) findViewById(R.id.activity_client_product__volume);
        tvSpe = (TextView) findViewById(R.id.activity_client_product__specification);
        tvPrice = (TextView) findViewById(R.id.activity_client_product__price);
        tvChannel = (TextView) findViewById(R.id.activity_client_product__chain1);
        tvChannel2 = (TextView) findViewById(R.id.activity_client_product__chain2);
        tvItemGroup = (TextView) findViewById(R.id.activity_client_product__item_group);
        //  tvChannel3 = (TextView) findViewById(R.id.activity_client_product__chain3);

        tvInventory = (TextView) findViewById(R.id.activity_client_product__inventory);
        btnDetail = (Button) findViewById(R.id.activity_client_product__button_details);
        btnCalculator = (Button) findViewById(R.id.activity_client_product__button_calculator);

        Bundle bound = getIntent().getExtras();
        itemcode = bound.getString(Config.ITEM_CODE);
        name = bound.getString(Config.NAME);
        brand = bound.getString(Config.BRAND);
        category = bound.getString(Config.CATEGORY);

        btnCalculator.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(Application.getInstance().getCurrentActivity(), ProductCalculatorActivity.class);
                Application.getInstance().getCurrentActivity().startActivity(mIntent);
            }
        });
        btnDetail.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(Application.getInstance().getCurrentActivity(), InventoryListActivity.class);
                Bundle b = new Bundle();
                b.putString(Config.ITEM_CODE, itemcode);
                b.putString(Config.NAME, "");
                b.putString(Config.BRAND, "");
                b.putString(Config.CATEGORY, "");
                b.putString(Config.CITYCODE, "");
                intent.putExtras(b);
                Application.getInstance().getCurrentActivity().startActivity(intent);

            }
        });
        setTitle(new String(itemcode));
        Application.getTaskservice().getItemCode(this, itemcode);
    }

    @Override
    public void onDataReady(ItemResultVO datas) {
        ItemVO data = datas.getData();
        tvCN.setText(data.getNameCN());
        tvEN.setText(data.getNameEN());
        tvVolume.setText(data.getVolume() + "ML");
        tvSpe.setText(data.getUnit() + "/" + data.getUom());
        tvInventory.setText(data.getInventory());
        tvPrice.setText(data.getRp());
        tvChannel.setText(data.getChannelFocus1());
        tvChannel2.setText(data.getChannelFocus2());
        tvItemGroup.setText(data.getItemgroup());
        // tvChannel3.setText("3."+data.getChannelFocus3());
    }

    @Override
    public void onError(Throwable throwable) {

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
