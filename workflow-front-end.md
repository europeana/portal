mirroring files
===============
make sure that you copy any logic in one of these files to the other
this needs to be sorted out so that there is only one file, but for now this needs to be done
* /portal2/src/main/webapp/WEB-INF/jsp/default/_common/variables/variables-javascript.jsp
* /portal2/src/main/webapp/WEB-INF/jsp/default/_common/variables-js.jsp


minifying files
===============
css
---
anytime you add a css file make sure it is account for in the following files :
/portal2/pom-minify.xml
/portal2/src/main/webapp/WEB-INF/jsp/default/_common/html/css/debug-css.jsp
/portal2/src/main/webapp/WEB-INF/jsp/default/_common/html/css/production-css.jsp

js
--
anytime you add a js file make sure it is account for in the following file :
/portal2/pom-minify.xml

building
--------
in order to create minified assets the following file, /portal2/pom-minify.xml, should be run with the following command :
/portal2/pom-minify.xml
mvn -f pom-minify.xml minify:minify package

eclipse
-------
add the min directories as a derived asset so that when you do a shift-cmd-r the minified files do not come up in the result set 
1. navigate to the min directory in the project explorer
2. right-click on the min directory and select properties
3. check the box “Derived” under the “Attributes” section
 