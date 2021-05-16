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
import androidx.recyclerview.widget.RecyclerView
import com.codinginflow.recyclerviewexample.ListAdapter
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentUserProfileTeacherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_profile_teacher.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.ListItem
import ktxGamePrototype01.sharedprefs

class UserTeacherProfileFragment : Fragment(){
    private lateinit var binding: FragmentUserProfileTeacherBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var savedDarkData: sharedprefs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, avedInstanceState: Bundle?): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile_teacher, container, false)
        // Initialize Firebase Auth
        auth = Firebase.auth
        savedDarkData = sharedprefs(requireContext() as AppActivity)

        // get current user id
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        readData(userID)

        val buttonUserInfo = binding.root.findViewById<Button>(R.id.user_info_buttonT)
        val buttonLogout = binding.root.findViewById<Button>(R.id.log_out_button)
        val buttonEdit = binding.root.findViewById<Button>(R.id.btn_edit_profT)


        buttonUserInfo.setOnClickListener(){
            findNavController().navigate(R.id.dest_user_teacher)
        }

        buttonLogout.setOnClickListener(){
            auth.signOut()
            Toast.makeText(activity,"Logged out!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.dest_login)
        }

        buttonEdit.setOnClickListener(){
            findNavController().navigate(R.id.dest_edit_profile)
        }

        val leList = binding.root.findViewById<RecyclerView>(R.id.recycler_view_user_profileT)

        if(savedDarkData.loadRedModeState() == true){
            leList.setBackgroundColor(resources.getColor(R.color.redfade))
        }else if(savedDarkData.loadGreenModeState() == true){
            leList.setBackgroundColor(resources.getColor(R.color.greenfade))
        }else if(savedDarkData.loadOrangeModeState() == true){
            leList.setBackgroundColor(resources.getColor(R.color.orangefade))
        }else if(savedDarkData.loadPurpleModeState() == true){
            leList.setBackgroundColor(resources.getColor(R.color.purplefade))
        }else{
            leList.setBackgroundColor(resources.getColor(R.color.bluefade))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userList : List<String> = User.getCourses()

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
                val name = task.result?.get("name")
                // getting the reference to the textViews

                val userName = binding.root.findViewById<TextView>(R.id.user_name)
                // displaying the data in the textViews
                userName.text = name.toString()
                //userScore.text = score.toString()
            }
        }
    }

    private fun makeDaList(size: Int): List<ListItem> {
        val list = ArrayList<ListItem>()
        val userList : List<String> = User.getAchievement()
        for (i in 0 until size-1) {
            val drawable = R.drawable.ic_attach_money_black_24dp
            val item = ListItem(drawable, userList[i])
            list += item
        }
        return list
    }

}