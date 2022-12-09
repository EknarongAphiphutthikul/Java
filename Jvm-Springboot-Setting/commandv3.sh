#!/bin/sh

if [ ! -d /home/appuser/applog/$KUBERNETES_NAMESPACE ]; then
	mkdir /home/appuser/applog/$KUBERNETES_NAMESPACE
fi

if [ ! -d /home/appuser/applog/$KUBERNETES_NAMESPACE/heap ]; then
	mkdir /home/appuser/applog/$KUBERNETES_NAMESPACE/heap
fi

java -Xdiag -server -Dfile.encoding=tis-620 -Dlog4j2.formatMsgNoLookups=true -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath="/home/appuser/applog/"$KUBERNETES_NAMESPACE"/heap/"$KUBERNETES_PODNAME"-heap-"$(date +%Y_%m_%d-%H_%M_%S).hprof -DKUBERNETES_NAMESPACE=${KUBERNETES_NAMESPACE} -DKUBERNETES_PODNAME=${KUBERNETES_PODNAME}  ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /home/appuser/app/app.jar