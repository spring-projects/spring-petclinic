#!/bin/sh

MyUSER="$1"
MyPASS="$2"

#sudo mysqladmin -u root password $MyPASS

C1="CREATE DATABASE IF NOT EXISTS petclinic;"
C2="USE petclinic;"
C3="GRANT ALL PRIVILEGES ON *.*  TO '$MyUSER'@'%' IDENTIFIED BY '$MyPASS' WITH GRANT OPTION;"
C4="FLUSH PRIVILEGES;"
SQL="${C1}${C2}${C3}${C4}"

#sudo MYSQL_PWD="$MyPASS" mysql -h localhost "--user=root" -Bse "$SQL"
sudo mysql -Bse "$SQL"
if [ $? != 0 ];then
	echo "ERROR: sudo mysql -Bse \"$SQL\""
	exit 1
fi


exit 0