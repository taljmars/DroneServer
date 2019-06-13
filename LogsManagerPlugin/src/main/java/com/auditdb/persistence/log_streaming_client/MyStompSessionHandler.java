/*
 * Tal Martsiano
 * Copyright (c) 2019.
 */

package com.auditdb.persistence.log_streaming_client;

import com.db.persistence.scheme.BaseObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaders;
import org.springframework.messaging.simp.stomp.StompSession;
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter;

import java.lang.reflect.Type;

public class MyStompSessionHandler extends StompSessionHandlerAdapter {

    private Logger logger = LogManager.getLogger(MyStompSessionHandler.class);

    @Override
    public void afterConnected(StompSession session, StompHeaders connectedHeaders) {
        logger.info("New session established : " + session.getSessionId());
        session.subscribe("/topic/event-logs", this);
        logger.info("Subscribed to /topic/event-logs");
//        session.send("/app/chat", getSampleMessage());
//        logger.info("Message sent to websocket server");
    }

    @Override
    public void handleException(StompSession session, StompCommand command, StompHeaders headers, byte[] payload, Throwable exception) {
        logger.error("Got an exception", exception);
    }

    @Override
    public Type getPayloadType(StompHeaders headers) {
        System.out.println(headers);

        if (! headers.containsKey("class"))
            throw new RuntimeException("Unknown element");

        String classPath = headers.get("class").get(0);
        try {
            Class clz = getClass().getClassLoader().loadClass(classPath);
            return clz;
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to find class");
        }
    }

    @Override
    public void handleFrame(StompHeaders headers, Object payload) {
        BaseObject jsonString = (BaseObject)payload;
        System.out.println("Got event");
        System.out.println(jsonString.toString());
    }

}
