# Polling pattern

The polling pattern is a method of polling records periodically from the database where events are stored.<br>
Polling is performed by executing queries such as `SELECT * FROM OUTBOX;`<br>
Messages read are sent to each topic in the message broker, and then deletes messages from the outbox table.<br>
Polling can be costly depending on how often the application polls and its performance depends on the query ability of the database where messages are stored.<br>

In this project, we will treat messages directly from polling service without using the message broker.<br>
## Structure
<img src="https://user-images.githubusercontent.com/17774927/186240212-0a690be4-4505-4935-b2a4-44fa90a31480.png" alt="structure">
<br>

## Event flow
1. Order service receives a http post request (/order) and then creates an order entity and an event entity
2. Saving an order entity to orders table and an event entity to outbox table. These two insertion operations are grouped together in one transaction.
3. Ticket service polls events from outbox table every three seconds and polls events that occur previous steps.
4. Ticket service takes events and makes operations to its database.
   <br>

## Usage
1. Build applications and docker images

        make compile
2. Launch applications and Wait until the servers to run (<a href="https://github.com/stedolan/jq">jq</a> required)

        make run
3. Testing post order api (order, ticket data will be saved in a database.)

        make test
4. Cleanup

        make clean