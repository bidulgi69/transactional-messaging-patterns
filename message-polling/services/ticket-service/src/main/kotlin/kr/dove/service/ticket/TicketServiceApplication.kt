package kr.dove.service.ticket

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
class TicketServiceApplication

fun main(args: Array<String>) {
	runApplication<TicketServiceApplication>(*args)
}
