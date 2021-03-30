package ktxGamePrototype01.fragments

import android.os.Bundle
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
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user.view.*
import ktxGamePrototype01.*

class UserFragment : Fragment() {

    private lateinit var binding: FragmentUserBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // To test db functionality (Adding classrooms and adding students to classrooms)
        val test = DBTest()

        // display bottom navigation menu bar
        (activity as AppActivity?)!!.showMenu()
        // display user data
        showUserData()

        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        val buttonProfile = binding.root.findViewById<Button>(R.id.btn_profile)

        buttonProfile.setOnClickListener() {
            findNavController().navigate(R.id.dest_user_profile)
        }

        // log off button and listener
        val buttonLogout = binding.root.findViewById<Button>(R.id.btn_logout)
        buttonLogout.setOnClickListener() {
            auth.signOut()
            (activity as AppActivity?)!!.hideMenu()
            findNavController().navigate(R.id.dest_start)
        }

        /*
        // button to navigate to createClassRoom fragment
        /*
        val buttonCreateClassroom = binding.root.findViewById<Button>(R.id.btn_classroom)
        buttonCreateClassroom.setOnClickListener() {
            if (User.checkForTeacher()) {
                findNavController().navigate(R.id.dest_create_classroom)
            } else Toast.makeText(activity, "Access denied", Toast.LENGTH_LONG).show()

        } */

         */

        return binding.root
    }

    // function to cast user data from the user object to the on-screen elements
    private fun showUserData() {
        // getting the reference to the textViews
        val userName = binding.root.findViewById<TextView>(R.id.user_name)
        val userEmail = binding.root.findViewById<TextView>(R.id.user_email)
        // displaying the data in the textViews
        userName.text = AppActivity().userObject.getName()
        userEmail.text = AppActivity().userObject.getEmail()
    }

    // OLD NOT IN USE function that retrieves data from user database and displays it
    private fun readData(userID: String) {
        // making the query
        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val name = task.result?.get("name")
                val email = task.result?.get("email")
                val score = task.result?.get("score")
                // getting the reference to the textViews
                val userName = binding.root.findViewById<TextView>(R.id.user_name)
                val userEmail = binding.root.findViewById<TextView>(R.id.user_email)
                val userScore = binding.root.findViewById<TextView>(R.id.user_score)
                // displaying the data in the textViews
                userName.text = name.toString()
                userEmail.text = email.toString()
                userScore.text = score.toString()
            }
        }
    }

    fun getName(name: String): String {
        return name
    }



}