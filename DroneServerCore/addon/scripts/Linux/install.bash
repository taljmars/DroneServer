#!/bin/bash

CURR_PATH=`pwd`

UINST_FILE=uninstall.bash

echo "Getting DB files"
sudo apt-get install postgresql

echo "Building uninstall script \"$UINST_FILE\""
echo "#!/bin/bash" > $UINST_FILE
echo "./externalTools/pgsqlStop.bash" >> $UINST_FILE
echo "sudo apt-get --purge remove postgresql" >> $UINST_FILE
echo "rm -rf *" >> $UINST_FILE

echo "Setting permissions"
chmod 777 $UINST_FILE

echo "Start DB Server"
./externalTools/pgsqlStart.bash

echo "Prepare Future launch"
cp scripts/runServer.bash run.bash

echo "Clearing redundant files"
rm install.bash

echo "Done"
