# transactional-messaging-patterns
Simple projects implementing two transactional messaging patterns in microservice.
- Polling pattern
- Log tailing pattern
<br>

## What is transactional messaging?
Exchange of events between microservices usually occurs when notifying DB updates from local transactions are delivered to other services. 
When sending events, problems arise if DB updates and event publication are not grouped into one transaction.<br>
For example, DB updates are successful, but event publication fails, or vice versa.<br>
If the above two operations are not performed atomically, the service will be very unstable.<br>

The easiest solution is to use the DB table as a temporary message queue, which is the <strong>transactional outbox pattern</strong>.<br>
Create a DB table called OUTBOX in the service that sends messages and insert the message into the OUTBOX table as part of the DB transaction. 
Because it is a local transaction, the operation is atomic.
