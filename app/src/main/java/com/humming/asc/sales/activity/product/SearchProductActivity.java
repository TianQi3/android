package com.humming.asc.sales.activity.product;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.type.TypeReference;
import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.RequestData.RequestNull;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.activity.Stocks.SimpleSelectorActivity;
import com.humming.asc.sales.adapter.product.TagAdapter;
import com.humming.asc.sales.component.CustomDialog;
import com.humming.asc.sales.component.product.FlowLayout;
import com.humming.asc.sales.component.product.TagFlowLayout;
import com.humming.asc.sales.service.Error;
import com.humming.asc.sales.service.OkHttpClientManager;
import com.humming.dto.ecatalogResponse.product.SearchResponse;
import com.humming.dto.response.base.ReturnStatus;
import com.squareup.okhttp.Request;

import java.util.List;

/**
 * Created by ztq on 2019/10/19.
 * 搜索
 */

public class SearchProductActivity extends AbstractActivity implements View.OnClickListener {

    private TagFlowLayout flowLayout;
    private EditText searchEd;
    private TextView cancleTv, moreTv;
    private ImageView clearImg;
    private LinearLayout moreContain;
    private CustomDialog customDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_search);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        String text = getIntent().getStringExtra("text");
        if ("".equals(text)) {

        } else {
            searchEd.setText(text);
            searchEd.setSelection(searchEd.getText().length());
        }
        customDialog = new CustomDialog(this, "Loading...");
        initDatas();
    }

    private void initDatas() {
        customDialog.show();
        RequestNull requestNull = new RequestNull();
        OkHttpClientManager.postAsyn(Config.ECATALLOG_SEARCH_HISTORY, new OkHttpClientManager.ResultCallback<List<SearchResponse>>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(final List<SearchResponse> response) {
                customDialog.dismiss();
                if (response.size() > 0) {
                    flowLayout.setVisibility(View.VISIBLE);
                    moreContain.setVisibility(View.VISIBLE);
                    final LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
                    flowLayout.setAdapter(new TagAdapter<SearchResponse>(response) {
                        @Override
                        public View getView(FlowLayout parent, int position, SearchResponse searchResponse) {
                            TextView tv = (TextView) inflater.inflate(R.layout.tag_tv,
                                    flowLayout, false);
                            tv.setText(searchResponse.getText().trim().replace(" ", ""));
                            return tv;
                        }
                    });
                    float density = SearchProductActivity.this.getResources().getDisplayMetrics().density;
                    int px = (int) (140 * density + 0.5f);// 4.9->4, 4.1->4, 四舍五入
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, px);

                    flowLayout.setLayoutParams(params);
                    flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                        @Override
                        public boolean onTagClick(View view, int position, FlowLayout parent) {
                            searchEd.setText(response.get(position).getText());
                            Bundle resultBundles = new Bundle();
                            resultBundles.putString(SimpleSelectorActivity.SELECTED_STRING, response.get(position).getText());
                            Intent resultIntent = new Intent()
                                    .putExtras(resultBundles);
                            setResult(
                                    RESULT_OK,
                                    resultIntent);
                            finish();
                            return false;
                        }
                    });

                } else {
                    flowLayout.setVisibility(View.GONE);
                    moreContain.setVisibility(View.GONE);
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, requestNull, new TypeReference<List<SearchResponse>>() {
        });
    }

    private void initView() {
        clearImg = findViewById(R.id.content_search_product__clear_history);
        moreContain = findViewById(R.id.content_search_product__more_layout);
        flowLayout = (TagFlowLayout) findViewById(R.id.content_search_product__flowlayout);
        searchEd = (EditText) findViewById(R.id.toolbar_search_ed);
        cancleTv = findViewById(R.id.toolbar_cancle);
        cancleTv.setOnClickListener(this);
        clearImg.setOnClickListener(this);
        moreTv = findViewById(R.id.content_search_product__more);
        moreTv.setOnClickListener(this);

        searchEd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!"".equals(searchEd.getText().toString())) {
                        //跳转页面
                        InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(searchEd.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                        Bundle resultBundles = new Bundle();
                        resultBundles.putString(SimpleSelectorActivity.SELECTED_STRING, searchEd.getText().toString());
                        Intent resultIntent = new Intent()
                                .putExtras(resultBundles);
                        setResult(
                                RESULT_OK,
                                resultIntent);
                        finish();
                    }
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.toolbar_cancle:
                finish();
                break;
            case R.id.content_search_product__more:
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                flowLayout.setLayoutParams(params);
                moreContain.setVisibility(View.GONE);
                break;
            case R.id.content_search_product__clear_history://清空历史
                showTip();
                break;

        }
    }

    //清空历史
    private void clearHistory() {
        customDialog.show();
        final RequestNull request = new RequestNull();
        OkHttpClientManager.postAsyn(Config.ECATALLOG_ENPTY_HISTORY, new OkHttpClientManager.ResultCallback<ReturnStatus>() {
            @Override
            public void onError(Request request, Error info) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , Error = " + info.getInfo());
            }

            @Override
            public void onResponse(ReturnStatus response) {
                customDialog.dismiss();
                if ("success".equals(response.getStatus())) {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.delect_success), Toast.LENGTH_SHORT).show();
                    initDatas();
                } else {
                    Toast.makeText(Application.getInstance().getCurrentActivity(), getResources().getString(R.string.delete_fail), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onOtherError(Request request, Exception exception) {
                customDialog.dismiss();
                Log.e("xxxxxx", "onError , e = " + exception.getMessage());
            }
        }, request, ReturnStatus.class);
    }

    private void showTip() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.confirm_delete_history));
        //点击对话框以外的区域是否让对话框消失
        builder.setCancelable(false);
        //设置正面按钮
        builder.setPositiveButton(getResources().getString(R.string.confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearHistory();
                dialog.dismiss();
            }
        });
        //设置反面按钮
        builder.setNegativeButton(getResources().getString(R.string.cancel1), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}