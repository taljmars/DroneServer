package com.auditdb.persistence.event_listeners;

import com.auditdb.persistence.dumper.EventQueue;
import com.auditdb.persistence.scheme.AuditLog;
import com.db.persistence.events.audit.AccessEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class AccessListener {

    private final static Logger LOGGER = Logger.getLogger(AccessListener.class);

    @Autowired
    private EventQueue eventQueue;

    @PostConstruct
    public void init() {
        LOGGER.debug("AccessListener Initialized");
    }

    @EventListener
    public void handleEvent(AccessEvent accessEvent) {
        LOGGER.debug("Modofication event: " + accessEvent);
        eventQueue.queue(accessEvent, new AuditLog());
    }
}
