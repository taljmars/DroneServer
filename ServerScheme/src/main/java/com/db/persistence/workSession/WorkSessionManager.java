package com.db.persistence.workSession;

import com.db.persistence.scheme.BaseObject;

public interface WorkSessionManager {

    WorkSession createSession(String userName);

    void destroySession(WorkSession workSession);

    <T extends BaseObject> Boolean markDirty(WorkSession workSession, T obj);
}
