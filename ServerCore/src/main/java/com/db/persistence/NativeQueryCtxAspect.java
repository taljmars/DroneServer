package com.db.persistence;

import com.db.persistence.workSessions.QueryExecutor;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;

//@Aspect
//@Component
public class NativeQueryCtxAspect {

    private final static Logger LOGGER = Logger.getLogger(NativeQueryCtxAspect.class);

    @Around(
            "(" +
                "execution(* com.db.persistence.workSessions.QueryExecutor.createNamedQuery(String, Class))" +
            ")" +
            "&& target(queryExecutor)"
    )
    public Object aroundExecution(ProceedingJoinPoint pjp, QueryExecutor queryExecutor) throws Throwable {
        LOGGER.debug("Using Aspect -> " + pjp);
        Object[] args = pjp.getArgs();
        String queryString = (String) args[0];
//        System.out.println("NativeQueryCtxAspect " + queryString);
        if (queryString.contains(":CTX")) {
            return null;
        }
//            System.out.println("NativeQueryCtxAspect BEFORE:" + queryString);
//            queryString = queryString.replaceAll(":CTX", simpleEntityManagerWrapper.ctx + "");
//            System.out.println("NativeQueryCtxAspect AFTER:" + queryString);
//            Object[] newArgs = args.clone();
//            newArgs[0] = queryString;
//            args = newArgs;
//        }
//        return pjp.proceed(args);
        return pjp.proceed();
    }
}
