package ktxGamePrototype01.fragments

import android.database.DatabaseUtils
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController

import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.DBObject

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, avedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false)
        // Initialize Firebase Auth
        auth = Firebase.auth

        val goToLogin = binding.root.findViewById<Button>(R.id.btnLogin)
        val goToRegister = binding.root.findViewById<Button>(R.id.btnRegister)

        goToLogin.setOnClickListener(){
            //val loginFragment = LoginFragment()
            //childFragmentManager.beginTransaction()
            //        .replace(R.id.flFragment, loginFragment)
            //        .addToBackStack(null)
            //        .commit()
            val currentUser = auth.currentUser
            // checking if there is an active user session, if there is user gets sent directly to profile page
            if (currentUser != null) {
                val userID = FirebaseAuth.getInstance().currentUser!!.uid  // get current user id
                DBObject.getUserData(userID)
                // check if user data has been loaded, and if user is teacher or student
                checkTeacherDB(userID)
            } else {
                // else they get sent to login page
                findNavController().navigate(R.id.dest_login)
            }
        }

        goToRegister.setOnClickListener(){
            //val registerFragment = RegisterFragment()
            //childFragmentManager.beginTransaction()
            //        .replace(R.id.flFragment, registerFragment)
            //        .addToBackStack(null)
            //        .commit()
            findNavController().navigate(R.id.dest_register)
        }

        return binding.root
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
        findNavController().navigate(R.id.dest_user_teacher)
    }

    // log in as student, go to student page
    private fun logInAsStudent() {
        Toast.makeText(activity, "Logged in as student!", Toast.LENGTH_SHORT).show()
        findNavController().navigate(R.id.dest_user_profile)
    }


}
