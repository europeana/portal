#!/bin/sh

. ./config.sh

rsync -avz --exclude '.svn' src/main/webapp/WEB-INF/jsp/ $TOMCAT_DIR/webapps/portal2/WEB-INF/jsp
rsync -avz --exclude '.svn' src/main/webapp/WEB-INF/tags/ $TOMCAT_DIR/webapps/portal2/WEB-INF/tags
rsync -avz --exclude '.svn' src/main/webapp/branding/ $TOMCAT_DIR/webapps/portal2/branding
