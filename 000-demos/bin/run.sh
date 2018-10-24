#!/bin/bash

nohup java -jar ../../discovery-server/target/discovery-server-*.jar > ./discovery.log 2>&1 &

nohup java -jar ../../service-a/target/service-a-*.jar -Dserver.port=8081 -Dserver.instance-name=Instance1 > ./service-a.log 2>&1 &

nohup java -jar ../../client/target/client-*.jar > ./client.log 2>&1 &
