package kr.dove.service.order

import core.event.EventType
import core.order.Order
import core.state.State
import exceptions.OrderNotFoundException
import kr.dove.service.order.persistence.OrderEntity
import kr.dove.service.order.persistence.OrderOutbox
import kr.dove.service.order.persistence.OrderOutboxRepository
import kr.dove.service.order.persistence.OrderRepository
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
class OrderServiceImpl(
    private val orderRepository: OrderRepository,
    private val orderOutboxRepository: OrderOutboxRepository,
) : OrderService {

    @Transactional
    override fun createOrder(order: Order): Mono<Order> {
        val orderEntity: Mono<OrderEntity> = orderRepository.save(
            OrderEntity(
                order.id,
                State.PENDING,
                order.customerId,
                order.restaurantId,
                order.payment,
                order.orderItems,
            )
        )
        val eventToOutbox = orderOutboxRepository.save(
            OrderOutbox(
                type = EventType.ORDER_CREATED,
                key = order.id,
                order = order
            )
        )

        return Mono.zip(
            orderEntity,
            eventToOutbox
        ).flatMap {
            Mono.just(
                it.t1.mapToApi()
            )
        }
    }

    override fun getOrder(orderId: String): Mono<Order> {
        return orderRepository.findById(orderId)
            .switchIfEmpty(Mono.error(OrderNotFoundException("Invalid order id.")))
            .flatMap { en ->
                Mono.just(
                    en.mapToApi()
                )
            }
    }

    @Transactional
    override fun deleteOrder(orderId: String): Mono<Void> {
        return orderRepository.findById(orderId)
            .flatMap { en ->
                val rejected = orderRepository.save(
                    en.apply {
                        this.state = State.REJECTED
                    }
                )
                val eventToOutbox = orderOutboxRepository.save(
                    OrderOutbox(
                        type = EventType.ORDER_REJECTED,
                        key = orderId,
                        order = en.mapToApi(),
                    )
                )

                Mono.zip(
                    rejected,
                    eventToOutbox
                )
            }.then()
    }
}