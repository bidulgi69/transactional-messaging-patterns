package kr.dove.service.ticket

import core.event.Event
import core.ticket.Ticket

interface TicketService {

    fun createTicket(event: Event): Ticket

    fun deleteTicket(event: Event)
}