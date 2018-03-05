package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class ServerAuthProvider implements AuthenticationProvider {

    private final static Logger LOGGER = Logger.getLogger(ServerAuthProvider.class);

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        LOGGER.debug("Try to authenticate '" + authentication.getName() + "'");
        String name = authentication.getName();

        if (name.equals("tester1") || name.equals("tester2")){
            LOGGER.debug("Authenticated ->" + authentication);
            String password = authentication.getCredentials().toString();
            UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(name, password, Collections.emptyList());
            return userAuth;
        }
        LOGGER.debug("Not Authenticated");
        throw new BadCredentialsException("Illegal user");
    }

    @Override
    public boolean supports(Class<?> authenticationClz) {
        LOGGER.debug("Check class " + authenticationClz);
        return authenticationClz.equals(UsernamePasswordAuthenticationToken.class);
    }
}
