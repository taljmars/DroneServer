package com.db.persistence.remote_exception;

import javax.xml.ws.WebFault;

/**
 * Created by taljmars on 4/5/17.
 */
@WebFault
public class DatabaseValidationRemoteException extends GenericServerException {

    public DatabaseValidationRemoteException(String message) {
        super(message);
    }
}
