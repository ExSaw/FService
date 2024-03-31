package com.rickrip.fservice.presentation.list

import android.graphics.Color
import androidx.core.view.doOnAttach
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.rickrip.fservice.R
import com.rickrip.fservice.databinding.ItemFBinding
import com.rickrip.fservice.util.FakeUploader
import com.rickrip.fservice.util.logUnlimited
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.nio.file.Files.find

class FViewHolder(
    private val binding: ItemFBinding,
    private val onClick: (UploadItem) -> Unit
) : RecyclerView.ViewHolder(binding.root), KoinComponent {

    private lateinit var item: UploadItem
    private val fakeUploader: FakeUploader by inject()

    init {
        binding.root.setOnClickListener {
            onClick(item)
        }
    }

    fun bind(item: UploadItem) {
        this.item = item
        binding.apply {
            val progress = item.progress
            root.setBackgroundColor(
                itemView.context.getColor(
                    if (progress < 100) {
                        R.color.orange_item
                    } else {
                        R.color.green_item
                    }
                )
            )
            tvOutput.text = "id=${item.id} " + progress.toString() + "%"
        }
//        binding.apply {
//            root.doOnAttach {
//                itemView.rootView.findViewTreeLifecycleOwner()
//                    ?.lifecycleScope
//                    ?.launch {
//                        fakeUploader.activeUploads
//                            .collectLatest {
//                                val progress = it.activeUploadItems
//                                    .find { it.id == item.id }?.progress
//                                root.setBackgroundColor(
//                                    itemView.context.getColor(
//                                        if (progress == null || progress < 100) {
//                                            R.color.orange_item
//                                        } else {
//                                            R.color.green_item
//                                        }
//                                    )
//                                )
//                                progress?.let {
//                                    tvOutput.text = "id=${item.id} " + progress.toString() + "%"
//                                }
//                            }
//                    }
//            }
//        }
    }

    fun updateProgress(item: UploadItem) {
        binding.apply {
            val progress = item.progress
            root.setBackgroundColor(
                itemView.context.getColor(
                    if (progress < 100) {
                        R.color.orange_item
                    } else {
                        R.color.green_item
                    }
                )
            )
            tvOutput.text = "id=${item.id} " + progress.toString() + "%"
        }
    }
}