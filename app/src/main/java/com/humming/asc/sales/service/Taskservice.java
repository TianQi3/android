package com.humming.asc.sales.service;

import com.humming.asc.dp.presentation.ro.cp.task.AddTaskProductRO;
import com.humming.asc.dp.presentation.ro.cp.task.AddTaskRO;
import com.humming.asc.dp.presentation.ro.cp.task.UpdateTaskProductRO;
import com.humming.asc.dp.presentation.ro.cp.task.UpdateTaskRO;
import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.BaseInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.baseinfo.MenuResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.ItemResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskDetailResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskListResultVO;
import com.humming.asc.dp.presentation.vo.cp.task.TaskProductResultVO;
import com.humming.asc.sales.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhtq on 16/1/21.
 */
public class Taskservice extends AbstractService {
    public void query(ICallback callback, String pageNo) {
        Map<String, String> params = new HashMap<String, String>();
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_TASK_ITEM_QUERY, params, callback, TaskListResultVO.class, null);
    }

    public void queryByCustomerRowId(ICallback callback, String rowId, String pageNo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("customerRowId", rowId);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_TASK_ITEM_QUERY, params, callback, TaskListResultVO.class, null);
    }

    //add task
    public void addTask(ICallback callback, AddTaskRO addTaskRO) {

        this.post(Config.URL_SERVICE_TASK_ITEM_ADD, addTaskRO, callback, ResultVO.class, null);
    }

    //update task
    public void updateTask(ICallback callback, UpdateTaskRO updateTaskRO) {

        this.post(Config.URL_SERVICE_TASK_ITEM_UPDATE, updateTaskRO, callback, ResultVO.class, null);
    }

    //edit task by queryTaskById
    public void queryTaskById(ICallback callback, String taskId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskId", taskId);
        this.get(Config.URL_SERVICE_TASK_ITEM_QUERYBYID, params, callback, TaskDetailResultVO.class, null);
    }

    //关键字搜索
    public void queryByName(ICallback callback, String name, String pageNo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_TASK_ITEM_QUERY, params, callback, TaskListResultVO.class, null);
    }

    //关键字搜索(customer进入)
    public void queryByNameCus(ICallback callback, String rowId, String name, String pageNo) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("name", name);
        params.put("customerRowId", rowId);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_TASK_ITEM_QUERY, params, callback, TaskListResultVO.class, null);
    }

    //根据condition来查询task(customer)
    public void queryByCOnditionValueCus(ICallback callback,String rowId, String menu, String menuValue, String pageNo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(menu, menuValue);
        params.put("customerRowId", rowId);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_TASK_ITEM_QUERY, params, callback, TaskListResultVO.class, null);
    }

    //根据condition来查询task
    public void queryByCOnditionValue(ICallback callback, String menu, String menuValue, String pageNo) {
        Map<String, String> params = new HashMap<String, String>();
        params.put(menu, menuValue);
        if (pageNo != null) {
            params.put("pageno", pageNo);
        } else {
        }
        this.get(Config.URL_SERVICE_TASK_ITEM_QUERY, params, callback, TaskListResultVO.class, null);
    }

    //查询task的 condition value
    public void queryTaskCondition(ICallback callback, String type) {

        Map<String, String> params = new HashMap<String, String>();
        params.put("type", type);
        this.get(Config.URL_SERVICE_INFO_BASE_DATA, params, callback, MenuResultVO.class, null);
    }

    //获取task type类型

    public void getTaskType(ICallback callback) {
        this.get(Config.URL_SERVICE_TASK_GET_TYPE, null, callback, BaseInfoResultVO.class, null);
    }

    //获取task stauts状态
    public void getTaskStatus(ICallback callback) {
        this.get(Config.URL_SERVICE_TASK_GET_STATUS, null, callback, BaseInfoResultVO.class, null);
    }

    //根据taskId来获取product
    public void queryProductByTaskId(ICallback callback, String taskId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("taskId", taskId);
        this.get(Config.URL_SERVICE_TASK_ITEM_QUERY_PRODUCT_BYTASKID, params, callback, TaskProductResultVO.class, null);
    }

    //add taskProduct
    public void addTaskProduct(ICallback callback, AddTaskProductRO addTaskProductRO) {
        this.post(Config.URL_SERVICE_TASK_ITEM_ADD_TASKPRODUCT, addTaskProductRO, callback, ResultVO.class, null);
    }

    //delete taskProduct
    public void deleteTaskProduct(ICallback callback, String rowId) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("rowId", rowId);
        this.get(Config.URL_SERVICE_TASK_ITEM_DELETE_TASKPRODUCT, params, callback, ResultVO.class, null);
    }

    //update taskProduct
    public void updateTaskProduct(ICallback callback, UpdateTaskProductRO updateTaskProductRO) {
        this.post(Config.URL_SERVICE_TASK_ITEM_UPDATE_TASKPRODUCT, updateTaskProductRO, callback, ResultVO.class, null);
    }

    //query taskProduct item
    public void getItemCode(ICallback callback, String itemCode) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("itemcode", itemCode);
        this.get(Config.URL_SERVICE_TASK_ITEM_GET_ITEMCODE, params, callback, ItemResultVO.class, null);
    }
}
