#!/bin/sh

. ./config.sh

MAVEN_OPTS="$MAVEN_OPTS -DEUROPEANA_PROPERTIES=$PROPERTY_FILE_DIR/europeana.portal2.properties"
mvn clean install
