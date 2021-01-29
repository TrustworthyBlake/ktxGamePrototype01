package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import ktx.app.KtxScreen
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01

private val LOG = logger<SecondScreen>()

class SecondScreen(game:Prot01) : AbstractScreen(game) {
    override fun show() {
        LOG.debug { "Second screen is displayed" }
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)){
            game.setScreen<FirstScreen>()
        }
    }
}