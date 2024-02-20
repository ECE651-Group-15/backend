#!/bin/bash
pwd
source ~/.bashrc

pkill -f quarkus-run.jar
sleep 5

# shellcheck disable=SC2164
cd /home/ubuntu/backend

./mvnw package

nohup java -jar target/quarkus-app/quarkus-run.jar > app.log 2>&1 &

echo "Deployment completed."