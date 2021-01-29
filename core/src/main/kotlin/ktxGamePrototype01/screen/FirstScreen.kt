package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01

/** First screen of the application. Displayed after the application is created.  */

private val LOG = logger<FirstScreen>()

class FirstScreen(game:Prot01) : AbstractScreen(game) {
    override fun show() {
        LOG.debug { "First screen is displayed" }
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
           game.setScreen<SecondScreen>()
        }
    }
}