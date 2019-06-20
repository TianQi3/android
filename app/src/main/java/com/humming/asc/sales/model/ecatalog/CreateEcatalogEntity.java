package com.humming.asc.sales.model.ecatalog;

import java.io.Serializable;

/**
 * Created by Zhtq on 19/4/11.
 */

public class CreateEcatalogEntity implements Serializable {
    private String title;
    private String ecatalogId;
    private String userName;
    private String contract;
    private String phone;
    private String date;

    public CreateEcatalogEntity() {
    }

    public String getEcatalogId() {
        return ecatalogId;
    }

    public void setEcatalogId(String ecatalogId) {
        this.ecatalogId = ecatalogId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContract() {
        return contract;
    }

    public void setContract(String contract) {
        this.contract = contract;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
