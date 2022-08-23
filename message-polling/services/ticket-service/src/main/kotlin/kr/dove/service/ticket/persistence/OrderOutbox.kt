package kr.dove.service.ticket.persistence

import core.event.EventType
import core.order.Order
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "outbox")
data class OrderOutbox(
    @Id var id: String,
    val type: EventType,
    val key: String,
    val order: Order,
)