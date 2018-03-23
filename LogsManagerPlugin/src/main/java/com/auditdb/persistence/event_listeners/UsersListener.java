package com.auditdb.persistence.event_listeners;

import com.auditdb.persistence.dumper.EventQueue;
import com.auditdb.persistence.scheme.AccessLog;
import com.auditdb.persistence.scheme.RegistrationLog;
import com.db.persistence.events.ServerEventMapper;
import com.db.persistence.events.audit.AccessEvent;
import com.db.persistence.events.audit.RegistrationEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class UsersListener {

    private final static Logger LOGGER = Logger.getLogger(UsersListener.class);

    @Autowired
    private EventQueue eventQueue;

    @Autowired
    private ServerEventMapper serverEventMapper;

    @PostConstruct
    public void init() {
        LOGGER.debug("AccessListener Initialized");
    }

    @EventListener
    public void handleEvent(AccessEvent accessEvent) {
        LOGGER.debug("Access event: " + accessEvent);
        AccessLog logEntry = new AccessLog();
        logEntry.setLogin(accessEvent.getEventType().equals(AccessEvent.AccessEventType.LOGIN) ? true : false);
        logEntry.setUserName(accessEvent.getUserName());
        eventQueue.queue(accessEvent, logEntry);
    }

    @EventListener
    public void handleEvent(RegistrationEvent registrationEvent) {
        LOGGER.debug("Registration event: " + registrationEvent);
        RegistrationLog logEntry = new RegistrationLog();
        logEntry.setDescription(registrationEvent.getEventType() + "");
        logEntry.setUserName(registrationEvent.getUserName());
        eventQueue.queue(registrationEvent, logEntry);
    }
}
