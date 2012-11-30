#!/bin/sh

. ./config.sh

FROM=src/main/webapp
TARGET=$TOMCAT_DIR/webapps/portal

rsync -avz --exclude '.svn' $FROM/WEB-INF/jsp/ $TARGET/WEB-INF/jsp
rsync -avz --exclude '.svn' $FROM/WEB-INF/tags/ $TARGET/WEB-INF/tags
rsync -avz --exclude '.svn' $FROM/branding/ $TARGET/branding
rsync -avz --exclude '.svn' $FROM/themes/ $TARGET/themes
