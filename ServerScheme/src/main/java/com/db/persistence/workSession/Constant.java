package com.db.persistence.workSession;

import org.springframework.util.Base64Utils;

import java.util.Date;

public class Constant {

    public static final String INTERNAL_SERVER_USER_NAME = "PUBLIC";
    public static final String INTERNAL_SERVER_USER_TOKEN = new String(Base64Utils.encode((INTERNAL_SERVER_USER_NAME + (new Date()).toString()).getBytes()));

}
