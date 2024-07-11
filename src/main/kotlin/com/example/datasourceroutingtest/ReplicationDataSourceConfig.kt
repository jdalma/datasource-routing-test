package com.example.datasourceroutingtest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.DataSourceTransactionManager
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.orm.jpa.JpaTransactionManager
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.TransactionManager
import javax.sql.DataSource
import javax.xml.crypto.Data

@Configuration
class ReplicationDataSourceConfig {

    @Bean
    @Primary
    @DependsOn("primaryDataSource", "secondaryDataSource", "routingDataSource")
    fun dataSource(): DataSource = LazyConnectionDataSourceProxy(routingDataSource())

    @Bean
    fun routingDataSource(): DataSource = RoutingDataSource(primaryDataSource(), secondaryDataSource())

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.primary")
    fun primaryDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.secondary")
    fun secondaryDataSource(): DataSource {
        return DataSourceBuilder.create().build()
    }
}
