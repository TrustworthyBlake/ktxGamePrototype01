package ktxGamePrototype01.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.codinginflow.recyclerviewexample.ListAdapter
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentUserBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user.view.*
import ktxGamePrototype01.*
import ktxGamePrototype01.adapters.ListItem

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

        // TODO REMOVE BEFORE LAUNCH
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


        val headImage = binding.root.findViewById<ImageView>(R.id.imageHead)
        val bodyImage = binding.root.findViewById<ImageView>(R.id.imageBody)

        val daImage1 : String = getHead(User.getName())
        when (daImage1){
            "default1" ->  {headImage.setImageResource(R.drawable.default1); }
            "default2" ->  {headImage.setImageResource(R.drawable.default2);  }
            "ebin" ->  {headImage.setImageResource(R.drawable.ebin);  }
            "gond" ->  {headImage.setImageResource(R.drawable.gond);  }
        }

        val daImage2 : String = getBody(User.getName())
        when (daImage2){
            "default1" ->  {bodyImage.setImageResource(R.drawable.default1); }
            "default2" ->  {bodyImage.setImageResource(R.drawable.default2);  }
            "ebin" ->  {bodyImage.setImageResource(R.drawable.ebin);  }
            "gond" ->  {bodyImage.setImageResource(R.drawable.gond);  }
        }

        /*

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

         */

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userList : List<String> = User.getCourses()

        val daList = makeDaList(userList.size)
        recycler_view_user.adapter = ListAdapter(daList)
        recycler_view_user.layoutManager = LinearLayoutManager(requireContext())
        recycler_view_user.setHasFixedSize(true)
    }

    // function to cast user data from the user object to the on-screen elements
    private fun showUserData() {
        // getting the reference to the textViews
        val userName = binding.root.findViewById<TextView>(R.id.user_name)
        val userEmail = binding.root.findViewById<TextView>(R.id.user_email)
        // displaying the data in the textViews
        userName.text = AppActivity().userObject.getName()
        userEmail.text = AppActivity().userObject.getEmail()
        //userScore.text = AppActivity().userObject.getScore().toString()

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

    private fun makeDaList(size: Int): List<ListItem> {
        val list = ArrayList<ListItem>()
        val userList : List<String> = User.getAchievement()
        for (i in 0 until size+1) {
            val drawable = R.drawable.ic_attach_money_black_24dp
            val item = ListItem(drawable, userList[i])
            list += item
        }
        return list
    }





}