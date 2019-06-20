package com.humming.asc.sales.RequestData;


import com.humming.asc.sales.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class ProductListRequest implements IRequestMainData {
    private String search;
    private String sortType;
    private String sortRule;
    private String pageNo;
    private FilterEntity data;
    private String exclusiveChannelML;
    private String exclusiveChannelHK;

    public FilterEntity getData() {
        return data;
    }

    public void setData(FilterEntity data) {
        this.data = data;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public String getSortRule() {
        return sortRule;
    }

    public void setSortRule(String sortRule) {
        this.sortRule = sortRule;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getExclusiveChannelML() {
        return exclusiveChannelML;
    }

    public void setExclusiveChannelML(String exclusiveChannelML) {
        this.exclusiveChannelML = exclusiveChannelML;
    }

    public String getExclusiveChannelHK() {
        return exclusiveChannelHK;
    }

    public void setExclusiveChannelHK(String exclusiveChannelHK) {
        this.exclusiveChannelHK = exclusiveChannelHK;
    }
}
