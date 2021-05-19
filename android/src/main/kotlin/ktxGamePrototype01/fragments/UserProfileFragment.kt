package ktxGamePrototype01.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.codinginflow.recyclerviewexample.ListAdapter
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user_profile.*
import kotlinx.android.synthetic.main.game_entry.view.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.ListItem
import ktxGamePrototype01.sharedprefs
import java.io.File
import java.io.FileOutputStream

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
    private val db = FirebaseFirestore.getInstance()
    private lateinit var auth: FirebaseAuth
    private lateinit var savedDarkData: sharedprefs

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, avedInstanceState: Bundle?): View? {
        savedDarkData = sharedprefs(requireContext() as AppActivity)
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_profile, container, false)
        // Initialize Firebase Auth
        auth = Firebase.auth

        // get current user id
        val userID = FirebaseAuth.getInstance().currentUser!!.uid
        readData(userID)

        val buttonUserInfo = binding.root.findViewById<Button>(R.id.user_info_button)
        val buttonLogout = binding.root.findViewById<Button>(R.id.log_out_button)
        val buttonEdit = binding.root.findViewById<Button>(R.id.btn_edit_prof)
        val levelText = binding.root.findViewById<TextView>(R.id.textView15)
        val scoreview = binding.root.findViewById<TextView>(R.id.user_score)
        val recyList = binding.root.findViewById<RecyclerView>(R.id.recycler_view_user_profile)


        /*
        var testwhen : Boolean = false

        when(testwhen){
            savedDarkData.loadRedModeState() -> levelText.setBackgroundResource(R.drawable.circlered)
            savedDarkData.loadGreenModeState() -> levelText.setBackgroundResource(R.drawable.circleblue)
            else -> levelText.setBackgroundResource(R.drawable.circleblue)
        }
         */

        if(savedDarkData.loadRedModeState() == true){
            levelText.setBackgroundResource(R.drawable.circlered)
            scoreview.setBackgroundResource(R.drawable.textview_borderred)
            recyList.setBackgroundColor(resources.getColor(R.color.redfade))
        }else if(savedDarkData.loadGreenModeState() == true){
            levelText.setBackgroundResource(R.drawable.circlegreen)
            scoreview.setBackgroundResource(R.drawable.textview_bordergreen)
            recyList.setBackgroundColor(resources.getColor(R.color.greenfade))
        }else if(savedDarkData.loadOrangeModeState() == true){
            levelText.setBackgroundResource(R.drawable.circleorange)
            scoreview.setBackgroundResource(R.drawable.textview_borderorange)
            recyList.setBackgroundColor(resources.getColor(R.color.orangefade))
        }else if(savedDarkData.loadPurpleModeState() == true){
            levelText.setBackgroundResource(R.drawable.circlepurple)
            scoreview.setBackgroundResource(R.drawable.textview_borderpurple)
            recyList.setBackgroundColor(resources.getColor(R.color.purplefade))
        }else{
            levelText.setBackgroundResource(R.drawable.circleblue)
            scoreview.setBackgroundResource(R.drawable.textview_borderblue)
            recyList.setBackgroundColor(resources.getColor(R.color.bluefade))
        }


        buttonUserInfo.setOnClickListener(){
            findNavController().navigate(R.id.dest_user)
        }

        buttonLogout.setOnClickListener(){
            auth.signOut()
            (activity as AppActivity?)!!.hideMenu()
            Toast.makeText(activity,"Logged out!", Toast.LENGTH_SHORT).show()
            findNavController().navigate(R.id.dest_login)
        }

        buttonEdit.setOnClickListener(){
            findNavController().navigate(R.id.dest_edit_profile)
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

        // reading in quizes from db
        // TODO Need to refresh fragment for this to work
        var list = User.getQuizes()
        var teacherList = User.getTeacherForQuizzes()

        var counter = 0
        for (item in list) {
            getQuizFromDatabase(item, teacherList[counter])
            counter+=1
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val userList : List<String> = User.getAchievement()

        val daList = makeDaList(userList.size)
        recycler_view_user_profile.adapter = ListAdapter(daList)
        recycler_view_user_profile.layoutManager = LinearLayoutManager(requireContext())
        recycler_view_user_profile.setHasFixedSize(true)
    }


    // function that retrieves data from user database and displays it
    private fun readData(userID: String) {
        // making the query
        db.collection("users").document(userID).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val score = task.result?.get("score")
                // getting the reference to the textViews

                val userScore = binding.root.findViewById<TextView>(R.id.user_score)
                // displaying the data in the textViews

                userScore.text = score.toString()
            }
        }
    }


    private fun makeDaList(size: Int): List<ListItem> {
        val list = ArrayList<ListItem>()
        val userList : List<String> = User.getAchievement()
        for (i in 0 until size) {
            val drawable = R.drawable.medal
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


    private fun getQuizFromDatabase(name: String, tName: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("quiz").document(name).get().addOnCompleteListener() { task ->
            if(task.isSuccessful){
                val quizList = task.result?.get("question") as? MutableList<String>
                if(quizList !=null){
                writeQuizToFile(name, quizList, tName)
                }
            }
        }
    }

    private fun writeQuizToFile(quizName: String, quizData: MutableList<String>, tName: String) {
        val pathInternal = activity?.filesDir
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/quizFiles")
            if (!pathTextFile.exists()){
                pathTextFile.mkdirs()
                Toast.makeText(activity, "Creating dir", Toast.LENGTH_SHORT).show()
            }

            var splitQuizName = quizName!!.split("-")
            var newQuizName = splitQuizName[0] + "-" + tName

            val quizTextFile = File(pathTextFile, newQuizName + ".txt")
            var tempStr = ""
            quizData.forEach { line ->
                tempStr += line + '\n'
            }
            FileOutputStream(quizTextFile).use {
                it.write((tempStr).toByteArray())
            }
            //Toast.makeText(activity, "Quiz written to file", Toast.LENGTH_SHORT).show()
        }
    }




}