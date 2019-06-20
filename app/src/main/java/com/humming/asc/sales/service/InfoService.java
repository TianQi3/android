package com.humming.asc.sales.service;

import com.humming.asc.dp.presentation.vo.cp.baseinfo.MenuResultVO;
import com.humming.asc.sales.Config;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zhtq on 12/22/15.
 */
public class InfoService extends AbstractService{
    public void query(ICallback callback,String type) {
        Map<String,String> params = new HashMap<String,String>();
        params.put("type",type);
        this.get(Config.URL_SERVICE_INFO_BASE_DATA,params,callback,MenuResultVO.class,null);
    }
}
