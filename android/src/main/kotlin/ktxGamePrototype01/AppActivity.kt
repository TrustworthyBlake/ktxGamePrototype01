package ktxGamePrototype01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.activityViewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.ActivityAppBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import ktxGamePrototype01.fragments.ClassroomViewModel
import java.lang.Thread.sleep

class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private val FIRST_GAME_REQUEST_CODE = 0
    private lateinit var savedDarkData: sharedprefs
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    var userObject = User

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        savedDarkData = sharedprefs(this)

        //checks if dark mode is enabled
        if(savedDarkData.loadDarkModeState() == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        //opens sharedprefs file and gets a string for what theme is set
        val themeChosen = savedDarkData.loadThemeColour()

        when (themeChosen){
            "Red" ->  {setTheme(R.style.RedTheme)}
            "Green" ->  {setTheme(R.style.GreenTheme)}
            "Purple" ->  {setTheme(R.style.PurpleTheme)}
            "Orange" ->  {setTheme(R.style.OrangeTheme)}
            else -> {setTheme(R.style.AppTheme)}
        }

        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_fragment)
        val appBarConfig = AppBarConfiguration(navController.graph, binding.mainDrawerLayout)

        binding.navigationView.setupWithNavController(navController)
        binding.bottomNav.setupWithNavController(navController)



        val currentUser = auth.currentUser
        if (currentUser != null) {
            val userID = FirebaseAuth.getInstance().currentUser!!.uid  // get current user id
            DBObject.getUserData(userID)
        }
        


        //val btn = findViewById<Button>(R.id.btnLogin)
        //btn.setOnClickListener(){ initialize(Prot01(), AndroidApplicationConfiguration()) }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_fragment).navigateUp()

    fun showMenu(){

        binding.bottomNav.visibility = View.VISIBLE
        val botMenu = binding.bottomNav.menu
        val menu = binding.navigationView.menu
        menu.findItem(R.id.dest_classroom_index).isVisible = true
        if(User.checkForTeacher()){
            menu.findItem(R.id.dest_teacher_profile).isVisible = true
            botMenu.findItem(R.id.dest_teacher_profile).isVisible = true
            menu.findItem(R.id.dest_user_profile).isVisible = false
            botMenu.findItem(R.id.dest_user_profile).isVisible = false
        }else{menu.findItem(R.id.dest_user_profile).isVisible = true
            botMenu.findItem(R.id.dest_user_profile).isVisible = true
            botMenu.findItem(R.id.dest_teacher_profile).isVisible = false
            menu.findItem(R.id.dest_teacher_profile).isVisible = false}
        menu.findItem(R.id.dest_settings).isVisible = true
    }

    fun hideMenu(){
        binding.bottomNav.visibility = View.INVISIBLE
        val menu = binding.navigationView.menu
        menu.findItem(R.id.dest_classroom_index).isVisible = false
        //menu.findItem(R.id.dest_profile).isVisible = false
        menu.findItem(R.id.dest_settings).isVisible = false
    }

    fun launchGame(showScreen: String, playerName: String,
                   quizToUse: String, teacherDataList: List<String>){
        val intent = Intent(this, GameActivity::class.java)
        //intent.putExtra("Game", 1)
        val newArrList = ArrayList<String>()
        for(item in teacherDataList){
            newArrList.add(item)
        }
        intent.putExtra("showScreen", showScreen)
        intent.putExtra("playerName", playerName)
        intent.putExtra("quizToUse", quizToUse)
        intent.putExtra("teacherDataList", newArrList)
        startActivityForResult(intent, FIRST_GAME_REQUEST_CODE)
    }

}
