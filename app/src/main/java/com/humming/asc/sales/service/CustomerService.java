package com.humming.asc.sales.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.asc.dp.presentation.vo.AccountARVO;
import com.humming.asc.dp.presentation.vo.AccountAddressVO;
import com.humming.asc.dp.presentation.vo.AccountCGVO;
import com.humming.asc.dp.presentation.vo.AccountContactVO;
import com.humming.asc.dp.presentation.vo.AccountInfoVO;
import com.humming.asc.dp.presentation.vo.AccountOrderDetailVO;
import com.humming.asc.dp.presentation.vo.AccountOrderVO;
import com.humming.asc.dp.presentation.vo.cp.CustomerVO;
import com.humming.asc.sales.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhtq on 12/22/15.
 */
public class CustomerService extends AbstractService {
    public void query(ICallback callback, String type, String pageno) {
        Map<String, String> params = new HashMap<>();
        if (type != null) {
            params.put("type", type);
        } else {
           // params.put("type", "Key Account");
        }
        if (pageno != null) {
            params.put("pageno", pageno);
        } else {

        }
        this.get(Config.URL_SERVICE_CUSTOMER_QUERY, params, callback, new TypeReference<List<CustomerVO>>() {
        }, null);
    }

    public void queryByKeyWord(ICallback callback, String keyWord, String type, String pageno) {

        Map<String, String> params = new HashMap<String, String>();
        if (keyWord != null) {
            params.put("keyword", keyWord);
        } else {
            params.put("keyword", "");
        }
        if (type != null) {
            params.put("type", type);
        } else {
            params.put("type", "Key Account");
        }
        if (pageno != null) {
            params.put("pageno", pageno);
        } else {

        }
        this.get(Config.URL_SERVICE_CUSTOMER_QUERY, params, callback, new TypeReference<List<CustomerVO>>() {
        }, null);
    }
    public void queryCusMessage(ICallback callback, String customercode) {
        Map<String, String> params = new HashMap<>();
        if (customercode != null) {
            params.put("customercode", customercode);
        } else {
        }

        this.get(Config.URL_SERVICE_CUSTOMER_MESSAGE, params, callback, AccountInfoVO.class, null);
    }
    public void queryCusAr(ICallback callback, String customercode) {
        Map<String, String> params = new HashMap<>();
        if (customercode != null) {
            params.put("customercode", customercode);
        } else {
        }

        this.get(Config.URL_SERVICE_CUSTOMER_AR, params, callback, AccountARVO.class, null);
    }
    public void queryCusCg(ICallback callback, String customercode) {
        Map<String, String> params = new HashMap<>();
        if (customercode != null) {
            params.put("customercode", customercode);
        } else {
        }

        this.get(Config.URL_SERVICE_CUSTOMER_CG, params, callback, AccountCGVO.class, null);
    }
    public void queryCusOrderList(ICallback callback, String customercode) {
        Map<String, String> params = new HashMap<>();
        if (customercode != null) {
            params.put("customercode", customercode);
        } else {
        }

        this.get(Config.URL_SERVICE_CUSTOMER_ORDER, params, callback, new TypeReference<List<AccountOrderVO>>() {
        }, null);
    }
    public void queryCusOrderDetail(ICallback callback, String customercode,String orderid) {
        Map<String, String> params = new HashMap<>();
        if (customercode != null) {
            params.put("customercode", customercode);
            params.put("orderid", orderid);
        } else {
        }

        this.get(Config.URL_SERVICE_CUSTOMER_ORDER_DETAIL, params, callback, AccountOrderDetailVO.class, null);
    }
    public void queryCusContactDetail(ICallback callback, String customercode) {
        Map<String, String> params = new HashMap<>();
        if (customercode != null) {
            params.put("customercode", customercode);
        } else {
        }

        this.get(Config.URL_SERVICE_CUSTOMER_CON_DETAIL, params, callback, new TypeReference<List<AccountContactVO>>() {
        }, null);
    }
    public void queryCusAddressDetail(ICallback callback, String customercode) {
        Map<String, String> params = new HashMap<>();
        if (customercode != null) {
            params.put("customercode", customercode);
        } else {
        }

        this.get(Config.URL_SERVICE_CUSTOMER_ADDRESS_DETAIL, params, callback, new TypeReference<List<AccountAddressVO>>() {
        }, null);
    }
}
