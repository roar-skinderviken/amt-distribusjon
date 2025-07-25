package no.nav.amt.distribusjon.utils

import ch.qos.logback.classic.Logger
import ch.qos.logback.classic.spi.ILoggingEvent
import ch.qos.logback.core.read.ListAppender
import kotlinx.coroutines.runBlocking
import org.slf4j.LoggerFactory

/**
 * Captures log messages emitted by the specified logger during execution of [block].
 *
 * @param loggerName The fully qualified logger name
 * @param block The suspend function block to run while capturing logs.
 */
fun withLogCapture(loggerName: String, block: suspend (List<ILoggingEvent>) -> Unit) {
    val logger = LoggerFactory.getLogger(loggerName) as Logger
    val listAppender = ListAppender<ILoggingEvent>().apply { start() }
    logger.addAppender(listAppender)

    try {
        runBlocking {
            block(listAppender.list)
        }
    } finally {
        logger.detachAppender(listAppender)
        listAppender.stop()
    }
}
