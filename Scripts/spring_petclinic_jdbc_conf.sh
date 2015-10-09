#!/bin/sh


# PetClinic appliaction deployment script
TMPDIR=$TMPDIR
PROPFILE=$TMPDIR/petclinic/WEB-INF/classes/spring/data-access.properties

mkdir -p $TMPDIR/petclinic
if [ $? != 0 ];then
	echo "ERROR: mkdir -p $TMPDIR/petclinic"
	exit 1
fi 

unzip /tmp/petclinic.war -d $TMPDIR/petclinic
if [ $? != 0 ];then
	echo "ERROR: unzip /tmp/petclinic.war -d $TMPDIR/petclinic"
	exit 1
fi 

sed -e "/# HSQL/,/# MySQL/ s/^/#/g" \
    -e "/# MySQL/,$ s/^#\([jh]\)/\1/g" \
    -e "s/\${HOSTNAME}/$1/g" \
    -e "s/\${USERNAME}/$2/g" \
    -e "s/\${PASSWORD}/$3/g" $PROPFILE > /tmp/jdbc.tmp$$
if [ $? != 0 ];then
	echo "ERROR: sed $PROFILE > /tmp/jdbc.tmp$$"
	exit 1
fi 


mv -f /tmp/jdbc.tmp$$ $PROPFILE
if [ $? != 0 ];then
	echo "ERROR: mv -f /tmp/jdbc.tmp$$ $PROPFILE"
	exit 1
fi 

cd $TMPDIR/petclinic
zip -r $TMPDIR/petclinic-updated.zip ./*
if [ $? != 0 ];then
	echo "ERROR: zip -r $TMPDIR/petclinic-updated.zip"
	exit 1
fi

mv $TMPDIR/petclinic-updated.zip $TMPDIR/petclinic.war
if [ $? != 0 ];then
	echo "ERROR: mv $TMPDIR/petclinic-updated.zip $TMPDIR/petclinic.war"
	exit 1
fi
sudo mv $TMPDIR/petclinic.war $4/webapps
if [ $? != 0 ];then
	echo "ERROR: mv $TMPDIR/petclinic.war $4/webapps"
	exit 1
fi

#sudo  sh /opt/tomcat7/bin/shutdown.sh
#sudo  sh /opt/tomcat7/bin/startup.sh
exit 0