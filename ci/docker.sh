#!/bin/sh

export HOME=`pwd`

if [ -z "${CI_COMMIT_SHORT_SHA}" ]; then
  export CI_COMMIT_SHORT_SHA=`git rev-parse --short HEAD`
fi

export IMAGE=ghcr.io/ayshe/letterboxd-dev-test
export LATEST=$IMAGE:latest
export TAG=$IMAGE:$CI_COMMIT_SHORT_SHA

echo Building $TAG
docker build -t $LATEST -t $TAG .
