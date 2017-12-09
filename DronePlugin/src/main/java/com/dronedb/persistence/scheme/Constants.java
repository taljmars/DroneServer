package com.dronedb.persistence.scheme;

import static com.db.persistence.scheme.Constants.TIP_REVISION;

/**
 * Created by taljmars on 4/30/17.
 */
public class Constants {

    // Queries
    public static final String MISSION_QUERY_FROM_TIP_AND_PRIVATE =
            // Not in private sessions
            "(objId, toRevision) NOT IN ( " +
                    "SELECT objId, toRevision FROM Mission " +
                    "WHERE NOT deleted AND entityManagerCtx != 0 " +
                    "GROUP BY objId, entityManagerCtx, toRevision " +
                    ") " +
                    // And in public session, this and the above give us the ones only in the public !
                    "AND (objId, toRevision) IN (" +
                    "SELECT objId, MAX(toRevision) " +
                    "FROM Mission " +
                    "WHERE NOT deleted AND entityManagerCtx = 0 AND torevision=" + TIP_REVISION + " " +
                    "GROUP BY objId " +
                    ") " +
                    "GROUP BY objId, entityManagerCtx, toRevision " +
                    "UNION " +
                    // Getting the private sessions missions
                    "SELECT * FROM Mission " +
                    "WHERE NOT deleted AND entityManagerCtx != 0 " +
                    "GROUP BY objId, entityManagerCtx, toRevision ";

    public static final String POLYGON_PERIMETER_QUERY_FROM_TIP_AND_PRIVATE =
            // Not in private sessions
            "(objId, toRevision) NOT IN ( " +
                    "SELECT objId, toRevision FROM PolygonPerimeter " +
                    "WHERE NOT deleted AND entityManagerCtx != 0 " +
                    "GROUP BY objId, entityManagerCtx, toRevision " +
                    ") " +
                    // And in public session, this and the above give us the ones only in the public !
                    "AND (objId, toRevision) IN (" +
                    "SELECT objId, MAX(toRevision) " +
                    "FROM PolygonPerimeter " +
                    "WHERE NOT deleted AND entityManagerCtx = 0 AND torevision=" + TIP_REVISION + " " +
                    "GROUP BY objId " +
                    ") " +
                    "GROUP BY objId, entityManagerCtx, toRevision " +
                    "UNION " +
                    // Getting the private sessions polygonperimeters
                    "SELECT * FROM PolygonPerimeter " +
                    "WHERE NOT deleted AND entityManagerCtx != 0 " +
                    "GROUP BY objId, entityManagerCtx, toRevision ";

    public static final String CIRCLE_PERIMETER_QUERY_FROM_TIP_AND_PRIVATE =
            // Not in private sessions
            "(objId, toRevision) NOT IN ( " +
                    "SELECT objId, toRevision FROM CirclePerimeter " +
                    "WHERE NOT deleted AND entityManagerCtx != 0 " +
                    "GROUP BY objId, entityManagerCtx, toRevision " +
                    ") " +
                    // And in public session, this and the above give us the ones only in the public !
                    "AND (objId, toRevision) IN (" +
                    "SELECT objId, MAX(toRevision) " +
                    "FROM CirclePerimeter " +
                    "WHERE NOT deleted AND entityManagerCtx = 0 AND torevision=" + TIP_REVISION + " " +
                    "GROUP BY objId " +
                    ") " +
                    "GROUP BY objId, entityManagerCtx, toRevision " +
                    "UNION " +
                    // Getting the private sessions circleperimeters
                    "SELECT * FROM CirclePerimeter " +
                    "WHERE NOT deleted AND entityManagerCtx != 0 " +
                    "GROUP BY objId, entityManagerCtx, toRevision ";

//    public static final String CIRCLE_PERIMETER_QUERY_FROM_TIP_AND_PRIVATE =
//            // Not in private sessions
//            "(objId, toRevision) NOT IN ( " +
//                    "SELECT objId, toRevision FROM circleperimeter " +
//                    "WHERE NOT deleted AND privatelyModified = true " +
//                    "GROUP BY objId, privatelyModified, toRevision " +
//                    ") " +
//                    // And in public session, this and the above give us the ones only in the public !
//                    "AND (objId, toRevision) IN (" +
//                    "SELECT objId, MAX(toRevision) " +
//                    "FROM circleperimeter " +
//                    "WHERE NOT deleted AND privatelyModified = false AND torevision=" + TIP_REVISION + " " +
//                    "GROUP BY objId " +
//                    ") " +
//                    "GROUP BY objId, privatelyModified, toRevision " +
//                    "UNION " +
//                    // Getting the private sessions circleperimeters
//                    "SELECT * FROM circleperimeter " +
//                    "WHERE NOT deleted AND privatelyModified = true " +
//                    "GROUP BY objId, privatelyModified, toRevision ";

}
