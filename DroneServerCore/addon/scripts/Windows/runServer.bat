
@ECHO "Running Server"

call externalTools/pgsqlStart.bat

@java -cp jars/* com.dronedb.server.DroneServer logs/ conf/