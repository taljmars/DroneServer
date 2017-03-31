package com.dronedb.persistence.scheme;

/**
 * Created by taljmars on 3/18/17.
 */
public interface ConvertDatabaseVisitor {

    void visit(Land land);

    void visit(Takeoff takeoff);

    void visit(Waypoint waypoint);

    void visit(Circle circle);

    void visit(ReturnToHome returnToHome);

    void visit(RegionOfInterest regionOfInterest);
}
