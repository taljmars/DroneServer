package com.auditdb.persistence.event_listeners;

import com.auditdb.persistence.base_scheme.EventLogObject;
import com.auditdb.persistence.dumper.EventQueue;
import com.auditdb.persistence.scheme.ObjectCreationLog;
import com.auditdb.persistence.scheme.ObjectDeletionLog;
import com.auditdb.persistence.scheme.ObjectUpdateLog;
import com.db.persistence.events.ServerEventMapper;
import com.db.persistence.events.audit.ObjectEvent;
import com.db.persistence.scheme.BaseObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    public void handleOrderCreatedEvent(ObjectEvent event) throws InvocationTargetException, IllegalAccessException {
        LOGGER.debug("Modification event: " + event);
        EventLogObject auditLog;
        switch (event.getObjectEventType()) {
            case CREATE:
                auditLog = new ObjectCreationLog();
                ((ObjectCreationLog) auditLog).setReferredObjId(event.getItem().getKeyId().getObjId());
                ((ObjectCreationLog) auditLog).setReferredObjType(event.getItem().getClz());
                break;
            case DELETE:
                auditLog = new ObjectDeletionLog();
                ((ObjectDeletionLog) auditLog).setReferredObjId(event.getItem().getKeyId().getObjId());
                ((ObjectDeletionLog) auditLog).setReferredObjType(event.getItem().getClz());
                break;
            case UPDATE:
                auditLog = new ObjectUpdateLog();
                ((ObjectUpdateLog) auditLog).setReferredObjId(event.getItem().getKeyId().getObjId());
                ((ObjectUpdateLog) auditLog).setReferredObjType(event.getItem().getClz());

                List<String> changedFields = new ArrayList();
                List<String> changedFromValues = new ArrayList();
                List<String> changedToValues = new ArrayList();
                BaseObject from = event.getTip();
                BaseObject to = event.getItem();
                Method[] methods = from.getClass().getMethods();
                for (Method method : methods) {
                    if (    ! method.getName().startsWith("get") ||
                            method.getName().equals("getKeyId") ||
                            method.getName().equals("getUpdatedAt"))
                        continue;

                    Object fromVal = method.invoke(from, null);
                    Object toVal = method.invoke(to, null);
                    if (Objects.equals(fromVal, toVal))
                        continue;

                    changedFields.add(method.getName().substring("get".length()));
                    changedFromValues.add(fromVal + "");
                    changedToValues.add(toVal + "");
                }

                ((ObjectUpdateLog) auditLog).setChangedFields(changedFields);
                ((ObjectUpdateLog) auditLog).setChangedFromValues(changedFromValues);
                ((ObjectUpdateLog) auditLog).setChangedToValues(changedToValues);
                break;
            default:
                throw new RuntimeException("Failed to handle event type");
        }

        auditLog.setUserName(event.getUserName());
        eventQueue.queue(event, auditLog);
    }

}
