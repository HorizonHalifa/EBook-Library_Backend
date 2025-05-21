#!/bin/sh
echo "Starting wait-for-db.sh..."
echo "Waiting for PostgreSQL..."
until nc -z db 5432; do
  sleep 1
done
echo "PostgreSQL started!"
exec java -jar app.jar
