Europeana Portal2 Setup
=======================
Requirements
------------
* Tomcat 6.x or 7.x
* Java 6.x JDK
* Maven 3.0.x
* (This document doesn't describe how to install, and setup all of these tools.)


==================
Odd things you need to know, to get a portal running
----------------------------------------------------
You need to setup postgres, details is in the
trunk/README_FIRST.markdown

When doing queries, you must add  &theme=vanilla

==================

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
   export CATALINA_OPTS="-Xmx4096m -XX:+CMSClassUnloadingEnabled -XX:+CMSPermGenSweepingEnabled -XX:MaxPermSize=1024M"
   
6. start tomcat in the terminal with the command sh /Applications/tomcat/bin/startup.sh
7. browse to http://localhost:8080/ to verify that tomcat is running
8. you can stop tomcat with the terminal command sh /Applications/tomcat/bin/shutdown.sh


### Jetty
#### Mac OSX

An alternative to Tomcat, running portal2 on the Jetty server allows developers to quickly see the results of their updates to css, js, and jsps without redeployment.
You'll need a run configuration, so select "Run Configurations..." / "Java Application" / "New", and fill in the fields as follows:

[Tab Main]
  Name: Portal 2 Jetty Starter (or whatever)
  Project: portal 2
  Main Class: eu.europeana.Portal2Starter
  Working Directory:  ${workspace_loc:portal2}

[Tab Arguments]
  VM Arguments: -Xms1024m -Xmx2048m

[Tab Classpath]
  Add all of the corelib projects, if not already present.

[Tab Environment]
  New ... > name = EUROPEANA_PROPERTIES, value = europeana.properties
  append to native environment


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
   edit [path to project]/trunk/portal2/redeploy.sh
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


Eclipse Setup
-------------
### Install Maven Support for Eclipse via the Maven Plugin
the maven plugin is at http://eclipse.org/m2e/download/
Latest m2e release (recommended) is http://download.eclipse.org/technology/m2e/releases
To install it

1. Help > Install New Software
2. Work with: http://download.eclipse.org/technology/m2e/releases
3. Click on Add...
4. Check the box next to Maven Integration for Eclipse
5. Follow the rest of the process to install the plugin.

### Import the project(s)
Depending on the Maven Projects you need to work on, import them by choosing:
1. File > Import
2. In the wizard select Maven > Existing Maven Projects
3. Browse to the checkout directory for the Maven Project(s)
4. Select Open
5. After some evaluating eclipse will show the Maven Projects that exist in the directory and a checkbox next to the ones it will import
6. Modify the checkboxes if you don't want to import all of the ones listed.
7. Click Finish

### Install Subclipse
find the appropriate eclipse update site for your version of svn, http://subclipse.tigris.org/servlets/ProjectProcess?pageID=p4wYuA, and install it similarly to the maven plugin



Indexing locally
---------------
Corelib contains a utility called ContentLoader for creating a local index. You have to make some changes before running it.

### Install latest Mongo and Solr 3.5.0
  Just follow the instructions of those tools.

### Start Mongo

### Start Solr
1. have to use the embedded Solr configuration, which is avalaible at 
   corelib/corelib-solr/src/test/java/resources/solr. It contains the configuration files, the extra 
   libraries, and the placeholder for the Solr index. Do not confuse it with the Solr server's directory.
2. Copy the solr directory to a convenient place (I use ~/Development/solr)
3. Start Solr with the above established directory:

   java -Dsolr.solr.home=[your absolute path to]/Development/solr -jar start.jar

4. Note if you want to reindex it, first stop Solr and delete the index directory (~/Development/solr/data/index) 
   and start Solr again.

### Use embedded Solr
1. open portal2/src/main/resources/internal/portal2-develpment.xml
2. comment out this lines:
  <bean name="corelib_solr_searchService" class="eu.europeana.corelib.solr.service.impl.SearchServiceImpl"
    p:solrServer-ref="corelib_solr_solrServer"
  />
  (use <!-- and --> strings before and after)
3. insert these lines:
  <bean name="corelib_solr_searchService" class="eu.europeana.corelib.solr.service.impl.SearchServiceImpl"
    p:solrServer-ref="corelib_solr_solrEmbedded"
  />

### Modify the europeana.properties file
1.  Comment out the existing solr.url line, and add this line:
    solr.url = http://localhost:8983/solr
2.  Comment out existing mongodb.host line and add this line:
    mongodb.host = 127.0.0.1

Warning: if you skip this step the server index will be overwriten.

### ContentLoader.java
The full qualified path of the class is eu.europeana.corelib.solr.ContentLoader.
It's location is corelib/corelib-solr/src/test/java/eu/europeana/corelib/solr/ContentLoader.java

There are two record sets available for content loader. One contains 200 records, the other contains 
30 000+ records. It is advisable to start with the first one. To change between the two you have to 
modify content loader class, but the change is minimal:

you have to find this line:
  private static String COLLECTION = "corelib/corelib-solr/src/test/resources/records-test.zip";
  
If you want to index the 200 records, leave it as it is:
  private static String COLLECTION = "corelib/corelib-solr/src/test/resources/records-test.zip";

If you want to index the full record set, just remove the "-test" from the file name, such like this:
  private static String COLLECTION = "corelib/corelib-solr/src/test/resources/records-test.zip";

#### Run inside Eclipse:
1. Run > Run configuration ... > New launch configuration (icon)
2. Fill the form: Name: ContentLoader, Project: corelib-solr, Class: ContentLoader (you can search it)
3. Arguments tab: Working directory: Other: ${workspace_loc}/trunk
4. Environment tab: create new variable: key: EUROPEANA_PROPERTIES, value: <your europeana.properties file>

### Properties
* *portal.responsive.widths*: the comma separated values of image widths in index page  
  example: 200,300,700,200
* *portal.responsive.labels*: the comma separated values of image width identifiers in index page  
  example: _1,_2,_3,_4


Tip: you can play with the markdown syntax here: http://joncom.be/experiments/markdown-editor/edit/