package com.db.persistence.exception;

/**
 * Created by taljmars on 5/8/17.
 */
public class ObjectInstanceException extends Exception {

    public ObjectInstanceException(Exception e) {
        super(e);
    }

    public ObjectInstanceException(String message) {
        super(message);
    }
}
