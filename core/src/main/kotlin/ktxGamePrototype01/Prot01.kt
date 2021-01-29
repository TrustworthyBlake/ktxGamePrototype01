package ktxGamePrototype01

import com.badlogic.gdx.Game
import ktx.app.KtxGame
import ktx.app.KtxScreen

class Prot01 : KtxGame<KtxScreen>() {
    override fun create() {
        addScreen(FirstScreen())
        setScreen<FirstScreen>()
    }
}