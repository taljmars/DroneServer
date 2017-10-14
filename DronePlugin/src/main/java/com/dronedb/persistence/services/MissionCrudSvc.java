package com.dronedb.persistence.services;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import javassist.tools.rmi.ObjectNotFoundException;

/**
 * Created by taljmars on 3/24/17.
 */
public interface MissionCrudSvc {

    /**
     * The following function responsible of cloning any mission exist in the database.
     * The cloned mission will have the same characteristics but with a different UUID.
     *
     * @param mission Required mission to be cloned
     * @return The cloned mission
     * @throws DatabaseValidationException in case of any issue with the fields in it
     * @throws ObjectNotFoundException in case the object or sub-object of it haven't been found
     * @throws ObjectInstanceException in case on any issue occur during the new clone creation
     */
    Mission cloneMission(Mission mission) throws DatabaseValidationException, ObjectNotFoundException, ObjectInstanceException;

    /**
     * The following function create a new mission, the new mission with have a fresh UUID but it will not
     * be written do the database. (this can be later be made by {@link ObjectCrudSvc}).
     * @return A new mission
     * @throws ObjectInstanceException in case of any issue during the mission creation
     */
    Mission createMission() throws ObjectInstanceException;

    /**
     * The following create a new mission item (waypoint, takeoff point, etc..), similar the the mission creation,
     * The return value we have a fresh UUID but it won't be written to the database.
     * @param clz The mission item type
     * @return A new instanse of the required mission item type
     * @throws ObjectInstanceException in case of any issue with the new item creation
     */
    <T extends MissionItem> T createMissionItem(String clz) throws ObjectInstanceException;
}
