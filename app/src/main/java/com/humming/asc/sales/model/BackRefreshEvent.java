package com.humming.asc.sales.model;

/**
 * Created by Zhtq on 18/1/11.
 */

public class BackRefreshEvent {
    private String message;

    public BackRefreshEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
