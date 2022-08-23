package kr.dove.service.ticket

import core.order.Order
import core.state.State
import core.ticket.Ticket
import kr.dove.service.ticket.persistence.TicketEntity
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.data.mongodb.core.query.Update
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import java.util.UUID

@RestController
class TicketServiceImpl(
    @Qualifier("secondaryMongoTemplate") private val template: ReactiveMongoTemplate,
) : TicketService {

    override fun createTicket(order: Order): Mono<Ticket> {
        return template.save(
            TicketEntity(
                id = UUID.randomUUID().toString(),
                state = State.PENDING,
                orderId = order.id,
                restaurantId = order.restaurantId,
                orderItems = order.orderItems,
            )
        ).flatMap { en ->
            Mono.just(
                en.mapToApi()
            )
        }
    }

    override fun getTicket(orderId: String): Mono<Ticket> {
        return template.findOne(
            Query.query(
                Criteria.where("orderId").`is`(orderId)
            ),
            TicketEntity::class.java
        ).flatMap { en ->
            Mono.just(
                en.mapToApi()
            )
        }
    }

    override fun deleteTicket(order: Order): Mono<Ticket> {
        return template.findAndModify(
            Query.query(
                Criteria.where("orderId").`is`(order.id)
            ),
            Update().apply {
                set("state", State.REJECTED)
            },
            TicketEntity::class.java
        ).flatMap { en ->
            Mono.just(
                en.mapToApi()
            )
        }
    }
}