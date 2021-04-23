package ktxGamePrototype01.screen

import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.log.logger
import ktxGamePrototype01.Prot01

private val LOG = logger<OpenWorldScreen>()
class OpenWorldScreen(game : Prot01) : AbstractScreen(game) {
    private var viewport = FitViewport(9f, 16f)

    override fun show() {}
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, false)
    }
    override fun render(delta: Float) {
        engine.update(delta)
    }
    override fun dispose() {}


}