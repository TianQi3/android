package com.humming.asc.sales.RequestData;


import com.humming.asc.sales.service.IRequestMainData;

/**
 * Created by Zhtq on 16/8/5.
 */
public class EcatalogSettingRequest implements IRequestMainData {
    private String id;
    private String readNotification; // 是否收到客户已读通知, 0为否, 1为是
    private String saveTemplate;// 是否生成后保存模板, 0为否, 1为是

    public EcatalogSettingRequest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReadNotification() {
        return readNotification;
    }

    public void setReadNotification(String readNotification) {
        this.readNotification = readNotification;
    }

    public String getSaveTemplate() {
        return saveTemplate;
    }

    public void setSaveTemplate(String saveTemplate) {
        this.saveTemplate = saveTemplate;
    }
}
