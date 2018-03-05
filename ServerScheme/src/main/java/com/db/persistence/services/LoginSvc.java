package com.db.persistence.services;

import com.db.persistence.scheme.KeepAliveResponse;
import com.db.persistence.scheme.LoginRequest;
import com.db.persistence.scheme.LoginResponse;
import com.db.persistence.scheme.LogoutResponse;

public interface LoginSvc extends TokenAwareSvc {

    LoginResponse login(LoginRequest loginRequest);

    // Token Aware
    LogoutResponse logout();

    // Token Aware
    KeepAliveResponse keepAlive();

}
