package com.example.datasourceroutingtest

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.DependsOn
import org.springframework.context.annotation.Primary
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType
import org.springframework.transaction.annotation.EnableTransactionManagement

@Configuration
class ReplicationDataSourceConfig {

    @Primary
    @Bean
    // @DependsOn required!! thanks to Michel Decima
//    @DependsOn({"primaryDataSource", "secondaryDataSource"})
    fun dataSource() = primaryDataSource()

//    @Bean
    fun primaryDataSource() = EmbeddedDatabaseBuilder()
        .setName("primary")
        .setType(EmbeddedDatabaseType.H2)
        .setScriptEncoding("UTF-8")
        .addScript("classpath:/primary.sql")
        .build()

//    @Bean
    fun secondaryDataSource() = EmbeddedDatabaseBuilder()
        .setName("secondary")
        .setType(EmbeddedDatabaseType.H2)
        .setScriptEncoding("UTF-8")
        .addScript("classpath:/secondary.sql")
        .build()

}
