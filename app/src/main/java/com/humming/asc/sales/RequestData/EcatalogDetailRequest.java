package com.humming.asc.sales.RequestData;


import com.humming.asc.sales.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class EcatalogDetailRequest implements IRequestMainData {
    private String id;

    public EcatalogDetailRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
