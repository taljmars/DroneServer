package com.dronedb.persistence.ws.internal;

import com.dronedb.persistence.scheme.apis.PerimeterCrudSvcRemote;
import com.dronedb.persistence.scheme.mission.Mission;
import com.dronedb.persistence.scheme.perimeter.Perimeter;
import com.dronedb.persistence.services.PerimeterCrudSvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Created by oem on 3/27/17.
 */
@Component
@WebService(endpointInterface = "com.dronedb.persistence.scheme.apis.PerimeterCrudSvcRemote")
public class PerimeterCrudSvcRemoteImpl implements PerimeterCrudSvcRemote {

    @Autowired
    private PerimeterCrudSvc perimeterCrudSvc;

    @Override
    public <T extends Perimeter> T clonePerimeter(@WebParam T perimeter) {
        return perimeterCrudSvc.clonePerimeter(perimeter);
    }
}
