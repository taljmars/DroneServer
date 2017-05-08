package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.*;
import com.dronedb.persistence.services.MissionCrudSvc;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebService;

/**
 * Created by taljmars on 3/24/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.MissionCrudSvcRemote")
public class MissionCrudSvcRemoteImpl implements MissionCrudSvcRemote
{
    final static Logger logger = Logger.getLogger(MissionCrudSvcRemoteImpl.class);

    @Autowired MissionCrudSvc missionCrudSvc;

    /**
     * Will clone the mission object and every mission item.
     * Mind the the objid of each object and subobject is being regenerated
     * @param mission
     * @return
     * @throws DatabaseValidationRemoteException
     */
    @Override
    public Mission cloneMission(Mission mission) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException {
        try {
            return (Mission) missionCrudSvc.cloneMission(mission).copy();
        }
        catch (DatabaseValidationException e) {
            logger.error("Failed to clone mission", e);
            throw new DatabaseValidationRemoteException(e.getMessage());
        }
        catch (ObjectNotFoundException e) {
            logger.error("Failed to clone mission", e);
            throw new ObjectNotFoundRemoteException(e.getMessage());
        }
        catch (ObjectInstanceException e) {
            logger.error("Failed to clone mission", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }

    @Override
    public <T extends MissionItem> T createMissionItem(Class<T> clz) throws ObjectInstanceRemoteException {
        try {
            return (T) missionCrudSvc.createMissionItem(clz).copy();
        }
        catch (ObjectInstanceException e) {
            logger.error("Failed to create mission item", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }

    @Override
    public Mission createMission() throws ObjectInstanceRemoteException {
        try {
            return (Mission) missionCrudSvc.createMission().copy();
        }
        catch (ObjectInstanceException e) {
            logger.error("Failed to create mission", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }
}
