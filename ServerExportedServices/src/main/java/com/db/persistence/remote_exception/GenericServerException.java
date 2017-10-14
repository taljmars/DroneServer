package com.db.persistence.remote_exception;


public abstract class GenericServerException extends Exception {

//    protected Class clz;

    public GenericServerException(String message) {
        super(message);
    }

}
