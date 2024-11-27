package com.wahid.dicodingevent

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.wahid.dicodingevent.data.model.ListEventsItem
import com.wahid.dicodingevent.databinding.HorizontalRowEventBinding
import com.wahid.dicodingevent.databinding.ItemRowEventBinding
import com.wahid.dicodingevent.ui.detail.DetailEventActivity
import com.wahid.dicodingevent.ui.utils.loadImage

class ListEventAdapter(
    private val listEvent: List<ListEventsItem>,
    private val horizontal: Boolean = false
) : RecyclerView.Adapter<ListEventAdapter.ListViewHolder>() {

    inner class ListViewHolder(val binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListEventsItem) {
            when (binding) {
                is HorizontalRowEventBinding -> {
                    binding.cardTitle.text = data.name
                    binding.cardCover.loadImage(data.imageLogo)
                }
                is ItemRowEventBinding -> {
                    binding.cardTitle.text = data.name
                    binding.cardSummary.text = data.summary
                    binding.cardCover.loadImage(data.imageLogo)
                }
            }

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, DetailEventActivity::class.java).apply {
                    putExtra(DetailEventActivity.EXTRA_EVENT_ID, data.id)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = if (horizontal) {
            HorizontalRowEventBinding.inflate(inflater, parent, false)
        } else {
            ItemRowEventBinding.inflate(inflater, parent, false)
        }
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(listEvent[position])
    }

    override fun getItemCount(): Int = listEvent.size
}
