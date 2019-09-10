#!/bin/sh -e
#
# create and run project build container
#
CMDPATH=$(cd $(dirname $0) && pwd)
BASEDIR=${CMDPATH%/*}
PROJECT=mbs

BBCACHE=$BASEDIR/bbcache

echo "==> create docker image"

cd $CMDPATH
docker build --build-arg UID=$(id -u) --build-arg GID=$(id -g) --tag $PROJECT .

echo "==> start $PROJECT build container"

mkdir -p $BBCACHE
docker run -it --rm -v $BASEDIR:/base -w /base $PROJECT $@
