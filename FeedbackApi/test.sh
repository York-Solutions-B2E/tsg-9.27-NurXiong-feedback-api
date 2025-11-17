#!/bin/bash

CONTAINER_NAME=test-container
IMAGE=maven:3.9-eclipse-temurin-21

# Check if container is already running
if [ ! "$(docker ps -q -f name=$CONTAINER_NAME)" ]; then
    echo "Starting container $CONTAINER_NAME..."
    docker run -d --rm \
        --name $CONTAINER_NAME \
        -v "$(pwd)":/workspace \
        -w /workspace \
        $IMAGE \
        bash -c "sleep 1800"  # container lives for 30 mins
else
    echo "Container $CONTAINER_NAME already running."
fi

# Run tests inside the running container
docker exec -it $CONTAINER_NAME mvn test -DtrimStackTrace=false -Dsurefire.redirectTestOutputToFile=false
