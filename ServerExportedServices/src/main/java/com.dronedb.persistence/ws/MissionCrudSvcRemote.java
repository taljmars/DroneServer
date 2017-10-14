package com.dronedb.persistence.ws;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by taljmars on 3/24/17.
 */
//@WebService(targetNamespace = "http://scheme.persistence.dronedb.com/")
@WebService
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface MissionCrudSvcRemote {

    @WebMethod
    Mission cloneMission(@WebParam Mission mission) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException;

    @WebMethod
    <T extends MissionItem> T createMissionItem(@WebParam Class<T> clz) throws ObjectInstanceRemoteException;

    @WebMethod
    Mission createMission() throws ObjectInstanceRemoteException;

}
