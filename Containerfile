#NOTE: maybe replace this image with a TSSC openjdk image if such a thing ever needs to exist
FROM registry.access.redhat.com/ubi8/openjdk-11

USER 0

##############################
# vulenerability remediation #
##############################
# IMPORTANT: need to exclude filesystem due to https://bugzilla.redhat.com/show_bug.cgi?id=1708249#c31
RUN printf "[main]\nexcludepkgs=filesystem" > /etc/dnf/dnf.conf && \
    microdnf update -y && \
    microdnf clean all

# NOTE / WARNING / IMPORTANT:
#   work around for https://bugzilla.redhat.com/show_bug.cgi?id=1798685
RUN rm -f /var/log/lastlog

##########################
# compliance remediation #
##########################
# Nothing to do at the moment

###############
# install app #
###############
RUN mkdir /app
ADD target/*.jar /app/app.jar
RUN chown -R 1001:0 /app && chmod -R 774 /app
EXPOSE 8080

###########
# run app #
###########
USER 1001
ENTRYPOINT ["java", "-jar"]
CMD ["/app/app.jar"]
