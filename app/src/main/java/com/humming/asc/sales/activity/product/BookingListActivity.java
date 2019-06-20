package com.humming.asc.sales.activity.product;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.product.BookingAdapter;
import com.humming.dto.ecatalogResponse.product.BookingResponse;

public class BookingListActivity extends AbstractActivity {

    private Toolbar toolbar;
    private RecyclerView mListviewMf;
    private BookingAdapter adapter;
    private BookingResponse response;
    private String vintage, whoseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_booking_list);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        response = (BookingResponse) getIntent().getExtras().getSerializable("booking");
        vintage = getIntent().getStringExtra("vintage");
        whoseName = getIntent().getStringExtra("whouse_name");
        setTitle(vintage + " " + whoseName + "  " + getResources().getString(R.string.stock_booking));
        adapter = new BookingAdapter(response.getBookingList());
        adapter.openLoadAnimation();
        mListviewMf.setAdapter(adapter);
    }

    private void initView() {
        mListviewMf = (RecyclerView) findViewById(R.id.mf_listview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mListviewMf.setLayoutManager(linearLayoutManager);
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
