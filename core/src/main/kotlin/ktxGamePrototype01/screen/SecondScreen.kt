package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.unitScale

private val LOG = logger<SecondScreen>()

class SecondScreen(game:Prot01) : AbstractScreen(game) {
    private val viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val player = engine.entity{
        with<TransformComponent>{
            posVec3.set(5f,5f,0f)
        }
        with<MovementComponent>()
        with<GraphicComponent>{
            sprite.run{
                setRegion(playerTexture)
                setSize(texture.width * unitScale, texture.height * unitScale)
                setOriginCenter()
            }
        }
        with<PlayerComponent>()
        with<OrientationComponent>()
    }

    override fun show() {
        LOG.debug { "Second screen is displayed" }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(50, 50, true)


    }

    override fun render(delta: Float) {
        viewport.apply()
        batch.use(viewport.camera.combined){
        }





        engine.update(delta)
        if(Gdx.input.isKeyJustPressed(Input.Keys.J)){
            dispose()
            game.setScreen<QuizScreen>()
        }
    }

    override fun dispose() {
        playerTexture.dispose()
        player.removeAll()
    }
}