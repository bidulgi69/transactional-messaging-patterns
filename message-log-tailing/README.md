# Log tailing pattern
<span style="font-weight: lighter">
The log tailing pattern is a method of tailing the transaction log of the database where events are stored.<br>
When applications save data in a database, transaction log entries are also saved.<br>
The transaction log miner reads the transaction log entry above and sends it to kafka as a message.<br>
Usually, log tailing pattern is recommended to use with RDBMS which supports ACID.<br><br>
This project uses <a href="https://github.com/debezium/debezium">Debezium</a> as the transaction log miner.<br><br>
The project simply uses MongoDB as a database,<br> but applying a log tailing pattern to NoSQL that does not support transactions is an <strong>issue</strong> to consider.<br>
</span>
<br>

## Structure
<img src="https://user-images.githubusercontent.com/17774927/186011929-e277eee5-c6ca-4f90-8580-412a806b73a5.png" alt="structure">
<br>

## Event flow
1. Order service receives a http post request (/order) and then creates an order entity and an event entity 
2. Saving an order entity to orders table and an event entity to outbox table. These two insertion operations are grouped together in one transaction.
3. Debezium detects the transaction log and sends a message to kafka (default topic name format: `server.db.collection`).
4. Ticket service consumes a message and saves a ticket entity to its database.
<br>

## Usage
1. Build applications and docker images

        make compile
2. Fire up mongo container first

        make setup
3. Launch applications and Wait until the servers to run (<a href="https://github.com/stedolan/jq">jq</a> required)

        make run
4. Testing post order api (order, ticket data will be saved in a database.)

        make test

5. Cleanup

        make clean
