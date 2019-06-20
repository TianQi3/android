package com.humming.asc.sales.activity.product.filter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.githang.statusbar.StatusBarCompat;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.AbstractActivity;
import com.humming.asc.sales.adapter.product.TagAdapter;
import com.humming.asc.sales.component.product.FlowLayout;
import com.humming.asc.sales.component.product.TagFlowLayout;
import com.humming.asc.sales.model.product.ProductDCListEntity;
import com.humming.dto.ecatalogResponse.bean.search.UDCList;

import java.util.ArrayList;
import java.util.List;

public class FilterPriceActivity extends AbstractActivity implements View.OnClickListener {

    private Toolbar toolbar;
    private TextView mPrice1;
    private TextView mPrice2;
    private TextView mPrice3;
    private TextView mPrice4;
    private EditText mPriceEdittext1;
    private EditText mPriceEdittext2;
    private TextView mFilterPriceSubmit;
    private String selectValue = "";
    private String selectNumber = "";//0为列表返回 1为自定义
    private TagFlowLayout flowLayout;
    private List<UDCList> udcLists;
    private List<ProductDCListEntity> dcListEntityList;
    private TagAdapter tagAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_filter_price);
        StatusBarCompat.setStatusBarColor(this, this.getResources().getColor(R.color.WhiteBG), true);
        initView();
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.select_price1);
        udcLists = (List<UDCList>) getIntent().getExtras().getSerializable("response");
        selectValue = getIntent().getExtras().getString("price_value");
        selectNumber = getIntent().getExtras().getString("price_number");
        initData();
    }

    private void initData() {
        final LayoutInflater inflater = LayoutInflater.from(Application.getInstance().getCurrentActivity());
        dcListEntityList = new ArrayList<>();
        if (udcLists != null && udcLists.size() > 0) {
            for (UDCList udcList : udcLists) {
                ProductDCListEntity entity = new ProductDCListEntity();
                entity.setUdcList(udcList);
                if (selectValue.equals(entity.getUdcList().getNameCn()))
                    entity.setCheckSelect(true);
                else
                    entity.setCheckSelect(false);
                dcListEntityList.add(entity);
            }
        }
        tagAdapter = new TagAdapter<ProductDCListEntity>(dcListEntityList) {
            @Override
            public View getView(FlowLayout parent, int position, ProductDCListEntity o) {
                TextView tv = (TextView) inflater.inflate(R.layout.tag_tv,
                        flowLayout, false);
                tv.setText(o.getUdcList().getNameCn());
                if (o.isCheckSelect()) {
                    tv.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                    tv.setTextColor(getResources().getColor(R.color.ffbfa574));

                } else {
                    tv.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
                    tv.setTextColor(getResources().getColor(R.color._f999));
                }
                return tv;
            }
        };
        flowLayout.setAdapter(tagAdapter);
        flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view1, int position, FlowLayout parent) {
                TextView view = (TextView) view1;
                if (!dcListEntityList.get(position).isCheckSelect()) {
                    view.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                    view.setTextColor(getResources().getColor(R.color.ffbfa574));
                    for (ProductDCListEntity entity : dcListEntityList) {
                        entity.setCheckSelect(false);
                    }
                    dcListEntityList.get(position).setCheckSelect(true);
                    selectValue = dcListEntityList.get(position).getUdcList().getNameCn();
                    tagAdapter.notifyDataChanged();
                } else {
                    view.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
                    view.setTextColor(getResources().getColor(R.color._f999));
                    dcListEntityList.get(position).setCheckSelect(false);
                    tagAdapter.notifyDataChanged();

                    selectValue = "";
                }
                selectNumber = "" + dcListEntityList.get(position).getUdcList().getId();
                return false;
            }
        });
    }

    private void initView() {
        flowLayout = (TagFlowLayout) findViewById(R.id.content_product_filter__flowlayout);
        mPrice1 = (TextView) findViewById(R.id.filter_price_1);
        mPrice2 = (TextView) findViewById(R.id.filter_price_2);
        mPrice3 = (TextView) findViewById(R.id.filter_price_3);
        mPrice4 = (TextView) findViewById(R.id.filter_price_4);
        mPriceEdittext1 = (EditText) findViewById(R.id.filter_price_edittext1);
        mPriceEdittext2 = (EditText) findViewById(R.id.filter_price_edittext2);
        mFilterPriceSubmit = (TextView) findViewById(R.id.content_product_filter__price_submit);
        mFilterPriceSubmit.setOnClickListener(this);
        mPrice1.setOnClickListener(this);
        mPrice2.setOnClickListener(this);
        mPrice3.setOnClickListener(this);
        mPrice4.setOnClickListener(this);
        //初始化虚拟键盘的状态
        mPriceEdittext1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPriceEdittext1.setFocusable(true);
                mPriceEdittext1.setFocusableInTouchMode(true);
                mPriceEdittext1.requestFocus();
                InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(mPriceEdittext1.getWindowToken(), 0);
                return false;
            }
        });
        mPriceEdittext1.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //跳转页面
                    InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPriceEdittext1.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
        //初始化虚拟键盘的状态
        mPriceEdittext2.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mPriceEdittext2.setFocusable(true);
                mPriceEdittext2.setFocusableInTouchMode(true);
                mPriceEdittext2.requestFocus();
                InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                imm.hideSoftInputFromWindow(mPriceEdittext2.getWindowToken(), 0);
                return false;
            }
        });
        mPriceEdittext2.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    //跳转页面
                    InputMethodManager imm = (InputMethodManager) Application.getInstance().getCurrentActivity().getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(mPriceEdittext2.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
                return false;
            }
        });
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_product_filter__price_submit:
                boolean a = true;
                StringBuilder sb = new StringBuilder();
                if (mPriceEdittext1.getText().toString() != null && !"".equals(mPriceEdittext1.getText().toString())) {
                    sb.append(mPriceEdittext1.getText().toString());
                    if (mPriceEdittext2.getText().toString() != null && !"".equals(mPriceEdittext2.getText().toString())) {
                        if (Integer.parseInt(mPriceEdittext2.getText().toString()) <= Integer.parseInt(mPriceEdittext1.getText().toString())) {
                            sb.delete(0, sb.length());
                            Toast.makeText(this, getResources().getString(R.string.enter_price_error), Toast.LENGTH_SHORT).show();
                            a = false;
                        } else {
                            sb.append("-" + mPriceEdittext2.getText().toString());
                        }
                    } else {
                        sb.append("-");
                    }
                } else {
                    if (mPriceEdittext2.getText().toString() != null && !"".equals(mPriceEdittext2.getText().toString())) {
                        sb.append("0-" + mPriceEdittext2.getText().toString());
                    } else {
                    }
                }
                if (!"".equals(sb.toString())) {
                    selectValue = sb.toString();
                    selectNumber = "-1";
                }
                if (a) {
                    //返回传值
                    Bundle resultBundles = new Bundle();
                    resultBundles.putString("select_price_value", selectValue);
                    resultBundles.putString("select_price_number", selectNumber);
                    Intent resultIntent = new Intent()
                            .putExtras(resultBundles);
                    setResult(
                            RESULT_OK,
                            resultIntent);
                    finish();
                }

                break;
            case R.id.filter_price_1:// TODO 19/04/11
                selectValue = mPrice1.getText().toString();
                // selectNumber = "1";
                setViewNotify();
                mPrice1.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                mPrice1.setTextColor(getResources().getColor(R.color.ffbfa574));
                break;
            case R.id.filter_price_2:// TODO 19/04/11
                selectValue = mPrice2.getText().toString();
                // selectNumber = "2";
                setViewNotify();
                mPrice2.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                mPrice2.setTextColor(getResources().getColor(R.color.ffbfa574));
                break;
            case R.id.filter_price_3:// TODO 19/04/11
                selectValue = mPrice3.getText().toString();
                //  selectNumber = "3";
                setViewNotify();
                mPrice3.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                mPrice3.setTextColor(getResources().getColor(R.color.ffbfa574));

                break;
            case R.id.filter_price_4:// TODO 19/04/11
                selectValue = mPrice4.getText().toString();
                // selectNumber = "4";
                setViewNotify();
                mPrice4.setBackground(getResources().getDrawable(R.drawable.flower_radius_select));
                mPrice4.setTextColor(getResources().getColor(R.color.ffbfa574));
                break;
        }
    }

    private void setViewNotify() {
        mPrice1.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
        mPrice1.setTextColor(getResources().getColor(R.color._f999));
        mPrice2.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
        mPrice2.setTextColor(getResources().getColor(R.color._f999));
        mPrice3.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
        mPrice3.setTextColor(getResources().getColor(R.color._f999));
        mPrice4.setBackground(getResources().getDrawable(R.drawable.bg_border_radius_gray));
        mPrice4.setTextColor(getResources().getColor(R.color._f999));

    }
}
