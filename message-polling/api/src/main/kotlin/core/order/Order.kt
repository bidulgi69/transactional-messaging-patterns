package core.order

import com.fasterxml.jackson.annotation.JsonProperty
import core.state.State
import core.values.CreditCard
import core.values.OrderItem

data class Order(
    @field:JsonProperty("_id") var id: String = "",
    @field:JsonProperty("state") var state: State = State.PENDING,
    @field:JsonProperty("customerId") val customerId: String = "",
    @field:JsonProperty("restaurantId") val restaurantId: String = "",
    @field:JsonProperty("payment") val payment: CreditCard = CreditCard(),
    @field:JsonProperty("orderItems") val orderItems: List<OrderItem> = listOf(),
    @field:JsonProperty("ticketId") var ticketId: String? = null,
    @field:JsonProperty("errorRate") var errorRate: Float = 0f,
)
