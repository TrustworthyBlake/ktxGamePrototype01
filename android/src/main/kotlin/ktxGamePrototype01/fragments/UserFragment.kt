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
import ktxGamePrototype01.AndroidLauncher
import ktxGamePrototype01.GameActivity

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

        // get current user id
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        readData(userID)

        val buttonLogout = binding.root.findViewById<Button>(R.id.btn_logout)

        buttonLogout.setOnClickListener() {
            auth.signOut()
            (activity as GameActivity?)!!.hideMenu()
            findNavController().navigate(R.id.dest_start)
        }

        return binding.root
    }

    // function that retrieves data from user database and displays it
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

    // old function for getting data from FireStore
    private fun getData(userID: String) {
        db.collection("users")
                .get()
                .addOnCompleteListener() {
                    var name = ""
                    var email = ""
                    var score = -1
                    if (it.isSuccessful) {
                        for (document in it.result!!) {
                            if (document.data.getValue("userid").toString() == userID) {
                                name = document.data.getValue("name").toString()
                                email = document.data.getValue("email").toString()
                                score = document.data.getValue("score").toString().toInt()
                            }
                        }
                    }
                    val userName = binding.root.findViewById<TextView>(R.id.user_name)
                    val userEmail = binding.root.findViewById<TextView>(R.id.user_email)
                    val userScore = binding.root.findViewById<TextView>(R.id.user_score)

                    userName.text = name
                    userEmail.text = email
                    userScore.text = score.toString()
                }
    }

}