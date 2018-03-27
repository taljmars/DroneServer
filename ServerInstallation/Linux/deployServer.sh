#!/bin/sh

echo "Deploy Server"

mkdir ServerCore
tar -xf ServerCore* -C ServerCorer
cd ServerCore
./install.bash
cd ..
rm ServerCore*
echo "Done"  
