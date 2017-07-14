@echo off

echo stop
pgsql\bin\pg_ctl.exe -D dronedb_data -l logs\mylog stop

echo "Unregister from startup"
pgsql\bin\pg_ctl.exe unregister -N postgres

echo "Done"