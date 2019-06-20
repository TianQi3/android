package com.humming.asc.sales.service;


import com.humming.asc.dp.presentation.ro.cp.dailycall.AddDailyCallCommentRO;
import com.humming.asc.dp.presentation.ro.cp.dailycall.AddDailyCallRO;
import com.humming.asc.dp.presentation.ro.cp.dailycall.UpdateDailyCallRO;
import com.humming.asc.dp.presentation.vo.cp.CPSummaryResultVO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.BaseInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.DCSubjectResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.CalendarTotalResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.CommentsResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallDetailResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.DailyCallQueryResultVO;
import com.humming.asc.dp.presentation.vo.cp.dailycall.QueryAllEmployeeResultVO;
import com.humming.asc.sales.Config;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhtq on 12/22/15.
 */
public class DailyCallService extends AbstractService {
    public void query(ICallback callback, String pageNo) {
        Map<String, String> params = new HashMap<String, String>();
        if (pageNo != null) {
            params.put("pageno", pageNo);
            params.put("status", "Planned");
        } else {
        }
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    public void queryLeadsDailyCall(ICallback callback, String id, String assoctype, String pageNo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("assoctype", assoctype);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    public void queryComments(ICallback callback, String pageNo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("commentStatus", "1");
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    //关键字搜索
    public void queryCommentsByName(ICallback callback, String name, String pageNo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("commentStatus", "1");
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    //根据taskId搜索
    public void queryDailyCallByTaskId(ICallback callback, String taskId, String pageNo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", taskId);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    public void getSummary(ICallback callback) {
        get(Config.URL_SERVICE_DAILY_CALL_SUMMARY, null, callback, CPSummaryResultVO.class, null);
    }

    //关键字搜索
    public void queryByName(ICallback callback, String name, String pageNo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("status", "Planned");
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    //关键字搜索(customer)
    public void queryByNameCus(ICallback callback, String id, String assoctype, String name, String pageNo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("assoctype", assoctype);
        params.put("name", name);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    //根据KeyAccount ID搜索
    public void queryByKeyAccount(ICallback callback, String rowId,String pageNo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("keyAccountId", rowId);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY_BYKEYACCOUNT, params, callback, DailyCallQueryResultVO.class, null);
    }
    //根据rowId搜索
    public void queryByRowId(ICallback callback, String rowId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("rowId", rowId);
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY_BYROWID, params, callback, DailyCallDetailResultVO.class, null);
    }

    //根据condition来查询task
    public void queryByCOnditionValue(ICallback callback, String menu, String menuValue) {

        Map<String, String> params = new HashMap<String, String>();
        //params.put("status", "Planned");
        params.put(menu, menuValue);
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    //根据condition来查询task
    public void queryCommentsByCOnditionValue(ICallback callback, String menu, String menuValue) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("commentStatus", "1");
        params.put(menu, menuValue);
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    //根据condition来查询task(customer)
    public void queryByCOnditionValueCus(ICallback callback, String id, String assoctype, String menu, String menuValue) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("assoctype", assoctype);
        params.put(menu, menuValue);
        this.get(Config.URL_SERVICE_DAILY_CALL_QUERY, params, callback, DailyCallQueryResultVO.class, null);
    }

    //add Daily call
    public void addDailyCall(ICallback callback, AddDailyCallRO addDailyCallRO) {
        this.post(Config.URL_SERVICE_DAILY_CALL_ADD, addDailyCallRO, callback, ResultVO.class, null);
    }

    //获取所有员工的信息
    public void queryAllEmployee(ICallback callback) {
        this.get(Config.URL_SERVICE_QUERY_ALL_EMPLOYEE, null, callback, QueryAllEmployeeResultVO.class, null);
    }

    //edit Daily call
    public void updateDailyCall(ICallback callback, UpdateDailyCallRO updateDailyCallRO) {
        this.post(Config.URL_SERVICE_DAILY_CALL_UPDATE, updateDailyCallRO, callback, ResultVO.class, null);
    }

    //获取dailycall 的状态
    public void getDCStatus(ICallback callback) {
        this.get(Config.URL_SERVICE_DAILY_CALL_STATUS, null, callback, BaseInfoResultVO.class, null);
    }

    //获取dailycall 的主题
    public void getDCSubject(ICallback callback) {
        this.get(Config.URL_SERVICE_DAILY_CALL_SUBJECT, null, callback, DCSubjectResultVO.class, null);
    }

    //获取dailycall 的类型
    public void getDCType(ICallback callback) {
        this.get(Config.URL_SERVICE_DAILY_CALL_TYPE, null, callback, BaseInfoResultVO.class, null);
    }

    //根据daily call id来查询comments
    public void queryByDcIdComments(ICallback callback, String dailyCallId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("dcId", dailyCallId);
        this.get(Config.URL_SERVICE_COMMENTS_QUERY_BYDCID, params, callback, CommentsResultVO.class, null);
    }

    //添加comments
    public void addComments(ICallback callback, AddDailyCallCommentRO addDailyCallCommentRO) {

        this.post(Config.URL_SERVICE_COMMENTS_QUERY_ADD, addDailyCallCommentRO, callback, ResultVO.class, null);
    }

    //删除comments
    public void deleteComments(ICallback callback, String rowId) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("rowId", rowId);
        this.get(Config.URL_SERVICE_COMMENTS_QUERY_DELETE, params, callback, ResultVO.class, null);
    }

    public void addImg(ICallback callback, String filePartName, File file, Map<String, String> params) {
        this.post(Config.URL_SERVICE_ALIIMAGE, filePartName, file, params, callback, ResultVO.class, null);
    }

    // 查询日历的统计信息
    public void queryCalendarTotal(ICallback callback) {
        this.get(Config.URL_SERVICE_DAILY_CALL_CALENDAR_TOTAL, null, callback, CalendarTotalResultVO.class, null);
    }

    public void queryCalendarDetail(ICallback callback, String date) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("date", date);
        this.get(Config.URL_SERVICE_DAILY_CALL_CALENDAR_DETAIL, params, callback, DailyCallQueryResultVO.class, null);
    }
}
