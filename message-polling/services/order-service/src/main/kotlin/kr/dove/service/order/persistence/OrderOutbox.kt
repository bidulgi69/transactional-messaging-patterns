package kr.dove.service.order.persistence

import core.event.EventType
import core.order.Order
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.UUID

@Document(collection = "outbox")
data class OrderOutbox(
    @Id val id: String = UUID.randomUUID().toString(),
    val type: EventType,
    val key: String,
    val order: Order,
)
