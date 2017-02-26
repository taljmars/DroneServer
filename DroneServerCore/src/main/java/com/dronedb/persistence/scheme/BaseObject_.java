package com.dronedb.persistence.scheme;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-20T00:10:18.602+0200")
@StaticMetamodel(BaseObject.class)
public class BaseObject_ {
	public static volatile SingularAttribute<BaseObject, String> objId;
	public static volatile SingularAttribute<BaseObject, Date> createdAt;
	public static volatile SingularAttribute<BaseObject, Date> updatedAt;
	public static volatile SingularAttribute<BaseObject, Long> version;
}
