package com.c6bank.investment.catalog.fi.service.testing.extension

import org.junit.jupiter.api.extension.BeforeAllCallback
import org.junit.jupiter.api.extension.ExtensionContext
import org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL
import java.lang.Runtime.getRuntime

abstract class AbstractEmbeddedExtension : BeforeAllCallback {
    override fun beforeAll(context: ExtensionContext) {
        // We need to use a unique key here, across all usages of this particular extension.
        if (getInstanceInStore(context) == null) {
            // First test container invocation.
            println("beforeAll-${this.javaClass.name}-${this.hashCode()}")
            context.root.getStore(GLOBAL).put(getKeyInstanceInStore(), this)
            setup()
            val currentExtension = this
            getRuntime().addShutdownHook(
                object : Thread() {
                    override fun run() {
                        currentExtension.close()
                    }
                }
            )
        }
    }

    fun getInstanceInStore(context: ExtensionContext): Any? {
        val uniqueKey = getKeyInstanceInStore()
        return context.root.getStore(GLOBAL)[uniqueKey]
    }

    private fun getKeyInstanceInStore(): String {
        return this.javaClass.name
    }

    // Callback that is invoked <em>exactly once</em>
    // before the start of <em>all</em> test containers.
    abstract fun setup()

    // Callback that is invoked <em>exactly once</em>
    // after the end of <em>all</em> test containers.
    abstract fun close()
}
