package com.humming.asc.sales.service;

import com.humming.asc.dp.presentation.vo.cp.ResultVO;
import com.humming.asc.dp.presentation.vo.cp.VersionInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.postn.PositionInfoResultVO;
import com.humming.asc.dp.presentation.vo.cp.postn.PositionInfoVO;
import com.humming.asc.sales.Config;
import com.humming.asc.sales.model.PositionRequest;

/**
 * Created by PuTi(编程即菩提) on 12/22/15.
 */
public class VersionService extends AbstractService {
    public void query(ICallback callback) {
        //  Map<String,String> params = new HashMap<String,String>();
        // params.put("type",type);
        this.get(Config.URL_VERSION_CONTROLLER, null, callback, VersionInfoResultVO.class, null);
    }

    public void queryAllPostnInfo(ICallback callback) {
        //  Map<String,String> params = new HashMap<String,String>();
        // params.put("type",type);
        this.get(Config.URL_SERVICE_QUERY_ALL_POSITION_INFO, null, callback, PositionInfoResultVO.class, null);
    }

    public void setDefaultPostn(ICallback callback, PositionRequest rowId) {
        this.post(Config.URL_SERVICE_SET_DEFAULT_POSITION, rowId, callback, ResultVO.class, null);
    }

    public void queryDefaultPostn(ICallback callback) {
        //  Map<String,String> params = new HashMap<String,String>();
        // params.put("type",type);
        this.get(Config.URL_SERVICE_QUERY_DEFAULT_POSITION, null, callback, PositionInfoVO.class, null);
    }
}
