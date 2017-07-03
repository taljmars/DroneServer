
@SET arg=%1
@IF %arg%. EQU . (
    @SET arg=start
)

@GOTO CASE_%arg%
@IF ERRORLEVEL 1 GOTO :DEFAULT_CASE

:CASE_start
	@ECHO "Start DB Server"
	@ECHO "Extracting DB files"
	@if NOT EXIST pgsql (
		@7za.exe x postgresql*
	)

	@ECHO "Loading DB Deamon"
	@SET firstTime=0

	@IF NOT EXIST dronedb_data (
		@ECHO "Creating PostGres basic"
		@pgsql\bin\initdb.exe --encoding=UTF-8 -D dronedb_data -U postgres
		@SET firstTime=1
	)

	@ECHO "Starting DB Deamon"
	@pgsql\bin\pg_ctl.exe -D dronedb_data -l mylog start

	@IF %firstTime%==1 (
		@ECHO "Creating DB"
		@pgsql\bin\createdb.exe -U postgres dronedb
	)

	@ECHO "Register to startup"
	@pgsql\bin\pg_ctl.exe register -N postgres -D "%cd%\dronedb_data"
	
	@GOTO exit

	
:CASE_stop
	@ECHO "Stopping DB Server"
	@pgsql\bin\pg_ctl.exe -D dronedb_data -l mylog stop
	
	@ECHO "Unregister from startup"
	@pgsql\bin\pg_ctl.exe unregister -N postgres 
	
	@GOTO exit
	
:CASE_restart
	@ECHO stop
	@pgsql\bin\pg_ctl.exe -D dronedb_data -l mylog restart
	
	@GOTO exit
	
	
:DEFAULT_CASE
	@ECHO Unknown action, exiting

	
:exit
	@ECHO "Done"