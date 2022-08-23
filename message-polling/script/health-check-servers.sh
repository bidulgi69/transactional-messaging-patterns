confirmed=0

function wait() {
  echo "Wait for: $1... "
  while [[ "$retry" -lt 10 ]]
  do
      status=$(curl -XGET "$1" -s | jq '.status')
      if [[ "$status" == "\"UP\"" ]]
      then
        echo "Server $2 is running."
        ((confirmed++))
        break
      fi
      sleep 2
      ((retry++))
  done
}

wait localhost:8080/actuator/health order-service
wait localhost:8081/actuator/health ticket-service

if [[ "$confirmed" == 2 ]]
then
  echo "All services are running now."
else
  echo "Some services does not running..."
fi