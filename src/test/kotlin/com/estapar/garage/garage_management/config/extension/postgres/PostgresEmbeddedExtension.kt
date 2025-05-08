package com.estapar.garage.garage_management.config.extension.postgres

import com.c6bank.investment.catalog.fi.service.testing.extension.AbstractEmbeddedExtension
import io.zonky.test.db.postgres.embedded.EmbeddedPostgres
import org.junit.jupiter.api.extension.BeforeEachCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL
import java.io.IOException

class PostgresEmbeddedExtension : AbstractEmbeddedExtension(), BeforeEachCallback {
    private lateinit var epg: EmbeddedPostgres

    override fun setup() {
        epg = pg()
    }

    private fun pg(): EmbeddedPostgres {
        return EmbeddedPostgres.builder()
            .setPort(PORT)
            .start()
    }

    override fun close() {
        try {
            epg.close()
        } catch (e: IOException) {
            throw AssertionError(e)
        }
    }

    companion object {
        private const val PORT = 25440
    }

    override fun beforeEach(context: ExtensionContext) {
        println("beforeEach-${this.javaClass.name}-${this.hashCode()}")
        getDatabaseCleaner(context).reset()
    }

    private fun getDatabaseCleaner(context: ExtensionContext): PostgresDatabaseCleaner {
        val uniqueKey = "${this.javaClass.name}-DatabaseCleaner"
        val value = context.root.getStore(GLOBAL)[uniqueKey]
        return if (value == null) {
            PostgresDatabaseCleaner {
                val instance = getInstanceInStore(context) as PostgresEmbeddedExtension
                instance.epg.postgresDatabase.connection
            }.excludeTable("flyway_schema_history").also {
                context.root.getStore(GLOBAL).put(uniqueKey, it)
            }
        } else {
            value as PostgresDatabaseCleaner
        }
    }
}
