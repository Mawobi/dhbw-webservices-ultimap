FROM mariadb:10.5

# Set defaults for Environment
ENV MYSQL_ROOT_PASSWORD="definitvKeinTestPW"
ENV MYSQL_DATABASE="ultimap"
ENV MYSQL_USER="spring"
ENV MYSQL_PASSWORD="changeit"

COPY init.sql /docker-entrypoint-initdb.d/init.sql
HEALTHCHECK --start-period=30s CMD mysqlcheck ultimap -p$MYSQL_ROOT_PASSWORD || exit 1