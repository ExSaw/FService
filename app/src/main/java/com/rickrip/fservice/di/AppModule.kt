package com.rickrip.fservice.di

import com.rickrip.fservice.util.Communicator
import com.rickrip.fservice.util.ICommunicator
import com.rickrip.fservice.util.FakeUploader
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    single(KoinQualifiers.APP_SCOPE.qualifier) {
        CoroutineScope(Dispatchers.Default + CoroutineName(KoinQualifiers.APP_SCOPE.name))
    }
    singleOf<IDispatchersProvider>(::StandardDispatchers)
    single<ICommunicator> { Communicator(get()) }
    single<FakeUploader> { FakeUploader(
        coroutineScope = get(KoinQualifiers.APP_SCOPE.qualifier),
        dispatchers = get(),
        communicator = get()
    ) }
}