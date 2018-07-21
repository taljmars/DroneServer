/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final static Logger LOGGER = Logger.getLogger(TokenAuthenticationProvider.class);

    @Autowired
    private MyTokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.debug("Try to authenticate using token -> " + authentication);
        MyToken token = (MyToken) authentication.getPrincipal();
        if (!tokenService.contains(token)) {
            LOGGER.error("Invalid token / token expired");
            throw new BadCredentialsException("Invalid token or token expired");
        }

        Authentication auth = tokenService.retrieve(token);
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        LOGGER.debug("Check class " + authentication);
        return authentication.equals(AuthenticationWithToken.class);
    }

}