package com.dronedb.persistence.services;

import com.dronedb.persistence.scheme.Mission;

/**
 * Created by oem on 3/24/17.
 */
public interface MissionCrudSvc {

    Mission cloneMission(Mission mission);

}
