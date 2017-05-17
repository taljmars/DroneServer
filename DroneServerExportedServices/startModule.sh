!#/bin/bash

EXP_PATH=src/main/java
echo "Clear Old Files"
rm -rf $EXP_PATH/*

echo "Exporting Services"
wsimport -d $EXP_PATH -keep http://178.62.1.156:1234/ws/MissionCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://178.62.1.156:1234/ws/PerimeterCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://178.62.1.156:1234/ws/DroneDbCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://178.62.1.156:1234/ws/QuerySvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://178.62.1.156:1234/ws/SessionsSvcRemote?wsdl

echo "Done"

