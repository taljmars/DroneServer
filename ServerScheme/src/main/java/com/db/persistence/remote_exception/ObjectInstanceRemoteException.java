package com.db.persistence.remote_exception;

import javax.xml.ws.WebFault;

/**
 * Created by taljmars on 5/8/17.
 */
@WebFault
//public class ObjectInstanceRemoteException extends Throwable {
public class ObjectInstanceRemoteException extends GenericServerException {
    public ObjectInstanceRemoteException(String e) {
        super(e);
    }
}
