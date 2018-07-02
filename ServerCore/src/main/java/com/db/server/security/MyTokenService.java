/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

@Component
public class MyTokenService {

	private final static Logger LOGGER = Logger.getLogger(UserAuthenticationProvider.class);

	private static Map restApiAuthTokenCache = new HashMap();

	public synchronized String generateNewToken(String userName) {
		Predicate<Authentication> predicate = t -> ((Optional)t.getPrincipal()).get().equals(userName);
		if (restApiAuthTokenCache.values().stream().anyMatch(predicate)) {
			LOGGER.debug("Token was already generated for this username: " + userName);
			return null;
		}
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