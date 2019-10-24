FROM openjdk:8-jdk
MAINTAINER jdvinueza@telconet.ec
ARG archivo_jar
ARG puerto
ADD target/$archivo_jar app.jar
COPY startservice.sh /
ENV TZ 'America/Guayaquil'
RUN chmod 777 /startservice.sh
RUN echo $TZ > /etc/timezone && \
    apt-get update && apt-get install -y tzdata && \
    rm /etc/localtime && \
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    dpkg-reconfigure -f noninteractive tzdata && \
    apt-get clean
RUN apt-get install -y locales locales-all  && \
    apt-get install telnet
ENV LANG es_ES.UTF-8
ENV LANGUAGE es_ES.UTF-8
ENV LC_ALL es_ES.UTF-8
EXPOSE $puerto
CMD sh /startservice.sh
