package com.humming.asc.sales.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.humming.asc.dp.presentation.ro.ad.mfreqeust.AddMfRequestRO;
import com.humming.asc.dp.presentation.ro.ad.mfreqeust.AuditMfRequestRO;
import com.humming.asc.dp.presentation.ro.ad.mfreqeust.UpdateMfRequestRO;
import com.humming.asc.dp.presentation.ro.ad.payment.AuditPaymentRO;
import com.humming.asc.dp.presentation.ro.ad.sample.AuditSampleRO;
import com.humming.asc.dp.presentation.vo.AccountForMfVO;
import com.humming.asc.dp.presentation.vo.ItemCostVO;
import com.humming.asc.dp.presentation.vo.MfInfoVO;
import com.humming.asc.dp.presentation.vo.ad.InsertMfRequestVO;
import com.humming.asc.dp.presentation.vo.ad.NewCountVO;
import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestDetailResultVO;
import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestListFromCustomerResultVO;
import com.humming.asc.dp.presentation.vo.ad.mfrequest.QueryMfRequestListResultVO;
import com.humming.asc.dp.presentation.vo.ad.payment.QueryPaymentDetailResultVO;
import com.humming.asc.dp.presentation.vo.ad.payment.QueryPaymentListResultVO;
import com.humming.asc.dp.presentation.vo.ad.sample.QuerySampleDetailResultVO;
import com.humming.asc.dp.presentation.vo.ad.sample.QuerySampleListResultVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.sales.Config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Zhtq on 06/27/18.
 */
public class ApprovalService extends AbstractService {

    //查询列表（通过不同状态）
    public void queryMfList(ICallback callback, String pageNo, int type, String keyword, String isRead) {
        Map<String, String> params = new HashMap<String, String>();
        if (pageNo != null) {
            params.put("pageno", pageNo);
            params.put("type", type + "");
            params.put("isRead", isRead + "");
            params.put("key", keyword + "");
        } else {
        }
        this.get(Config.MF_REQUEST_LIST, params, callback, QueryMfRequestListResultVO.class, null);
    }

    //通过客户查询mf列表
    public void queryMfListFromCustomer(ICallback callback, String pageNo, String keyword, String customerRowId, String auditStatus) {
        Map<String, String> params = new HashMap<String, String>();
        if (pageNo != null) {
            params.put("pageno", pageNo);
            params.put("customerRowId", customerRowId);
            params.put("key", keyword + "");
            params.put("auditStatus", auditStatus + "");
        } else {
        }
        this.get(Config.MF_REQUEST_LIST_FROM_CUSTOMER, params, callback, QueryMfRequestListFromCustomerResultVO.class, null);
    }

    //mf Detail
    public void mfDetail(ICallback callback, String id, String type) {
        Map<String, String> params = new HashMap<String, String>();
        if (id != null) {
            params.put("id", id);
            params.put("type", type);
        } else {
        }
        this.get(Config.MF_DETAIL, params, callback, QueryMfRequestDetailResultVO.class, null);
    }

    //审核MF
    public void auditMfrequest(ICallback callback, AuditMfRequestRO ro) {
        this.post(Config.MF_AUDIT, ro, callback, ResultVO.class, null);
    }

    //添加MF
    public void addMf(ICallback callback, AddMfRequestRO ro) {
        this.post(Config.MF_ADD, ro, callback, InsertMfRequestVO.class, null);
    }

    //修改MF
    public void updateMf(ICallback callback, UpdateMfRequestRO ro) {
        this.post(Config.MF_UPDATE, ro, callback, InsertMfRequestVO.class, null);
    }

    //查询item Code列表
    public void queryItemCode(ICallback callback, String pageno, String searchName) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("searchName", searchName);
        params.put("pageno", pageno);
        this.get(Config.MF_ITEM_CODE, params, callback, new TypeReference<List<ItemCostVO>>() {
        }, null);
    }

    //查询MF Customer列表
    public void queryAccForMf(ICallback callback, String pageNo, String keyword) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pageno", pageNo);
        params.put("keyword", keyword);
        this.get(Config.MF_CUSTOMER, params, callback, new TypeReference<List<AccountForMfVO>>() {
        }, null);
    }


    //Sample查询列表（通过不同状态）
    public void querySampleList(ICallback callback, String pageNo, int type, String keyword, String isRead) {
        Map<String, String> params = new HashMap<String, String>();
        if (pageNo != null) {
            params.put("pageno", pageNo);
            params.put("key", keyword + "");
            params.put("isRead", isRead + "");
            params.put("type", type + "");
        } else {
        }
        this.get(Config.SAMPLE_LIST, params, callback, QuerySampleListResultVO.class, null);
    }

    //Sample Detail
    public void sampleDetail(ICallback callback, String id, String type) {
        Map<String, String> params = new HashMap<String, String>();
        if (id != null) {
            params.put("id", id);
            params.put("type", type);
        } else {
        }
        this.get(Config.SAMPLE_DETAIL, params, callback, QuerySampleDetailResultVO.class, null);
    }

    //审核Sample
    public void auditSampleRequest(ICallback callback, AuditSampleRO ro) {
        this.post(Config.SAMPLE_AUDIT, ro, callback, ResultVO.class, null);
    }


    //payment（通过不同状态）
    public void queryPaymentList(ICallback callback, String pageNo, int type, String keyword, String isRead) {
        Map<String, String> params = new HashMap<String, String>();
        if (pageNo != null) {
            params.put("pageno", pageNo);
            params.put("type", type + "");
            params.put("isRead", isRead + "");
            params.put("key", keyword + "");
        } else {
        }
        this.get(Config.PAYMENT_LIST, params, callback, QueryPaymentListResultVO.class, null);
    }

    //payment Detail
    public void paymentDetail(ICallback callback, String id, String type) {
        Map<String, String> params = new HashMap<String, String>();
        if (id != null) {
            params.put("id", id);
            params.put("type", type);
        } else {
        }
        this.get(Config.PAYMENT_DETAIL, params, callback, QueryPaymentDetailResultVO.class, null);
    }

    //审核payment
    public void auditPaymentRequest(ICallback callback, AuditPaymentRO ro) {
        this.post(Config.PAYMENT_AUDIT, ro, callback, ResultVO.class, null);
    }


    //货币Currency
    public void queryCurrency(ICallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        this.get(Config.CURRENCY, params, callback, MfInfoVO.class, null);
    }

    //customer 进入获取MF信息
    public void queryMFInfoFromCustomer(ICallback callback, String customerCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("customerCode", customerCode);
        this.get(Config.FROM_CUSTOMER_MFQUEST_INFO, params, callback, AccountForMfVO.class, null);
    }

    //customer 进入获取MF状态 Menu
    public void queryAduitMenu(ICallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        this.get(Config.AUDIT_STAUS, params, callback, new TypeReference<List<String>>() {
        }, null);
    }

    //customer 进入获取MF状态 Menu
    public void querynewCount(ICallback callback) {
        Map<String, String> params = new HashMap<String, String>();
        this.get(Config.BASEINFO_NEWCOUNT, params, callback, NewCountVO.class, null);
    }

    //customer 进入获取MF状态 Menu
    public void updateStage(ICallback callback, String requestId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("rowId", requestId);
        this.get(Config.UPDATE_STAGE, params, callback, ResultVO.class, null);
    }
}
