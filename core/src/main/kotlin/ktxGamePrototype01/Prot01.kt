package ktxGamePrototype01

import com.badlogic.gdx.Application.LOG_DEBUG
import com.badlogic.gdx.Gdx
import ktx.app.KtxGame
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.screen.FirstScreen
import ktxGamePrototype01.screen.SecondScreen

private val LOG = logger<Prot01>()
const val unitScale = 1 / 16f

class Prot01 : KtxGame<KtxScreen>() {
    override fun create() {
        Gdx.app.logLevel = LOG_DEBUG
        LOG.debug { "Game instance created" }
        addScreen(FirstScreen(this))
        addScreen(SecondScreen(this))
        setScreen<FirstScreen>()
    }
}