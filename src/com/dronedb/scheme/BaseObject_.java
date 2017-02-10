package com.dronedb.scheme;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2017-02-10T17:09:19.793+0200")
@StaticMetamodel(BaseObject.class)
public class BaseObject_ {
	public static volatile SingularAttribute<BaseObject, Integer> objId;
	public static volatile SingularAttribute<BaseObject, Date> createdAt;
	public static volatile SingularAttribute<BaseObject, Date> updatedAt;
	public static volatile SingularAttribute<BaseObject, Long> version;
}
