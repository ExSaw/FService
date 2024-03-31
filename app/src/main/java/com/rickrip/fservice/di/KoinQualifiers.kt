package com.rickrip.fservice.di

import org.koin.core.qualifier.Qualifier
import org.koin.core.qualifier.named

enum class KoinQualifiers(val qualifier: Qualifier) {
    APP_SCOPE(named("APP_SCOPE"))
}