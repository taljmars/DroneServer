package com.db.persistence.objectStore;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.stereotype.Component;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

import java.util.Map;

public class MyJpaTransactionManager extends JpaTransactionManager {

    private final static Logger LOGGER = Logger.getLogger(MyJpaTransactionManager.class);
    static int itr;

    public MyJpaTransactionManager() {
        itr++;
        System.out.println("Creation " + itr);
    }

    @Override
    public Map<String, Object> getJpaPropertyMap() {
        Map<String, Object> mapper = super.getJpaPropertyMap();
        for (Map.Entry<String, Object> entry : mapper.entrySet()) {
            System.out.println("-> " + entry.getKey() + " " + entry.getValue());
        }
        return mapper;
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        LOGGER.debug("DoBegin " + transaction + " " + definition);
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        LOGGER.debug("DoCommit " + status);
//        getJpaPropertyMap();
        super.doCommit(status);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
        LOGGER.debug("DoRollback " + status);
        super.doRollback(status);
    }
}
