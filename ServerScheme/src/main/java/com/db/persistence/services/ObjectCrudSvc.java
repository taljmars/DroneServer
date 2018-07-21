package com.db.persistence.services;

import com.db.persistence.exception.DatabaseValidationException;
import com.db.persistence.exception.ObjectInstanceException;
import com.db.persistence.scheme.BaseObject;
import javassist.tools.rmi.ObjectNotFoundException;

public interface ObjectCrudSvc {

	/**
	 * The following create an object of clz type.
	 * Mind that this object is not created at the DB (not in private nor public).
	 * The new object will have a generated object ID.
	 * Note: no trigger will be invoked for the newly created object at this point.
	 *
	 * @param clz object type
	 * @param <T> object genric type
	 * @return A clean object
	 */
	<T extends BaseObject> T create(String clz) throws ObjectInstanceException;

	/**
	 * The following update an object in the DB, mind that if an object is being updated for
	 * the first time, a copy of it will appear in the private session and this will be the
	 * modified object.
	 * During this object update, some triggers may invoked (for create/update action).
	 * Note: create triggers will occur on the first update of a new object.
	 *
	 * @param object Object to update
	 * @param <T> Generic type of the object
	 * @return The update object in the DB (private session)
	 * @throws DatabaseValidationException in case of a validation failure of the object
	 */
	<T extends BaseObject> T update(T object) throws DatabaseValidationException, ObjectInstanceException;

	/**
	 * The following update multiple objects in the DB, mind that if an object is being updated for
	 * the first time, a copy of it will appear in the private session and this will be the
	 * modified object.
	 * During this object update, some triggers may invoked (for create/update action).
	 * Note 1: create triggers will occur on the first update of a new object.
	 * Note 2: the trigger will be invoked at the same order of the sent objects.
	 *
	 * @param objects List of object to update
	 */
	void updateArray(BaseObject[] objects) throws DatabaseValidationException, ObjectInstanceException;

	/**
	 * The following delete an object from the DB, the is a unique behavior for two main cases.
	 * In case the object is in the public area (no private copy exist), it will be copied to the
	 * private session and be marked as deleted. In case it exist in the private session, it will
	 * simply be marked as deleted.
	 * Note: Delete triggers might be executed when deleting an object.
	 *
	 * @param object Object to be deleted
	 * @param <T> Generic type of the object
	 * @return The deleted object in the DB (private session)
	 * @throws DatabaseValidationException in case of a validation failure
	 */
	<T extends BaseObject> T delete(T object) throws DatabaseValidationException, ObjectInstanceException, ObjectNotFoundException;

	/**
	 * The following retrieve an object exist in the database, an exception will be thrown
	 * in case the object doesn't exist in the DB.
	 *
	 * @param objId MyUUID of the required object
	 * @return The object exist in the DB (the exact one)
	 * @throws ObjectNotFoundException In case the object not found in the database
	 */
	BaseObject read(final String objId) throws ObjectNotFoundException;

	/**
	 * The following retrieve an object exist in the database, unlike the standard read API.
	 * This get the object and verify it is of the type mentioned in the parameters least
	 * An exception will be thrown in case the object doesn't exist.
	 *
	 * @param objId UUID of the required object
	 * @param clz Object type
	 * @param <T> Generic object type
	 * @return the existing object.
	 * @throws ObjectNotFoundException In case the object not found in the database
	 */
	<T extends BaseObject> T readByClass(final String objId, final Class<T> clz) throws ObjectNotFoundException;

}
