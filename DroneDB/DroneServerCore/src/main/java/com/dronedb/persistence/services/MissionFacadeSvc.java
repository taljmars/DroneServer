package com.dronedb.persistence.services;

import com.dronedb.persistence.scheme.BaseObject;
import com.dronedb.persistence.scheme.Mission;

import java.util.List;
import java.util.Set;

/**
 * Created by taljmars on 3/18/17.
 */
public interface MissionFacadeSvc {
    Mission write(Mission mission);

    void delete(Mission mission);

    Set<Mission> getAll();

}
