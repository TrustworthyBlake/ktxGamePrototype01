package ktxGamePrototype01

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.badlogic.gdx.Game
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.ActivityAppBinding

class AppActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAppBinding
    private val FIRST_GAME_REQUEST_CODE = 0
    private lateinit var savedDarkData: sharedprefs

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
        } else if(savedDarkData.loadPurpleModeState() == true){
            setTheme(R.style.PurpleTheme)
        } else{
            setTheme((R.style.AppTheme))
        }

        super.onCreate(savedInstanceState)
        binding = ActivityAppBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.setupWithNavController(navController, appBarConfig)
        val navController = findNavController(R.id.nav_fragment)
        val appBarConfig = AppBarConfiguration(navController.graph, binding.mainDrawerLayout)

        binding.navigationView.setupWithNavController(navController)
        binding.bottomNav.setupWithNavController(navController)

        showMenu()



        //val btn = findViewById<Button>(R.id.btnLogin)
        //btn.setOnClickListener(){ initialize(Prot01(), AndroidApplicationConfiguration()) }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_fragment).navigateUp()

    fun showMenu(){
        binding.bottomNav.visibility = View.VISIBLE
        val menu = binding.navigationView.menu
        menu.findItem(R.id.dest_classroom_index).isVisible = true
        menu.findItem(R.id.dest_user_profile).isVisible = true
        menu.findItem(R.id.dest_settings).isVisible = true
    }

    fun hideMenu(){
        binding.bottomNav.visibility = View.INVISIBLE
        val menu = binding.navigationView.menu
        menu.findItem(R.id.dest_classroom_index).isVisible = false
        //menu.findItem(R.id.dest_profile).isVisible = false
        menu.findItem(R.id.dest_settings).isVisible = false
    }

    fun launchGame(gameID: Int){
        val intent = Intent(this, GameActivity::class.java)
        intent.putExtra("Game", 1)
        startActivityForResult(intent, FIRST_GAME_REQUEST_CODE)
    }
}
