package com.humming.asc.sales.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.asc.dp.presentation.ro.cp.leads.AddLeadsRO;
import com.humming.asc.dp.presentation.ro.cp.leads.UpdateLeadsRO;
import com.humming.asc.dp.presentation.ro.cp.leads.UpdateTargetFlagRO;
import com.humming.asc.dp.presentation.vo.cp.CustomerVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.SaleInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.LinkageResultVO;
import com.humming.asc.dp.presentation.vo.cp.leads.LeadsDetailResultVO;
import com.humming.asc.sales.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhtq on 16/1/19.
 */
public class LeadsService extends AbstractService {
    public void query(ICallback callback, String type, String pageno) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("status", "Active");
        if (type != null) {
            params.put("type", type);
        } else {
            params.put("type", "Key Account");
        }
        if (pageno != null) {
            params.put("pageno", pageno);
        } else {

        }
        this.get(Config.URL_SERVICE_LEADS_QUERY, params, callback, new TypeReference<List<CustomerVO>>() {
        }, null);
    }

    public void queryCustomerLeads(ICallback callback, String type, String pageno) {

        Map<String, String> params = new HashMap<String, String>();
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

    //query chain
    public void queryChain(ICallback callback, String type, String pageno) {

        Map<String, String> params = new HashMap<String, String>();
        if (type != null) {
            params.put("chain", type);
        }
        if (pageno != null) {
            params.put("pageno", pageno);
        } else {

        }
        this.get(Config.URL_SERVICE_CUSTOMER_QUERY_BY_CHAIN, params, callback, new TypeReference<List<CustomerVO>>() {
        }, null);
    }

    //query chain by keyWords
    public void queryChainByKeywords(ICallback callback, String keywords,String chain, String pageno) {

        Map<String, String> params = new HashMap<String, String>();
        if (keywords != null) {
            params.put("keyword", keywords);
        }
        if(chain!=null){
          params.put("chain",chain);
        }
        if (pageno != null) {
            params.put("pageno", pageno);
        } else {

        }
        this.get(Config.URL_SERVICE_CUSTOMER_QUERY_BY_CHAIN, params, callback, new TypeReference<List<CustomerVO>>() {
        }, null);
    }

    public void queryById(ICallback callback, String rowId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("rowId", rowId);
        this.get(Config.URL_SERVICE_LEADS_ITEM_QUERYBYID, params, callback, LeadsDetailResultVO.class, null);
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

    //根据condition来查询leads
    public void queryByConditionValue(ICallback callback, String menu, String menuValue, String pageno) {

        Map<String, String> params = new HashMap<String, String>();
        params.put(menu, menuValue);
        if (pageno != null) {
            params.put("pageno", pageno);
        } else {

        }
        this.get(Config.URL_SERVICE_LEADS_QUERY, params, callback, new TypeReference<List<CustomerVO>>() {
        }, null);
    }

    //add leads
    public void addLeads(ICallback callback, AddLeadsRO addLeadsRO) {

        this.post(Config.URL_SERVICE_LEADS_ADD, addLeadsRO, callback, ResultVO.class, null);
    }

    //update leads
    public void updateLeads(ICallback callback, UpdateLeadsRO updateLeadsRO) {

        this.post(Config.URL_SERVICE_LEADS_UPDATE, updateLeadsRO, callback, ResultVO.class, null);
    }

    //获取leads的status
    public void getLeadsStatus(ICallback callback) {
        this.get(Config.URL_SERVICE_LEADS_STATUS, null, callback, LinkageResultVO.class, null);
    }

    //获取leads的stage
    public void getLeadsStage(ICallback callback) {
        this.get(Config.URL_SERVICE_LEADS_STAGE, null, callback, LinkageResultVO.class, null);
    }

    //获取leads的class
    public void getLeadsClass(ICallback callback) {
        this.get(Config.URL_SERVICE_LEADS_CLASS, null, callback, LinkageResultVO.class, null);
    }

    //获取leads的region
    public void getLeadsRegion(ICallback callback) {
        this.get(Config.URL_SERVICE_LEADS_REGION, null, callback, LinkageResultVO.class, null);
    }

    //获取leads的Province
    public void getLeadsProvince(ICallback callback, String name) {
        this.get(Config.URL_SERVICE_LEADS_PROVINCE, null, callback, LinkageResultVO.class, null);
    }

    //获取leads的City
    public void getLeadsCity(ICallback callback, String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        this.get(Config.URL_SERVICE_LEADS_PROVINCE, params, callback, LinkageResultVO.class, null);
    }

    //获取leads的Channel
    public void getLeadsChannel(ICallback callback) {
        this.get(Config.URL_SERVICE_LEADS_CHANNEL, null, callback, LinkageResultVO.class, null);
    }

    //获取leads的SubChannel
    public void getLeadsSubChannel(ICallback callback, String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        this.get(Config.URL_SERVICE_LEADS_CHANNEL, params, callback, LinkageResultVO.class, null);
    }

    //获取leads的subChain
    public void getLeadsSubChain(ICallback callback, String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        this.get(Config.URL_SERVICE_LEADS_CHAIN, params, callback, LinkageResultVO.class, null);
    }

    //获取leads的Chain
    public void getLeadsChain(ICallback callback) {
        this.get(Config.URL_SERVICE_LEADS_CHAIN, null, callback, LinkageResultVO.class, null);
    }

    //获取leads的sales
    public void getLeadsSales(ICallback callback) {
        this.get(Config.URL_SERVICE_LEADS_SALES_INFO, null, callback, SaleInfoResultVO.class, null);
    }

    //update leadsType
    public void updateLeadsType(ICallback callback, UpdateTargetFlagRO ro) {
        this.post(Config.URL_SERVICE_LEADS_UPDATE_TYPE, ro, callback, ResultVO.class, null);
    }
}
