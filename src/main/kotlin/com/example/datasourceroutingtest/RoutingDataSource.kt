package com.example.datasourceroutingtest

import com.example.datasourceroutingtest.RoutingDataSource.DataSourceType.*
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
        this.setTargetDataSources(mapOf(
            READ_WRITE to primary,
            READ_ONLY to secondary
        ))
        this.setDefaultTargetDataSource(primary)
    }

    override fun determineCurrentLookupKey(): Any {
        return DataSourceType.from(TransactionSynchronizationManager.isCurrentTransactionReadOnly()).apply {
            log.info("DataSource Type : {}", this)
        }
    }

    private enum class DataSourceType {
        READ_WRITE,
        READ_ONLY;

        companion object {
            fun from(isReadOnly: Boolean) = if (isReadOnly) READ_ONLY else READ_WRITE
        }
    }
}
