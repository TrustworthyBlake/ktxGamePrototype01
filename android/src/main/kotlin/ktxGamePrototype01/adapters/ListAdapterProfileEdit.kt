package ktxGamePrototype01.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import kotlinx.android.synthetic.main.list_item.view.*
import kotlinx.android.synthetic.main.list_item.view.item_image_view
import kotlinx.android.synthetic.main.list_item.view.item_text_view
import kotlinx.android.synthetic.main.list_item_profile_edit.view.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.sharedprefs

class ListAdapterProfileEdit(private val listVar: List<ListItemProfileEdit>, private val listenerList: ListClickListener) : RecyclerView.Adapter<ListAdapterProfileEdit.ListItemHolderProfile>() {

    inner class ListItemHolderProfile(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener { //this is the content of each item in list
        val imageView: ImageView = itemView.item_image_view //ids from list_item.xml
        val textView1: TextView = itemView.item_text_view

        init {
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            val pos = adapterPosition
            listenerList.onItemClick(pos)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListItemHolderProfile {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.list_item_profile_edit, parent, false)
        return ListItemHolderProfile(itemView)
    }

    override fun getItemCount() = listVar.size


    override fun onBindViewHolder(holder: ListItemHolderProfile, position: Int) {
        val currentItem = listVar[position]
        holder.imageView.setImageResource(currentItem.imageResource)
        holder.textView1.text = currentItem.text
    }

    interface ListClickListener {
        fun onItemClick(pos: Int)
    }

}



