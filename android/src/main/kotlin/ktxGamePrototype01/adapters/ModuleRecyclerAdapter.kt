package ktxGamePrototype01.adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import kotlinx.android.synthetic.main.module_entry.view.*
import ktxGamePrototype01.inflate

class ModuleRecyclerAdapter(private val classText: ArrayList<String>) : RecyclerView.Adapter<ModuleRecyclerAdapter.ModuleHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ModuleRecyclerAdapter.ModuleHolder {
        val view = parent.inflate(R.layout.module_entry, false)
        return ModuleHolder(view)
    }

    override fun getItemCount() = classText.size

    override fun onBindViewHolder(holder: ModuleRecyclerAdapter.ModuleHolder, position: Int) {
        val itemText = classText[position]
        holder.bindText(itemText)
    }

    //1
    class ModuleHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        //2
        private var text: String? = null

        //3
        init {
            view.setOnClickListener(this)
        }

        fun bindText(photo: String) {
            this.text = photo
            view.module_name.text = this.text
        }

        //4
        override fun onClick(v: View) {
            // val context = itemView.context
            // val showPhotoIntent = Intent(context, PhotoActivity::class.java)
            // showPhotoIntent.putExtra(PHOTO_KEY, photo)
            // context.startActivity(showPhotoIntent)
            val classRoomName = "Error 404"
            val bundle = bundleOf("classroom" to classRoomName)
            findNavController(v).navigate(R.id.dest_classroom, bundle)



            Log.d("RecyclerView", "CLICK!")
        }

    }


}