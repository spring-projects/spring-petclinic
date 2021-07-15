FROM openjdk:8

ADD target /src

CMD [ "/usr/bin/find",  "/" ]