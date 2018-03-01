package com.db.persistence.events.audit;

import com.db.persistence.events.ServerEvent;

import java.util.UUID;

@ServerEvent(id = 5)
public class AccessEvent implements AuditEvent {

}
