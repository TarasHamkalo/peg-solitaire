#!/usr/bin/bash

docker run -d --rm \
  --name users-and-feedback-db \
  -e POSTGRES_USER=taras \
  -e POSTGRES_PASSWORD=taras \
  -e POSTGRES_DB=gamestudio \
  -v pg_data:/var/lib/postgresql/data \
  -p 5432:5432 \
  postgres 

