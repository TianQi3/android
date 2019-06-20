package com.humming.asc.sales.service;

import com.humming.asc.sales.Application;

/**
 * Created by Zhtq on 12/31/15.
 */
public class MAuthService implements IAuthService {
    @Override
    public void login(ICallback callback, String username, String password) {
        int state = 1;
        String token = "mock_token";
        String userId = "mock_userId";

        Application.setToken(token);
        Application.setUserId(userId);
        callback.onDataReady(state == 1);
    }
}
