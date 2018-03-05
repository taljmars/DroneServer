package com.db.server.transactions;

import org.apache.log4j.Logger;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.DefaultTransactionStatus;

public class MyDataSourceTransactionManager extends DataSourceTransactionManager {

    private final static Logger LOGGER = Logger.getLogger(MyDataSourceTransactionManager.class);
    static int itr;

    public MyDataSourceTransactionManager() {
        itr++;
//        LOGGER.debug("Creation " + itr);
    }

    @Override
    protected void doBegin(Object transaction, TransactionDefinition definition) {
        LOGGER.error("DoBegin " + transaction + " " + definition);
        super.doBegin(transaction, definition);
    }

    @Override
    protected void doCommit(DefaultTransactionStatus status) {
        LOGGER.error("DoCommit " + status);
//        getJpaPropertyMap();
        super.doCommit(status);
    }

    @Override
    protected void doRollback(DefaultTransactionStatus status) {
//        LOGGER.debug("DoRollback " + status);
        super.doRollback(status);
    }

}
