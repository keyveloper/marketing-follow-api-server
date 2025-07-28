echo "Validating service..."

sleep 5

STATUS=$(curl -s http://localhost:8080/actuator/health | grep '"status":"UP"')

if [ -n "$STATUS" ]; then
  echo "Validation succeeded."
  exit 0
else
  echo "Validation failed."
  exit 1
fi