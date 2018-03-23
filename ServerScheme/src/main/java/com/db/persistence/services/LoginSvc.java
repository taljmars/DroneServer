package com.db.persistence.services;

import com.db.persistence.scheme.*;

public interface LoginSvc extends TokenAwareSvc {

    LoginResponse login(LoginRequest loginRequest);

    // Token Aware
    LogoutResponse logout();

    // Token Aware
    KeepAliveResponse keepAlive();

//    User getUserByName(String userName);
}
