package ktxGamePrototype01.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentAddStudentsBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentNewClassroomBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import ktxGamePrototype01.DBObject
import java.io.File
import java.lang.Exception

class AddStudentsFragment : Fragment() {

    private lateinit var binding: FragmentAddStudentsBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private val failTAG = "DATABASE ENTRY FAILED"
    private val successTAG = "DATABASE ENTRY SUCCESS"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_students, container, false)



        val buttonAddStudentName = binding.root.findViewById<Button>(R.id.btn_add_student_name)
        buttonAddStudentName.setOnClickListener() {
            getName()
        }

        val buttonAddStudentsFile = binding.root.findViewById<Button>(R.id.btn_add_student_file)
        buttonAddStudentsFile.setOnClickListener() {
            getFile()
        }


        return binding.root
    }

    private fun getName() {
        val textFieldClassroom = binding.root.findViewById<EditText>(R.id.et_classroom_name)
        val textFieldStudentName = binding.root.findViewById<EditText>(R.id.et_student_file_name)
        val classroom = textFieldClassroom.text.toString()
        val student = DBObject.capitalize(textFieldStudentName.text.toString())

        if (classroom != "" && student != "") {
            findUserByName(student, classroom)
        } else Toast.makeText(activity, "Invalid input", Toast.LENGTH_LONG).show()

    }

    private fun getFile() {
        val textFieldClassroom = binding.root.findViewById<EditText>(R.id.et_classroom_name)
        val textFieldFileName = binding.root.findViewById<EditText>(R.id.et_student_file_name)
        val classroom = textFieldClassroom.text.toString()
        val file = textFieldFileName.text.toString().trim()

        selectFile(file, classroom)

    }

    // reads userIDs from file
    private fun selectFile(fileName: String, className: String) {

        var studentsList = listOf("")

        val filePath = "assets/$fileName"
        val filetest = "C:/BSc/gameAlpha/ktxGamePrototype01/assets/file.txt"
        val lines: List<String> = File(filetest).readLines()
        lines.forEach { line ->
            studentsList = studentsList+line
        }
        addStudents(className, studentsList)
    }

    // function for adding students to a class
    private fun addStudents(className: String, studentList: List<String>) {
        val db = FirebaseFirestore.getInstance()
        // looping through all given students
        for (item in studentList) {
            db.collection("classrooms")
                .document(className)    // add user to classroom
                .update("students", FieldValue.arrayUnion(item))
                .addOnSuccessListener { Log.d(successTAG, "Successfully added students to classroom")
                    Toast.makeText(activity, "Students successfully added to classroom", Toast.LENGTH_LONG).show()
                    // if successfully added student to classroom - add course to student
                    db.collection("users")
                        .document(item) // course to user
                        .update("courses", FieldValue.arrayUnion(className))
                        .addOnSuccessListener { Log.d(successTAG, "Successfully added course to student") }
                        .addOnFailureListener { e -> Log.w(failTAG, "Error adding course to student", e) }
                }
                .addOnFailureListener { e -> Log.w(failTAG, "Error adding students to classroom", e)
                    Toast.makeText(activity, "Error adding students to classroom", Toast.LENGTH_LONG).show() }
        }
    }

    // find a user by the users name
    private fun findUserByName(name: String, classroom: String) {
        // TODO no error displaying to user if given name doesn't exist - onFailureListener not working
        val db = FirebaseFirestore.getInstance()
        val doc = db.collection("users").whereEqualTo("name", name).get()
        doc.addOnSuccessListener { documents ->
            for (document in documents) {
                val uid = document["userid"].toString()
                val student = listOf(uid)
                addStudents(classroom, student)
            }
        }
        doc.addOnFailureListener {
            Toast.makeText(activity, "Error finding given student", Toast.LENGTH_LONG).show()
        }
    }

}