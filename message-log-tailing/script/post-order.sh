HOST=localhost
PORT=8080
orderId=1
order="'{
  \"_id\": \"$orderId\",
  \"customerId\": 1,
  \"restaurantId\": 1,
  \"payment\": {
    \"cvc\": \"343\",
    \"number\": \"0123456789\",
    \"yy\": \"24\",
    \"mm\": \"06\"
  },
  \"orderItems\":[
    {\"menuItemId\": \"1\", \"quantity\": 2},
    {\"menuItemId\": \"2\", \"quantity\": 1}
  ]
}'"

function assertCurl() {

  local expectedHttpStatus=$1
  local commands="$2 -w \"%{http_code}\""
  local result
  result=$(eval "$commands")
  local httpCode="${result:(-3)}"
  RESPONSE='' && (( ${#result} > 3 )) && RESPONSE="${result%???}"

  if [ "$httpCode" = "$expectedHttpStatus" ]
  then
    echo "Test OK (HTTP Code: $httpCode, $RESPONSE)"
  else
    echo  "Test FAILED, EXPECTED HTTP Code: $expectedHttpStatus, GOT: $httpCode"
    exit 1
  fi
}

# check order entity is created.
assertCurl 200 "curl -X POST -H 'Content-Type: application/json' $HOST:$PORT/order --data $order -s"
# check ticket entity is created.
ticket=$(eval "docker-compose exec mongo /bin/bash -c \"mongosh -u root -p root --quiet < /script/verify_ticket.js\" | grep 'orderId'")
echo $ticket
if [[ "$ticket" != "" ]]
then
  echo "Test OK, Ticket is created"
else
  echo "Test FAILED, Ticket is not created."
  exit 1
fi