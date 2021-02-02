package ktxGamePrototype01.screen

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.PooledEngine
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.app.KtxScreen
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.entityComponentSystem.components.GraphicComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent
import ktxGamePrototype01.unitScale

/** First screen of the application. Displayed after the application is created.  */

private val LOG = logger<FirstScreen>()

class FirstScreen(game:Prot01) : AbstractScreen(game) {
    private val viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val player = engine.entity{
        with<TransformComponent>{
            posVec3.set(1f,1f,0f)
        }
        with<GraphicComponent>{
            sprite.run{
                setRegion(playerTexture)
                setSize(texture.width * unitScale, texture.height * unitScale)
                setOriginCenter()
            }
        }
    }

    override fun show() {
        LOG.debug { "First screen is displayed" }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply()
        batch.use(viewport.camera.combined){
        }
        engine.update(delta)
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
           game.setScreen<SecondScreen>()
        }
    }

    override fun dispose() {
        playerTexture.dispose()
    }
}