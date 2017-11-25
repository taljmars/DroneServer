package com.db.persistence.services.internal;

import com.db.persistence.services.ObjectCrudSvc;
import com.db.persistence.services.QuerySvc;
import com.db.persistence.services.SessionsSvc;
import com.db.persistence.workSessions.WorkSession;
import com.db.persistence.workSessions.WorkSessionManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Lazy
@Component
public class SessionsSvcImpl implements SessionsSvc {

	private final static Logger logger = Logger.getLogger(SessionsSvcImpl.class);

	@Autowired
	private WorkSessionManager objectStoreSessionManager;

	private WorkSession workSession;

	@PostConstruct
	public void init() {
		setForUser("PUBLIC");
	}

	String currentUserName = "";
	public void setForUser(String userName) {
		if (currentUserName.equals(userName))
			return;

		logger.debug("Context was changed for user : " + userName);
		workSession = objectStoreSessionManager.createSession(userName);
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
		logger.debug("PUBLISH START !!!");
		logger.debug("Update revision value in revision manager");
		workSession.publish();
		workSession.flush();
		logger.debug("PUBLISH END !!!");
	}

	@Override
	@Transactional
	public void discard() {
		logger.debug("DISCARD START !!!");
		workSession.discard();
		logger.debug("DISCARD END !!!");
	}
}
