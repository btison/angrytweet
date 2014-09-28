#! /bin/bash

# Build script to build the Docker images for the AngryTweet application.

SCRIPT_DIR=$(cd -P -- "$(dirname -- "$0")" && pwd -P)

TAG_REPO=angrytweet

TAG_VERSION=latest

#
# Usage
#
function usage
{
    echo "usage: $0 [--repo TAG_REPO] [--version TAG_VERSION]"
}

#
# Parse command lines
#
while [ "$1" != "" ]; do
    case $1 in
        --repo )
            TAG_REPO="$2"
            shift
            ;;
        --version )    
            TAG_VERSION="$2"
            shift
            ;;
        * )
            usage
            exit 1
    esac
    shift 
done

echo "###########################################"
echo "Build Angrytweet MySQL container"
echo "###########################################"
docker build -t $TAG_REPO/mysql:$TAG_VERSION $SCRIPT_DIR/mysql

echo "###########################################"
echo "Build Angrytweet FSW container"
echo "###########################################"
docker build -t $TAG_REPO/fsw:$TAG_VERSION $SCRIPT_DIR/fsw

echo "###########################################"
echo "Build Angrytweet RTGOV container"
echo "###########################################"
docker build -t $TAG_REPO/rtgov:$TAG_VERSION $SCRIPT_DIR/rtgov

echo "###########################################"
echo "Build Angrytweet BAM container"
echo "###########################################"
docker build -t $TAG_REPO/bam:$TAG_VERSION $SCRIPT_DIR/bam
