#!/bin/sh

TOMCAT_DIR=~/tomcat/apache-tomcat-7.0.27

kill `ps aux | grep tomcat | grep -v grep | awk '{print $2}'`
rm -rf $TOMCAT_DIR/webapps/portal2
rm $TOMCAT_DIR/webapps/portal2.war
mvn clean install
cp target/portal2-1.0-SNAPSHOT.war $TOMCAT_DIR/webapps/portal2.war
$TOMCAT_DIR/bin/startup.sh
