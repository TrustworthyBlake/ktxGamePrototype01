package ktxGamePrototype01.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
    private lateinit var savedDarkData: sharedprefs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user, container, false)

        // Initialize Firebase Auth
        auth = Firebase.auth


        // TODO REMOVE BEFORE LAUNCH
        // To test db functionality (Adding classrooms and adding students to classrooms)
        val test = DBTest()
        val className = "2nd grade English 2021"
        getClassroomData(className)


        // display user data
        showUserData()

        val userID = FirebaseAuth.getInstance().currentUser!!.uid

        val buttonProfile = binding.root.findViewById<Button>(R.id.btn_profile)

        //loads the theme
        loadTheme()
        //loads the avatar image
        loadAvatarImage()


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
        if (!daList.isNullOrEmpty()) {
            recycler_view_user.adapter = ListAdapter(daList)
            recycler_view_user.layoutManager = LinearLayoutManager(requireContext())
            recycler_view_user.setHasFixedSize(true)
        }
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
        if(TextUtils.isEmpty(headPog)){return "head1"}
        else
            return headPog
    }

    private fun getBody(userName : String): String {
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
        val bodyPog : String = prefs.getString("avatarBody")
        if(TextUtils.isEmpty(bodyPog)){return "body1"}
        else
            return bodyPog
    }

    private fun makeDaList(size: Int): List<ListItem> {
        val list = ArrayList<ListItem>()
        val userList : List<String> = User.getCourses()
        if (!userList.isNullOrEmpty()){
            for (i in 0 until size) {
                val drawable = R.drawable.baseline_school_black_24
                val item = ListItem(drawable, userList[i])
                list += item
            }
        }
        return list
    }

    // getting all data from a user and storing it in a user object
    fun getClassroomData(className: String) {
        val db = FirebaseFirestore.getInstance()
        db.collection("classrooms").document(className).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val quizList = task.result?.get("quizes") as List<String>?
                val studentList = task.result?.get("students") as List<String>?
                val teacherName = task.result?.get("teacher name").toString()
                val announcementsList = task.result?.get("announcements") as List<String>?

                // TODO do something with data here

            }
        }
    }

    private fun setUserScore(userName : String, score : Int){
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + userName)
        prefs.putInteger("playerScore", User.getScore() + score)
        prefs.flush()
    }

    private fun loadTheme(){
        savedDarkData = sharedprefs(requireContext() as AppActivity)
        val textName = binding.root.findViewById<TextView>(R.id.user_name)
        val textEmail = binding.root.findViewById<TextView>(R.id.user_email)
        val leList = binding.root.findViewById<RecyclerView>(R.id.recycler_view_user)

        val themeChosen = savedDarkData.loadThemeColour()

        when (themeChosen){
            "Red" ->  {textName.setBackgroundResource(R.drawable.textview_borderred)
                textEmail.setBackgroundResource(R.drawable.textview_borderred)
                leList.setBackgroundColor(resources.getColor(R.color.redfade))}
            "Green" ->  {textName.setBackgroundResource(R.drawable.textview_bordergreen)
                textEmail.setBackgroundResource(R.drawable.textview_bordergreen)
                leList.setBackgroundColor(resources.getColor(R.color.greenfade))}
            "Purple" ->  {textName.setBackgroundResource(R.drawable.textview_borderpurple)
                textEmail.setBackgroundResource(R.drawable.textview_borderpurple)
                leList.setBackgroundColor(resources.getColor(R.color.purplefade))}
            "Orange" ->  {textName.setBackgroundResource(R.drawable.textview_borderorange)
                textEmail.setBackgroundResource(R.drawable.textview_borderorange)
                leList.setBackgroundColor(resources.getColor(R.color.orangefade))}
            else -> {textName.setBackgroundResource(R.drawable.textview_borderblue)
                textEmail.setBackgroundResource(R.drawable.textview_borderblue)
                leList.setBackgroundColor(resources.getColor(R.color.bluefade))}
        }
    }

    private fun loadAvatarImage(){
        val headImage = binding.root.findViewById<ImageView>(R.id.imageHead)
        val bodyImage = binding.root.findViewById<ImageView>(R.id.imageBody)

        val daImage1 : String = getHead(User.getName())
        when (daImage1){
            "head1" ->  {headImage.setImageResource(R.drawable.head1);}
            "head2" ->  {headImage.setImageResource(R.drawable.head2);}
            "head3" ->  {headImage.setImageResource(R.drawable.head3);}
            "head4" ->  {headImage.setImageResource(R.drawable.head4);}
        }

        val daImage2 : String = getBody(User.getName())
        when (daImage2){
            "body1" -> {bodyImage.setImageResource(R.drawable.body1);}
            "body2" -> {bodyImage.setImageResource(R.drawable.body2);}
            "body3" -> {bodyImage.setImageResource(R.drawable.body3);}
            "body4" -> {bodyImage.setImageResource(R.drawable.body4);}
            "bodyA" -> {bodyImage.setImageResource(R.drawable.bodya);}
            "bodyB" -> {bodyImage.setImageResource(R.drawable.bodyb);}
        }

    }

}