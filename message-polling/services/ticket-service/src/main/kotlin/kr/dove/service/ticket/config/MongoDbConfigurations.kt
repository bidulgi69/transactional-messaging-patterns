package kr.dove.service.ticket.config

import com.mongodb.ConnectionString
import com.mongodb.MongoClientSettings
import com.mongodb.reactivestreams.client.MongoClient
import com.mongodb.reactivestreams.client.MongoClients
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory

@Configuration
class MongoDbConfigurations {

    @Primary
    @Bean(name = ["primaryProperties"])
    @ConfigurationProperties(prefix = "spring.data.mongodb.primary")
    fun getPrimaryProperties(): MongoProperties {
        return MongoProperties()
    }

    @Bean(name = ["secondaryProperties"])
    @ConfigurationProperties(prefix = "spring.data.mongodb.secondary")
    fun getSecondaryProperties(): MongoProperties {
        return MongoProperties()
    }

    @Primary
    @Bean
    fun primaryMongoTemplate(
        @Qualifier("primaryMongoDatabaseFactory") databaseFactory: ReactiveMongoDatabaseFactory
    ): ReactiveMongoTemplate {
        return ReactiveMongoTemplate(databaseFactory)
    }

    @Bean
    fun secondaryMongoTemplate(
        @Qualifier("secondaryMongoDatabaseFactory") databaseFactory: ReactiveMongoDatabaseFactory
    ): ReactiveMongoTemplate {
        return ReactiveMongoTemplate(databaseFactory)
    }

    @Primary
    @Bean
    fun primaryMongoDatabaseFactory(
        @Qualifier("primaryProperties") properties: MongoProperties
    ): ReactiveMongoDatabaseFactory {
        return SimpleReactiveMongoDatabaseFactory(
            createReactiveClientFromProperties(properties),
            properties.database
        )
    }

    @Bean
    fun secondaryMongoDatabaseFactory(
        @Qualifier("secondaryProperties") properties: MongoProperties
    ): ReactiveMongoDatabaseFactory {
        return SimpleReactiveMongoDatabaseFactory(
            createReactiveClientFromProperties(properties),
            properties.database
        )
    }

    private fun createReactiveClientFromProperties(properties: MongoProperties): MongoClient {
        return MongoClients.create(MongoClientSettings.builder()
            .applyConnectionString(
                ConnectionString(properties.uri)
            )
            .build())
    }
}