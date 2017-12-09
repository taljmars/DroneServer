package com.dronedb.persistence.validations.internal;

import com.dronedb.persistence.scheme.Land;
import com.dronedb.persistence.scheme.Mission;
import com.dronedb.persistence.scheme.MissionItem;
import com.dronedb.persistence.scheme.ReturnToHome;
import com.db.persistence.services.ObjectCrudSvc;
import com.dronedb.persistence.validations.NoPostLandOrRTLItemsValidation;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

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
            if (objectCrudSvc == null) {
                LOGGER.debug("TALMA lose");
            }
            else {
                LOGGER.debug("TALMA win");
            }
            for (UUID missionItemUid : value.getMissionItemsUids()) {
                if (foundLandOrRTL) {
                    LOGGER.debug("Found illegal point"); // TODO: print normal
                    return false;
                }
                MissionItem item = objectCrudSvc.readByClass(missionItemUid, MissionItem.class);
                if (item instanceof Land || item instanceof ReturnToHome) {
                    foundLandOrRTL = true;
                }
            }
        }
//        catch (ObjectNotFoundException e) {
//            System.err.print("Failed to find item");
//            return false;
//        }
        catch (Exception e) {
            e.printStackTrace();
            LOGGER.debug("Unexpected failure in validation");
            return false;
        }

        return true;
    }
}
