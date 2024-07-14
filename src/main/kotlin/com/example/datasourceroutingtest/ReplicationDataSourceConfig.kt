package com.example.datasourceroutingtest

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.jdbc.DataSourceBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource

@Configuration
class ReplicationDataSourceConfig {

    @Bean
    @Primary
    @DependsOn("primaryDataSource", "secondaryDataSource")
    fun dataSource(): DataSource = LazyConnectionDataSourceProxy(primaryDataSource()).apply {
        setReadOnlyDataSource(secondaryDataSource())
    }

    // @Bean
    // fun routingDataSource(): DataSource = RoutingDataSource(primaryDataSource(), secondaryDataSource())

    // @Bean
    // @Primary
    // @DependsOn("primaryDataSource", "secondaryDataSource")
    // fun dataSource(): DataSource = LazyConnectionDataSourceProxy(RoutingDataSource(primaryDataSource(), secondaryDataSource()))

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
