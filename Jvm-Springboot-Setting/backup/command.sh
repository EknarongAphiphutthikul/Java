#!/bin/sh

/usr/lib/jvm/java-1.8-openjdk/bin/jstatd -J-Djava.security.policy=jstatd.all.policy -J-Djava.rmi.server.hostname=169.254.172.18 -p 8082 > /dev/null 2>&1

java -Dfile.encoding=tis-620 -Dlog4j2.formatMsgNoLookups=true -DKUBERNETES_NAMESPACE=${KUBERNETES_NAMESPACE} -DKUBERNETES_PODNAME=${KUBERNETES_PODNAME}  ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /home/appuser/app/app.jar

