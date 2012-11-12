#!/bin/sh
##############
# Builds and deploy portal2 and all its dependencies inside Europeana
##############

# read in config
. ./config.sh

# read in arguments
. ./arguments.sh

# building corelib
if [ "$SKIP_CORELIB" = "0" ]; then
  cd ../corelib/ && mvn clean install -DskipTests && cd ../portal2
fi

# building API2
if [ "$SKIP_API" = "0" ]; then
  cd ../api2 && mvn clean install -DskipTests && cd ../portal2
fi

# building portal2 and deploy API2 and portal2
./redeploy.sh $@
