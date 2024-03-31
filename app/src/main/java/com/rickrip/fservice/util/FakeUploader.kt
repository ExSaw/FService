package com.rickrip.fservice.util

import androidx.recyclerview.widget.ListAdapter
import com.rickrip.fservice.di.IDispatchersProvider
import com.rickrip.fservice.presentation.list.UploadItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.random.Random

class FakeUploader(
    coroutineScope: CoroutineScope,
    private val dispatchers: IDispatchersProvider,
    communicator: ICommunicator,
) {
    val activeUploads = MutableStateFlow(UploadsState())

    init {
        logUnlimited("---FakeUploader->INIT")
        coroutineScope.launch(dispatchers.default) {
            while (true) {
                delay(100L)
                var progress = 0
                val updatedItems =
                    activeUploads.first().activeUploadItems
                        .map {
                            if (it.progress < 100) {
                                progress += (it.progress + 1)
                                it.copy(progress = it.progress + 1)
                            } else it
                        }
                logUnlimited("---FakeUploader->all progress=$progress")
                activeUploads.update {
                    it.copy(
                        forceUpdateCounter = it.forceUpdateCounter + 1,
                        activeUploadItems = updatedItems
                    )
                }
                val itemsInProgress = updatedItems.filter { it.progress < 100 }
                val totalProgress = if (itemsInProgress.isNotEmpty()) {
                    itemsInProgress.sumOf { it.progress } / itemsInProgress.size
                } else 100
                logUnlimited("---FakeUploader->totalProgress=$totalProgress")
                communicator.getFlow().emit(totalProgress)
            }
        }
    }

    fun uploadNewItem(adapter: ListAdapter<UploadItem, *>) {

        val newItem = UploadItem(
            id = Random.nextInt(),
            progress = 0
        )

        activeUploads.update {
            it.copy(
                forceUpdateCounter = it.forceUpdateCounter + 1,
                activeUploadItems = it.activeUploadItems + newItem
            )
        }

        adapter.submitList(activeUploads.value.activeUploadItems)
    }

    fun stopActiveUploads(adapter: ListAdapter<UploadItem, *>) {
        activeUploads.update {
            it.copy(
                forceUpdateCounter = it.forceUpdateCounter + 1,
                activeUploadItems = it.activeUploadItems.filter { it.progress == 100 }
            )
        }
        adapter.submitList(activeUploads.value.activeUploadItems)
    }
}