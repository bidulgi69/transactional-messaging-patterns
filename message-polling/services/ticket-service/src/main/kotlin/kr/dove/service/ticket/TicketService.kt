package kr.dove.service.ticket

import core.order.Order
import core.ticket.Ticket
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import reactor.core.publisher.Mono

interface TicketService {

    fun createTicket(order: Order): Mono<Ticket>

    @GetMapping(
        value = ["/ticket/{orderId}"],
        produces = ["application/json"]
    )
    fun getTicket(@PathVariable(name = "orderId") orderId: String): Mono<Ticket>

    fun deleteTicket(order: Order): Mono<Ticket>
}