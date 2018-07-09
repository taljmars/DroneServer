/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Component
public class MyTokenService {

	private final static Logger LOGGER = Logger.getLogger(UserAuthenticationProvider.class);

	@Autowired
	ApplicationContext applicationContext;

	private static Map<String, Authentication> restApiAuthTokenCache = new HashMap<String, Authentication>();

	public synchronized String generateNewToken(String userName) {
		Predicate<Authentication> predicate = t -> {
			if (t.getPrincipal() instanceof Optional)
				return ((Optional)t.getPrincipal()).get().equals(userName);
			return t.getPrincipal().equals(userName);
		};
		if (restApiAuthTokenCache.values().stream().anyMatch(predicate)) {
			LOGGER.debug("Token was already generated for this username: " + userName);
			throw new BadCredentialsException("User already logged in");
		}
		Integer sessionLimitation = (Integer) applicationContext.getBean("sessionLimitation");
		if (restApiAuthTokenCache.keySet().size() >= sessionLimitation)
			throw new SessionAuthenticationException("Session Limitation Reached");
		return UUID.randomUUID().toString();
	}

	public synchronized void store(String token, Authentication authentication) {
		restApiAuthTokenCache.put(token, authentication);
	}

	public synchronized boolean contains(String token) {
		return restApiAuthTokenCache.get(token) != null;
	}

	public synchronized Authentication retrieve(String token) {
		return (Authentication) restApiAuthTokenCache.get(token);
	}

	public synchronized void expire(String token) {
		restApiAuthTokenCache.remove(token);
	}
}