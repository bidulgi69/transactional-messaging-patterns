package kr.dove.service.ticket.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DebeziumConnectorConfiguration(
    @Value("\${debezium.replication.name}") private val replicationName: String,
    @Value("\${debezium.replication.primary.hosts}") private val hosts: String,
    @Value("\${debezium.replication.primary.port}") private val port: String,
    @Value("\${debezium.replication.primary.username}") private val username: String,
    @Value("\${debezium.replication.primary.password}") private val password: String,
) {

    @Bean
    fun sourceConnector(): io.debezium.config.Configuration {
        return io.debezium.config.Configuration
            .create()
            .with("name", "mongo-outbox-connector")
            .with("connector.class", "io.debezium.connector.mongodb.MongoDbConnector")
            .with("tasks.max", "1")
            .with("mongodb.name", "mongo")
            .with("mongodb.hosts", "$replicationName/$hosts:$port")
            .with("mongodb.user", username)
            .with("mongodb.password", password)
            .with("collection.include.list", "order_db.outbox")
            .with("offset.storage", "org.apache.kafka.connect.storage.FileOffsetBackingStore")
            .with("offset.storage.file.filename", "/data/offsets.dat")
            .with("offset.flush.interval.ms", "60000")
            .with("database.include.name", "order_db")
            .with("database.history", "io.debezium.relational.history.FileDatabaseHistory")
            .build()
    }
}