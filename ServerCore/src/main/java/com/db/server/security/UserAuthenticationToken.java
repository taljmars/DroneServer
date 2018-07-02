/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.server.security;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class UserAuthenticationToken extends UsernamePasswordAuthenticationToken {

    private final String remoteAddress;

    public UserAuthenticationToken(Object principal, Object credentials, String remoteAddress) {
        super(principal, credentials);
        this.remoteAddress = remoteAddress;
    }

    public UserAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String remoteAddress) {
        super(principal, credentials, authorities);
        this.remoteAddress = remoteAddress;
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }
}
