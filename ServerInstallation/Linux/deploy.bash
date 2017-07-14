#!/bin/bash

echo "Deploy Server"

mkdir DroneServer
tar -xf DroneServerCore* -C DroneServer
cd DroneServer
./install.bash
cd ..
rm DroneServerCore*
echo "Done"  
