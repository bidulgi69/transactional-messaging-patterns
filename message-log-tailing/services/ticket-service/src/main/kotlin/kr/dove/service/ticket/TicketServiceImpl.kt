package kr.dove.service.ticket

import core.event.Event
import core.event.EventType
import core.state.State
import core.ticket.Ticket
import exceptions.UnsupportedEventTypeException
import kr.dove.service.ticket.persistence.TicketEntity
import kr.dove.service.ticket.persistence.TicketRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class TicketServiceImpl(
    private val ticketRepository: TicketRepository,
) : TicketService {

    override fun createTicket(event: Event): Ticket {
        return when (event.type) {
            EventType.ORDER_CREATED -> {
                with(event.order) {
                    val ticketEntity = ticketRepository.save(
                        TicketEntity(
                            id = UUID.randomUUID().toString(),
                            state = State.PENDING,
                            orderId = id,
                            restaurantId = restaurantId,
                            acceptTime = LocalDateTime.now(),
                            orderItems = orderItems
                        )
                    )

                    ticketEntity.mapToApi()
                }
            }
            else -> {
                throw UnsupportedEventTypeException("Invalid event type ${event.type}.")
            }
        }
    }

    override fun deleteTicket(event: Event) {
        when (event.type) {
            EventType.ORDER_REJECTED -> {
                with(event.order) {
                    val ticketEntity: TicketEntity? = ticketRepository.findByOrderId(id)
                    ticketEntity ?. let { en ->
                        ticketRepository.save(
                            en.apply {
                                this.state = State.REJECTED
                            }
                        )
                    }
                }
            }
            else -> {
                throw UnsupportedEventTypeException("Invalid event type ${event.type}.")
            }
        }
    }
}