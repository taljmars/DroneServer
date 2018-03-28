@echo off 
echo Deploy Server

7za.exe x ServerCore* -oServerCore
cd ServerCore
call install.bat
cd ..
echo "Done"  
