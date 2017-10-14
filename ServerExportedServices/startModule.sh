!#/bin/bash

EXP_PATH=src/main/java
echo "Clear Old Files"
rm -rf $EXP_PATH/*

IP=127.0.0.1
#IP=178.62.1.156

echo "Exporting Services"
wsimport -d $EXP_PATH -keep http://$IP:1234/ws/MissionCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://$IP:1234/ws/PerimeterCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://$IP:1234/ws/objectCrudSvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://$IP:1234/ws/QuerySvcRemote?wsdl
wsimport -d $EXP_PATH -keep http://$IP:1234/ws/SessionsSvcRemote?wsdl

echo "Done"

