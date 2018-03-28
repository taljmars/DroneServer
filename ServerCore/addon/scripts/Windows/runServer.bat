
@ECHO "Running Server"

if EXIST externalTools/Windows/pgsqlStart.bat (
    call externalTools/Windows/pgsqlStart.bat
)

@java -cp jars/* com.db.server.ServerMain logs/ conf/