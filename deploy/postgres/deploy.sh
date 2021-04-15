#!/bin/bash

IMAGE="catan/postgres"
TAG="latest"
FULL="$IMAGE:$TAG"
NAME="catan_database"
PASS="susan_catan"
PORT=5432

if [ -n  "$(docker container ls -a | grep $NAME)" ]; then 
    docker rm "$NAME"
fi


docker run -d --name $NAME -e POSTGRES_PASSWORD=$PASS  -p $PORT:$PORT -d $FULL

