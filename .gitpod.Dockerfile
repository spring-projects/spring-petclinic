FROM ubuntu:20.04

RUN apt-get -qq update && \
    apt-get install -yq runit wget chrpath tzdata \
    lsof lshw sysstat net-tools numactl bzip2 maven default-jdk && \
    apt-get autoremove && apt-get clean && \
    rm -rf /var/lib/apt/lists/* /tmp/* /var/tmp/*

RUN if [ ! -x /usr/sbin/runsvdir-start ]; then \
        cp -a /etc/runit/2 /usr/sbin/runsvdir-start; \
    fi

ENV PATH=$PATH:/opt/couchbase/bin:/opt/couchbase/bin/tools:/opt/couchbase/bin/install
RUN groupadd -g 1000 couchbase && useradd couchbase -u 1000 -g couchbase -M

RUN mkdir -p /tmp/couchbase && \
    cd /tmp/couchbase && \
    wget https://packages.couchbase.com/releases/7.0.0-beta/couchbase-server-enterprise_7.0.0-beta-ubuntu20.04_amd64.deb && \
    dpkg -i ./couchbase-server-enterprise_7.0.0-beta-ubuntu20.04_amd64.deb

RUN sed -i -e '1 s/$/\/docker/' /opt/couchbase/VARIANT.txt


COPY scripts/run /etc/service/couchbase-server/run

RUN chrpath -r '$ORIGIN/../lib' /opt/couchbase/bin/curl
COPY scripts/start-cb.sh /opt/couchbase/
RUN chmod 777 /opt/couchbase/start-cb.sh

RUN cd /opt/couchbase && \
    mkdir -p var/lib/couchbase \
             var/lib/couchbase/config \
             var/lib/couchbase/data \
             var/lib/couchbase/stats \
             var/lib/couchbase/logs \
             var/lib/moxi

RUN chmod -R 777 /opt/couchbase/
RUN chmod 777 /etc/service/couchbase-server/run

# 8091: Couchbase Web console, REST/HTTP interface
# 8092: Views, queries, XDCR
# 8093: Query services (4.0+)
# 8094: Full-text Search (4.5+)
# 8095: Analytics (5.5+)
# 8096: Eventing (5.5+)
# 11207: Smart client library data node access (SSL)
# 11210: Smart client library/moxi data node access
# 11211: Legacy non-smart client library data node access
# 18091: Couchbase Web console, REST/HTTP interface (SSL)
# 18092: Views, query, XDCR (SSL)
# 18093: Query services (SSL) (4.0+)
# 18094: Full-text Search (SSL) (4.5+)
# 18095: Analytics (SSL) (5.5+)
# 18096: Eventing (SSL) (5.5+)
EXPOSE 8091 8092 8093 8094 8095 8096 11207 11210 11211 18091 18092 18093 18094 18095 18096
VOLUME /opt/couchbase/var













#FROM couchbase:enterprise-7.0.0-beta
#
#RUN apt-get update \
# && apt-get install -y sudo
#
#RUN chmod -R 777 /opt/couchbase/var/lib/