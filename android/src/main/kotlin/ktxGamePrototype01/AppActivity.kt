package ktxGamePrototype01

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.ActivityAppBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private val FIRST_GAME_REQUEST_CODE = 0
    private lateinit var savedDarkData: sharedprefs
    private lateinit var auth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()

    var userObject = User

    override fun onCreate(savedInstanceState: Bundle?) {
        savedDarkData = sharedprefs(this)
        if(savedDarkData.loadDarkModeState() == true){
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else{
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
        //set red theme
        if(savedDarkData.loadRedModeState() == true){
            setTheme(R.style.RedTheme)
        }else if(savedDarkData.loadGreenModeState() == true){
            setTheme(R.style.GreenTheme)
        }else if(savedDarkData.loadOrangeModeState() == true){
            setTheme(R.style.OrangeTheme)
        }else if(savedDarkData.loadPurpleModeState() == true){
            setTheme(R.style.PurpleTheme)
        }else{
            setTheme((R.style.AppTheme))
        }

        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController = findNavController(R.id.nav_fragment)
        val appBarConfig = AppBarConfiguration(navController.graph, binding.mainDrawerLayout)

        binding.navigationView.setupWithNavController(navController)
        binding.bottomNav.setupWithNavController(navController)




        //val btn = findViewById<Button>(R.id.btnLogin)
        //btn.setOnClickListener(){ initialize(Prot01(), AndroidApplicationConfiguration()) }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_fragment).navigateUp()

    fun showMenu(){
        binding.bottomNav.visibility = View.VISIBLE
        val menu = binding.navigationView.menu
        menu.findItem(R.id.dest_classroom_index).isVisible = true
        if(checkTeacher(User.getName())){
            menu.findItem(R.id.dest_teacher_profile).isVisible = true
        }else{menu.findItem(R.id.dest_user_profile).isVisible = true}
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

    fun checkTeacher(name : String) : Boolean{
        var kotlinBS : Boolean = false
        /*
        db.collection("users").document(name).get().addOnCompleteListener() { task ->
            if (task.isSuccessful) {
                // if query is successful, reads the data and stores in variables
                val res = task.result?.get("teacher")
                // check if user logging in is teacher or student
                if (res as Boolean) {
                    kotlinBS
                } else kotlinBS = false
            } else {
                // database read fail
                Log.w("Failed to read database", "Error checking specified user in database")
            }

         */

        return kotlinBS
    }

}
