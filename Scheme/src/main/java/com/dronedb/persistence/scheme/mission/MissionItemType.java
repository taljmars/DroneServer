package com.dronedb.persistence.scheme.mission;

import java.util.HashMap;
import java.util.Map;

public enum MissionItemType {
	
	WAYPOINT ("WP"), 
	SPLINE_WAYPOINT ("SPL_WP"), 
	TAKEOFF ("TAKEOFF"), 
	RTL ("RTL"), 
	LAND ("LAND"), 
	CIRCLE ("CIRCLE"), 
	ROI ("ROI"), 
	SURVEY ("SRVY"), 
	CYLINDRICAL_SURVEY ("C_SRVY"), 
	CHANGE_SPEED ("CNG_SPEED"), 
	CAMERA_TRIGGER ("CAMERA_TRG"), 
	EPM_GRIPPER ("EPM_GRP");
	
	private final String order;
	
	private MissionItemType(String val) {
		order = val;
	}
	
	public String getOrder() {
		return order;
	}
	
	// DB VALUE -> ENUM CONVERSION
    // static reverse resolving:
    public static final Map<String, MissionItemType> dbValues = new HashMap();
 
    static {
        for (MissionItemType value : values()) {
            dbValues.put(value.order, value);
        }
    }
 
    public static MissionItemType fromDbValue(String dbValue) {
        // this returns null for invalid value, check for null and throw exception if you need it
        return dbValues.get(dbValue);
    }
}
