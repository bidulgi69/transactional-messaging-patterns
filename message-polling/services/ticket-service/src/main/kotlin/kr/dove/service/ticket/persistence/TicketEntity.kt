package kr.dove.service.ticket.persistence

import core.state.State
import core.ticket.Ticket
import core.values.OrderItem
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.Version
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "tickets")
data class TicketEntity(
    @Id val id: String,
    var state: State = State.PENDING,
    val orderId: String,
    val restaurantId: String,
    var acceptTime: LocalDateTime = LocalDateTime.now(),
    var readyTime: LocalDateTime? = null,
    var preparingTime: Long? = null,
    var pickedUpTime: LocalDateTime? = null,
    var orderItems: List<OrderItem>,
    @Version val version: Int = 0,
) {

    fun mapToApi(): Ticket = Ticket(
        id,
        state,
        orderId,
        restaurantId,
        acceptTime,
        readyTime,
        preparingTime,
        pickedUpTime,
        orderItems
    )
}