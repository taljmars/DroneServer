package com.dronedb.persistence.ws.wsRest;

import com.db.persistence.remote_exception.DatabaseValidationRemoteException;
import com.db.persistence.remote_exception.ObjectInstanceRemoteException;
import com.db.persistence.remote_exception.ObjectNotFoundRemoteException;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Created by taljmars on 3/24/17.
 */
public interface MissionCrudRestSvcRemote {

    @RequestMapping(value = "/cloneMission", method = RequestMethod.POST)
    @ResponseBody
    ResponseEntity<Mission> cloneMission(@RequestBody Mission mission) throws DatabaseValidationRemoteException, ObjectNotFoundRemoteException, ObjectInstanceRemoteException;

    @RequestMapping(value = "/createMissionItem", method = RequestMethod.GET)
    @ResponseBody
    <T extends MissionItem> ResponseEntity<T> createMissionItem(@RequestParam String clz) throws ObjectInstanceRemoteException;

    @RequestMapping(value = "/createMission", method = RequestMethod.GET)
    @ResponseBody
    ResponseEntity<Mission> createMission() throws ObjectInstanceRemoteException;

}
