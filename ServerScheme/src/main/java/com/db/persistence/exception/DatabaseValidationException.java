package com.db.persistence.exception;

/**
 * Created by taljmars on 4/5/17.
 */
public class DatabaseValidationException extends Exception {

    public DatabaseValidationException(Exception e) {
        super(e);
    }

    public DatabaseValidationException(String message) {
        super(message);
    }
}
