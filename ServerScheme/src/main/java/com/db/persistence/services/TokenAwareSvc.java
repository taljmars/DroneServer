package com.db.persistence.services;

import org.springframework.transaction.annotation.Transactional;

public interface TokenAwareSvc {

    @Transactional
    void setToken(String token);

    @Transactional
    String getToken();

    @Transactional
    void flushToken();

}
