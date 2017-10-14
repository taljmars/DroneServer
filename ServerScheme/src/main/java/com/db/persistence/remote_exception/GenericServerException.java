package com.db.persistence.remote_exception;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@JsonTypeInfo(use=JsonTypeInfo.Id.CLASS, include=JsonTypeInfo.As.EXTERNAL_PROPERTY, property="clz")
public abstract class GenericServerException extends Exception {

    public GenericServerException(String message) {
        super(message);
    }

}
