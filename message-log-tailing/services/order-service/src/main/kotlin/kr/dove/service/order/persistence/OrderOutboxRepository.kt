package kr.dove.service.order.persistence

import org.springframework.data.mongodb.repository.ReactiveMongoRepository

interface OrderOutboxRepository : ReactiveMongoRepository<OrderOutbox, String>