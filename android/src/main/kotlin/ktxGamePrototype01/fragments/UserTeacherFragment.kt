package ktxGamePrototype01.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.codinginflow.recyclerviewexample.ListAdapter
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentUserTeacherBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.fragment_user_teacher.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.ListItem

class UserTeacherFragment : Fragment() {

    private lateinit var binding: FragmentUserTeacherBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_teacher, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth

        // get current user id
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        readData(userID)

        val buttonProfile = binding.root.findViewById<Button>(R.id.btn_profileT)

        buttonProfile.setOnClickListener() {
            findNavController().navigate(R.id.dest_teacher_profile)
        }

        val buttonLogout = binding.root.findViewById<Button>(R.id.btn_logout)

        buttonLogout.setOnClickListener() {
            auth.signOut()
            (activity as AppActivity?)!!.hideMenu()
            findNavController().navigate(R.id.dest_start)
        }

        val headImage = binding.root.findViewById<ImageView>(R.id.imageHead)
        val bodyImage = binding.root.findViewById<ImageView>(R.id.imageBody)

        val daImage1 : String = getHead(User.getName())
        when (daImage1){
            "colour1" ->  {headImage.setImageResource(R.drawable.head1);}
            "colour2" ->  {headImage.setImageResource(R.drawable.head2);}
            "colour3" ->  {headImage.setImageResource(R.drawable.head3);}
            "colour4" ->  {headImage.setImageResource(R.drawable.head4);}
        }

        val daImage2 : String = getBody(User.getName())
        when (daImage2){
            "colour1" -> {bodyImage.setImageResource(R.drawable.body1);}
            "colour2" -> {bodyImage.setImageResource(R.drawable.body2);}
            "colour3" -> {bodyImage.setImageResource(R.drawable.body3);}
            "colour4" -> {bodyImage.setImageResource(R.drawable.body4);}
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userList : List<String> = User.getAchievement()

        val daList = makeDaList(userList.size)
        recycler_view_userT.adapter = ListAdapter(daList)
        recycler_view_userT.layoutManager = LinearLayoutManager(requireContext())
        recycler_view_userT.setHasFixedSize(true)
    }

    // function that retrieves data from user database and displays it
    private fun readData(userID: String) {
        // making the query
        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val name = task.result?.get("name")
                val email = task.result?.get("email")
                //val score = task.result?.get("score")
                // getting the reference to the textViews
                val userName = binding.root.findViewById<TextView>(R.id.user_name)
                val userEmail = binding.root.findViewById<TextView>(R.id.user_email)
                // val userScore = binding.root.findViewById<TextView>(R.id.user_score)
                // displaying the data in the textViews
                userName.text = name.toString()
                userEmail.text = email.toString()
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

    private fun getHead(userName : String): String {
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
        val headPog : String = prefs.getString("avatarHead")
        return headPog
    }

    private fun getBody(userName : String): String {
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
        val bodyPog : String = prefs.getString("avatarBody")
        return bodyPog
    }

}