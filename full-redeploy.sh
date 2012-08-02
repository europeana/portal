#!/bin/sh
##############
# Builds and deploy portal2 and all its dependencies inside Europeana
##############

# building corelib
cd ../corelib/ && mvn clean install -DskipTests && cd ../portal2

# building API2
cd ../api2 && mvn clean install -DskipTests && cd ../portal2

# building portal2 and deploy API2 and portal2
./redeploy.sh
