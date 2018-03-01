package com.auditdb.persistence.event_listeners;

import com.auditdb.persistence.dumper.EventQueue;
import com.auditdb.persistence.scheme.AuditLog;
import com.db.persistence.events.audit.ObjectModificationEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ObjectModificationListener {

    private final static Logger LOGGER = Logger.getLogger(ObjectModificationListener.class);

    @Autowired
    private EventQueue eventQueue;

    @PostConstruct
    public void init() {
        LOGGER.debug("ObjectModificationListener Initialized");
    }

//    @TransactionalEventListener
    @EventListener
    public void handleOrderCreatedEvent(ObjectModificationEvent objectModificationEvent) {
        LOGGER.debug("Modification event: " + objectModificationEvent);
        AuditLog auditLog = new AuditLog();
        auditLog.setReferredObjId(objectModificationEvent.getItem().getKeyId().getObjId());
        auditLog.setUserName("tester");
        auditLog.setChangedFields(null);
        auditLog.setChangedValues(null);
        eventQueue.queue(objectModificationEvent, auditLog);
    }

}
