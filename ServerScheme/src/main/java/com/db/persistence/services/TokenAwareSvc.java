package com.db.persistence.services;

import com.db.persistence.workSession.WorkSession;
import org.springframework.context.annotation.Scope;
import org.springframework.transaction.annotation.Transactional;

public interface TokenAwareSvc {

    @Transactional
    <T extends TokenAwareSvc> T setToken(String token);

    @Transactional
    String getToken();

    @Transactional
    void flushToken();

    @Transactional
    WorkSession workSession();
}
