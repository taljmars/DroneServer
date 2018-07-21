/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.server.security;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@Component
public class MyTokenService {

	private final static Logger LOGGER = Logger.getLogger(UserAuthenticationProvider.class);

	@Autowired
	private ApplicationContext applicationContext;

	public Map<MyToken, Authentication> restApiAuthTokenCache = new HashMap<MyToken, Authentication>();

	public synchronized MyToken generateNewToken(String userName) {
		Iterator<Map.Entry<MyToken, Authentication>> it = restApiAuthTokenCache.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<MyToken, Authentication> entry = it.next();
			if (entry.getValue().getPrincipal() instanceof Optional) {
				if (! ((Optional)entry.getValue().getPrincipal()).get().equals(userName))
					continue;
			}
			else if (! entry.getValue().getPrincipal().equals(userName)) {
				continue;
			}

			// Same user name
			if (!entry.getKey().isRevoked()) {
				LOGGER.debug("Token was already generated for this username: " + userName);
				throw new BadCredentialsException("User already logged in");
			}

			LOGGER.debug("Old Token was generated in the past for " + userName + ", removing token");
			it.remove();
			break;
		}

		return new MyToken();
	}

	public synchronized void store(MyToken token, Authentication authentication) {
		if (token.isUninitialized() || token.isInitialized())
			restApiAuthTokenCache.put(token, authentication);
		else
			throw new RuntimeException("Try to push invalid token state: " + token);
	}

	public synchronized boolean contains(MyToken token) {
		Optional<MyToken> existTokens = restApiAuthTokenCache.keySet().stream().filter(a -> a.equals(token)).findFirst();
		return existTokens.isPresent() && !existTokens.get().isRevoked();
	}

	public synchronized Authentication retrieve(MyToken token) {
		return (Authentication) restApiAuthTokenCache.get(token);
	}

	public synchronized void revoke(MyToken token) {
		token.revokeNow();
		restApiAuthTokenCache.remove(token);
	}

	@Scheduled(fixedRate = 15 * 1000)
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void tik() {
		LOGGER.info("=============================================================================");
		LOGGER.info("============================= TOKEN EXPIRATION ==============================");
		try {
			Set<MyToken> tokens = restApiAuthTokenCache.keySet();
			LOGGER.debug("There are " + tokens.size() + " tokens available");
			Iterator<MyToken> it = tokens.iterator();
			while (it.hasNext()) {
				MyToken token = it.next();
				if (token.isRevoked()) {
					LOGGER.debug("Revoked token was cleared");
					it.remove();
				}
				else if (token.isUninitialized() && ((new Date()).getTime() - token.getCreationDate().getTime() > 30 * 1000)) {
					LOGGER.debug("Open token was cleared");
					token.revokeNow();
					it.remove();
				}
			}
		}
		catch (Exception e) {
			LOGGER.error("Failed to check tokens", e);
		}
		LOGGER.info("=============================================================================");
	}
}