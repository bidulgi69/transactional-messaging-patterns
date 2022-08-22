package core.event

import com.fasterxml.jackson.annotation.JsonProperty
import core.order.Order

data class Event(
    @field:JsonProperty("_id") val _id: Id = Id(),
    @field:JsonProperty("type") val type: EventType = EventType.UNRECOGNIZABLE,
    @field:JsonProperty("key") val key: String = "",
    @field:JsonProperty("order") val order: Order = Order(),
    @field:JsonProperty("_class") val classname: String = "kr.dove.service.order.persistence.OrderOutBox",
)

data class Id(
    @field:JsonProperty("\$oid") val oid: String = "",
)

enum class EventType {
    ORDER_CREATED,
    ORDER_APPROVED,
    ORDER_REJECTED,
    TICKET_CREATED,
    TICKET_REJECTED,
    UNRECOGNIZABLE
}