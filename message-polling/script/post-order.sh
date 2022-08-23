HOST=localhost
ORDER_PORT=8080
TICKET_PORT=8081
orderId=1
order="'{
       	\"_id\": 1,
       	\"customerId\": 1,
       	\"restaurantId\": 1,
       	\"payment\": {
       		 \"cvc\": \"343\",
       		 \"number\": \"0123456789\",
       		 \"yy\": \"24\",
       		 \"mm\": \"06\"
       	},
       	\"orderItems\": [
       		{
       			\"menuItemId\": \"1\",
       			\"quantity\": 2
       		},
       		{
       			\"menuItemId\": \"2\",
       			\"quantity\": 1
       		}
       	]
       }'"

function assertCurl() {

  local expectedHttpStatus=$1
  local commands="$2 -w \"%{http_code}\""
  local result
  result=$(eval "$commands")
  local httpCode="${result:(-3)}"

  if [ "$httpCode" = "$expectedHttpStatus" ]
  then
    echo "Test OK (HTTP Code: $httpCode)"
  else
    echo "Test FAILED, EXPECTED HTTP Code: $expectedHttpStatus, GOT: $httpCode"
    exit 1
  fi
}

# check order entity is created.
assertCurl 200 "curl -X POST -H 'Content-Type: application/json' '$HOST:$ORDER_PORT/order' --data $order -s"
# check ticket entity is created.
assertCurl 200 "curl -X GET '$HOST:$TICKET_PORT/ticket/$orderId' -s"