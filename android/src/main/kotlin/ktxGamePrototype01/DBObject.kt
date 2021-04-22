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
import java.util.concurrent.Future

object DBObject {

    private lateinit var auth: FirebaseAuth
    private const val failTAG = "DATABASE ENTRY FAILED"
    private const val successTAG = "DATABASE ENTRY SUCCESS"

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
                    task.result?.get("teacher") as Boolean,  // is teacher or not
                    task.result?.get("courses") as List<String>,
                    task.result?.get("achievement") as List<String>
                )
            }
        }
    }

    // creating a database entry for a new user
    fun createDatabaseEntry(email: String, name: String, isTeacher: Boolean, userID: String) {
        val db = getInstance()

        val courseList: List<String> = emptyList()
        val achievList: List<String> = emptyList()
        // Create a new user entry in the database
        val user = hashMapOf(
            "userid" to userID,
            "email" to email,
            "name" to capitalize(name),
            "score" to 0,
            "teacher" to isTeacher,
            "courses" to courseList,
            "achievement" to achievList
        )
        // add selected data to database
        db.collection("users").document(userID)
            .set(user)
            .addOnSuccessListener { Log.d(successTAG, "Successfully added user to DB") }
            .addOnFailureListener { e -> Log.w(failTAG, "Error adding user to DB", e) }
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
    fun getUserName(userID: String, onComplete: (String) -> Unit) {
        val db = getInstance()
        // returning the query as a string
        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                onComplete(task.result?.get("name").toString())
            }
        }

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

    // capitalize a string
    fun capitalize(s: String): String {
        return s.split(" ").joinToString(" ") { it.toLowerCase().capitalize() }
    }

}