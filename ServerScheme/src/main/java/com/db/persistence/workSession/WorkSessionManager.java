package com.db.persistence.workSession;

import com.db.persistence.scheme.BaseObject;

public interface WorkSessionManager<TOKEN> {

    WorkSession createSession(TOKEN token, String userName);

    WorkSession destroySession(WorkSession workSession);

    <T extends BaseObject> Boolean markDirty(WorkSession workSession, T obj);

    WorkSession getSessionByToken(TOKEN currentToken);

    WorkSession getOrhpanSessionByUserName(String userName);

    WorkSession reviveSession(WorkSession workSession, TOKEN token);

    WorkSession orphanizeSession(TOKEN token);

    String getUserNameByCtx(Integer ctx);

}
