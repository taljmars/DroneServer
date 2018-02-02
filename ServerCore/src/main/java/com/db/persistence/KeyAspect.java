package com.db.persistence;

import com.db.persistence.objectStore.SimpleEntityManagerWrapper;
import com.db.persistence.workSessions.QueryExecutor;
import com.db.persistence.workSessions.WorkSession;
import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Before;
import org.hibernate.Session;

//@Aspect
//@Component
public class KeyAspect {

    private final static Logger LOGGER = Logger.getLogger(KeyAspect.class);

    private static int tenantContext;
    public static void setTenantContext(int tId) {
        tenantContext = tId;
    }

//    // only applicable to user service
//    @Before("execution(* com.db.persistence.objectStore.VirtualizedEntityManager.*(..)) && target(virtualizedEntityManager)")
//    public void aroundExecution(JoinPoint pjp, VirtualizedEntityManager virtualizedEntityManager) throws Throwable {
//        LOGGER.debug("Using Aspect -> " + tenantContext);
//        org.hibernate.Filter filter = virtualizedEntityManager.getEntityManager().unwrap(Session.class).enableFilter("tenantFilter");
//        filter.setParameter("entityManagerCtx", tenantContext);
//        filter.validate();
//    }

    @Before(
            "(" +
                "execution(* com.db.persistence.workSessions.WorkSession.find(..)) ||" +
                "execution(* com.db.persistence.workSessions.WorkSession.update(..)) ||" +
                "execution(* com.db.persistence.workSessions.WorkSession.delete(..)) ||" +
                "execution(* com.db.persistence.workSessions.WorkSession.pull(..)) " +
            ")" +
            "&& target(workSession)"
    )
    public void aroundExecution(JoinPoint pjp, WorkSession workSession) throws Throwable {
        LOGGER.debug("Using Aspect -> " + tenantContext + " " + pjp + " " + workSession.getSessionId());
        org.hibernate.Filter filter = workSession.getEntityManager().getEntityManager().unwrap(Session.class).enableFilter("tenantFilter");
        filter.setParameter("entityManagerCtx", tenantContext);
        filter.setParameter("cont",
                "entitymanagerctx = 0 AND " +
                        "(objid, entitymanagerctx in " +
                        "(select entitymanagerctx from lockedObject where entitymanagerctx != " + tenantContext + ")");
        filter.validate();
    }

    @Before(
            "(" +
                "execution(* com.db.persistence.workSessions.QueryExecutor.translateResults(..)) ||" +
                "execution(* com.db.persistence.workSessions.QueryExecutor.createNativeQuery(..)) ||" +
                "execution(* com.db.persistence.workSessions.QueryExecutor.createNativeQuery(..)) ||" +
                "execution(* com.db.persistence.workSessions.QueryExecutor.createNamedQuery(..)) " +
            ")" +
            "&& target(queryExecutor)"
    )
    public void aroundExecution(JoinPoint pjp, QueryExecutor queryExecutor) throws Throwable {
        LOGGER.debug("Using Aspect -> " + tenantContext + " " + pjp + " " + queryExecutor.getWorkSession().getSessionId());
        org.hibernate.Filter filter = queryExecutor.getWorkSession().getEntityManager().getEntityManager().unwrap(Session.class).enableFilter("tenantFilter");
        filter.setParameter("entityManagerCtx", tenantContext);
        filter.setParameter("cont",
                "entitymanagerctx = 0 AND " +
                        "(objid, entitymanagerctx in " +
                        "(select entitymanagerctx from lockedObject where entitymanagerctx != " + tenantContext + ")");
        filter.validate();
    }

    @Before("(" +
            "execution(* com.db.persistence.objectStore.SimpleEntityManagerWrapper.*(..))" +
            ")" +
            "&& target(simpleEntityManagerWrapper)"
    )
    public void aroundExecution(JoinPoint pjp, SimpleEntityManagerWrapper simpleEntityManagerWrapper) throws Throwable {
        System.out.println("Using Aspect -> " + pjp + " " + simpleEntityManagerWrapper.ctx);
        org.hibernate.Filter filter = simpleEntityManagerWrapper.entityManager.unwrap(Session.class).enableFilter("tenantFilter");
        filter.setParameter("entityManagerCtx", simpleEntityManagerWrapper.ctx);
        filter.setParameter("cont",
                "entitymanagerctx = 0 AND " +
                        "(objid, entitymanagerctx in " +
                        "(select entitymanagerctx from lockedObject where entitymanagerctx != " + simpleEntityManagerWrapper.ctx + ")");
        filter.validate();
    }
}
