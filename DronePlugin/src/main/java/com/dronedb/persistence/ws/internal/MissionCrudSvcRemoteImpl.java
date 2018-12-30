/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.dronedb.persistence.ws.internal;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.services.MissionCrudSvc;
import com.dronedb.persistence.ws.MissionCrudSvcRemote;
import javassist.tools.rmi.ObjectNotFoundException;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Created by taljmars on 3/24/17.
 */
@RestController
public class MissionCrudSvcRemoteImpl implements MissionCrudSvcRemote
{
    private final static Logger LOGGER = Logger.getLogger(MissionCrudSvcRemoteImpl.class);

    @Autowired
    private MissionCrudSvc missionCrudSvc;

    /**
     * Will clone the mission object and every mission item.
     * Mind the the objid of each object and subobject is being regenerated
     * @param mission
     * @return
     * @throws DatabaseValidationRemoteException
     */
    @Override
    @RequestMapping(value = "/cloneMission", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<Mission> cloneMission(@RequestBody Mission mission) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException {
        try {
            Mission clonedMission = (Mission) missionCrudSvc.cloneMission(mission).copy();
            return new ResponseEntity<Mission>(clonedMission, HttpStatus.OK);
        }
        catch (DatabaseValidationException e) {
            LOGGER.error("Failed to clone mission", e);
            throw new DatabaseValidationRemoteException(e.getMessage());
        }
        catch (ObjectNotFoundException e) {
            LOGGER.error("Failed to clone mission", e);
            throw new ObjectNotFoundRemoteException(e.getMessage());
        }
        catch (ObjectInstanceException e) {
            LOGGER.error("Failed to clone mission", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }

    @Override
    @RequestMapping(value = "/createMissionItem", method = RequestMethod.GET)
    @ResponseBody
    @SuppressWarnings("unchecked")
//    public <T extends MissionItem> T createMissionItem(@RequestParam String clz) throws ObjectInstanceRemoteException {
    public <T extends MissionItem> ResponseEntity<T> createMissionItem(@RequestParam String clz) {
//        try {
            LOGGER.debug("Crud mission item REMOTE CREATE called '" + clz + "'");
            try {
                T t = (T) missionCrudSvc.createMissionItem(clz).copy();
                LOGGER.debug("TALMA Mission Item Crud REMOTE CREATE called " + t);
                return new ResponseEntity<T>(t, HttpStatus.OK);
//            return (T) missionCrudSvc.createMissionItem(clz).copy();
            }
            catch (ObjectInstanceException e) {
                LOGGER.error("Failed to create object", e);
//			throw new ObjectInstanceRemoteException("Failed to create object");
                throw new RuntimeException("Failed to create object");
            }
//        }
//        catch (ObjectInstanceException e) {
//            LOGGER.error("Failed to create mission item", e);
//            throw new ObjectInstanceRemoteException(e.getMessage());
//        }
    }

    @Override
    @RequestMapping(value = "/createMission", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<Mission> createMission() throws ObjectInstanceRemoteException {
        try {
            Mission mission = (Mission) missionCrudSvc.createMission().copy();
            return new ResponseEntity<Mission>(mission, HttpStatus.OK);
        }
        catch (ObjectInstanceException e) {
            LOGGER.error("Failed to create mission", e);
            throw new ObjectInstanceRemoteException(e.getMessage());
        }
    }
}
