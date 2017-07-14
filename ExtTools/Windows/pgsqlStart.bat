@echo off


echo "Loading DB Deamon"
set firstTime=0

if NOT EXIST dronedb_data (
    echo "Creating PostGres basic"
    pgsql\bin\initdb.exe --encoding=UTF-8 -D dronedb_data -U postgres
    set firstTime=1
)

echo "Starting deamon"
pgsql\bin\pg_ctl.exe -D dronedb_data -l logs\mylog start

if %firstTime%==1 (
    echo "Creating DB"
	pgsql\bin\createdb.exe -U postgres dronedb
)

echo "Register to startup"
pgsql\bin\pg_ctl.exe register -N postgres -D "%cd%\dronedb_data"

echo "Done"