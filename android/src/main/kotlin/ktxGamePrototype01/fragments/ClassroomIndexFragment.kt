package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomIndexBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.Chat
import ktxGamePrototype01.adapters.ClassroomIndexRecyclerAdapter
import java.util.*


class ClassroomIndexFragment : Fragment() {
    private lateinit var binding: FragmentClassroomIndexBinding
    private var classList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ClassroomIndexRecyclerAdapter
    private val db = FirebaseFirestore.getInstance()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom_index, container, false)
        adapter = ClassroomIndexRecyclerAdapter(classList)
        binding.recyclerViewClasses.adapter = adapter
        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(context)
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        fetchClassrooms(userID)

        binding.btnJoinClassroom.setOnClickListener() {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            classList.clear()
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->
                classList.add(editText.text.toString())
                adapter.notifyItemInserted(classList.size - 1)
            }
            builder.show()


        }
        binding.btnOpenCreateQuiz.setOnClickListener {
            findNavController().navigate(R.id.dest_create_quiz)
        }

        binding.btnLaunchGame.setOnClickListener {
            val x = User.getTeacherAvatars()
            val y = User.getName()
            (activity as AppActivity?)!!.launchGame("OpenWorldScreen", y, "test9", x )
        }

        return binding.root
    }

    private fun fetchClassrooms(id: String ){
        db.collection("users").document(id).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val classroomList = task.result?.get("courses") as List<String>
                // getting the reference to the textViews
                // displaying the data in the textViews
                for(classroom in classroomList) {
                    classList.add(classroom)
                    adapter.notifyItemInserted(classList.size - 1)
                }
            }
        }
    }
}