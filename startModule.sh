!#/bin/bash

EXP_PATH=DroneServerExportedServices/src/main/java
echo "Clear Old Files"
rm -rf $EXP_PATH/*

echo "Exporting Services"
wsimport -d $EXP_PATH -keep http://localhost:9999/ws/MissionCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://localhost:9999/ws/PerimeterCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://localhost:9999/ws/DroneDbCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://localhost:9999/ws/QuerySvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://localhost:9999/ws/SessionsSvcRemote?wsdl

echo "Done"

