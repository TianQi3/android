package com.humming.asc.sales.service;

/**
 * Created by Zhtq on 12/22/15.
 */
public interface IAuthService {
    void login(ICallback callback, String username, String password);
}
