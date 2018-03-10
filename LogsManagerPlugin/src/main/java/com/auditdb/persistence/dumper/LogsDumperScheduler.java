package com.auditdb.persistence.dumper;

import com.auditdb.persistence.base_scheme.EventLogLinkerObject;
import com.db.persistence.scheme.BaseObject;
import com.auditdb.persistence.base_scheme.EventLogObject;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.persistence.Table;
import java.util.*;

import static com.db.persistence.workSession.Constant.INTERNAL_SERVER_USER_TOKEN;

@Component
public class LogsDumperScheduler {

    private final static Logger LOGGER = Logger.getLogger(LogsDumperScheduler.class);

    @Autowired
    private WorkSessionManager workSessionManager;

    @Autowired
    private EventQueue eventQueue;

    private Map<Class, String> storingTable;

    @PostConstruct
    public void init() {
        LOGGER.debug("Dumper timer");
        storingTable = new HashMap<>();
    }

    @Scheduled(fixedRate = 45 * 1000)
    public void tik() {
        LOGGER.debug("============================================================================");
        LOGGER.debug("============================ LOG DUMPER BEGIN =============================");
        LOGGER.debug("Audit Log" + (new Date()).toString() + " , logs amount=" + eventQueue.size());

        List<BaseObject> eventList = new ArrayList<>();
        int size = eventQueue.size();
        for (int i = 0 ; i < size ; i++) {
            EventLogObject se = eventQueue.pop();

            eventList.add(se);
            EventLogLinkerObject eventLogLinkerObject = new EventLogLinkerObject();
            eventLogLinkerObject.setReferedObj(se.getKeyId().getObjId());
            eventLogLinkerObject.setType(getStoredTable(se.getClass()));
            eventLogLinkerObject.setEventCode(se.getEventCode());

            eventList.add(eventLogLinkerObject);
        }

        if (!eventList.isEmpty()) {
//            WorkSession workSession = workSessionManager.createSession("public", "");
            WorkSession workSession = workSessionManager.getSessionByToken(INTERNAL_SERVER_USER_TOKEN);
            for (BaseObject auditLog : eventList)
                workSession.update(auditLog);

            workSession.publish();
        }

        LOGGER.debug("============================== LOG DUMPER END ==============================");
        LOGGER.debug("============================================================================");
    }

    private String getStoredTable(Class<? extends EventLogObject> se) {
        String storeTable = storingTable.get(se);
        if (storeTable != null)
            return storeTable;

        Class clz = se;
        while (!clz.getName().equals(Object.class.getName())) {
            if (clz.isAnnotationPresent(Table.class)) {
                LOGGER.debug("Found table is " + clz.getSimpleName() + " for object of type " + clz.getSimpleName());
                Table table = (Table) clz.getAnnotation(Table.class);
                String tableName = table.name();
                if (tableName.isEmpty())
                    tableName = clz.getSimpleName();
                storingTable.put(se, tableName);
                return tableName;
            }
            clz = clz.getSuperclass();
        }

        throw new RuntimeException("Object doesn't have storing definitions or it is not a POJO");
    }
}
