package core.values

import com.fasterxml.jackson.annotation.JsonProperty

data class OrderItem(
    @field:JsonProperty("menuItemId") val menuItemId: String,
    @field:JsonProperty("quantity") val quantity: Int,
) {
    constructor(): this(
        menuItemId = "",
        quantity = 0
    )
}