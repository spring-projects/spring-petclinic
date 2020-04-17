#!/bin/bash
if [ "$1" == "" ];
then
  BASEDIR=$(cd ../..; pwd)
else
  BASEDIR=$1
fi
echo "Base directory: $BASEDIR"
if [ ! -d $BASEDIR/jdk/zulu8.46.0.19-ca-jdk8.0.252-linux_x64 ];
then
  echo "Downloading JDKs..."
  pushd .
  cd $BASEDIR
  mkdir jdk
  cd jdk
  curl -OL https://cdn.azul.com/zulu/bin/zulu8.46.0.19-ca-jdk8.0.252-linux_x64.tar.gz
  tar xzvf zulu8.46.0.19-ca-jdk8.0.252-linux_x64.tar.gz
  curl -OL https://github.com/AdoptOpenJDK/openjdk11-binaries/releases/download/jdk-11.0.7%2B10/OpenJDK11U-jdk_x64_linux_hotspot_11.0.7_10.tar.gz
  tar xzvf OpenJDK11U-jdk_x64_linux_hotspot_11.0.7_10.tar.gz
  curl -OL https://builds.shipilev.net/openjdk-jdk8-dev/openjdk-jdk8-dev-latest-linux-x86_64-release.tar.xz
  tar xJvf openjdk-jdk8-dev-latest-linux-x86_64-release.tar.xz
  mv j2sdk-image jdk8dev
  popd
fi
if [ ! -d $BASEDIR/ap ];
then
  ./fetch_ap.sh
fi
if [ ! -f $BASEDIR/dd-java-agent.jar ];
then
  ./fetch_dd.sh
fi
echo "Building spring-petclinic"
cd $BASEDIR/spring-petclinic
export JAVA_HOME=$BASEDIR/jdk/zulu8.46.0.19-ca-jdk8.0.252-linux_x64
./mvnw package -DskipTests=true
