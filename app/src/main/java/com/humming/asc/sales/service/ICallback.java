package com.humming.asc.sales.service;

/**
 * Created by PuTi(编程即菩提) on 1/4/16.
 */
public interface ICallback<T> {
    void onDataReady(T data);
    void onError(Throwable throwable);
}
