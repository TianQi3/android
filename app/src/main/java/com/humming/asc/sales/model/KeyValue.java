package com.humming.asc.sales.model;

/**
 * Created by Zhtq on 1/7/16.
 */
public final class KeyValue{
    private String key;
    private int value;

    public KeyValue(){

    }
    public KeyValue(String key, int value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
