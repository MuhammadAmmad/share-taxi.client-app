#!/bin/bash
clear
echo "starting build"

# Set Project Dir
WORK_DIR="/Users/naama/workspace/ShareTaxiClient/sharetaxi"
APP_COMPAT_DIR="../android-support-v7-appcompat"
cd $WORK_DIR
DIR=$(pwd)
echo "Building $DIR"

# Build Project
# Target list = android list targets
android update project -p . -s -t android-16
ant clean release

# Get the right app file (3 are created)
APK=$(find . -type f -name \*.apk | tail -1)
echo "installing $APK on device"
adb install -r $APK

# Change Permissions back to Read Write Execute for all users.
chmod -R 777 .
cd $APP_COMPAT_DIR 
chmod -R 777 .
echo "permissions restored"
