package ktxGamePrototype01.adapters

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import kotlinx.android.synthetic.main.module_entry.view.*
import ktxGamePrototype01.inflate

class ClassroomIndexRecyclerAdapter(private val classText: ArrayList<String>) : RecyclerView.Adapter<ClassroomIndexRecyclerAdapter.ClassroomHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomIndexRecyclerAdapter.ClassroomHolder {
        val view = parent.inflate(R.layout.module_entry, false)
        return ClassroomHolder(view)
    }

    override fun getItemCount() = classText.size

    override fun onBindViewHolder(holder: ClassroomIndexRecyclerAdapter.ClassroomHolder, position: Int) {
        val itemText = classText[position]
        holder.bindText(itemText)
    }

    //1
    class ClassroomHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
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
            findNavController(v).navigate(R.id.dest_classroom_index)



            Log.d("RecyclerView", "CLICK!")
        }

    }


}