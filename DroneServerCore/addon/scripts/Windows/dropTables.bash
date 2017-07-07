#!/bin/bash

sudo -u postgres psql dronedb -c "DROP SCHEMA public CASCADE;"

sudo -u postgres psql dronedb -c "drop TABLE circle;"
sudo -u postgres psql dronedb -c "drop TABLE circleperimeters;"
sudo -u postgres psql dronedb -c "drop TABLE mission_missionitemsuids;"
sudo -u postgres psql dronedb -c "drop TABLE Mission_Missionitemsuids;"
sudo -u postgres psql dronedb -c "drop TABLE mission;"
sudo -u postgres psql dronedb -c "drop TABLE Mission;"
sudo -u postgres psql dronedb -c "drop TABLE point;"
sudo -u postgres psql dronedb -c "drop TABLE Point;"
sudo -u postgres psql dronedb -c "drop TABLE polygonperimeter_points;"
sudo -u postgres psql dronedb -c "drop TABLE polygonperimeters;"
sudo -u postgres psql dronedb -c "drop TABLE regionofinterest;"
sudo -u postgres psql dronedb -c "drop TABLE returntohome;"
sudo -u postgres psql dronedb -c "drop TABLE takeoff;"
sudo -u postgres psql dronedb -c "drop TABLE land;"
sudo -u postgres psql dronedb -c "drop TABLE waypoint;"
sudo -u postgres psql dronedb -c "drop TABLE Waypoint;"

sudo -u postgres psql dronedb -c "CREATE SCHEMA public;"



