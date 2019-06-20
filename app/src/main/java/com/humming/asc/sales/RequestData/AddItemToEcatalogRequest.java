package com.humming.asc.sales.RequestData;


import com.humming.asc.sales.service.IRequestMainData;

import java.util.List;

/**
 * Created by Zhtq on 16/8/5.
 */
public class AddItemToEcatalogRequest implements IRequestMainData {
    private String id;
    private List<String> itemCode;

    public AddItemToEcatalogRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getItemCode() {
        return itemCode;
    }

    public void setItemCode(List<String> itemCode) {
        this.itemCode = itemCode;
    }
}
