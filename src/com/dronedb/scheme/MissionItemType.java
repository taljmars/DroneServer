package com.dronedb.scheme;

import java.util.HashMap;
import java.util.Map;

public enum MissionItemType {
	
	WAYPOINT (0), 
	SPLINE_WAYPOINT (1), 
	TAKEOFF (2), 
	RTL (3), 
	LAND (4), 
	CIRCLE (5), 
	ROI (6), 
	SURVEY (7), 
	CYLINDRICAL_SURVEY (8), 
	CHANGE_SPEED (9), 
	CAMERA_TRIGGER (10), 
	EPM_GRIPPER (11);
	
	private final Integer order;
	
	private MissionItemType(Integer val) {
		order = val;
	}
	
	public Integer getOrder() {
		return order;
	}
	
	// DB VALUE -> ENUM CONVERSION
    // static reverse resolving:
    public static final Map<Integer, MissionItemType> dbValues = new HashMap<>();
 
    static {
        for (MissionItemType value : values()) {
            dbValues.put(value.order, value);
        }
    }
 
    public static MissionItemType fromDbValue(Integer dbValue) {
        // this returns null for invalid value, check for null and throw exception if you need it
        return dbValues.get(dbValue);
    }
}
