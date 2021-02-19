package ktxGamePrototype01.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_main.*

class RegisterFragment : Fragment() {
    private lateinit var binding: FragmentRegisterBinding
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_register, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // getting data from the text fields
        val textFieldUsername = binding.root.findViewById<EditText>(R.id.et_register_email)
        val textFieldPassword = binding.root.findViewById<EditText>(R.id.et_register_password)
        val textFieldName = binding.root.findViewById<EditText>(R.id.et_register_name)
        val checkBox = binding.root.findViewById<CheckBox>(R.id.cb_isTeacher)
        val buttonRegister = binding.root.findViewById<Button>(R.id.btn_register)

        buttonRegister.setOnClickListener(){
            val currentUser = auth.currentUser
            if (currentUser != null) {
                // if someone is already signed in, sign them out
                auth.signOut()
            }
            val userEmail = textFieldUsername.text.toString()
            val password = textFieldPassword.text.toString()
            val name = textFieldName.text.toString()
            val isTeacher = checkBox.isChecked
            // logging in using the data entered in the text fields
            if (userEmail != "" && password != "" && name != "") {
                createUser(userEmail, password, name, isTeacher)
                auth.signOut()
            } else {
                Toast.makeText(activity, "Please fill all the fields!", Toast.LENGTH_LONG).show()
            }
        }
        return binding.root
   }     

    private fun createUser(email: String, password: String, name: String, isTeacher: Boolean) {
        auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success
                        createDatabaseEntry(email, name, isTeacher)
                        Toast.makeText(activity,"User successfully created!",Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.dest_start)
                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(activity,"Error registering user!",Toast.LENGTH_SHORT).show()
                        Log.w("Failed to create new user", "Error creating new user")
                    }
                }
    }

    private fun createDatabaseEntry(email: String, name: String, isTeacher: Boolean) {
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        // Create a new user entry in the database
        val user = hashMapOf(
                "userid" to userID,
                "email" to email,
                "name" to name,
                "score" to 0,
                "teacher" to isTeacher
        )
        // add selected data to database
        db.collection("users").document(userID)
                .set(user)
                .addOnSuccessListener { Log.d("Successfully added to DB", "DocumentSnapshot successfully written!") }
                .addOnFailureListener { e -> Log.w("Failed adding to DB", "Error writing document", e) }
    }

}    