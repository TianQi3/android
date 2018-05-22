package com.humming.asc.sales.service;

import java.util.List;

public interface ISettingService {


	public abstract void getItemcodeList(
			IDataReadyCallback<List<String>> dataReadyCallback);
	public abstract void getCategoryNameList(
			IDataReadyCallback<List<String>> dataReadyCallback);
	public abstract void getBrandList(
			IDataReadyCallback<List<String>> dataReadyCallback);
}