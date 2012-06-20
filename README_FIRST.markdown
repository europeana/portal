# Europeana Development HOWTO #

Requirement:
Tomcat 6.x or 7.x
Java 6.x JDK
Maven

(This document doesn't describe how to install, and setup these tools.)

Register the europeana.properties file in /etc/profile:

export EUROPEANA_PROPERTIES=<your path to>/europeana.properties.new

(The name of the file can be anything, but I use new to distinguish it from the old portal's europeana.properties file.)

Build and run:

1) cd trunk/portal2

2) edit redeploy.sh 

line 3 is something like this:
TOMCAT_DIR=~/tomcat/apache-tomcat-7.0.27

change the path to point to your Tomcat server

If you don't have fresh corelib build, run the following line:

cd ../corelib/ && mvn clean install -DskipTests && cd ../portal2

This build the corelib, which is a must have for portal2

Finally build and deploy portal2:

./redeploy.sh 

This build and deploy the portal2 war file to Tomcat, and even make start Tomcat

Now can use portal2 which is available at http://localhost:8080/portal2/search.html



