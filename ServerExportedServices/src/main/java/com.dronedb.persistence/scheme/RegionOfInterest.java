package com.dronedb.persistence.scheme;

/**
 * Created by taljmars on 3/25/17.
 */
public class RegionOfInterest extends MissionItem implements Altitudable {

    protected Double altitude;

    public RegionOfInterest() {
        super();
    }

    public RegionOfInterest(RegionOfInterest regionOfInterest) {
//        super(regionOfInterest);
//        this.altitude = regionOfInterest.getAltitude();
    }

    @Override
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Override
    public Double getAltitude() {
        return this.altitude;
    }

}
