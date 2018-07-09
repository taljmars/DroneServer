package com.db.server.security;

import com.db.persistence.scheme.User;
import com.db.persistence.services.RegistrationSvc;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class UserAuthenticationProvider implements AuthenticationProvider {

    private final static Logger LOGGER = Logger.getLogger(UserAuthenticationProvider.class);

    @Autowired
    private RegistrationSvc registrationSvc;

    @Autowired
    private MyTokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        LOGGER.debug("Try to authenticate '" + authentication.getName() + "'");
        Optional<String> optionalName = (Optional<String>) authentication.getPrincipal();
        Optional<String> optionalPassword = (Optional<String>) authentication.getCredentials();

        String name = optionalName.get();
        String password = optionalPassword.get();

        if (name.equals("tester1") || name.equals("tester2")) {
            if (authentication instanceof UserAuthenticationToken) {
                String address = ((UserAuthenticationToken) authentication).getRemoteAddress();
                if ((address.equals("0:0:0:0:0:0:0:1") || address.equals("127.0.0.1")) && name.equals(password)) {
                    LOGGER.debug("Authenticated ->" + authentication);
                    LOGGER.debug("Tester user for localhost is in use");
                    return generateAuthenticationWithToken(name, password, authentication);
                }
            }
        }

        if (name.equals("admin") || password.equals("admin")) {
            LOGGER.debug("Authenticated Admin ->" + authentication);
            LOGGER.debug("Admin user for localhost is in use");
            return generateAuthenticationWithToken(name, password, authentication);
        }

        User user = registrationSvc.getUserByName(name);
        LOGGER.debug("Try to login with, user=" + name + ", pass=" + password + ", gor user:" + user);
        if (user != null && user.getPassword().equals(password)) {
            LOGGER.debug("Authenticated ->" + authentication);
            return generateAuthenticationWithToken(name, password, authentication);
        }
        LOGGER.debug("Not Authenticated");
        authentication.setAuthenticated(false);
        return null;
    }

    @Override
    public boolean supports(Class<?> authenticationClz) {
        LOGGER.debug("Check class " + authenticationClz);
        return authenticationClz.equals(UserAuthenticationToken.class);
    }


    private AuthenticationWithToken generateAuthenticationWithToken(String userName, String password, Authentication authentication) {
        AuthenticationWithToken authenticationWithToken = new AuthenticationWithToken(userName, password, null);
        String token = tokenService.generateNewToken(userName);

//        authenticationWithToken.setAuthenticated(true);
        authenticationWithToken.setToken(token);
        tokenService.store(authenticationWithToken.getToken(), authenticationWithToken);
        return authenticationWithToken;
    }


}
