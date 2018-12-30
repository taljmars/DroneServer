#!/bin/sh

echo "Deploy Server"

mkdir ServerCore
tar -xf ServerCore*.tar -C ServerCore
cd ServerCore
chmod +x install.bash
./install.bash
cd ..
rm ServerCore*.tar
echo "Done"  
