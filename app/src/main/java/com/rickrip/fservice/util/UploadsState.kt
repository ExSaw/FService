package com.rickrip.fservice.util

import com.rickrip.fservice.presentation.list.UploadItem

data class UploadsState(
    val forceUpdateCounter: Int = 0,
    val activeUploadItems: List<UploadItem> = emptyList()
)