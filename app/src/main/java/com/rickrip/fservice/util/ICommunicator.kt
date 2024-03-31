package com.rickrip.fservice.util

import kotlinx.coroutines.flow.MutableSharedFlow

interface ICommunicator {
    fun getFlow(): MutableSharedFlow<Any?>
}