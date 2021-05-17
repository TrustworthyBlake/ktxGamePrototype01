package ktxGamePrototype01

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestore.getInstance
import java.io.File
import java.io.FileOutputStream

object DBObject {

    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
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
                    task.result?.get("head").toString(),
                    task.result?.get("body").toString(),
                    task.result?.get("courses") as List<String>,
                    task.result?.get("achievement") as List<String>
                )
                getTeachersFromCourses(task.result?.get("courses") as List<String>)
                getQuizesFromCourses(task.result?.get("courses") as List<String>)
                getTeachersForQuizzes(task.result?.get("courses") as List<String>)
            }
        }
    }


    // creating a database entry for a new user
    fun createDatabaseEntry(email: String, name: String, isTeacher: Boolean, userID: String) {
        val db = getInstance()

        val courseList: List<String> = emptyList()
        val achievList: List<String> = emptyList()
        val head = "head1"
        val body = "body1"
        // Create a new user entry in the database
        val user = hashMapOf(
            "userid" to userID,
            "email" to email,
            "name" to capitalize(name),
            "score" to 0,
            "teacher" to isTeacher,
            "courses" to courseList,
            "achievement" to achievList,
            "head" to head,
            "body" to body
        )
        // add selected data to database
        db.collection("users").document(userID)
            .set(user)
            .addOnSuccessListener { Log.d(successTAG, "Successfully added user to DB") }
            .addOnFailureListener { e -> Log.w(failTAG, "Error adding user to DB", e) }
    }

    // adding new module to firestore
    fun newModule(className: String, moduleName: String) {
        val db = getInstance()

        val emptyList: List<String> = emptyList()
        val module = hashMapOf(
                "quizes" to emptyList,
                "classroom" to className,
                "name" to moduleName
        )
        // add selected data to database
        db.collection("modules").document(moduleName)
                .set(module)
                .addOnSuccessListener {
                    // add module to classroom
                    db.collection("classrooms")
                            .document(className)
                            .update("modules", FieldValue.arrayUnion(moduleName))
                    Log.d(successTAG, "Successfully added user to DB") }
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

    fun newAnnouncement(className: String, message: String) {
        val db = getInstance()

        db.collection("classrooms")
                .document(className)
                .update("announcements", FieldValue.arrayUnion(message))
                .addOnFailureListener { e -> Log.w(failTAG, "Error adding message to Firestore", e) }
    }

    // find a user by the users name
    private fun findTeacherByName(name: String) {
        val db = FirebaseFirestore.getInstance()
        val doc = db.collection("users").whereEqualTo("name", name).get()
        doc.addOnSuccessListener { documents ->
            for (document in documents) {
                val head = document["head"].toString()
                val body = document["body"].toString()
                User.setTeacherAvatars(name, head, body)
            }
        }
    }

    // find a user by the users name
    private fun findStudentByName(name: String) {
        val db = FirebaseFirestore.getInstance()
        val doc = db.collection("users").whereEqualTo("name", name).get()
        doc.addOnSuccessListener { documents ->
            for (document in documents) {
                val uid = document["userid"].toString()
                // do something with given user
            }
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

    // getting all teachers from courses user is in
    private fun getTeachersFromCourses(list: List<String>) {

        var teacherList: List<String> = emptyList()

        for(course in list) {

            db.collection("classrooms").document(course).get().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val teacher = task.result?.get("teacher name").toString()
                    teacherList = teacherList+teacher
                    findTeacherByName(teacher)
                }
                setTeachers(teacherList)
            }
        }
    }

    private fun setTeachers(list: List<String>){
        User.setTeachers(list)
    }

    // getting all quizes from all the courses user is in
    private fun getQuizesFromCourses(list: List<String>) {

        var quizList: List<String> = emptyList()

        for(course in list) {
            db.collection("classrooms").document(course).get().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val quizes = task.result?.get("quizes") as? List<String>
                    if(quizes != null) {
                        for (quiz in quizes) {
                            quizList = quizList + quiz
                        }
                    }

                }
                setQuizes(quizList)

            }
        }
    }

    private fun setQuizes(list: List<String>){
        User.setQuizes(list)
    }

    private fun setQuizTeachers(list: List<String>){
        User.setTeacherForQuizzes(list)
    }



    fun addAchievement(userID: String, achievement: String) {
        val db = getInstance()
        db.collection("users")
            .document(userID)
            .update("achievement", FieldValue.arrayUnion(achievement))
            .addOnFailureListener { e -> Log.w(failTAG, "Error adding user to DB", e) }
    }


    fun updateUser(userId: String, userName: String, userEmail: String, userScore: Int, playerHead: String, playerBody: String) {
        val db = getInstance()

        val docRef = db.collection("users").document(userId)

        docRef.update("name", userName).addOnFailureListener { e -> Log.w(failTAG, "Error updating user", e) }
        docRef.update("email", userEmail).addOnFailureListener { e -> Log.w(failTAG, "Error updating user", e) }
        docRef.update("score", userScore).addOnFailureListener { e -> Log.w(failTAG, "Error updating user", e) }
        docRef.update("head", playerHead).addOnFailureListener { e -> Log.w(failTAG, "Error updating user", e) }
        docRef.update("body", playerBody).addOnFailureListener { e -> Log.w(failTAG, "Error updating user", e) }

    }



    //      Gets a list of teacher for each quiz
    private fun getTeachersForQuizzes(list: List<String>) {

        var teacherQuizList: List<String> = emptyList()

        for(course in list) {
            db.collection("classrooms").document(course).get().addOnCompleteListener() { task ->
                if (task.isSuccessful) {
                    val quizes = task.result?.get("quizes") as? List<String>
                    val teacher = task.result?.get("teacher name").toString()
                    if(quizes != null) {
                        for (quiz in quizes) {
                            teacherQuizList = teacherQuizList + teacher
                        }
                    }
                }
                setQuizTeachers(teacherQuizList)

            }
        }
    }
}