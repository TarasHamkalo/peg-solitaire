#!/usr/bin/bash

docker run -d --rm \
  --name users-and-feedback-db \
  -e POSTGRES_USER=taras \
  -e POSTGRES_PASSWORD=taras \
  postgres 

