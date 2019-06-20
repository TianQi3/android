package com.humming.asc.sales.content;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.humming.asc.dp.presentation.vo.cp.CityReusltVO;
import com.humming.asc.dp.presentation.vo.cp.CityVO;
import com.humming.asc.dp.presentation.vo.cp.ItemInfoReusltVO;
import com.humming.asc.sales.Application;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.R;
import com.humming.asc.sales.activity.MainActivity;
import com.humming.asc.sales.activity.Stocks.InventoryListActivity;
import com.humming.asc.sales.activity.Stocks.SimpleSelectorActivity;
import com.humming.asc.sales.service.ICallback;
import com.humming.asc.sales.service.InventoryService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zhtq on 1/7/16.
 */
public class StocksContent extends LinearLayout {

    private final View view;
    private Context context;
    private Button btnSearch;
    private Button btnRest;
    private EditText etName;
    private TextView etBrand;
    private TextView etCategory;
    private TextView etCode;
    private TextView etCity;
    private ArrayList<String> codeList;
    private ArrayList<String> brandList;
    private ArrayList<String> categoryList;
    private ArrayList<String> cityNameList;
    private List<CityVO>  cityList;

    private Activity activity;
    public final static int categoryCode = 19000;
    public final static int codeCode = 19001;
    public final static int brandCode = 19002;
    public final static int cityCode = 19003;

    public StocksContent(Context context) {
        this(context, null);
    }

    public StocksContent(final Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        view = inflate(context, R.layout.fragment_stocks, this);
        // spCode = (Spinner)
        // findViewById(R.id.content_inventory__spinner_code);
        etCode = (TextView) findViewById(R.id.content_inventory__input_code);
        etName = (EditText) findViewById(R.id.content_inventory__input_name);
        etCity = (TextView) findViewById(R.id.content_inventory__input_city);
        etBrand = (TextView) findViewById(R.id.content_inventory__input_brand);
        etCategory = (TextView) findViewById(R.id.content_inventory__input_category);

        btnSearch = (Button) findViewById(R.id.content_inventory__button_search);
        btnRest = (Button) findViewById(R.id.content_inventory__button_reset);
        btnSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String itemcode = etCode.getText().toString();
                String brand = etBrand.getText().toString();
                String category = etCategory.getText().toString();
                String name = etName.getText().toString();
                String city = etCity.getText().toString();
                String cityCode = "";
                String position  = MainActivity.stockCityPosition;
                if(position!=null && !"".equals(position)){
                    cityCode = cityList.get(Integer.parseInt(position)).getCityCode();
                }else{

                }
                if (itemcode.length() == 0 && brand.length() == 0
                        && category.length() == 0 && name.length() == 0 && city.length() ==0) {
                    Toast.makeText(context, R.string.msg_at_least_one_item,
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(context, InventoryListActivity.class);
                Bundle b = new Bundle();
                b.putString(Config.ITEM_CODE, itemcode);
                b.putString(Config.BRAND, brand);
                b.putString(Config.CATEGORY, category);
                b.putString(Config.NAME, name);
                b.putString(Config.CITYCODE, cityCode);
                intent.putExtras(b);
                Application.getInstance().getCurrentActivity().startActivity(intent);
            }
        });

        btnRest.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                etName.setText("");
                etCode.setText("");
                etBrand.setText("");
                etCategory.setText("");
                etCity.setText("");

            }

        });

       /*// codeList = new ArrayList<String>();
        initAutoText(etCode, codeList,
                R.string.content_inventory__label_item_code, codeCode);

       // brandList = new ArrayList<String>();
        initAutoText(etBrand, brandList,
                R.string.content_inventory__label_brand, brandCode);

       // categoryList = new ArrayList<String>();
        initAutoText(etCategory, categoryList,
                R.string.content_inventory__label_category, categoryCode);
*/
        /*ISettingService settingService = Application.getSettingService();
        settingService.getBrandList(new IDataReadyCallback<List<String>>() {
            @Override
            public void onDataReady(List<String> list,
                                    RESTException restException) {
                if (list == null) {
                    return;
                }
                Log.v("xxxxxxx", "xxxxxxxx");
                brandList.addAll(list);
            }
        });
        settingService.getItemcodeList(new IDataReadyCallback<List<String>>() {
            @Override
            public void onDataReady(List<String> list,
                                    RESTException restException) {
                if (list == null) {
                    return;
                }
                codeList.addAll(list);
            }
        });
        settingService.getCategoryNameList(new IDataReadyCallback<List<String>>() {
            @Override
            public void onDataReady(List<String> list,
                                    RESTException restException) {
                if (list == null) {
                    return;
                }
                categoryList.addAll(list);
            }
        });*/
        InventoryService service = Application.getInventoryService();
        service.queryItemCodes(new ICallback<ItemInfoReusltVO>() {
            @Override
            public void onDataReady(ItemInfoReusltVO data) {
                codeList = (ArrayList<String>) data.getData();
                initAutoText(etCode, codeList,
                        getResources().getString(R.string.content_inventory__label_item_code), codeCode);
                Application.getInstance().setItemCodeLists(codeList);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        service.queryBrands(new ICallback<ItemInfoReusltVO>() {
            @Override
            public void onDataReady(ItemInfoReusltVO data) {
                brandList = (ArrayList<String>) data.getData();
                initAutoText(etBrand, brandList,
                        getResources().getString(R.string.content_inventory__label_brand), brandCode);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

        service.queryCategorys(new ICallback<ItemInfoReusltVO>() {
            @Override
            public void onDataReady(ItemInfoReusltVO data) {
                categoryList = (ArrayList<String>) data.getData();
                initAutoText(etCategory, categoryList,
                        getResources().getString(R.string.content_inventory__label_category), categoryCode);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });
        service.queryCitys(new ICallback<CityReusltVO>() {
            @Override
            public void onDataReady(CityReusltVO data) {
                cityList =  data.getData();
                cityNameList = new ArrayList<String>();
                for(CityVO cityVo:cityList){
                    cityNameList.add(cityVo.getCityName());
                }
                initAutoText(etCity, cityNameList,
                        getResources().getString(R.string.city), cityCode);
            }

            @Override
            public void onError(Throwable throwable) {

            }
        });

    }

    private void initAutoText(final TextView editText,
                              final ArrayList<String> list, final String title,
                              final int selectedItemCode) {
        editText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        SimpleSelectorActivity.class);
                Bundle b = new Bundle();
                b.putInt(SimpleSelectorActivity.SELECTED_ITEM_CODE,
                        selectedItemCode);
                b.putString(SimpleSelectorActivity.SELECTOR_TITLE, title);
                b.putStringArrayList(SimpleSelectorActivity.STRING_LIST, list);
                intent.putExtras(b);
                Application.getInstance().getCurrentActivity().startActivityForResult(intent, selectedItemCode);
            }
        });
    }
}
