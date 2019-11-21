package com.humming.asc.sales;

/**
 * Created by Ztq on 12/22/15.
 */
public class Config {
    public static final boolean DEBUG = false;
    //public static final String URL_SERVICE = "http://101.231.101.70:8089/dp/service/";//测试环境
    //public static final String URL_SERVICE = "http://172.30.129.200:8080/dp/service/";//测试环境
    //public static final String URL_SERVICE = "http://172.30.17.75:8080/dp/service/";//测试环境
    public static final String URL_SERVICE = "http://sm.asc-wines.com/dp/service/";//正式环境


    /*ecatalog 地址*/
    //产品详情webView 地址
    //public static final String WEBVIEW_URL = "http://101.231.101.70:8089/dp/catalog/itemDetails/";//测试
     public static final String WEBVIEW_URL = "http://sm.asc-wines.com/dp/catalog/itemDetails/";//正式
    //public static final String URL_SERVICE_ECATALOG = "http://test.mdm.asc-wines.com/cgi";//测试ecatalog地址
     public static final String URL_SERVICE_ECATALOG = "http://mdm.asc-wines.com/cgi";//正式ecatalog地址


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

    public static final String URL_SERVICE_QUERY_ALL_EMPLOYEE = Config.URL_SERVICE + "dc/queryAllEmployee";


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


    //MF
    public static final String MF_REQUEST_LIST = Config.URL_SERVICE + "mfRequest/list";
    public static final String MF_REQUEST_LIST_FROM_CUSTOMER = Config.URL_SERVICE + "mfRequest/listFromCustomer";
    public static final String MF_DETAIL = Config.URL_SERVICE + "mfRequest/detail";
    public static final String MF_AUDIT = Config.URL_SERVICE + "mfRequest/audit";
    public static final String MF_ADD = Config.URL_SERVICE + "mfRequest/add";
    public static final String MF_UPDATE = Config.URL_SERVICE + "mfRequest/update";
    public static final String MF_ITEM_CODE = Config.URL_SERVICE + "biz/cost";
    public static final String MF_CUSTOMER = Config.URL_SERVICE + "biz/accForMf";
    public static final String CURRENCY = Config.URL_SERVICE + "biz/mfInfo";
    public static final String AUDIT_STAUS = Config.URL_SERVICE + "adBaseInfo/aduitMenu";
    public static final String FROM_CUSTOMER_MFQUEST_INFO = Config.URL_SERVICE + "biz/accForMfByCustomerCode";
    public static final String BASEINFO_NEWCOUNT = Config.URL_SERVICE + "adBaseInfo/newCount";
    public static final String UPDATE_STAGE = Config.URL_SERVICE + "mfRequest/updateStage";
    //sample
    public static final String SAMPLE_LIST = Config.URL_SERVICE + "sample/list";
    public static final String SAMPLE_DETAIL = Config.URL_SERVICE + "sample/detail";
    public static final String SAMPLE_AUDIT = Config.URL_SERVICE + "sample/audit";
    //payment
    public static final String PAYMENT_LIST = Config.URL_SERVICE + "payment/list";
    public static final String PAYMENT_DETAIL = Config.URL_SERVICE + "payment/detail";
    public static final String PAYMENT_AUDIT = Config.URL_SERVICE + "payment/audit";


    public static final String ITEM_CODE_DOWNLOAD_PATH = "conf/itemcode.plist";
    public static final String ITEM_CODE_STORAGE_PATH = "itemcode.plist";
    public static final String CATEGORY_NAME_JSON_DOWNLOAD_PATH = "conf/cateName.plist";
    public static final String CATEGORY_NAME_JSON_STORAGE_PATH = "cateName.plist";
    public static final String BRAND_DOWNLOAD_PATH = "conf/brand.plist";
    public static final String BRAND_STORAGE_PATH = "brand.plist";


    //product   Ecatalog
    public static final String ECATALLOG_SEARCH_HISTORY = "ecatalog/search/history";//搜索历史
    public static final String ECATALLOG_PRODUCT_LIST = "ecatalog/product/list"; // 产品列表
    public static final String ECATALLOG_STOCK_BY_ITEMCODE = "ecatalog/product/stockByItemCode";//展开库存
    public static final String ECATALLOG_PRODUCT_STOCK = "ecatalog/product/stockDetailByItemCode";//库存详情
    public static final String ECATALLOG_SEARCH_UDC = "ecatalog/search/udc";//筛选条件
    public static final String ECATALLOG_STOCK_WARN = "ecatalog/product/stockEarlyWarning";//库存预警
    public static final String ECATALLOG_STOCK_WARN_CANCEL = "ecatalog/product/cancelEarlyWarning";//库存预警
    public static final String ECATALLOG_PRODUCT_BOOKING = "ecatalog/product/booking";//预定记录
    public static final String ECATALLOG_ENPTY_HISTORY = "ecatalog/search/emptyHistory";//清空搜索历史
    public static final String ECATALLOG_MY_ECATALOG = "ecatalog/ecatalog/list";//我的报价单
    public static final String ECATALLOG_MY_ECATALOG_NO_PAGE = "ecatalog/ecatalog/NoPageablelist";//我的报价单没有分页
    public static final String ECATALLOG_ADD_ITEM = "ecatalog/ecatalog/addItem";//添加产品到我的报价单
    public static final String ECATALLOG_COLLECT_PRODUCT = "ecatalog/product/collection";//加入收藏
    public static final String ECATALLOG_CREATE_ECATALOG = "ecatalog/ecatalog/create";//创建报价单
    public static final String ECATALLOG_DETAIL_ECATALOG = "ecatalog/ecatalog/detail";//报价单详情
    public static final String ECATALLOG_STATUS_ECATALOG = "ecatalog/ecatalog/status";//报价单状态时间轴
    public static final String ECATALLOG_ECATALOG_DELETE = "ecatalog/ecatalog/del";//删除报价单
    public static final String ECATALLOG_ECATALOG_ROOF = "ecatalog/ecatalog/roofPlacement";//报价单产品置顶
    public static final String ECATALLOG_ECATALOG_MARGIN = "ecatalog/ecatalog/margin";//查询margin
    public static final String ECATALLOG_ECATALOG_ISCOLLECTION = "ecatalog/ecatalog/isCollection";//是否收藏
    public static final String ECATALLOG_USER_DETAIL = "ecatalog/user/detail";//用户信息
    public static final String ECATALLOG_VALIDITYDATE = "ecatalog/ecatalog/validityDate";//报价单设置有效期
    public static final String ECATALLOG_ECATALOG_EDIT = "ecatalog/ecatalog/edit";//编辑报价单
    public static final String ECATALLOG_ECATALOG_SAVESET = "ecatalog/ecatalog/saveSet";//保存报价单设置
    public static final String ECATALLOG_ECATALOG_SET = "ecatalog/ecatalog/set";//报价单设置信息
    public static final String ECATALLOG_ECATALOG_DELETE_ITEM = "ecatalog/ecatalog/delItem";//删除产品
    public static final String ECATALLOG_USER_NOTICE = "ecatalog/user/notice";//消息列表
    public static final String ECATALLOG_ECATALOG_VINTAGE = "ecatalog/ecatalog/itemVintage";//报价单产品数据内选择年份
    public static final String ECATALLOG_COLLECT_LIST = "ecatalog/collection/list";//收藏夹列表
    public static final String ECATALLOG_COLLECT_DELITEM = "ecatalog/collection/delItem";//删除收藏产品
    public static final String ECATALLOG_ECATALOG_SEND = "ecatalog/ecatalog/send";//发送
    public static final String ECATALLOG_ECATALOG_STATUS_UPDATE = "ecatalog/ecatalog/updateStatus";//状态


    public static final String ITEM_CODE = "itemcode";
    public static final String BRAND = "brand";
    public static final String CATEGORY = "category";
    public static final String NAME = "category";
    public static final String CITYCODE = "citycode";
    public static final String VINTAGE = "vintage";
    public static String USERNAME = "username";


}



