package kr.dove.service.order

import core.order.Order
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import reactor.core.publisher.Mono

interface OrderService {

    @PostMapping(
        value = ["/order"],
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun createOrder(@RequestBody order: Order): Mono<Order>

    @GetMapping(
        value = ["/order/{orderId}"],
        produces = ["application/json"]
    )
    fun getOrder(@PathVariable(name = "orderId") orderId: String): Mono<Order>

    @DeleteMapping(
        value = ["/order/{orderId}"]
    )
    fun deleteOrder(@PathVariable(name = "orderId") orderId: String): Mono<Void>
}