package com.dronedb.persistence.scheme;

/**
 * Created by oem on 4/30/17.
 */
public class Constants {

    // Values
    public static final int TIP_REVISION = Integer.MAX_VALUE;

    // Queries
    public static final String MISSION_QUERY_FROM_TIP_AND_PRIVATE =
                    // Not in private sessions
                    "(objId, toRevision) NOT IN ( " +
                    "SELECT objId, toRevision FROM mission " +
                    "WHERE NOT deleted AND privatelyModified = true " +
                    "GROUP BY objId, privatelyModified, toRevision " +
                    ") " +
                    // And in public session, this and the above give us the ones only in the public !
                    "AND (objId, toRevision) IN (" +
                    "SELECT objId, MAX(toRevision) " +
                    "FROM mission " +
                    "WHERE NOT deleted AND privatelyModified = false AND torevision=" + Constants.TIP_REVISION + " " +
                    "GROUP BY objId " +
                    ") " +
                    "GROUP BY objId, privatelyModified, toRevision " +
                    "UNION " +
                    // Getting the private sessions missions
                    "SELECT * FROM mission " +
                    "WHERE NOT deleted AND privatelyModified = true " +
                    "GROUP BY objId, privatelyModified, toRevision ";
}
