package ktxGamePrototype01.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import kotlinx.android.synthetic.main.module_entry.view.*
import ktxGamePrototype01.inflate

class ClassroomModuleRecyclerAdapter (private val moduleText: ArrayList<String>) : RecyclerView.Adapter<ClassroomModuleRecyclerAdapter.ModuleHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomModuleRecyclerAdapter.ModuleHolder {
        val view = parent.inflate(R.layout.module_entry, false)
        return ModuleHolder(view)
    }

    override fun getItemCount() = moduleText.size

    override fun onBindViewHolder(holder: ClassroomModuleRecyclerAdapter.ModuleHolder, position: Int) {
        val itemText = moduleText[position]
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

        fun bindText(moduleName: String) {
            this.text = moduleName
            view.module_name.text = this.text
        }

        //4
        override fun onClick(v: View) {
            // val context = itemView.context
            // val showPhotoIntent = Intent(context, PhotoActivity::class.java)
            // showPhotoIntent.putExtra(PHOTO_KEY, photo)
            // context.startActivity(showPhotoIntent)
            val module = this.text
            val bundle = bundleOf("module" to module)

            Navigation.findNavController(v).navigate(R.id.dest_module, bundle)



            Log.d("RecyclerView", "CLICK!")
        }

    }


}