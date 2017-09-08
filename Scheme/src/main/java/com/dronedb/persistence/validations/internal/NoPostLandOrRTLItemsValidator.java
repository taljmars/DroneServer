package com.dronedb.persistence.validations.internal;

import com.dronedb.persistence.scheme.*;
import com.dronedb.persistence.validations.NoPostLandOrRTLItemsValidation;
import javassist.tools.rmi.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.UUID;

/**
 * Created by taljmars on 4/5/17.
 */
public class NoPostLandOrRTLItemsValidator implements ConstraintValidator<NoPostLandOrRTLItemsValidation, Mission> {

    @Autowired
    private DroneDbCrudSvcRemote droneDbCrudSvcRemote;

    @Override
    public void initialize(NoPostLandOrRTLItemsValidation constraintAnnotation) {
        System.err.print("Initialize validator " + getClass().getSimpleName());
    }

    @Override
    public boolean isValid(Mission value, ConstraintValidatorContext context) {
        try {
            boolean foundLandOrRTL = false;
            if (droneDbCrudSvcRemote == null) {
                System.err.println("TALMA lose");
            }
            else {
                System.err.println("TALMA win");
            }
//            DroneDbCrudSvcRemote droneDbCrudSvcRemote = null; // TODO: Must be fixed to add this validation
            for (UUID missionItemUid : value.getMissionItemsUids()) {
                if (foundLandOrRTL) {
                    System.out.print("Found illegal point"); // TODO: print normal
                    return false;
                }
                MissionItem item = droneDbCrudSvcRemote.readByClass(missionItemUid, MissionItem.class);
                if (item instanceof Land || item instanceof ReturnToHome) {
                    foundLandOrRTL = true;
                }
            }
        } catch (ObjectNotFoundException e) {
            System.err.print("Failed to find item");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            System.err.print("Unexpected failure in validation");
            return false;
        }

        return true;
    }
}
