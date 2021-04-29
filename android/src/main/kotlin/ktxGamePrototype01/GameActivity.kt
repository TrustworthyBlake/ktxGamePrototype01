package ktxGamePrototype01

import android.os.Bundle
import com.badlogic.gdx.backends.android.AndroidApplication
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration
import com.github.trustworthyblake.ktxGamePrototype01.databinding.ActivityGameBinding


class GameActivity : AndroidApplication() {
    private lateinit var binding: ActivityGameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val showScreen = intent.getStringExtra("showScreen")
        val playerName = intent.getStringExtra("playerName")
        val quizToUse = intent.getStringExtra("quizToUse")
        val teacherDataList = intent.getStringArrayListExtra("teacherDataList")
        initialize(Prot01(showScreen, playerName, quizToUse, teacherDataList), AndroidApplicationConfiguration())
        }


}