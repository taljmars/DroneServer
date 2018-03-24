package com.db.aspects;

import com.db.persistence.events.audit.ObjectEvent;
import com.db.persistence.scheme.BaseObject;
import com.db.persistence.workSession.WorkSessionManager;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;

@Aspect
public class ObjectsModificationAspect {

    private final static Logger LOGGER = Logger.getLogger(ObjectsModificationAspect.class);

    @Autowired
    private ApplicationEventPublisher publisher;

    @Autowired
    private WorkSessionManager workSessionManager;

    @PostConstruct
    public void init() {
        LOGGER.debug("Initialized ObjectsModificationAspect, EventPublisher=" + publisher + ", WorkSessionManager=" + workSessionManager);
        if (publisher == null) {
            LOGGER.error("Event publisher is null, make sure you initialized the aspects via AspectFactory");
            System.exit(-1);
        }
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.movePrivateToPublic(..))")
    @Transactional
    public void aroundMovePrivateToPublic(ProceedingJoinPoint pjp) throws Throwable {
        try {
            LOGGER.debug("Using Aspect -> " + pjp);

            Object[] args = pjp.getArgs();
            BaseObject privateItem = ((BaseObject) args[0]).copy();
            BaseObject publicItem = ((BaseObject) args[1]).copy();
            Integer nextRevision = (Integer) args[3];
            LOGGER.info("Object changed from: " + privateItem);
            LOGGER.info("To: " + publicItem);

            String userName = workSessionManager.getUserNameByCtx(privateItem.getKeyId().getEntityManagerCtx());

            LOGGER.info("Around before is running! - By User:" + userName);
            pjp.proceed();
            if (privateItem.isDeleted())
                this.publisher.publishEvent(new ObjectEvent(ObjectEvent.ObjectEventType.DELETE, publicItem, privateItem, nextRevision, userName));
            else
                this.publisher.publishEvent(new ObjectEvent(ObjectEvent.ObjectEventType.UPDATE, publicItem, privateItem, nextRevision, userName));
            LOGGER.info("Around after is running!");
        }
        catch (Throwable t) {LOGGER.error("Error occur during event publishing:" + t.getMessage(), t);}
    }

    @Around("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.movePrivateToPublicForFirstTime(..))")
    @Transactional
    public void aroundMovePrivateToPublicForFirstTime(ProceedingJoinPoint pjp) throws Throwable {
        try {
            LOGGER.debug("Using Aspect -> " + pjp);

            Object[] args = pjp.getArgs();
            BaseObject privateItem = (BaseObject) args[0];
            Integer nextRevision = (Integer) args[2];
            LOGGER.info("Object will be added to public db: " + privateItem);

            String userName = workSessionManager.getUserNameByCtx(privateItem.getKeyId().getEntityManagerCtx());

            LOGGER.info("Around before is running! - By User:" + userName);
            pjp.proceed();
            this.publisher.publishEvent(new ObjectEvent(ObjectEvent.ObjectEventType.CREATE, null, privateItem, nextRevision, userName));
            LOGGER.info("Around after is running!");
        }
        catch (Throwable t) {LOGGER.error("Error occur during event publishing:" + t.getMessage(), t);}
    }

}
