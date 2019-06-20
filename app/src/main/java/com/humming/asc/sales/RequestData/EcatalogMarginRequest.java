package com.humming.asc.sales.RequestData;


import com.humming.asc.sales.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class EcatalogMarginRequest implements IRequestMainData {
    private String id;
    private String unitPrice;
    private String num;
    private String tradValue;

    public EcatalogMarginRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getNum() {
        return num;
    }

    public void setNum(String num) {
        this.num = num;
    }

    public String getTradValue() {
        return tradValue;
    }

    public void setTradValue(String tradValue) {
        this.tradValue = tradValue;
    }
}
