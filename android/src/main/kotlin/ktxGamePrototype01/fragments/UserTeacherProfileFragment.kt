package ktxGamePrototype01.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.codinginflow.recyclerviewexample.ListAdapter
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentUserProfileTeacherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile_teacher.*
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.ListItem

class UserTeacherProfileFragment : Fragment(){
    private lateinit var binding: FragmentUserProfileTeacherBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, avedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        // Initialize Firebase Auth
        auth = Firebase.auth

        // get current user id
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        readData(userID)

        val buttonSettings = binding.root.findViewById<Button>(R.id.settings_button)
        val buttonUserInfo = binding.root.findViewById<Button>(R.id.user_info_buttonT)
        val buttonLogout = binding.root.findViewById<Button>(R.id.log_out_button)
        val buttonEdit = binding.root.findViewById<Button>(R.id.btn_edit_profT)

        buttonSettings.setOnClickListener(){
            findNavController().navigate(R.id.dest_settings)
        }

        buttonUserInfo.setOnClickListener(){
            findNavController().navigate(R.id.dest_user)
        }

        buttonLogout.setOnClickListener(){
            auth.signOut()
            Toast.makeText(activity,"Logged out!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.dest_login)
        }

        buttonEdit.setOnClickListener(){
            findNavController().navigate(R.id.dest_edit_profile)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userList : List<String> = User.getAchievement()

        val daList = makeDaList(userList.size)
        recycler_view_user_profileT.adapter = ListAdapter(daList)
        recycler_view_user_profileT.layoutManager = LinearLayoutManager(requireContext())
        recycler_view_user_profileT.setHasFixedSize(true)
    }

    private fun readData(userID: String) {
        // making the query
        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                //val score = task.result?.get("score")
                // getting the reference to the textViews

                val userScore = binding.root.findViewById<TextView>(R.id.user_score)
                // displaying the data in the textViews

                //userScore.text = score.toString()
            }
        }
    }

    private fun makeDaList(size: Int): List<ListItem> {
        val list = ArrayList<ListItem>()
        val userList : List<String> = User.getAchievement()
        for (i in 0 until size) {
            val drawable = R.drawable.ic_attach_money_black_24dp
            val item = ListItem(drawable, userList[i])
            list += item
        }
        return list
    }

}