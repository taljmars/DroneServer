package com.db.persistence.scheme;

/**
 * Created by taljmars on 4/30/17.
 */
public class Constants {

    // Values
    public static final int TIP_REVISION = Integer.MAX_VALUE;

    public static final String GEN_CTX =
            "( " +
                "entityManagerCtx = :CTX " +
                "OR " +
                "(entityManagerCtx = 0 AND (objId NOT IN (SELECT referredObjId FROM LockedObject WHERE referredCtx = :CTX ))) " +
            ")";

}
