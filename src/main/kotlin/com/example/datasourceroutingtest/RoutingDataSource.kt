package com.example.datasourceroutingtest

import org.slf4j.LoggerFactory
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource
import org.springframework.transaction.support.TransactionSynchronizationManager
import javax.sql.DataSource

class RoutingDataSource(
    primary: DataSource,
    secondary: DataSource
): AbstractRoutingDataSource() {
    private val log = LoggerFactory.getLogger(RoutingDataSource::class.java)

    init {
        this.setTargetDataSources(mapOf("write" to primary, "read" to secondary))
        this.setDefaultTargetDataSource(primary)
    }

    override fun determineCurrentLookupKey(): Any {
        val type = if (TransactionSynchronizationManager.isCurrentTransactionReadOnly()) "read" else "write"
        log.info("DataSource Type : {}", type)
        return type
    }

}
