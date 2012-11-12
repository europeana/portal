#!/bin/sh
##################################
# Deployment script for Europeana portal2 and api2
#
# Usage:
#    Without argument it makes use europeana.portal2.properties
#    $ ./redeploy.sh
#    With argument "local" it makes use europeana.portal2.local.properties, which points to local Mongo and Solr
#    $ ./redeploy.sh local
#    With argument "skipPortal" it skips portal building
#    $ ./redeploy.sh skipPortal
#    With argument "debug" it starts portal in debug mode
#    $ ./redeploy.sh debug
#
# The configuration settings are in config.sh (rename config.example.sh to config.sh and make your changes).
##################################

# read in the configuration
. ./config.sh

# read in arguments
. ./arguments.sh

# use local DB settings property file if user call script with "local" parameter
if [ "$LOCAL" = "1" ]; then
  PROP_FILE=europeana.portal2.local.properties
else
  PROP_FILE=europeana.portal2.properties
fi

# creating a variable, which we can to Maven and Tomcat
EUROPEANA_OPTS=-DEUROPEANA_PROPERTIES=$PROPERTY_FILE_DIR/$PROP_FILE

# stop tomcat
kill `ps aux | grep tomcat | grep -v grep | awk '{print $2}'`
sleep 3

# remove dirs
rm -rf $TOMCAT_DIR/webapps/portal
rm -rf $TOMCAT_DIR/webapps/api
rm -rf $TOMCAT_DIR/webapps/api-demo

# remove wars
rm $TOMCAT_DIR/webapps/portal.war
rm $TOMCAT_DIR/webapps/api.war
rm $TOMCAT_DIR/webapps/api-demo.war

if [ "$SKIP_PORTAL" = "0" ]; then
  # build
  MAVEN_OPTS="$MAVEN_OPTS $EUROPEANA_OPTS"
  echo $MAVEN_OPTS
  mvn clean install -DskipTests
fi

# copy to Tomcat
cp target/portal.war $TOMCAT_DIR/webapps/portal.war
cp ../api2/api2-demo/target/api-demo.war $TOMCAT_DIR/webapps/api-demo.war
cp ../api2/api2-war/target/api.war $TOMCAT_DIR/webapps/api.war

# prepare Tomcat variables
CATALINA_OPTS="$CATALINA_OPTS -Xmx4096m -XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled -XX:MaxPermSize=1024M"
if [ "$DEBUG" = "1" ]; then
  CATALINA_OPTS="$CATALINA_OPTS -Xdebug -Xrunjdwp:transport=dt_socket,address=8000,server=y,suspend=n"
fi

# run Tomcat
export CATALINA_OPTS="$CATALINA_OPTS $EUROPEANA_OPTS" && echo $CATALINA_OPTS && $TOMCAT_DIR/bin/startup.sh
