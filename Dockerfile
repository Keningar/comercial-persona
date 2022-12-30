FROM registry.gitlab.telconet.ec/docker/images/microservicio:11-v2
ARG ARCHIVO_JAR
ARG PUERTO_INTERNO_EXP
COPY --chown=sistemas:sistemas target/$ARCHIVO_JAR app.jar
EXPOSE $PUERTO_INTERNO_EXP
ENTRYPOINT ["dumb-init", "java", "-jar", "-Dlogging.config=/app/log4j2.xml", "app.jar", "-Dspring.config.location=/app/application.properties"]
