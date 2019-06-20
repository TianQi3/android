package com.humming.asc.sales.RequestData;


import com.humming.asc.sales.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class StockByItemRequest implements IRequestMainData {
    private String itemCode;

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }
}
