package com.dronedb.persistence.validations.internal;

import com.db.persistence.services.ObjectCrudSvc;
import com.dronedb.persistence.scheme.Land;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.scheme.ReturnToHome;
import com.dronedb.persistence.validations.NoPostLandOrRTLItemsValidation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by taljmars on 4/5/17.
 */
public class NoPostLandOrRTLItemsValidator implements ConstraintValidator<NoPostLandOrRTLItemsValidation, Mission> {

    private final static Logger LOGGER = Logger.getLogger(NoPostLandOrRTLItemsValidator.class);

    @Autowired
    private ObjectCrudSvc objectCrudSvc;

    @Override
    public void initialize(NoPostLandOrRTLItemsValidation constraintAnnotation) {
        LOGGER.debug("Initialize validator " + getClass().getSimpleName());
    }

    @Override
    public boolean isValid(Mission value, ConstraintValidatorContext context) {
        try {
            boolean foundLandOrRTL = false;
            assert objectCrudSvc != null : "Critical Error, failed to initialized service";

            for (String missionItemUid : value.getMissionItemsUids()) {
                if (foundLandOrRTL) {
                    LOGGER.debug("Land/RTL was already set");
                    return false;
                }
                MissionItem item = objectCrudSvc.readByClass(missionItemUid, MissionItem.class);
                if (item instanceof Land || item instanceof ReturnToHome) {
                    foundLandOrRTL = true;
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("Unexpected failure in validation");
            return false;
        }

        return true;
    }
}
