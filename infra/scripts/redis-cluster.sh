#!/usr/bin/env bash
# Settings
# Make sure you run "brew install redis"

# BIN_PATH="/opt/homebrew/bin"
REDIS_CLI=`which redis-cli`
REDIS_SERVER=`which redis-server`
CLUSTER_HOST=127.0.0.1
# Creates a cluster at ports 6001-6006 with 3 masters 6001-6003 and 3 slaves 6004-6006
PORT=${2:-6000}
TIMEOUT=2000
NODES=6
REPLICAS=1
PROTECTED_MODE=yes
ADDITIONAL_OPTIONS=""

if [ -a config.sh ]
then
    source "config.sh"
fi

# Computed vars
ENDPORT=$((PORT+NODES))

if [ "$1" == "start" ]
then
    while [ $((PORT < ENDPORT)) != "0" ]; do
        PORT=$((PORT+1))
        echo "Starting $PORT"
        $REDIS_SERVER --port $PORT --protected-mode $PROTECTED_MODE --cluster-enabled yes --cluster-config-file nodes-${PORT}.conf --cluster-node-timeout $TIMEOUT --appendonly yes --appendfilename appendonly-${PORT}.aof --dbfilename dump-${PORT}.rdb --logfile ${PORT}.log --daemonize yes ${ADDITIONAL_OPTIONS}
    done
    exit 0
fi

if [ "$1" == "create" ]
then
    HOSTS=""
    while [ $((PORT < ENDPORT)) != "0" ]; do
        PORT=$((PORT+1))
        HOSTS="$HOSTS $CLUSTER_HOST:$PORT"
    done
    OPT_ARG=""
    if [ "$2" == "-f" ]; then
        OPT_ARG="--cluster-yes"
    fi
    $REDIS_CLI --cluster create $HOSTS --cluster-replicas $REPLICAS $OPT_ARG
    exit 0
fi

if [ "$1" == "stop" ]
then
    while [ $((PORT < ENDPORT)) != "0" ]; do
        PORT=$((PORT+1))
        echo "Stopping $PORT"
        $REDIS_CLI -p $PORT shutdown nosave
    done
    exit 0
fi

if [ "$1" == "watch" ]
then
    PORT=$((PORT+1))
    while [ 1 ]; do
        clear
        date
        $REDIS_CLI -p $PORT cluster nodes | head -30
        sleep 1
    done
    exit 0
fi

if [ "$1" == "clean" ]
then
    echo "Cleaning *.log"
    rm -rf *.log
    echo "Cleaning appendonly-*"
    rm -rf appendonly-*
    echo "Cleaning dump-*.rdb"
    rm -rf dump-*.rdb
    echo "Cleaning nodes-*.conf"
    rm -rf nodes-*.conf
    exit 0
fi

if [ "$1" == "clean-logs" ]
then
    echo "Cleaning *.log"
    rm -rf *.log
    exit 0
fi

echo "Usage: $0 [start|create|stop|watch|clean|clean-logs|call]"
echo "start  [PORT]       -- Launch Redis Cluster instances."
echo "create [PORT] [-f]  -- Create a cluster using redis-cli --cluster create."
echo "stop   [PORT]       -- Stop Redis Cluster instances."
echo "watch  [PORT]       -- Show CLUSTER NODES output (first 30 lines) of first node."
echo "clean               -- Remove all instances data, logs, configs."
echo "clean-logs   -- Remove just instances logs."
