/*
 * Tal Martsiano
 * Copyright (c) 2018.
 */

package com.auditdb.persistence.dumper;

import com.auditdb.persistence.base_scheme.EventLogLinkerObject;
import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.services.SessionsSvc;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.WebSocketStompClient;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.persistence.Table;
import java.util.*;

@Component
public class LogsDumperScheduler {

    private final static Logger LOGGER = Logger.getLogger(LogsDumperScheduler.class);

    @Autowired
    private WorkSessionManager workSessionManager;

    @Autowired
    private SessionsSvc sessionsSvc;

    @Autowired
    private EventQueue eventQueue;


    @Resource(name = "internalUserToken")
    private Object internalUserToken;

    private Map<Class, String> storingTable;

    WebSocketStompClient stompClient;

    @Autowired
    private SimpMessagingTemplate template;

    @PostConstruct
    public void init() {
        LOGGER.debug("Dumper timer");
        storingTable = new HashMap<>();
    }

    static final int SEC = 1000;
    @Scheduled(fixedRate = 45 * SEC)
    public void tik() {
        try {
            List<BaseObject> eventList = new ArrayList<>();
            int size = eventQueue.size();
            if (size == 0)
                return;

            LOGGER.info("============================================================================");
            LOGGER.info("============================ LOG DUMPER BEGIN =============================");
            LOGGER.info("Audit Log" + (new Date()).toString() + " , logs amount=" + eventQueue.size());

            for (int i = 0; i < size; i++) {
                EventLogObject se = eventQueue.pop();

                eventList.add(se);
                EventLogLinkerObject eventLogLinkerObject = new EventLogLinkerObject();
                eventLogLinkerObject.setReferedObj(se.getKeyId().getObjId());
                eventLogLinkerObject.setType(getStoredTable(se.getClass()));
                eventLogLinkerObject.setEventCode(se.getEventCode());

                eventList.add(eventLogLinkerObject);

                steamEvent(se);
            }

            if (!eventList.isEmpty()) {
                WorkSession workSession = workSessionManager.getSessionByToken(internalUserToken);
                for (BaseObject auditLog : eventList) {
                    auditLog.setCreationDate(new Date());
                    workSession.update(auditLog);
                }

//                sessionsSvc.setToken(internalUserToken).publish();
                // This logic is similar to publish funtion - TODO: Remove this code duplication
//                LOGGER.debug("PUBLISH START !!! (Log Dumper)");
//                LOGGER.debug("Update revision value in revision manager");
                workSession = workSession.publish();
//                LOGGER.debug("PUBLISH END !!!  (Log Dumper)");
                workSessionManager.createSession(internalUserToken, workSession.getUserName());
            }

            LOGGER.info("============================== LOG DUMPER END ==============================");
            LOGGER.info("============================================================================");
        }
        catch (Exception e) {
            LOGGER.error("Failed to dump events", e);
            LOGGER.info("============================== LOG DUMPER END ==============================");
            LOGGER.info("============================================================================");
        }
    }

    private void steamEvent(BaseObject auditLog) {
        Map<String, Object> headers = new HashMap<>();
        headers.put("class", auditLog.getClz().getCanonicalName());
        MessageHeaders messageHeaders = new MessageHeaders(headers);
//        System.out.println(String.format("STREAM event %s", auditLog));
        template.convertAndSend("/topic/event-logs", auditLog, messageHeaders);
    }

    private String getStoredTable(Class<? extends EventLogObject> se) {
        String storeTable = storingTable.get(se);
        if (storeTable != null)
            return storeTable;

        Class<?> clz = se;
        while (!clz.getName().equals(Object.class.getName())) {
            if (clz.isAnnotationPresent(Table.class)) {
                LOGGER.debug("Found table is " + clz.getSimpleName() + " for object of type " + clz.getSimpleName());
                Table table = clz.getAnnotation(Table.class);
                String tableName = table.name();
                if (tableName.isEmpty())
                    tableName = clz.getSimpleName();
                storingTable.put(se, tableName);
                return tableName;
            }
            if (EventLogObject.class.isAssignableFrom(clz.getSuperclass()))
                clz = clz.getSuperclass();
        }

        throw new RuntimeException("Object doesn't have storing definitions or it is not a POJO");
    }
}
