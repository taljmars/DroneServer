package com.db.persistence.workSession;

import com.db.persistence.scheme.BaseObject;

public interface WorkSessionManager {

    WorkSession createSession(String token, String userName);

    WorkSession destroySession(WorkSession workSession);

    <T extends BaseObject> Boolean markDirty(WorkSession workSession, T obj);

    WorkSession getSessionByToken(String currentToken);

    WorkSession getOrhpanSessionByUserName(String currentToken);

    WorkSession reviveSession(WorkSession workSession, String token);

    WorkSession orphanizeSession(String token);

    String getUserNameByCtx(Integer ctx);
}
