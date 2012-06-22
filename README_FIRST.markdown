Europeana Portal2 Setup
=======================
Requirements
------------
* Tomcat 6.x or 7.x
* Java 6.x JDK
* Maven 3.0.x
* (This document doesn't describe how to install, and setup all of these tools.)


Installation
------------
### Tomcat
#### Mac OSX
1. download the Core tar.gz distribution to your Downloads folder, e.g., http://tomcat.apache.org/download-70.cgi
2. untar the Core distribution in your Downloads folder
3. move the distribution folder to your Applications folder
4. add a symbolic link to the distribution folder, e.g. ln -s /Applications/apache-tomcat-7.0.28/ ./tomcat
5. edit /Applications/tomcat/bin/startup.sh
   add the following lines after the EXECUTABLE=catalina.sh line
   export JAVA_HOME=/System/Library/Frameworks/JavaVM.framework/Versions/1.6/Home
   export EUROPEANA_PROPERTIES=~/workspace/europeana-portal/portal2/europeana.properties
6. start tomcat in the terminal with the command sh /Applications/tomcat/bin/startup.sh
7. browse to http://localhost:8080/ to verify that tomcat is running
8. you can stop tomcat with the terminal command sh /Applications/tomcat/bin/shutdown.sh


### maven
the .m2 repository may not be able to source the following repositories. both of these are available as zip files in http://dropbox.com/home/EuropeanaShared/tmp/ if that folder has been shared with your dropbox account:
1. .m2/commons-logging/commons-logging-api/99.0-does-not-exist/
2. .m2/woodstock/wstx-asl/3.2.7/



Configuration
-------------
### europeana.properties
1. you can register the europeana.properties file in tomcat as mentioned above in step 5 or in /etc/profile:
   export EUROPEANA_PROPERTIES=[path to project]/europeana.properties.new
   (The name of the file can be anything, but I use new to distinguish it from the old portal's europeana.properties file.)
2. make sure the static.page.path = is set to your static directroy
   nb: make sure you use an absolute path here, e.g., don't use ~/
   e.g. static.page.path = /Users/[your username]/workspace/europeana-portal/portal2/src/test/staticpages/

### redeploy.sh
1. adjust the path to tomcat in the redeploy.sh file so that the path points to your Tomcat server
   edit &lt;path to project&gt;/trunk/portal2/redeploy.sh
   e.g., TOMCAT_DIR=~/tomcat/apache-tomcat-7.0.27

### corelib build
If you don't have fresh corelib build, run the following line, which builds the corelib; a requirement in order for the portal2 to work properly:

1. cd [path to project]/corelib/corelib-solr-definitions/
2. run mvn clean install -DskipTests
3. cd [path to project]/corelib/
4. run mvn clean install -DskipTests

### portal2 build
1. cd [path to project]/portal2/
2. ./redeploy.sh - this will build the project, deploy the portal2 war file to Tomcat, and start Tomcat

### browse the portal
Now can use portal2 which is available at http://localhost:8080/portal2/search.html?query=*:*