package com.humming.asc.sales.RequestData;


import com.humming.asc.sales.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class StockWarnRequest implements IRequestMainData {
    private String itemCode;
    private String vintage;
    private String whouseName;
    private String earlyWarning;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }

    public String getWhouseName() {
        return whouseName;
    }

    public void setWhouseName(String whouseName) {
        this.whouseName = whouseName;
    }

    public String getEarlyWarning() {
        return earlyWarning;
    }

    public void setEarlyWarning(String earlyWarning) {
        this.earlyWarning = earlyWarning;
    }
}
