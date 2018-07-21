/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.db.server.security;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

public class MyToken {

    enum TokenLC {
        UNKNOWN,
        NOT_INITIALIZE,
        INITIALIZE,
        REVOKED
    }

    private final String uid;
    private Date creationDate;
    private TokenLC status;

    public MyToken() {
        this(UUID.randomUUID().toString());
    }

    private MyToken(String tokenString) {
        uid = tokenString;
        creationDate = new Date();
        status = TokenLC.NOT_INITIALIZE;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public static MyToken deserialize(String tokenString) {
        MyToken token = new MyToken(tokenString);
        token.status = TokenLC.UNKNOWN;
        return token;
    }

    public String serialize() {
        return uid;
    }

    public boolean isInitialized() {
        return status.equals(TokenLC.INITIALIZE);
    }

    public boolean isUninitialized() {
        return status.equals(TokenLC.NOT_INITIALIZE);
    }

    public MyToken initializeNow() {
        status = TokenLC.INITIALIZE;
        return this;
    }

    public void revokeNow() {
        status = TokenLC.REVOKED;
    }

    public boolean isRevoked() {
        return status.equals(TokenLC.REVOKED);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyToken token = (MyToken) o;
//        if (token.status.equals(TokenLC.REVOKED))
//            return status.equals(token.status) && uid.equals(token.uid);

        return uid.equals(token.uid);
    }

    @Override
    public int hashCode() {
//        if (status.equals(TokenLC.REVOKED))
//            return Objects.hash(uid, status);
        return Objects.hash(uid);
    }

    @Override
    public String toString() {
        return "MyToken{" +
                "uid='" + uid + '\'' +
                ", creationDate=" + creationDate +
                ", status=" + status +
                '}';
    }
}
