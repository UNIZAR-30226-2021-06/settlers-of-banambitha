#!/bin/bash

IMAGE=catan/postgres
TAG=latest
FULL="$IMAGE:$TAG"

if [ -n  "$(docker images | grep $IMAGE)" ]; then 
    docker rmi "$IMAGE"
fi

docker build -t "$FULL" Dockerfile/
