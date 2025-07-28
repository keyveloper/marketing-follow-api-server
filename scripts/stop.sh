#!/bin/bash
echo "Stopping existing application..."

PID=$(pgrep -f ".jar")

if [ -n "$PID" ]; then
  echo "Killing process $PID"
  kill -9 $PID
  echo "Application stopped."
else
  echo "No application found running."
fi