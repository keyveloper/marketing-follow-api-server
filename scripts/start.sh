#!/bin/bash
echo "Starting application..."

cd /home/ec2-user/app
JAR_FILE=$(ls *.jar | head -n 1)

nohup java -jar $JAR_FILE > app.log 2>&1 &

echo "Application started."