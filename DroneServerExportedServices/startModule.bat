@echo off 

set EXP_PATH=src\main\java
echo "Clear Old Files"

del /q %cd%\%EXP_PATH%\*

set IP=127.0.0.1
:: IP=178.62.1.156

echo "Exporting Services"
"C:\Program Files\Java\jdk1.8.0_112\bin\wsimport" -d %EXP_PATH% -keep http://%IP%:1234/ws/MissionCrudSvcRemote?wsdl
"C:\Program Files\Java\jdk1.8.0_112\bin\wsimport" -d %EXP_PATH% -keep http://%IP%:1234/ws/PerimeterCrudSvcRemote?wsdl
"C:\Program Files\Java\jdk1.8.0_112\bin\wsimport" -d %EXP_PATH% -keep http://%IP%:1234/ws/DroneDbCrudSvcRemote?wsdl
"C:\Program Files\Java\jdk1.8.0_112\bin\wsimport" -d %EXP_PATH% -keep http://%IP%:1234/ws/QuerySvcRemote?wsdl
"C:\Program Files\Java\jdk1.8.0_112\bin\wsimport" -d %EXP_PATH% -keep http://%IP%:1234/ws/SessionsSvcRemote?wsdl

echo "Done"

