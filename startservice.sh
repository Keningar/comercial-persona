#!/bin/bash

#Maintainer="Jose Vinueza <jdvinueza@telconet.ec>" 
#Description="Script para levantar ambiente local telcos"


while [ -z "$BD_TRUE"  ]; do
ipkaflka=${ipkfa}
ips=`echo $ipkaflka | sed -e 's/,/\n/g'`
for i in `echo $ips`
do
  ip=`echo $i | awk -F\: ' { print $1 } '`
  puerto=`echo $i | awk -F\: ' { print $2 } '`
  if  telnet $ip $puerto </dev/null 2>&1 | grep -q Escape 
  then
    echo "servicio kafka disponible"
    java -XX:+PrintFlagsFinal $JAVA_OPTS -jar /app.jar -Dspring.config.location=/application.properties -Dlogging.config=/log4j2.xm
    BD_TRUE="true"
    exit
  else
    echo "servicio kafka $i no esta disponible "
  fi
done
done
