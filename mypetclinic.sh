#!/bin/bash
arg=${1}

DATE_TIME=`date '+%Y-%m-%d %H:%M:%S'`
printf -v date '%(%Y-%m-%d %H:%M:%S)T\n' 
# Contanier details at https://hub.docker.com/orgs/krishnamanchikalapudi/repositories

export containerName=mypetclinic
export orgName=krishnamanchikalapudi
export imageName=mypetclinic
export hostAddress=127.0.0.1
export exposePort=7080
export WEB_ADDR="http://${hostAddress}:${exposePort}/"

current_folder=`pwd`
# docker container ls -a | grep petclinic | awk '{print $1}'
containerId=`docker container ls -a | grep ${imageName} | awk '{print $1}'`
imageId=`docker image ls -a | grep ${containerName} | awk '{print $3}'`

clean() {
    printf "\n Cleaning container: ${containerName} and image-id: ${imageId} \n "
    # docker rmi -f ${imageId}  
    docker image prune -a -f --filter "until=1h"
    docker container prune -f --filter "until=1h"
    docker system prune -f --filter "until=1h"
    sleep 3
    docker rmi -f $(docker images -aq)
}
startContainer() {
    printf "\n -------- Downloading container: ${containerName} -------- \n "
    docker pull ${orgName}/${imageName}:latest 

    # docker run -d --name mypetclinic -p 7080:8080 petclinic:cli
    docker run -d --name ${containerName} -p ${exposePort}:8080 ${orgName}/${imageName}:latest

    sleep 5
    docker logs -f ${containerName} &

    # xdg-open $WEB_ADDR  # ubuntu
    # open 'Google Chrome' $WEB_ADDR # mac 
    printf "\n ----------------------------------------------------------------  "
    printf "\n ---------- In Web Browser, open http://localhost:7080 ----------  "
    printf "\n ----------------------------------------------------------------  "
}
stopContainer() {
     printf "\n -------- Stop container: ${containerName} -------- \n "
     # docker container stop ${docker container ls -a | grep postgres | awk '{print $1}'}
     docker stop $containerName -t 0
     sleep 5
     docker rm -f $containerName
}

# -z option with $1, if the first argument is NULL. Set to default
if  [[ -z "$1" ]] ; then # check for null
    echo "User action is NULL, setting to default RESTART"
    arg='START'
fi
# -n string - True if the string length is non-zero.
if [[ -n $arg ]] ; then
    arg_len=${#arg}
    # uppercase the argument
    arg=$(echo ${arg} | tr [a-z] [A-Z] | xargs)
    echo "User Action: ${arg}, and arg length: ${arg_len}"

    if  [[ "CLEAN" == "${arg}" ]] ; then # Clean 
        clean
    elif [[ "START" == "${arg}" ]] ; then   # Download & start 
        startContainer
    elif [[ "START" == "${arg}" ]] ; then   # stop 
        stopContainer
    fi
fi


printf '%(%Y-%m-%d %H:%M:%S)T\n'