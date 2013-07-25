Project Set Up:

The project can be checked out from here:

https://europeanalabs.eu/svn/europeana/trunk/portal-selenium-tests

The client and the server can be the same machine, but both client and server require:

	1) java
	
	2) maven
	
while the server also requires:

	3) firefox, chrome and IE (version x)
	
If the client and the server are to be run on different machines the server machine should open port 4444 for jetty.
If it is not possible to open port 4444 for jetty, a url with a different port number can be configured in the start.ini file - see this line:

webdriverIP		=	http://127.0.0.1:4444/wd 
	 


Using the Test Project:

Start the server:

	comment out the webdriverIP line in the .ini file to use the default in-built webdriver server


Start a remote server:

	1) build an executable jar with the command:

	mvn clean package -DskipTests

	2) run the jar with the command:

	java -cp target/portal-selenium-tests-0.0.1-SNAPSHOT-jar-with-dependencies.jar eu.europeana.server.TestServer



Run a single test:

	mvn test -Dtest=LightboxTest
	mvn test -Dtest=runNavigationTest

Run all tests:

	mvn test
	
	
Create a test:

	Test classes should extend EuropeanaTest.
	Tests are understood to be tests by the presence of the @Test annotation
	If a test is to be run against real data rather than the controlled mock dataset then its constructor should override the "realData" variable, ie:
		this.realData = true;
	By default test will run against the mock data portal
	
			
Mock Data in the Portal

	To run the portal in mock-data mode, enable this line in portal2-context.xml:
	
		<import resource="internal/portal2-selenium-test.xml" />
	

	
	
	