package com.humming.asc.sales.service;

/**
 * Created by PuTi(编程即菩提) on 12/22/15.
 */
public interface IAuthService {
    void login(ICallback callback, String username, String password);
}
