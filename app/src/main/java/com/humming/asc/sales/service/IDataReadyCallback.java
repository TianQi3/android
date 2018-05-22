package com.humming.asc.sales.service;

public interface IDataReadyCallback<T> {
	public abstract void onDataReady(T result, RESTException restException);
}
