package com.rickrip.fservice.util

import org.koin.android.BuildConfig
import java.util.UUID

fun logUnlimited(
    string: String,
    maxLogSize: Int = 4000,
) {
    if (string.length.isNotNullOrZero() ) {
        println(
            string.chunked(maxLogSize)
                .joinToString(separator = "\n${string.first()}${string.first()}${string.first()}>>")
                .trimIndent()
        )
    }
}

fun getRandomUUID(): String = UUID.randomUUID().toString()

fun Int?.isNotNullOrZero() = this != null && this != 0