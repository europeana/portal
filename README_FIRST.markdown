Europeana Portal2 Setup
=======================

Requirements
------------
* Java Webserver : Tomcat 6.x, Tomcat 7.x, or Jetty (included in eclipse)
* Java 6.x JDK
* Maven 2.2.x

*note* This document doesn't describe how to install, and setup all of these tools.



Recommendations
---------------
use [Eclipse IDE for Java EE Developers]( http://www.eclipse.org/downloads/ ) as your IDE



Odd things you need to know, to get a portal running
----------------------------------------------------
* You need to setup postgres, details is in the
trunk/README_FIRST.markdown



Checkout the projects
---------------------
1. create a working directory for all of the projects, e.g., /Users/your_user_directory/workspace/europeana-portal
2. make sure you’re in that directory.
3. checkout the portal2 project http://europeanalabs.eu/svn/europeana/trunk/portal2/
4. checkout the corelib projects http://europeanalabs.eu/svn/europeana/trunk/corelib/
5. checkout the staticpages https://europeanalabs.eu/svn/europeana/trunk/staticpages2/



Install & Setup Eclipse
-----------------------
### Download and Install Eclipse
[Eclipse IDE for Java EE Developers]( http://www.eclipse.org/downloads/ )


### Install Maven Support for Eclipse via the Maven Plugin
The maven plugin is at http://eclipse.org/m2e/download/
Latest m2e release (recommended) is http://download.eclipse.org/technology/m2e/releases

1. Select the menu “Help : Install New Software”
2. Work with: http://download.eclipse.org/technology/m2e/releases
3. Click on “Add …”
4. Click on “OK”
5. Check the box next to Maven Integration for Eclipse
6. Follow the rest of the process to install the plugin.


### Install Subclipse
To work with svn within eclipse it’s necessary to install a plugin. of the two that exist, subclipse seems to be the better.

1. figure out which version of svn is installed, e.g. svn --version
2. find the appropriate eclipse update site for your version of svn, http://subclipse.tigris.org/servlets/ProjectProcess?pageID=p4wYuA
	
	Links for 1.8.x Release (for Subversion 1.7.x):
	Eclipse update site URL: http://subclipse.tigris.org/update_1.8.x
	
	Links for 1.6.x Release (for Subversion 1.6.x):
	Eclipse update site URL: http://subclipse.tigris.org/update_1.6.x

3. Help > Install New Software
4. Work with: update_site_from_step2_above
5. Click on “Add …”
6. Click on “OK”
7. Check the box next to subclipse plugins you want to install
8. Follow the rest of the process to install the plugin.


### Import the project(s)
Depending on the Maven Projects you need to work on, import them by choosing:

1. File > Import
2. In the wizard select Maven > Existing Maven Projects
3. Browse to the checkout directory for the Maven Project(s)
4. Select Open
5. After some evaluating eclipse will show the Maven Projects that exist in the directory and a checkbox next to the ones it will import
6. Modify the checkboxes if you don't want to import all of the ones listed.
7. Click Finish



Install Tomcat or Setup Jetty in Eclipse
----------------------------------------
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

An alternative to Tomcat, running portal2 on the Jetty server in eclipse, allows developers to quickly see the results of their updates to css, js, and jsps without redeployment. You’ll need a setup a configuration file in eclipse.

1. from eclipse’s “project explorer”, right click on the “portal2” project.
2. select “Run As : Run Configurations …”
3. double-click on “Java Application” to create a new Java Application configuration.
4. [Tab Main]
	Name: Portal 2 Jetty Starter (or whatever)
	Project: portal2
	Main Class: eu.europeana.Portal2Starter
5. [Tab Arguments]
	VM Arguments: -Xms1024m -Xmx2048m
	Working Directory:  ${workspace_loc:portal2}
6. [Tab Classpath]
	make sure the following projects are under “User Entries” :
		portal2
		Maven Dependencies
		corelib-solr-definitions
		corelib-db
		corelib-definitions
		corelib-solr
		corelib-solr-dereference
		corelib-solr-tools
		corelib-utils
		corelib-web
	if not, click “Add Projects …”
	click “Select All” or select the appropriate projects as needed
	checked - “Add exported entriesof selected projects.”
	checked - “Add required projects of selected projects.”
	click “OK”
7. [Tab Environment]
	click on “New …”
	name = EUROPEANA_PROPERTIES
	value = europeana.properties
	checked - “Append to native environment”
8. click “Apply”
9. if all other setup has been completed, mentioned in this readme - click “Run”
10. if all is well you should be able to view portal2 on http://localhost:8081/portal2/



Configuration
-------------
### europeana.properties
1. you can register the europeana.properties file in Tomcat as mentioned above in step 5 of the Tomcat section or in /etc/profile:
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

1. cd [path to project]/corelib/
2. run mvn clean install -DskipTests

### portal2 build
1. cd [path to project]/portal2/
2. run mvn clean install -DskipTests
3. if running under Tomcat, you will need to copy the portal2 war to Tomcat. you can use ./redeploy.sh, instead of following the above to build the project, deploy the portal2 war file to Tomcat, and start Tomcat

### browse the portal
Now can use portal2 which is available at http://localhost:8080/portal2/search.html?query=*:*



Indexing locally
----------------
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



additional assets
-----------------
### static pages
you’ll need a directory for the static pages. checkout the following to a local directory in your project and make sure the europeana.properties file has the corresponding reference, static.page.path = full_os_path_to_staticpages2

svn co https://europeanalabs.eu/svn/europeana/trunk/staticpages2/

### message keys
they are part of the portal2 project and reside in portal2/src/main/resources/message_keys/. the europeana.properties entry should look something like the following :

message.resource=file:portal2/src/main/resources/message_keys/messages




markdown syntax
---------------
Tip: you can play with the markdown syntax here: <http://joncom.be/experiments/markdown-editor/edit/>

and here is a link to more on its syntax
<http://daringfireball.net/projects/markdown/syntax>