package com.rickrip.fservice.util

import com.rickrip.fservice.di.IDispatchersProvider
import com.rickrip.fservice.di.StandardDispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow

class Communicator(
    dispatchers: IDispatchersProvider
):ICommunicator {
    private val _sharedFlow = MutableSharedFlow<Any?>(
        replay = 1,
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    override fun getFlow(): MutableSharedFlow<Any?> = _sharedFlow
}