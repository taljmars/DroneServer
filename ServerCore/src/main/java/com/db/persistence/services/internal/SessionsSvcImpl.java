package com.db.persistence.services.internal;

import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.services.QuerySvc;
import com.db.persistence.services.SessionsSvc;
import com.db.persistence.workSession.WorkSession;
import com.db.persistence.workSession.WorkSessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Lazy
@Component
public class SessionsSvcImpl implements SessionsSvc {

	private final static Logger LOGGER = Logger.getLogger(SessionsSvcImpl.class);

	@Autowired
	private WorkSessionManager workSessionManager;

	private WorkSession workSession;

	@PostConstruct
	public void init() {
		setForUser("PUBLIC");
	}

	String currentUserName = "";
	public void setForUser(String userName) {
		currentUserName = userName;
		LOGGER.debug("Context was changed for user : " + userName);
		workSession = workSessionManager.createSession(userName);
//		KeyAspect.setTenantContext(workSession.getSessionId());
	}

	@Autowired
	private RevisionManager revisionManager;

	@Autowired
	private QuerySvc querySvc;

	@Autowired
	private ObjectCrudSvc objectCrudSvc;

	@Override
	@Transactional
	public void publish() {
		// In case we've modified Revision
		LOGGER.debug("PUBLISH START !!!");
		LOGGER.debug("Update revision value in revision manager");
		workSession.publish();
		LOGGER.debug("PUBLISH END !!!");
	}

	@Override
	@Transactional
	public void discard() {
		LOGGER.debug("DISCARD START !!!");
		workSession.discard();
		LOGGER.debug("DISCARD END !!!");
	}
}
