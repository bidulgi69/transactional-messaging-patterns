package kr.dove.service.ticket.listener

import com.fasterxml.jackson.databind.ObjectMapper
import core.event.Event
import core.event.EventType
import exceptions.UnsupportedEventTypeException
import io.debezium.config.Configuration
import io.debezium.data.Envelope.FieldName
import io.debezium.data.Envelope.Operation
import io.debezium.embedded.Connect
import io.debezium.engine.DebeziumEngine
import io.debezium.engine.RecordChangeEvent
import io.debezium.engine.format.ChangeEventFormat
import kr.dove.service.ticket.TicketService
import org.apache.kafka.connect.data.Struct
import org.apache.kafka.connect.source.SourceRecord
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.DisposableBean
import org.springframework.beans.factory.InitializingBean
import org.springframework.stereotype.Component

@Component
class DebeziumListener(
    sourceConnector: Configuration,
    private val ticketService: TicketService,
) : InitializingBean, DisposableBean {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)
    private val debeziumEngine: DebeziumEngine<RecordChangeEvent<SourceRecord>> = DebeziumEngine
        .create(ChangeEventFormat.of(Connect::class.java))
        .using(sourceConnector.asProperties())
        .notifying { event -> handleChangeEvent(event) }
        .build()

    private fun handleChangeEvent(sourceRecordChangeEvent: RecordChangeEvent<SourceRecord>) {
        val objectMapper = ObjectMapper()
        val sourceRecord: SourceRecord = sourceRecordChangeEvent.record()
        logger.info("Key = ${sourceRecord.key()}, value = ${sourceRecord.value()}")

        try {
            with(sourceRecord.value() as Struct) {
                val operation = Operation.forCode(get(FieldName.OPERATION) as String) ?. let { op ->
                    when (op) {
                        Operation.CREATE -> FieldName.AFTER
                        Operation.DELETE -> FieldName.BEFORE
                        else -> throw ClassCastException("Operation $op: does not supported.")
                    }
                }
                val event = objectMapper.readValue(get(operation) as String, Event::class.java)
                when (event.type) {
                    EventType.ORDER_CREATED -> ticketService.createTicket(event)
                    EventType.ORDER_REJECTED -> ticketService.deleteTicket(event)
                    else -> throw UnsupportedEventTypeException("Invalid event type.")
                }
            }
        } catch (e: RuntimeException) {
            //  throw error
            //  e.printStackTrace()
        }
    }

    override fun afterPropertiesSet() {
        debeziumEngine.run()
    }

    override fun destroy() {
        debeziumEngine.close()
    }
}