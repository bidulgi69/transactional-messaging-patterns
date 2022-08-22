package kr.dove.service.order.persistence

import core.event.EventType
import core.order.Order
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "outbox")
data class OrderOutbox(
    val type: EventType,
    val key: String,
    val order: Order,
) {
    @Id lateinit var id: String
}
