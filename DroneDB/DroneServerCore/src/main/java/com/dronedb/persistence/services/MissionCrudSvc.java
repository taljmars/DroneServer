package com.dronedb.persistence.services;

import com.dronedb.persistence.scheme.mission.Mission;

/**
 * Created by oem on 3/24/17.
 */
public interface MissionCrudSvc {

    Mission cloneMission(Mission mission);

}
