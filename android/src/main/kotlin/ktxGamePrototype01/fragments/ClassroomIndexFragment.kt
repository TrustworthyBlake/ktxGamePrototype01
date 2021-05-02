package ktxGamePrototype01.fragments

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentClassroomIndexBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.DBObject
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.Chat
import ktxGamePrototype01.adapters.ClassroomIndexRecyclerAdapter
import java.util.*
import kotlin.collections.ArrayList

private val failTAG = "DATABASE ENTRY FAILED"
private val successTAG = "DATABASE ENTRY SUCCESS"

class ClassroomIndexFragment : Fragment() {
    private lateinit var binding: FragmentClassroomIndexBinding
    private var classList: ArrayList<String> = ArrayList()
    private lateinit var adapter: ClassroomIndexRecyclerAdapter
    private val db = FirebaseFirestore.getInstance()
    private val classroomVM: ClassroomViewModel by activityViewModels()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_classroom_index, container, false)
        adapter = ClassroomIndexRecyclerAdapter(classList)
        binding.recyclerViewClasses.adapter = adapter
        binding.recyclerViewClasses.layoutManager = LinearLayoutManager(context)
        classroomVM.select("")
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        fetchClassrooms(userID)

        binding.btnJoinClassroom.setOnClickListener() {
            val builder = AlertDialog.Builder(context)
            val dialogLayout = inflater.inflate(R.layout.prompt_join_classroom, null)
            val editText  = dialogLayout.findViewById<EditText>(R.id.editText)
            builder.setView(dialogLayout)
            builder.setPositiveButton("OK") { dialogInterface, i ->

                addClassroom(DBObject.capitalize(editText.text.toString()), "3rd", "Teacher test", "2021".toInt(), userID)

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

    private fun addClassroom(courseName: String, grade: String, teacher: String, year: Int, uid: String) {
        val db = FirebaseFirestore.getInstance()
        // create data entry for the course
        val studentList: List<String> = emptyList()
        val announcementList: List<String> = emptyList()
        val modules: List<String> = emptyList()
        //val userID = findUserByName(capitalize(teacher))

        val course = hashMapOf(
            "course name" to courseName,
            "grade" to grade,
            "modules" to modules,
            "teacher name" to DBObject.capitalize(teacher),
            "year" to year,
            "students" to studentList,
            "announcements" to announcementList
        )
        // add the data into the database
        db.collection("classrooms").document("$grade grade $courseName $year")
            .set(course)
            .addOnSuccessListener { Log.d(successTAG, "Successfully added classroom to DB")
                Toast.makeText(activity, "Classroom successfully created", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.dest_user)}
            .addOnFailureListener { e -> Log.w(failTAG, "Error adding classroom to DB", e)
                Toast.makeText(activity, "Classroom creation error", Toast.LENGTH_LONG).show()}

        db.collection("classrooms")
            .document("$grade grade $courseName $year")
            .update("students", FieldValue.arrayUnion(uid))

        db.collection("users")
            .document(uid)
            .update("courses", FieldValue.arrayUnion("$grade grade $courseName $year"))

    }

    private fun fetchClassrooms(id: String ){
        db.collection("users").document(id).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val classroomList = task.result?.get("courses") as List<String>
                for(classroom in classroomList) {
                    if(!classList.contains(classroom)){
                        classList.add(classroom)
                        adapter.notifyItemInserted(classList.size - 1)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()

    }
}