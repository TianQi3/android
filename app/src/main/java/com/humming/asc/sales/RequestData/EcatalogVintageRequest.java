package com.humming.asc.sales.RequestData;


import com.humming.asc.sales.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class EcatalogVintageRequest implements IRequestMainData {
    private String id;
    private String vintage;

    public EcatalogVintageRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getVintage() {
        return vintage;
    }

    public void setVintage(String vintage) {
        this.vintage = vintage;
    }
}
