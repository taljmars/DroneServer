/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;
import org.springframework.web.util.UrlPathHelper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

import static com.db.server.SecurityConfig.*;

public class AuthenticationFilter extends GenericFilterBean {

    private final static Logger LOGGER = Logger.getLogger(AuthenticationFilter.class);

    public static final String TOKEN_SESSION_KEY = "token";
    public static final String USER_SESSION_KEY = "user";
    private AuthenticationManager authenticationManager;

    public AuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        Optional<String> username = Optional.ofNullable(httpRequest.getHeader(AUTH_USERNAME_KEY));
        Optional<String> password = Optional.ofNullable(httpRequest.getHeader(AUTH_PASSWORD_KEY));
        Optional<String> tokenString = Optional.ofNullable(httpRequest.getHeader(AUTH_TOKEN_KEY));
        String resourcePath = new UrlPathHelper().getPathWithinApplication(httpRequest);

        try {
            if (tokenString.isPresent()) {
                MyToken token = MyToken.deserialize(tokenString.get());
                LOGGER.debug("Trying to authenticate user by X-Auth-Token method. Token: " + token);
                processTokenAuthentication(token);
            }
            else if (postToAuthenticate(httpRequest, resourcePath)) {
                LOGGER.debug("Trying to authenticate user '" + username + "' by X-Auth-Username method");
                processUsernamePasswordAuthentication(httpRequest, httpResponse, username, password);
            }

            LOGGER.debug("AuthenticationFilter is passing request down the filter chain");
            chain.doFilter(request, response);
        }
        catch (InternalAuthenticationServiceException internalAuthenticationServiceException) {
            SecurityContextHolder.clearContext();
            LOGGER.error("Internal authentication service exception", internalAuthenticationServiceException);
            httpResponse.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        catch (AuthenticationException authenticationException) {
            SecurityContextHolder.clearContext();
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, authenticationException.getMessage());
        }
        finally {
//            MDC.remove(TOKEN_SESSION_KEY);
//            MDC.remove(USER_SESSION_KEY);
        }
    }

    private boolean postToAuthenticate(HttpServletRequest httpRequest, String resourcePath) {
        return resourcePath.equals("/login") && httpRequest.getMethod().equals("POST");
    }

    private void processUsernamePasswordAuthentication(HttpServletRequest httpRequest, HttpServletResponse httpResponse, Optional<String> username, Optional<String> password) throws IOException {
        Authentication resultOfAuthentication = tryToAuthenticateWithUsernameAndPassword(username, password, httpRequest.getRemoteAddr());
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    private  Authentication tryToAuthenticateWithUsernameAndPassword(Optional<String> username, Optional<String> password, String remoteAddress) {
        UserAuthenticationToken requestAuthentication = new UserAuthenticationToken(username, password, remoteAddress);
        return tryToAuthenticate(requestAuthentication);
    }

    private void processTokenAuthentication(MyToken token) {
        Authentication resultOfAuthentication = tryToAuthenticateWithToken(token);
        SecurityContextHolder.getContext().setAuthentication(resultOfAuthentication);
    }

    private Authentication tryToAuthenticateWithToken(MyToken token) {
        AuthenticationWithToken requestAuthentication = new AuthenticationWithToken(token, null);
        return tryToAuthenticate(requestAuthentication);
    }

    private Authentication tryToAuthenticate(Authentication requestAuthentication) {
        Authentication responseAuthentication = authenticationManager.authenticate(requestAuthentication);
        if (responseAuthentication == null || !responseAuthentication.isAuthenticated()) {
            LOGGER.debug("Failed to authenticate");
            throw new InternalAuthenticationServiceException("Unable to authenticate Domain User for provided credentials");
        }
        LOGGER.debug("User successfully authenticated");
        return responseAuthentication;
    }
}