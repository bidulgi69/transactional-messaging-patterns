package core.values

import com.fasterxml.jackson.annotation.JsonProperty

data class CreditCard(
    @field:JsonProperty("cvc") val cvc: String = "",
    @field:JsonProperty("number") val number: String = "",
    @field:JsonProperty("yy") val yy: String = "",
    @field:JsonProperty("mm") val mm: String = "",
)