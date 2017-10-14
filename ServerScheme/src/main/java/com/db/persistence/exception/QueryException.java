package com.db.persistence.exception;

/**
 * Created by taljmars on 4/5/17.
 */
public class QueryException extends Exception {

    public QueryException(Exception e) {
        super(e);
    }

    public QueryException(String message) {
        super(message);
    }
}
