#!/bin/sh

. ./config.sh

rsync -avz --exclude '.svn' src/main/webapp/WEB-INF/jsp/ $TOMCAT_DIR/webapps/portal/WEB-INF/jsp
rsync -avz --exclude '.svn' src/main/webapp/WEB-INF/tags/ $TOMCAT_DIR/webapps/portal/WEB-INF/tags
rsync -avz --exclude '.svn' src/main/webapp/branding/ $TOMCAT_DIR/webapps/portal/branding
rsync -avz --exclude '.svn' src/main/webapp/themes/ $TOMCAT_DIR/webapps/portal/themes
