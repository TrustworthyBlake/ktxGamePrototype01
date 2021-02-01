package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import ktx.app.KtxScreen
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01

/** First screen of the application. Displayed after the application is created.  */

private val LOG = logger<FirstScreen>()

class FirstScreen(game:Prot01) : AbstractScreen(game) {
    val batch : Batch by lazy { SpriteBatch() }
    private val texture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val sprite = Sprite(texture)

    override fun show() {
        LOG.debug { "First screen is displayed" }
        sprite.setPosition(1f, 1f)
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
           game.setScreen<SecondScreen>()
        }
       batch.use{
            sprite.draw(it)
       }
    }

    override fun dispose() {
        texture.dispose()
        batch.dispose()
    }
}