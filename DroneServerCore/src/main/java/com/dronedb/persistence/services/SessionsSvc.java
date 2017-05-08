package com.dronedb.persistence.services;

/**
 * Created by oem on 4/28/17.
 */
public interface SessionsSvc {

    /**
     * The following publish all the private changes to the public database.
     * After this action no object should exist under the private session of the user.
     */
    void publish();

    /**
     * The following discard all the changes the user have made in it session.
     * After this action no object should exist under the private session.
     */
    void discard();
}
