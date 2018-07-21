package com.db.persistence.services;

import com.db.persistence.scheme.*;

public interface RegistrationSvc {

    RegistrationResponse registerNewUser(RegistrationRequest signingRequest);

    User getUserByName(String userName);

}
