package com.example.datasourceroutingtest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
class ReplicationDataSourceConfig {

    @Primary
    @Bean
    fun dataSource() = LazyConnectionDataSourceProxy(routingDataSource())

    @Bean
    fun routingDataSource() = RoutingDataSource(primaryDataSource(), secondaryDataSource())

   @Bean
    fun primaryDataSource() = EmbeddedDatabaseBuilder()
        .setName("primary")
        .setType(EmbeddedDatabaseType.H2)
        .setScriptEncoding("UTF-8")
        .addScript("primary.sql")
        .build()

   @Bean
    fun secondaryDataSource() = EmbeddedDatabaseBuilder()
        .setName("secondary")
        .setType(EmbeddedDatabaseType.H2)
        .setScriptEncoding("UTF-8")
        .addScript("secondary.sql")
        .build()

}
