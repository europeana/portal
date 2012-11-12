#!/bin/sh

LOCAL=0
SKIP_CORELIB=0
SKIP_API=0
SKIP_PORTAL=0
DEBUG=0

for i
do
  if [ "$i" = "-local" ]; then
    LOCAL=1
  fi;
  if [ "$i" = "-skipCorelib" ]; then
    SKIP_CORELIB=1
  fi;
  if [ "$i" = "-skipApi" ]; then
    SKIP_API=1
  fi;
  if [ "$i" = "-skipPortal" ]; then
    SKIP_PORTAL=1
  fi;
  if [ "$i" = "-debug" ]; then
    DEBUG=1
  fi;
done

echo local=$LOCAL, skipApi=$SKIP_API, skipPortal=$SKIP_PORTAL, skipCorelib=$SKIP_CORELIB, debug==$DEBUG
