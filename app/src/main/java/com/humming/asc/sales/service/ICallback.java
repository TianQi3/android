package com.humming.asc.sales.service;

/**
 * Created by Zhtq on 1/4/16.
 */
public interface ICallback<T> {
    void onDataReady(T data);
    void onError(Throwable throwable);
}
