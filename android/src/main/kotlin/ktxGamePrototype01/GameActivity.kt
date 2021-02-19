package ktxGamePrototype01

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.ActivityGameBinding

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //binding.toolbar.setupWithNavController(navController, appBarConfig)
        val navController = findNavController(R.id.nav_fragment)
        val appBarConfig = AppBarConfiguration(navController.graph, binding.mainDrawerLayout)

        binding.navigationView.setupWithNavController(navController)
        binding.bottomNav.setupWithNavController(navController)



        /*

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

         */

        //val btn = findViewById<Button>(R.id.btnLogin)
        //btn.setOnClickListener(){ initialize(Prot01(), AndroidApplicationConfiguration()) }
    }

    override fun onSupportNavigateUp() = findNavController(R.id.nav_fragment).navigateUp()

    fun showMenu(){
        binding.bottomNav.visibility = View.VISIBLE
        val menu = binding.navigationView.menu
        menu.findItem(R.id.dest_classroom_index).isVisible = true
        menu.findItem(R.id.dest_profile).isVisible = true
        menu.findItem(R.id.dest_settings).isVisible = true
        myGame()
    }

    fun hideMenu(){
        binding.bottomNav.visibility = View.INVISIBLE
        val menu = binding.navigationView.menu
        menu.findItem(R.id.dest_classroom_index).isVisible = false
        menu.findItem(R.id.dest_profile).isVisible = false
        menu.findItem(R.id.dest_settings).isVisible = false
    }

    fun myGame(){

    }
}
