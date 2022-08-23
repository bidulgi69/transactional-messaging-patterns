package kr.dove.service.ticket

import core.event.EventType
import kr.dove.service.ticket.persistence.OrderOutbox
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class MessageRelay(
    private val primaryMongoTemplate: ReactiveMongoTemplate,
    private val ticketService: TicketService,
) {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    //  Reads events from the database every three seconds.
    @Scheduled(fixedDelay = 3000)
    fun read() {
        logger.info("Read schedule is executed...")

        primaryMongoTemplate.findAll(
            OrderOutbox::class.java
        ).flatMap { event ->
            logger.info("Processing event {} and order aggregate is {}", event.type, event.order)
            when (event.type) {
                EventType.ORDER_CREATED -> {
                    ticketService.createTicket(
                        event.order
                    ).flatMap { ticket ->
                        Mono.just(
                            ticket.orderId
                        )
                    }
                }
                EventType.ORDER_REJECTED -> {
                    ticketService.deleteTicket(
                        event.order
                    ).flatMap { ticket ->
                        Mono.just(
                            ticket.orderId
                        )
                    }
                }
                else -> Mono.just(event.order.id)
            }
        }
            .collectList()
            .flatMap { orderIds ->  //  cleanup
                if (orderIds.isNotEmpty()) {
                    primaryMongoTemplate.remove(
                        Query.query(
                            Criteria.where("key").`in`(orderIds)
                        ),
                        OrderOutbox::class.java
                    )
                } else Mono.fromRunnable{}
            }.subscribe {
            logger.info("Reading events from database...")
        }
    }
}