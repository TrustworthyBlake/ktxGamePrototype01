package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.error
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.PlayerComponent
import ktxGamePrototype01.entityComponentSystem.components.TextComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent

private val LOG = logger<RenderSystemText2D>()

// The system handles all of the TEXT entities to draw to the screen
class RenderSystemText2D(
        private val batchText: Batch, private var gameViewport: Viewport
) :  SortedIteratingSystem(
        allOf(TextComponent::class).get(),
        compareBy { entity -> entity[TextComponent.mapper] }
){
    private var cam = OrthographicCamera(1080f, 1920f)//(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private var vp = FitViewport(cam.viewportWidth,cam.viewportHeight, cam)
    private val playerEntities by lazy {
        engine.getEntitiesFor(allOf(PlayerComponent::class).get())
    }
    private val viewportSizeMultiplier = 120f

    override fun update(deltaTime: Float) {
        vp.update(Gdx.graphics.width,Gdx.graphics.height,false)
        batchText.use(vp.camera.combined){
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textComp = entity[TextComponent.mapper]
        require(textComp != null) { "Error 5000: entity=$entity" }
        val player = entity[PlayerComponent.mapper]
        playerEntities.forEach { pl ->              // Player
            if (pl != null) {
                val transComp = pl[TransformComponent.mapper]
                require(transComp != null) { "Error: Transform comp null Text2D" }
                transComp?.let { trans ->
                    vp.camera.position.x = trans.posVec3.x
                    vp.camera.position.y = trans.posVec3.y
                    val plC = pl[PlayerComponent.mapper]    // Player component

                    when {
                        // Draws the strings for text entities
                        textComp.isText && !textComp.drawPlayScoreHUD -> {
                            textComp.font.draw(batchText, textComp.textStr,
                                    (textComp.posTextVec2.x * viewportSizeMultiplier)
                                            - (trans.posVec3.x * viewportSizeMultiplier),
                                    (textComp.posTextVec2.y * viewportSizeMultiplier)
                                            - (trans.posVec3.y * viewportSizeMultiplier))
                            //LOG.debug { "pos text: x = $x, y = $y" }
                        }
                        // Draws the player score HUD
                        textComp.isText && textComp.drawPlayScoreHUD && plC != null -> {
                            textComp.font.draw(batchText, "Score: " + plC.playerScore.toInt().toString(),
                                    -540f + trans.posVec3.x, 960f + trans.posVec3.y)
                            //LOG.debug { "pos text score: x = $x, y = $y" }
                        }
                        else ->
                            LOG.error { "Error: 5010. entity=$entity" }
                    }
                }
            }
        }
    }
}