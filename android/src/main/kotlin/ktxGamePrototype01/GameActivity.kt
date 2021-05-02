package ktxGamePrototype01

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.databinding.ActivityGameBinding

// Activity for the game
class GameActivity : AndroidApplication() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Which game screen to be set, string
        val showScreen = intent.getStringExtra("showScreen")
        // Username, string
        val playerName = intent.getStringExtra("playerName")
        // Name of quiz, string
        val quizToUse = intent.getStringExtra("quizToUse")
        // Teacher data, string array list
        val teacherDataList = intent.getStringArrayListExtra("teacherDataList")

        // Initializing the Prot01 class containing the game engine and starts the game
        // with the chosen game screen.
        initialize(Prot01(showScreen, playerName, quizToUse, teacherDataList),
                AndroidApplicationConfiguration())
        }
}