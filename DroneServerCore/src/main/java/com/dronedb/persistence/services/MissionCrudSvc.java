package com.dronedb.persistence.services;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import javassist.tools.rmi.ObjectNotFoundException;

/**
 * Created by taljmars on 3/24/17.
 */
public interface MissionCrudSvc {

    Mission cloneMission(Mission mission) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException;

    Mission createMission() throws ObjectInstanceException;

    <T extends MissionItem> T createMissionItem(Class<T> clz) throws ObjectInstanceException;
}
