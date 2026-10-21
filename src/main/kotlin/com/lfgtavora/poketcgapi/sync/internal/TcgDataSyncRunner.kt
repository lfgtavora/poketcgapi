package com.lfgtavora.poketcgapi.sync.internal

import com.lfgtavora.poketcgapi.sync.internal.config.SyncProperties
import org.slf4j.LoggerFactory
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class TcgDataSyncRunner(
    private val properties: SyncProperties,
    private val syncService: TcgDataSyncService,
) : ApplicationRunner {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun run(args: ApplicationArguments) {
        if (!properties.enabled) {
            log.info("TCG data sync disabled")
            return
        }
        try {
            syncService.sync()
        } catch (e: Exception) {
            if (properties.failOnError) throw e
            log.error("TCG data sync failed; starting with existing/empty catalog", e)
        }
    }
}
