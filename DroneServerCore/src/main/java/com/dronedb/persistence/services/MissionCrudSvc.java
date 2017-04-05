package com.dronedb.persistence.services;

import com.dronedb.persistence.exception.DatabaseValidationException;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;

/**
 * Created by oem on 3/24/17.
 */
public interface MissionCrudSvc {

    Mission cloneMission(Mission mission) throws DatabaseValidationException;

    Mission createMission();

    <T extends MissionItem> T createMissionItem(Class<T> clz);
}
