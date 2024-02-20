#!/bin/bash
export MAVEN_HOME=/opt/apache-maven-3.9.6
export PATH=$PATH:$MAVEN_HOME/bin

export JAVA_HOME=/opt/amazon-corretto-17.0.10.8.1-linux-x64
export PATH=$PATH:$JAVA_HOME/bin

export DB_USERNAME=$DB_USERNAME
export DB_PASSWORD=$DB_PASSWORD
export DB_NAME=$DB_NAME

pkill -f quarkus-run.jar
sleep 5

# shellcheck disable=SC2164
cd /home/ubuntu/backend

./mvnw package

nohup java -jar target/quarkus-app/quarkus-run.jar > app.log 2>&1 &

echo "Deployment completed."