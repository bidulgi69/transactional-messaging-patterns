package kr.dove.service.ticket.persistence

import org.springframework.data.mongodb.repository.MongoRepository

interface TicketRepository : MongoRepository<TicketEntity, String> {
    fun findByOrderId(orderId: String): TicketEntity?
}