#!/bin/bash

CURR_PATH=`pwd`

UINST_FILE=uninstall.bash

echo "Building uninstall script \"$UINST_FILE\""
echo "#!/bin/bash" > $UINST_FILE
echo "rm -rf *" >> $UINST_FILE

echo "Setting permissions"
chmod 777 $UINST_FILE

echo "Prepare Future launch"
chmod 777 scripts/runServer.bash
cp scripts/runServer.bash run.bash
echo "Clearing redundant files"
rm install.bash

echo "Done"
