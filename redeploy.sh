#!/bin/sh

kill `ps aux | grep tomcat | grep -v grep | awk '{print $2}'`
rm -rf ~/tomcat/apache-tomcat-7.0.27/webapps/portal2
rm ~/tomcat/apache-tomcat-7.0.27/webapps/portal2.war
mvn clean install
cp target/portal2-1.0-SNAPSHOT.war ~/tomcat/apache-tomcat-7.0.27/webapps/portal2.war
~/tomcat/apache-tomcat-7.0.27/bin/startup.sh
