package com.humming.asc.sales;

/**
 * Created by PuTi(编程即菩提) on 12/22/15.
 */
public class Config {
    public static final boolean DEBUG = false;
    // public static final String URL_SERVICE = "http://101.231.101.70:8080/dp/service/";//测试环境
    // public static final String URL_SERVICE = "http://101.231.101.70:8089/dp/service/";//测试环境
    public static final String URL_SERVICE = "http://sm.asc-wines.com/dp/service/";//正式环境
    public static final String SERVER = "sm.asc-wines.com/dp";

    public static final String URL_SERVICE_LOGIN = Config.URL_SERVICE + "login";

    public static final String URL_SERVICE_ALIIMAGE = Config.URL_SERVICE + "img/addPic";

    //version
    public static final String URL_VERSION_CONTROLLER = Config.URL_SERVICE + "version";
    //customer
    public static final String URL_SERVICE_CUSTOMER_MESSAGE = Config.URL_SERVICE + "biz/accInfo";
    public static final String URL_SERVICE_CUSTOMER_AR = Config.URL_SERVICE + "biz/accAR";
    public static final String URL_SERVICE_CUSTOMER_CG = Config.URL_SERVICE + "biz/accCG";
    public static final String URL_SERVICE_CUSTOMER_ORDER = Config.URL_SERVICE + "biz/accOrders";
    public static final String URL_SERVICE_CUSTOMER_ORDER_DETAIL = Config.URL_SERVICE + "biz/accOrderDetail";
    public static final String URL_SERVICE_CUSTOMER_CON_DETAIL = Config.URL_SERVICE + "biz/accCon";
    public static final String URL_SERVICE_CUSTOMER_ADDRESS_DETAIL = Config.URL_SERVICE + "biz/accAddr";

    //daily call
    public static final String URL_SERVICE_DAILY_CALL_QUERY = Config.URL_SERVICE + "dc/query";
    public static final String URL_SERVICE_DAILY_CALL_ADD = Config.URL_SERVICE + "dc/add";
    public static final String URL_SERVICE_DAILY_CALL_QUERY_BYKEYACCOUNT = Config.URL_SERVICE + "dc/queryByKeyAccount";
    public static final String URL_SERVICE_DAILY_CALL_QUERY_BYROWID = Config.URL_SERVICE + "dc/queryByRowId";
    public static final String URL_SERVICE_DAILY_CALL_UPDATE = Config.URL_SERVICE + "dc/update";
    public static final String URL_SERVICE_DAILY_CALL_CALENDAR_TOTAL = Config.URL_SERVICE + "dc/queryCalendarTotal";
    public static final String URL_SERVICE_DAILY_CALL_CALENDAR_DETAIL = Config.URL_SERVICE + "dc/queryCalendarDetail";
    public static final String URL_SERVICE_DAILY_CALL_SUMMARY = Config.URL_SERVICE + "biz/cpInfo";
    public static final String URL_SERVICE_DAILY_CALL_STATUS = Config.URL_SERVICE + "info/getDCStatus";
    public static final String URL_SERVICE_DAILY_CALL_SUBJECT = Config.URL_SERVICE + "info/getDCSubject";
    public static final String URL_SERVICE_DAILY_CALL_TYPE = Config.URL_SERVICE + "info/getDCType";

    public static final String URL_SERVICE_COMMENTS_QUERY_BYDCID = Config.URL_SERVICE + "comment/queryByDcId";
    public static final String URL_SERVICE_COMMENTS_QUERY_ADD = Config.URL_SERVICE + "comment/add";
    public static final String URL_SERVICE_COMMENTS_QUERY_DELETE = Config.URL_SERVICE + "comment/delete";

    public static final String URL_SERVICE_CUSTOMER_QUERY = Config.URL_SERVICE + "info/cusByType";
    public static final String URL_SERVICE_CUSTOMER_QUERY_BY_CHAIN = Config.URL_SERVICE + "info/cusByChain";

    public static final String URL_SERVICE_INFO_BASE_DATA = Config.URL_SERVICE + "info/getQueryMenu";
    //leads
    public static final String URL_SERVICE_LEADS_ITEM_QUERYBYID = Config.URL_SERVICE + "leads/queryById";
    public static final String URL_SERVICE_LEADS_QUERY = Config.URL_SERVICE + "leads/query";
    public static final String URL_SERVICE_LEADS_ADD = Config.URL_SERVICE + "leads/add";
    public static final String URL_SERVICE_LEADS_UPDATE = Config.URL_SERVICE + "leads/update";
    public static final String URL_SERVICE_LEADS_UPDATE_TYPE = Config.URL_SERVICE + "leads/updateTargetFlag";
    public static final String URL_SERVICE_LEADS_STATUS = Config.URL_SERVICE + "info/getLeadStatus";
    public static final String URL_SERVICE_LEADS_STAGE = Config.URL_SERVICE + "info/getLeadStage";
    public static final String URL_SERVICE_LEADS_CLASS = Config.URL_SERVICE + "info/getClass";
    public static final String URL_SERVICE_LEADS_REGION = Config.URL_SERVICE + "info/getRegion";
    public static final String URL_SERVICE_LEADS_PROVINCE = Config.URL_SERVICE + "info/getProvince";
    public static final String URL_SERVICE_LEADS_CHANNEL = Config.URL_SERVICE + "info/getChannel";
    public static final String URL_SERVICE_LEADS_CHAIN = Config.URL_SERVICE + "info/getChain";
    public static final String URL_SERVICE_LEADS_SALES_INFO = Config.URL_SERVICE + "info/saleByEid";

    //position
    public static final String URL_SERVICE_QUERY_DEFAULT_POSITION = Config.URL_SERVICE + "postn/queryDefaultPostn";
    public static final String URL_SERVICE_QUERY_ALL_POSITION_INFO = Config.URL_SERVICE + "postn/queryAllPostnInfo";
    public static final String URL_SERVICE_SET_DEFAULT_POSITION = Config.URL_SERVICE + "postn/setDefaultPostn";

    //task
    public static final String URL_SERVICE_TASK_ITEM_QUERY = Config.URL_SERVICE + "task/taskList";
    public static final String URL_SERVICE_TASK_ITEM_ADD = Config.URL_SERVICE + "task/addTask";
    public static final String URL_SERVICE_TASK_ITEM_UPDATE = Config.URL_SERVICE + "task/updateTask";
    public static final String URL_SERVICE_TASK_ITEM_QUERYBYID = Config.URL_SERVICE + "task/queryTaskById";
    public static final String URL_SERVICE_TASK_ITEM_QUERY_PRODUCT_BYTASKID = Config.URL_SERVICE + "task/queryProductByTaskId";
    public static final String URL_SERVICE_TASK_ITEM_ADD_TASKPRODUCT = Config.URL_SERVICE + "task/addTaskProduct";
    public static final String URL_SERVICE_TASK_ITEM_UPDATE_TASKPRODUCT = Config.URL_SERVICE + "task/updateTaskProduct";
    public static final String URL_SERVICE_TASK_ITEM_DELETE_TASKPRODUCT = Config.URL_SERVICE + "task/deleteTaskProduct";
    public static final String URL_SERVICE_TASK_ITEM_GET_ITEMCODE = Config.URL_SERVICE + "task/getItem";

    public static final String URL_SERVICE_TASK_GET_TYPE = Config.URL_SERVICE + "info/getTaskType";
    public static final String URL_SERVICE_TASK_GET_STATUS = Config.URL_SERVICE + "info/getTaskStatus";
    //stocks
    public static final String URL_SERVICE_DPNEW_INVENTORY_QUERY = Config.URL_SERVICE + "biz/itemQuery";
    public static final String PRODUCT_INFO_PATH = Config.URL_SERVICE + "biz/item";
    public static final String INVENTORY_DETAIL_PATH = Config.URL_SERVICE + "biz/inventory";
    public static final String INVENTORY_WOTS_PATH = Config.URL_SERVICE + "biz/wots";
    public static final String INVENTORY_WOTS_PATH_NEW = Config.URL_SERVICE + "biz/wotsNew";
    public static final String INVENTORY_BOOKING_PATH = Config.URL_SERVICE + "biz/booking";
    public static final String INVENTORY_ITEMCODE_PATH = Config.URL_SERVICE + "biz/queryItemCodes";
    public static final String INVENTORY_BRANDS_PATH = Config.URL_SERVICE + "biz/queryBrands";
    public static final String INVENTORY_CATEGORYS_PATH = Config.URL_SERVICE + "biz/queryCategorys";
    public static final String INVENTORY_CITYS_PATH = Config.URL_SERVICE + "biz/queryCitys";

    public static final String ITEM_CODE_DOWNLOAD_PATH = "conf/itemcode.plist";
    public static final String ITEM_CODE_STORAGE_PATH = "itemcode.plist";
    public static final String CATEGORY_NAME_JSON_DOWNLOAD_PATH = "conf/cateName.plist";
    public static final String CATEGORY_NAME_JSON_STORAGE_PATH = "cateName.plist";
    public static final String BRAND_DOWNLOAD_PATH = "conf/brand.plist";
    public static final String BRAND_STORAGE_PATH = "brand.plist";

    public static final String ITEM_CODE = "itemcode";
    public static final String BRAND = "brand";
    public static final String CATEGORY = "category";
    public static final String NAME = "category";
    public static final String CITYCODE = "citycode";
    public static final String VINTAGE = "vintage";
    public static String USERNAME = "username";
}



