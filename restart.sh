#!/bin/sh
##################################
# Deployment script for Europeana portal2 and api2
#
# Usage:
#    Without argument it makes use europeana.portal2.properties
#    $ ./redeploy.sh
#    With argument "local" it makes use europeana.portal2.local.properties, which points to local Mongo and Solr
#    $ ./redeploy.sh local
#
# The configuration settings are in config.sh (rename config.example.sh to config.sh and make your changes).
##################################

# read in the configuration
. ./config.sh

# use local DB settings property file if user call script with "local" parameter
if [ "$1" = "local" ]; then
  PROP_FILE=europeana.portal2.local.properties
else
  PROP_FILE=europeana.portal2.properties
fi

# creating a variable, which we can to Maven and Tomcat
EUROPEANA_OPTS=-DEUROPEANA_PROPERTIES=$PROPERTY_FILE_DIR/$PROP_FILE

# stop tomcat
kill `ps aux | grep tomcat | grep -v grep | awk '{print $2}'`
sleep 5

# prepare Tomcat variables
CATALINA_OPTS="$CATALINA_OPTS -Xmx4096m -XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled -XX:MaxPermSize=1024M"

# run Tomcat
export CATALINA_OPTS="$CATALINA_OPTS $EUROPEANA_OPTS" && echo $CATALINA_OPTS && $TOMCAT_DIR/bin/startup.sh
