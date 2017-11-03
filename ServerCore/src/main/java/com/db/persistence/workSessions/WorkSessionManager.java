package com.db.persistence.workSessions;

import com.db.persistence.objectStore.PersistencyManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Component
public class WorkSessionManager {

    @Autowired
    private PersistencyManager objectStore;

    private Map<String, WorkSession> workSessionMap;

    public WorkSessionManager() {
        workSessionMap = new HashMap<>();
    }

    @Transactional
    public WorkSession createSession(String userName) {
        if (workSessionMap.keySet().contains(userName))
            return workSessionMap.get(userName);

        WorkSession session = objectStore.createWorkSession(userName);
        workSessionMap.put(userName, session);
        return session;
    }
}
