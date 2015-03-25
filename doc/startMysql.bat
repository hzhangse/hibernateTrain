set MYSQL_HOME=D:\work\tools\mysql\mysql-5.6.23-winx64
set path=%path%;%MYSQL_HOME%\bin

%MYSQL_HOME%\bin\mysqld --defaults-file="%MYSQL_HOME%\my.ini" --console