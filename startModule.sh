echo "Generating WSDL"
#mkdir CMpub/wsdl
#wsgen -wsdl -d CMpub/wsdl/ -cp CMpub/classes/ -s DroneServerCore/src/main/java/ com.dronedb.persistence.ws.internal.PerimeterCrudSvcRemoteImpl -wsdl
#wsgen -wsdl -d CMpub/wsdl/ -cp CMpub/classes/ -s DroneServerCore/src/main/java/ com.dronedb.persistence.ws.internal.MissionCrudSvcRemoteImpl -wsdl
#wsgen -wsdl -d CMpub/wsdl/ -cp CMpub/classes/ -s DroneServerCore/src/main/java/ com.dronedb.persistence.ws.internal.DroneDbCrudSvcRemoteImpl -wsdl
#wsgen -wsdl -d CMpub/wsdl/ -cp CMpub/classes/ -s DroneServerCore/src/main/java/ com.dronedb.persistence.ws.internal.QuerySvcRemoteImpl -wsdl

wsimport -keep http://localhost:9999/ws/MissionCrudSvcRemote?wsdl
wsimport -keep http://localhost:9999/ws/PerimeterCrudSvcRemote?wsdl
wsimport -keep http://localhost:9999/ws/DroneDbCrudSvcRemote?wsdl
wsimport -keep http://localhost:9999/ws/QuerySvcRemote?wsdl

echo "Done"

