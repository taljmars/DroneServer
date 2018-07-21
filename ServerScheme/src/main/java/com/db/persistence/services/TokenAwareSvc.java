package com.db.persistence.services;

import com.db.persistence.workSession.WorkSession;
import org.springframework.transaction.annotation.Transactional;

public interface TokenAwareSvc<T> {

    @Transactional
    <S extends TokenAwareSvc> S setToken(T token);

    @Transactional
    T getToken();

    @Transactional
    WorkSession workSession();

}
