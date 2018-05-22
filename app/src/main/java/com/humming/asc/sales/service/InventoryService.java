package com.humming.asc.sales.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.asc.dp.presentation.vo.InventoryBookingVO;
import com.humming.asc.dp.presentation.vo.InventoryVO;
import com.humming.asc.dp.presentation.vo.InventoryWotsVO;
import com.humming.asc.dp.presentation.vo.ItemListVO;
import com.humming.asc.dp.presentation.vo.ItemVO;
import com.humming.asc.dp.presentation.vo.WOTSDetailResult;
import com.humming.asc.dp.presentation.vo.cp.CityReusltVO;
import com.humming.asc.dp.presentation.vo.cp.ItemInfoReusltVO;
import com.humming.asc.sales.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryService extends AbstractService {
    //search Inventory
    public void getInventoryList(ICallback callback, String itemcodes, String name, String brand,
                                 String category,String citycode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("itemcode", itemcodes);
        params.put("name", name);
        params.put("brand", brand);
        params.put("cate", category);
        params.put("citycode", citycode);
        this.get(Config.URL_SERVICE_DPNEW_INVENTORY_QUERY, params, callback, new TypeReference<List<ItemListVO>>() {
        }, null);
    }

    public void getProductDetail(ICallback callback, String itemcodes) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("itemcode", itemcodes);

        this.get(Config.PRODUCT_INFO_PATH, params, callback, ItemVO.class, null);
    }
    public void getInventoryDetail(ICallback callback, String itemcodes,String year) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("itemcode", itemcodes);
        params.put("year", year);
        this.get(Config.INVENTORY_DETAIL_PATH, params, callback, InventoryVO.class, null);
    }
    public void getWOTSList(ICallback callback, String itemcodes) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("itemcode", itemcodes);
        this.get(Config.INVENTORY_WOTS_PATH, params, callback, new TypeReference<List<InventoryWotsVO>>() {
        }, null);
    }
    public void getWOTSListNew(ICallback callback, String itemcodes) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("itemcode", itemcodes);
        this.get(Config.INVENTORY_WOTS_PATH_NEW, params, callback, WOTSDetailResult.class, null);
    }
    public void getBookingList(ICallback callback, String itemcodes,String year) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("itemcode", itemcodes);
        params.put("year", year);
        this.get(Config.INVENTORY_BOOKING_PATH, params, callback, new TypeReference<List<InventoryBookingVO>>() {
        }, null);
    }
    public void queryItemCodes(ICallback callback){
        this.get(Config.INVENTORY_ITEMCODE_PATH, null, callback, ItemInfoReusltVO.class, null);
    }
    public void queryCitys(ICallback callback){
        this.get(Config.INVENTORY_CITYS_PATH, null, callback, CityReusltVO.class, null);
    }
    public void queryBrands(ICallback callback){
        this.get(Config.INVENTORY_BRANDS_PATH, null, callback, ItemInfoReusltVO.class, null);
    }
    public void queryCategorys(ICallback callback){
        this.get(Config.INVENTORY_CATEGORYS_PATH, null, callback, ItemInfoReusltVO.class, null);
    }
}
