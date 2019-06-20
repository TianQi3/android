package com.humming.asc.sales.RequestData;

import com.humming.asc.sales.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class MyEcatalogRequest implements IRequestMainData {
    private String sortType;// 排序类型, 0为全部, 1为草稿, 2已发送, 3为MFR
    private String pagable;

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getPagable() {
        return pagable;
    }

    public void setPagable(String pagable) {
        this.pagable = pagable;
    }
}
