
@ECHO "Running Server"

call externalTools/Windows/pgsqlStart.bat

@java -cp jars/* com.dronedb.server.DroneServer logs/ conf/