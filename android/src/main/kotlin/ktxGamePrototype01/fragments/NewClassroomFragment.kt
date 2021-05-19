package ktxGamePrototype01.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentNewClassroomBinding
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.DBObject
import ktxGamePrototype01.DBTest
import ktxGamePrototype01.User

class NewClassroomFragment : Fragment() {

    private lateinit var binding: FragmentNewClassroomBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private val failTAG = "DATABASE ENTRY FAILED"
    private val successTAG = "DATABASE ENTRY SUCCESS"
    val userID = FirebaseAuth.getInstance().currentUser!!.uid

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_new_classroom, container, false)


        val buttonCreateClassroom = binding.root.findViewById<Button>(R.id.btn_create_classroom)
        buttonCreateClassroom.setOnClickListener() {
            getData()
        }

        return binding.root
    }

    // find a user by the users name
    private fun findUserByName(name: String) {
        val db = FirebaseFirestore.getInstance()
        val doc = db.collection("users").whereEqualTo("name", name).get()
        doc.addOnSuccessListener { documents ->
            for (document in documents) {
                val uid = document["userid"].toString()
                Toast.makeText(activity, uid, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getData() {
        // getting user input from the text fields
        val textFieldCourseName = binding.root.findViewById<EditText>(R.id.et_course_name)
        val textFieldCourseGrade = binding.root.findViewById<EditText>(R.id.et_course_grade)
        val textFieldCourseYear = binding.root.findViewById<EditText>(R.id.et_course_year)
        val courseName = textFieldCourseName.text.toString()
        val courseGrade = textFieldCourseGrade.text.toString()
        val courseYear = textFieldCourseYear.text.toString()

        if (courseName != "" && courseGrade != "" && courseYear != ""){
            val teacher = User.getName()
            addClassroom(DBObject.capitalize(courseName), courseGrade, teacher, courseYear.toInt())
        }


    }

    // add a new classroom to the database
    private fun addClassroom(courseName: String, grade: String, teacher: String, year: Int) {
        val db = FirebaseFirestore.getInstance()
        // create data entry for the course
        val emptyList: List<String> = emptyList()
        //val userID = findUserByName(capitalize(teacher))

        val course = hashMapOf(
                "name" to "$grade grade $courseName $year",
                "course name" to courseName,
                "grade" to grade,
                "teacher name" to DBObject.capitalize(teacher),
                "year" to year,
                "students" to emptyList,
                "announcements" to emptyList,
                "modules" to emptyList
        )
        // add the data into the database
        db.collection("classrooms").document("$grade grade $courseName $year")
                .set(course)
                .addOnSuccessListener { Log.d(successTAG, "Successfully added classroom to DB")
                    Toast.makeText(activity, "Classroom successfully created", Toast.LENGTH_LONG).show()

                    db.collection("users")
                            .document(userID)
                            .update("courses", FieldValue.arrayUnion("$grade grade $courseName $year"))
                    findNavController().navigateUp()}
                .addOnFailureListener { e -> Log.w(failTAG, "Error adding classroom to DB", e)
                    Toast.makeText(activity, "Classroom creation error", Toast.LENGTH_LONG).show()}
        }
    }



