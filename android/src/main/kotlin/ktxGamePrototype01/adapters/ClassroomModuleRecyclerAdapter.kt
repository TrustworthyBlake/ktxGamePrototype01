package ktxGamePrototype01.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.core.view.get
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.classroom_index_entry.view.*
import kotlinx.android.synthetic.main.fragment_classroom_module.view.*
import kotlinx.android.synthetic.main.module_entry.view.*
import ktxGamePrototype01.User
import ktxGamePrototype01.fragments.ClassroomViewModel
import ktxGamePrototype01.inflate

class ClassroomModuleRecyclerAdapter (private var moduleText: ArrayList<String>) : RecyclerView.Adapter<ClassroomModuleRecyclerAdapter.ModuleHolder>() {
    private val db = FirebaseFirestore.getInstance()



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClassroomModuleRecyclerAdapter.ModuleHolder {
        val view = parent.inflate(R.layout.module_entry, false)
        return ModuleHolder(view)
    }

    override fun getItemCount() = moduleText.size

    private var onClickListener : OnClickListener? = null

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    interface OnClickListener {
        fun onClick(position: Int){
        }
    }

    private fun removeItem(position: Int){
        moduleText.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, moduleText.size)
    }


    override fun onBindViewHolder(holder: ClassroomModuleRecyclerAdapter.ModuleHolder, position: Int) {
        val itemText = moduleText[position]
        holder.bindText(itemText)

        if(User.checkForTeacher()){
            holder.itemView.module_delete_icon.visibility = View.VISIBLE
        }

        holder.itemView.module_delete_icon.setOnClickListener {

            /*
            db.collection("classrooms")
                .document(holder.itemView.lbl_classroom_name.text.toString())
                .update("quizes", FieldValue.arrayRemove(tempModule))
                .addOnFailureListener { e ->
                    Log.w("FAIL", "Error deleting module from classroom", e)
                }
            */

            removeItem(position)
        }
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

        override fun onClick(v: View) {


            val module = this.text
            val bundle = bundleOf("module" to module)

            Navigation.findNavController(v).navigate(R.id.dest_module, bundle)




        }

    }


}