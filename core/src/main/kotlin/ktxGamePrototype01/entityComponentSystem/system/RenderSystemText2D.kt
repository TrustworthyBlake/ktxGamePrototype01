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
import ktx.log.debug
import ktx.log.error
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.PlayerComponent
import ktxGamePrototype01.entityComponentSystem.components.TextComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent

private val LOG = logger<RenderSystemText2D>()
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
    var x = 0f
    var y = 0f

    override fun update(deltaTime: Float) {
        vp.update(Gdx.graphics.width,Gdx.graphics.height,false)
        batchText.use(vp.camera.combined){
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textComp = entity[TextComponent.mapper]
        require(textComp!= null){"Error 5000: entity=$entity"}
        val player = entity[PlayerComponent.mapper]
        if(player != null) {
            val transComp = entity[TransformComponent.mapper]
            require(transComp != null){"Error: Transform comp null Text2D"}
            transComp?.let { trans ->
                x = trans.posVec3.x
                y = trans.posVec3.y
            }
            vp.camera.position.x = x
            vp.camera.position.y = y
        }
        when {
            textComp.isText && !textComp.drawPlayScoreHUD-> {
                textComp.font.draw(batchText, textComp.textStr, (textComp.posTextVec2.x * 120f)
                        - (x * 120f), (textComp.posTextVec2.y * 120f) - (y * 120f))
                LOG.debug { "pos text: x = $x, y = $y" }
            }
            textComp.isText && textComp.drawPlayScoreHUD && player != null ->{
                    textComp.font.draw(batchText, "Score: "+player.playerScore.toInt().toString(),
                            -540f + x, 960f + y)
                LOG.debug { "pos text score: x = $x, y = $y" }
                }
            else ->
                LOG.error { "Error: 5010. entity=$entity" }
            }
    }
}