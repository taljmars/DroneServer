@echo off

set UINST_FILE="uninstall.bat"

echo "Extracting DB files"
if NOT EXIST pgsql (
    externalTools\7za.exe x externalTools\postgresql* -o%cd%
)

echo "Starting DB Deamon"
call externalTools\pgsqlStart.bat

echo "Prepare Future launch of Server"
copy scripts\runServer.bat run.bat

echo "Creating Uninstall file"
echo @echo off > %UINST_FILE%
echo echo Build uninstall script >> %UINST_FILE%
echo echo "Stop Server DB Deamon"  >> %UINST_FILE%
echo call externalTools\pgsqlStop.bat >> %UINST_FILE%
echo set INST_DIR=%%cd%% >> %UINST_FILE%
echo cd .. >> %UINST_FILE%
echo start /b "" cmd /c rmdir %%INST_DIR%% /q /s ^&^exit /b >> %UINST_FILE%
echo echo "Done"  >> %UINST_FILE%

start /b "" cmd /c del install.bat /q /s &exit /b

echo "Done"