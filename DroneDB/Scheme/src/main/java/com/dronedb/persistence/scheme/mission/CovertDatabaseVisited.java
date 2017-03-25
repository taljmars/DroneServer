package com.dronedb.persistence.scheme;

/**
 * Created by taljmars on 3/18/17.
 */
public interface CovertDatabaseVisited {

    void accept(ConvertDatabaseVisitor convertDatabaseVisitor);
}
