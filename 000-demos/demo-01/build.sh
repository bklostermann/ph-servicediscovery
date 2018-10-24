#!/bin/bash

cd ../../discovery-server
mvn package

cd ../service-a
mvn package

cd ../client
mvn package
