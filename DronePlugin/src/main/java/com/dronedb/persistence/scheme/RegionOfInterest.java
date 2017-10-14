package com.dronedb.persistence.scheme;

import com.db.persistence.scheme.BaseObject;
import com.db.persistence.scheme.Sessionable;
import com.db.persistence.triggers.UpdateTrigger;
import org.springframework.beans.factory.annotation.Value;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.io.Serializable;

/**
 * Created by taljmars on 3/25/17.
 */
@Entity
@UpdateTrigger(trigger = "com.db.persistence.triggers.DefaultValuesSetterTrigger", phase = UpdateTrigger.PHASE.PRE_PERSIST)
@Sessionable
public class RegionOfInterest extends MissionItem implements Altitudable, Serializable {

    protected Double altitude;

    public RegionOfInterest() {
        super();
    }

    public RegionOfInterest(RegionOfInterest regionOfInterest) {
        super(regionOfInterest);
        this.altitude = regionOfInterest.getAltitude();
    }

    @Override
    public RegionOfInterest clone() {
        return new RegionOfInterest(this);
    }

    @Override
    public BaseObject copy() {
//        RegionOfInterest regionOfInterest = this.clone();
//        regionOfInterest.setKeyId(this.getKeyId());
        RegionOfInterest regionOfInterest = (RegionOfInterest) super.copy();
        return regionOfInterest;
    }

    @Override
    public void set(BaseObject baseObject) {
        super.set(baseObject);
        RegionOfInterest regionOfInterest = (RegionOfInterest) baseObject;
        this.altitude = regionOfInterest.getAltitude();
    }

    @Override
    public void setAltitude(Double altitude) {
        this.altitude = altitude;
    }

    @Override
    @Value(value = "10")
    @Column(nullable = true, name = "Alt")
    public Double getAltitude() {
        return this.altitude;
    }

}
