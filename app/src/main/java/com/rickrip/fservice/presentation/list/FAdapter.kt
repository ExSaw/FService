package com.rickrip.fservice.presentation.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.rickrip.fservice.databinding.ItemFBinding

internal class FAdapter(
    private val layoutInflater: LayoutInflater,
    private val recyclerView: RecyclerView,
    private val onClick: (UploadItem) -> Unit
) : ListAdapter<UploadItem, FViewHolder>(
    DifferCallback()
) {

    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): FViewHolder {
        return FViewHolder(
            binding = ItemFBinding.inflate(layoutInflater, parent, false),
            onClick = onClick
        )
    }

    override fun onBindViewHolder(holder: FViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onBindViewHolder(
        holder: FViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        super.onBindViewHolder(holder, position, payloads)
        if (payloads.isEmpty()) {
            super.onBindViewHolder(holder, position, payloads)
        } else {
            if (payloads[0] == true) {
               holder.updateProgress(getItem(position))
            }
        }
    }
}

internal class DifferCallback : DiffUtil.ItemCallback<UploadItem>() {

    override fun areItemsTheSame(
        oldItem: UploadItem,
        newItem: UploadItem
    ): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(
        oldItem: UploadItem,
        newItem: UploadItem
    ): Boolean {
        return oldItem == newItem
    }

    // will trigger if areItemsTheSame and !areContentsTheSame
    override fun getChangePayload(oldItem: UploadItem, newItem: UploadItem): Any {
        return oldItem.progress != newItem.progress
    }
}