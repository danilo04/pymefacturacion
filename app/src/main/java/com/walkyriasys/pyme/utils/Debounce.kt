package com.walkyriasys.pyme.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.ClosedSendChannelException
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

fun <T> Flow<T>.debounceAndSend(
    timeout: Long,
    emittedItemCount: Int = 20,
    scope: CoroutineScope
): Flow<T> {
    return DebounceFlowImpl(this, scope, timeout, emittedItemCount)
}

private class DebounceFlowImpl<T>(
    private val upstream: Flow<T>,
    private val scope: CoroutineScope,
    val timeout: Long,
    val emittedItemCount: Int = 20
) : Flow<T> {
    override suspend fun collect(collector: FlowCollector<T>) {
        var lastItem: T?
        var iterator = 0
        var time: Long = System.currentTimeMillis()
        var job: Job? = null
        upstream.collect { item ->
            val now = System.currentTimeMillis()
            job?.cancel()
            job = null
            if ((time + timeout) < now || iterator > emittedItemCount) {
                lastItem = null
                time = now
                collector.emit(item)
                iterator = 0
            } else {
                iterator += 1
                lastItem = item
                job = scope.launch {
                    delay(timeout)
                    if (this.isActive) {
                        lastItem?.let {
                            try {
                                collector.emit(it)
                            } catch (e: ClosedSendChannelException) {

                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "DebounceFlowImpl"
    }
}
