package ktxGamePrototype01.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.badlogic.gdx.Game
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*
import ktxGamePrototype01.AndroidLauncher
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.DBObject

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // getting data from the text fields
        val textFieldUsername = binding.root.findViewById<EditText>(R.id.et_login_email)
        val textFieldPassword = binding.root.findViewById<EditText>(R.id.et_login_password)
        val buttonLogin = binding.root.findViewById<Button>(R.id.loginButton)


        buttonLogin.setOnClickListener() {
            val userEmail = textFieldUsername.text.toString()
            val password = textFieldPassword.text.toString()
            // logging in using the data entered in the text fields
            if (userEmail != "" && password != "") {
                login(userEmail, password)
            } else {
                Toast.makeText(activity, "Username or password can't be empty!", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    // function to login user using input values from user
    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                ) { task ->
                    if (task.isSuccessful) {   // Sign in success
                        val userID = FirebaseAuth.getInstance().currentUser!!.uid  // get current user id
                        DBObject.getUserData(userID)
                        // check if user data has been loaded, and if user is teacher or student
                        checkTeacherDB(userID)
                    } else {
                        loginError()
                    }
                }
    }

    // TODO Fix so you can move these four functions into db object, so we don't need to have identical functions in main-, register- and loginFragment
    // checking userObject if user is teacher NOT WORKING AS INTENDED - HAS TO PRESS LOGIN BUTTON TWICE
    fun checkTeacherObject() {
        if (AppActivity().userObject.isUSerLoaded()) {
            if (AppActivity().userObject.checkForTeacher()) {
                logInAsTeacher() // sign in as teacher
            } else logInAsStudent() // sign in as student
        } else Log.w("Failed to read database", "Error checking specified user in database") // database read fail
    }

    // checking db if the user is a teacher
    private fun checkTeacherDB(userID: String) {
        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val res = task.result?.get("teacher")
                // check if user logging in is teacher or student
                if (res as Boolean) {
                    logInAsTeacher() // sign in as teacher
                } else logInAsStudent() // sign in as student
            } else {
                // database read fail
                Log.w("Failed to read database", "Error checking specified user in database")
            }
        }
    }

    // log in as teacher, go to teacher page
    private fun logInAsTeacher() {
        Toast.makeText(activity, "Logged in as teacher!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.dest_teacher_profile)
    }

    // log in as student, go to student page
    private fun logInAsStudent() {
        Toast.makeText(activity, "Logged in as student!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.dest_user_profile)
    }

    // error displaying if logon fail
    private fun loginError() {
        // If sign in fails, display a message to the user.
        Toast.makeText(activity,"Login failed!",Toast.LENGTH_SHORT).show()
        Log.w("Failed to log in", "Error logging in to specified user")
    }

    // OLD NOT IN USE function to login user using input values from user
    private fun oldLogin(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                ) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        (activity as AppActivity?)!!.showMenu()
                        Toast.makeText(activity,"Logged in!",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.dest_user_profile)
                        // Sign in success
                        // get current user id
                        val userID = FirebaseAuth.getInstance().currentUser!!.uid
                        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
                            if (task.isSuccessful) {
                                // if query is successful, reads the data and stores in variables
                                val res = task.result?.get("teacher")
                                // check if user logging in is teacher or student
                                if (res as Boolean) {
                                    // Sign in success, update UI with the signed-in teacher's information
                                    Toast.makeText(activity, "Logged in as teacher!", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.dest_teacher_profile)
                                } else {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(activity, "Logged in as student!", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.dest_user_profile)
                                }
                            } else {
                                // database read fail
                                Log.w("Failed to read database", "Error checking specified user in database")
                            }
                        }
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity,"Login failed!",Toast.LENGTH_SHORT).show()
                        Log.w("Failed to log in", "Error logging in to specified user")
                    }
                }
    }

}