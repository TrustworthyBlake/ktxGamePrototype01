package ktxGamePrototype01.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.game_entry.view.*
import kotlinx.android.synthetic.main.student_entry.view.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.User
import ktxGamePrototype01.inflate
import java.io.File
import java.io.FileOutputStream

class StudentRecyclerAdapter(private val studentObject: ArrayList<String>) : RecyclerView.Adapter<StudentRecyclerAdapter.StudentHolder>() {
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentRecyclerAdapter.StudentHolder {
        val view = parent.inflate(R.layout.student_entry, false)
        return StudentHolder(view)
    }

    override fun getItemCount() = studentObject.size


    override fun onBindViewHolder(holder: StudentRecyclerAdapter.StudentHolder, position: Int) {
        val gameItem = studentObject[position]
        holder.bindText(gameItem)
    }

    //1
    class StudentHolder(private val view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        //2
        private var sName: String? = null

        //3
        init {
            view.setOnClickListener(this)
        }

        fun bindText(studentName: String) {
            this.sName = studentName
            view.student_name.text = studentName
        }

        //4
        override fun onClick(v: View) {

            Toast.makeText(view.context, "Student clicked", Toast.LENGTH_SHORT).show()

        }
    }
}