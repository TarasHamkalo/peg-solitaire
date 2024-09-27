#!/usr/bin/bash

rebuild_images() {
  echo "rebuilding"

  ./gradlew :resource-server:app:bootJar
  docker build --no-cache \
    --build-arg ARTIFACT_PATH=./resource-server/app/build/libs/resource-server-0.0.1.jar \
    --build-arg APPLICATION_PORT=8080 \
    -t data \
    .

  ./gradlew :game-server:bootJar
  docker build --no-cache \
    --build-arg ARTIFACT_PATH=./game-server/build/libs/game-server-0.0.1.jar \
    --build-arg APPLICATION_PORT=9090 \
    -t game \
    .

  ./gradlew :oauth-server:bootJar
  docker build --no-cache \
    --build-arg ARTIFACT_PATH=./oauth-server/build/libs/oauth-server-0.0.1.jar \
    --build-arg APPLICATION_PORT=7070 \
    -t auth \
    .

  docker image prune -f
}

run_postgres() {
  docker run -d --rm \
    --name users-and-feedback-db \
    --net gamestudio-net \
    -e POSTGRES_USER=taras \
    -e POSTGRES_PASSWORD=taras \
    -e POSTGRES_DB=gamestudio \
    -v pg_data:/var/lib/postgresql/data \
    -p 5432:5432 \
    postgres

}

compose_up() {
  docker network create gamestudio-net &> /dev/null

  run_postgres

  docker run -d --rm \
    --net gamestudio-net \
    --name auth-server \
    -p 7070:7070 \
    auth

  docker run -d --rm \
    --net gamestudio-net \
    --name resource-server \
    -p 8080:8080 \
    data
  
  sleep 5
  docker run -d --rm \
    --net gamestudio-net \
    --name game-server \
    -p 9090:9090 \
    game
}

