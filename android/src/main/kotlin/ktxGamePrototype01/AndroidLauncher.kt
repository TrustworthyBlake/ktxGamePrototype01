package ktxGamePrototype01

import android.content.Intent
import com.badlogic.gdx.backends.android.AndroidApplication
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.R
import com.github.trustworthyblake.ktxGamePrototype01.databinding.ActivityMainBinding
import ktxGamePrototype01.Prot01

/** Launches the Android application.  */
class AndroidLauncher : AndroidApplication() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var savedDarkData: sharedprefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialize(null, AndroidApplicationConfiguration())         // Needed to initialise "Core" contents
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val intent = Intent(this, AppActivity::class.java)
        startActivity(intent)
        }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==2){
            initialize(Prot01(), AndroidApplicationConfiguration())
        }
    }
}

