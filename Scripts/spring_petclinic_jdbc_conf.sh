#!/bin/sh

# PetClinic appliaction deployment script
TMPDIR=/tmp/arm-workdir
PROPFILE=$TMPDIR/petclinic/WEB-INF/classes/spring/data-access.properties

mkdir -p $TMPDIR/petclinic
unzip /tmp/petclinic.war -d $TMPDIR/petclinic


sed -e "/# HSQL/,/# MySQL/ s/^/#/g" \
    -e "/# MySQL/,$ s/^#\([jh]\)/\1/g" \
    -e "s/\${HOSTNAME}/$1/g" \
    -e "s/\${SERNAME}/$2/g" \
    -e "s/\${PASSWORD}/$3/g" $PROPFILE > /tmp/jdbc.tmp$$


cp -f /tmp/jdbc.tmp$$ $PROPFILE
rm -f /tmp/jdbc.tmp$$

cd /tmp/arm-workdir/petclinic
zip -r /tmp/arm-workdir/petclinic-updated.zip ./*
mv /tmp/arm-workdir/petclinic-updated.zip /tmp/arm-workdir/petclinic.war
sudo mv /tmp/arm-workdir/petclinic.war $4/webapps

#sudo  sh /opt/tomcat7/bin/shutdown.sh
#sudo  sh /opt/tomcat7/bin/startup.sh
exit 0