@echo off 
echo Deploy Server

7za.exe x DroneServer* -oDroneServer
cd DroneServer
call install.bat
cd ..
echo "Done"  
