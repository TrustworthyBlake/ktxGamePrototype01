package ktxGamePrototype01

import android.util.Log
import android.widget.Toast
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.getInstance

object DBObject {

    private lateinit var auth: FirebaseAuth

    // getting all data from a user and storing it in a user object
    fun getUserData(userID: String) {
        val db = getInstance()
        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                AppActivity().userObject.setUser(userID,
                    task.result?.get("name").toString(), // name
                    task.result?.get("email").toString(),  // email
                    task.result?.get("score").toString().toInt(),  // score
                    task.result?.get("teacher") as Boolean  // is teacher or not
                )
            }
        }
    }

    // creating a database entry for a new user
    fun createDatabaseEntry(email: String, name: String, isTeacher: Boolean, userID: String) {
        val db = getInstance()

        val courseList: List<String> = emptyList()
        // Create a new user entry in the database
        val user = hashMapOf(
            "userid" to userID,
            "email" to email,
            "name" to capitalize(name),
            "score" to 0,
            "teacher" to isTeacher,
            "courses" to courseList
        )
        // add selected data to database
        db.collection("users").document(userID)
            .set(user)
            .addOnSuccessListener { Log.d("Successfully added to DB", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Failed adding to DB", "Error writing document", e) }
    }

    // add a new classroom to the database
    fun addClassroom(courseName: String, grade: String, teacher: String, year: Int) {
        val db = getInstance()
        // create data entry for the course
        val studentList: List<String> = emptyList()
        //val userID = findUserByName(capitalize(teacher))

        val course = hashMapOf(
            "course name" to courseName,
            "grade" to grade,
            "teacher name" to capitalize(teacher),
            "year" to year,
            "students" to studentList
        )
        // add the data into the database
        db.collection("classrooms").document("$grade grade $courseName $year")
            .set(course)
            .addOnSuccessListener { Log.d("Successfully added to DB", "DocumentSnapshot successfully written!") }
            .addOnFailureListener { e -> Log.w("Failed adding to DB", "Error writing document", e) }
    }

    // function for adding students to a class
    fun addStudents(className: String, studentList: List<String>) {
        val db = getInstance()

        for (item in studentList) {
            db.collection("classrooms")
                .document(className)
                .update("students", FieldValue.arrayUnion(item))

            db.collection("users")
                .document(item)
                .update("courses", FieldValue.arrayUnion(className))
        }
    }

    // set a given users score
    fun setScore(userID: String, newScore: Int) {
        val db = getInstance()
        db.collection("users")
            .document(userID)
            .update("score", newScore)
    }

    // TODO - NOT WORKING
    // function that retrieves data from user database and displays it
    fun getDocSnapshot(userID: String): Task<DocumentSnapshot> {
        val db = getInstance()
        // returning document snapshot

        return db.collection("users").document(userID).get().addOnCompleteListener() {  task->
            if (task.isComplete)
                task.result.toString()
        }
    }

    // TODO - NOT WORKING
    // function that retrieves data from user database and displays it
    fun getUserName(userID: String): String {
        val db = getInstance()
        var name = ""
        // returning the query as a string
        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                name = task.result?.get("name").toString()
            }
        }
        return name
    }

    // TODO - NOT WORKING
    // function that retrieves data from user database and displays it
    fun getUserEmail(userID: String): String {
        val db = getInstance()
        // returning the query as a string

        return db.collection("users").document(userID).get().result!!.get("email").toString()
    }

    // TODO - NOT WORKING
    // function that retrieves data from user database
    fun getUserScore(userID: String): Int {
        val db = getInstance()
        // returning query as int
        return db.collection("users").document(userID).get().result!!.get("score").toString().toInt()
    }

    // TODO - NOT WORKING
    // find a user by the users name
    fun findUserByName(name: String): String{
        val db = getInstance()
        val doc = db.collection("users").whereEqualTo("name", name).get()
        var uid = ""
        doc.addOnSuccessListener { documents ->
            for (document in documents) {
                uid = document["userid"].toString()
            }
        }
        return uid
    }

    // capitalize a string
    private fun capitalize(s: String): String {
        return s.split(" ").joinToString(" ") { it.toLowerCase().capitalize() }
    }

}