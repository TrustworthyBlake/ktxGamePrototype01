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
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.codinginflow.recyclerviewexample.ListAdapter
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.FragmentUserProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_user.*
import kotlinx.android.synthetic.main.fragment_user_profile.*
import ktxGamePrototype01.AppActivity
import ktxGamePrototype01.User
import ktxGamePrototype01.adapters.ListItem
import java.io.File
import java.io.FileOutputStream

class UserProfileFragment : Fragment() {
    private lateinit var binding: FragmentUserProfileBinding
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
        val buttonUserInfo = binding.root.findViewById<Button>(R.id.user_info_button)
        val buttonLogout = binding.root.findViewById<Button>(R.id.log_out_button)
        val buttonEdit = binding.root.findViewById<Button>(R.id.btn_edit_prof)

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

        for (item in list) {
            getQuizFromDatabase(item)
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


    private fun getQuizFromDatabase(name: String) {
        val db = FirebaseFirestore.getInstance()

        db.collection("quiz").document(name).get().addOnCompleteListener() { task ->
            if(task.isSuccessful){
                val quizList = task.result?.get("question") as MutableList<String>

                writeQuizToFile(name, quizList)
            }
        }
    }

    private fun writeQuizToFile(quizName: String, quizData: MutableList<String>) {
        val pathInternal = activity?.filesDir
        if (pathInternal != null) {
            val pathTextFile = File(pathInternal, "assets/quizFiles")
            if (!pathTextFile.exists()){
                pathTextFile.mkdirs()
                Toast.makeText(activity, "Creating dir", Toast.LENGTH_SHORT).show()
            }
            val quizTextFile = File(pathTextFile, quizName + ".txt")
            var tempStr = ""
            quizData.forEach { line ->
                tempStr += line + '\n'
            }
            FileOutputStream(quizTextFile).use {
                it.write((tempStr).toByteArray())
            }
            Toast.makeText(activity, "Quiz written to file", Toast.LENGTH_SHORT).show()
        }
    }




}