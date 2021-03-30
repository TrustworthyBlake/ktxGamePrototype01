package ktxGamePrototype01

import android.os.Bundle
import com.badlogic.gdx.Game
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.databinding.ActivityGameBinding

class GameActivity : AndroidApplication() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var gameID = intent.getIntExtra("Game", 1)
        when (gameID) {
            1 -> {
                initialize(Prot01(), AndroidApplicationConfiguration())
            }
        }
    }

}