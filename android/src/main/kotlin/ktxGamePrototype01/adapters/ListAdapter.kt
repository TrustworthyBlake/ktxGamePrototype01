package com.codinginflow.recyclerviewexample
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import kotlinx.android.synthetic.main.list_item.view.*

import ktxGamePrototype01.adapters.ListItem

class ListAdapter(private val listVar: List<ListItem>) : RecyclerView.Adapter<ListAdapter.ListItemHolder>() {

    class ListItemHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.item_image_view //ids from list_item.xml
        val textView1: TextView = itemView.item_text_view
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ListItemHolder(itemView)
    }

    override fun onBindViewHolder(holder: ListItemHolder, position: Int) {
        val currentItem = listVar[position]
        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView1.text = currentItem.text
    }

    override fun getItemCount() = listVar.size

}