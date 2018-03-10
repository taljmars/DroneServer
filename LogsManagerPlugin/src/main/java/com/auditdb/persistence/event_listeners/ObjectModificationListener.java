package com.auditdb.persistence.event_listeners;

import com.auditdb.persistence.dumper.EventQueue;
import com.auditdb.persistence.scheme.AccessLog;
import com.auditdb.persistence.scheme.ObjectModificationLog;
import com.db.persistence.events.ServerEventMapper;
import com.db.persistence.events.audit.ObjectModificationEvent;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Date;

@Component
public class ObjectModificationListener {

    private final static Logger LOGGER = Logger.getLogger(ObjectModificationListener.class);

    @Autowired
    private EventQueue eventQueue;

    @Autowired
    private ServerEventMapper serverEventMapper;

    @PostConstruct
    public void init() {
        LOGGER.debug("ObjectModificationListener Initialized");
    }

//    @TransactionalEventListener
    @EventListener
    public void handleOrderCreatedEvent(ObjectModificationEvent event) {
        LOGGER.debug("Modification event: " + event);
        ObjectModificationLog auditLog = new ObjectModificationLog();
        auditLog.setReferredObjId(event.getItem().getKeyId().getObjId());
        auditLog.setChangedFields(null);
        auditLog.setChangedValues(null);

        auditLog.setUserName(event.getUserName());
        eventQueue.queue(event, auditLog);
    }

}
