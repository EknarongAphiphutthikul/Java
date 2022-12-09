#!/bin/sh

if [ ! -d /home/appuser/applog/$KUBERNETES_NAMESPACE ]; then
	mkdir /home/appuser/applog/$KUBERNETES_NAMESPACE
fi

if [ ! -d /home/appuser/applog/$KUBERNETES_NAMESPACE/gc ]; then
	mkdir /home/appuser/applog/$KUBERNETES_NAMESPACE/gc
fi

java -Xdiag -server -Dfile.encoding=tis-620 -Dlog4j2.formatMsgNoLookups=true -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -verbose:gc -XX:+PrintGCDetails -XX:+PrintReferenceGC -XX:+PrintGCCause -XX:+PrintPromotionFailure -Xloggc:"/home/appuser/applog/"$KUBERNETES_NAMESPACE"/gc/"$KUBERNETES_PODNAME"-gc-"$(date +%Y_%m_%d-%H_%M).log  -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=20 -XX:GCLogFileSize=10M -DKUBERNETES_NAMESPACE=${KUBERNETES_NAMESPACE} -DKUBERNETES_PODNAME=${KUBERNETES_PODNAME}  ${JAVA_OPTS} -Djava.security.egd=file:/dev/./urandom -jar /home/appuser/app/app.jar