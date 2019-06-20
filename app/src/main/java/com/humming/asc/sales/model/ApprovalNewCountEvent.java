package com.humming.asc.sales.model;

/**
 * Created by Zhtq on 18/1/11.
 */

public class ApprovalNewCountEvent {

    private String message;

    public ApprovalNewCountEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

}
