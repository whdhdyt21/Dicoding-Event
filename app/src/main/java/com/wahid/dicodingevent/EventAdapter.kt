package com.wahid.dicodingevent

import android.annotation.SuppressLint
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
    private var listEvent: List<ListEventsItem>,
    private val horizontal: Boolean = false
) : RecyclerView.Adapter<ListEventAdapter.ListViewHolder>() {

    class ListViewHolder(var binding: ViewBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = if (horizontal) {
            HorizontalRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        } else {
            ItemRowEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        }
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val data = listEvent[position]
        if (horizontal) {
            val binding = holder.binding as HorizontalRowEventBinding
            binding.cardTitle.text = data.name
            binding.cardCover.loadImage(data.imageLogo)
        } else {
            val binding = holder.binding as ItemRowEventBinding
            binding.cardTitle.text = data.name
            binding.cardSummary.text = data.summary
            binding.cardCover.loadImage(data.imageLogo)
        }

        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailEventActivity::class.java)
            intent.putExtra(DetailEventActivity.EXTRA_EVENT_ID, data.id)
            holder.itemView.context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateEvents(list: List<ListEventsItem>) {
        listEvent = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listEvent.size
}