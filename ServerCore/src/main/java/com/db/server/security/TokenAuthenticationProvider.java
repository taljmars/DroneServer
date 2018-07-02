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
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TokenAuthenticationProvider implements AuthenticationProvider {

    private final static Logger LOGGER = Logger.getLogger(TokenAuthenticationProvider.class);

    @Autowired
    private MyTokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        LOGGER.debug("Try to authenicate using token -> " + authentication);
        Optional<String> token = (Optional) authentication.getPrincipal();
        if (!token.isPresent() || token.get().isEmpty()) {
            throw new BadCredentialsException("Invalid token");
        }
        if (!tokenService.contains(token.get())) {
            throw new BadCredentialsException("Invalid token or token expired");
        }

        Authentication auth = tokenService.retrieve(token.get());
        return auth;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        LOGGER.debug("Check class " + authentication);
        return authentication.equals(AuthenticationWithToken.class);
    }
}