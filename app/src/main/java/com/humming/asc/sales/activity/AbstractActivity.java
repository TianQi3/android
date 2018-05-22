package com.humming.asc.sales.activity;

import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;

import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.component.Loading;


/**
 * Created by PuTi(编程即菩提) on 12/22/15.
 */
public class AbstractActivity extends AppCompatActivity {
    protected Loading mLoading;

    @Override
    protected void onResume() {
        super.onResume();
        Application.getInstance().setCurrentActivity(this);
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mLoading = (Loading) findViewById(R.id.activity_loading);
    }
}
