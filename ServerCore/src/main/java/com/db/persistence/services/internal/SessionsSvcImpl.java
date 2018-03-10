package com.db.persistence.services.internal;

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
public class SessionsSvcImpl extends TokenAwareSvcImpl implements SessionsSvc {

	private final static Logger LOGGER = Logger.getLogger(SessionsSvcImpl.class);

	@Autowired
	private WorkSessionManager workSessionManager;

	@PostConstruct
	public void init() {
		LOGGER.debug("Initialize SessionSvc");
	}

	@Override
	@Transactional
	public void publish() {
		// In case we've modified Revision
		LOGGER.debug("PUBLISH START !!!");
		LOGGER.debug("Update revision value in revision manager");
		WorkSession workSession = workSession().publish();
		LOGGER.debug("PUBLISH END !!!");
		workSessionManager.createSession(getToken(), workSession.getUserName1());
	}

	@Override
	@Transactional
	public void discard() {
		LOGGER.debug("DISCARD START !!!");
		WorkSession workSession = workSession().discard();
		LOGGER.debug("DISCARD END !!!");
		workSessionManager.createSession(getToken(), workSession.getUserName1());
	}

}
