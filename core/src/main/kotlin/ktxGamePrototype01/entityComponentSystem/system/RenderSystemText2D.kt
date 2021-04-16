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
import ktxGamePrototype01.entityComponentSystem.components.GraphicComponent
import ktxGamePrototype01.entityComponentSystem.components.TextComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent
import ktxGamePrototype01.screen.FirstScreen

private val LOG = logger<RenderSystemText2D>()
class RenderSystemText2D(
        private val batchText: Batch, private var gameViewport: Viewport
) :  SortedIteratingSystem(
        allOf(TextComponent::class).get(),
        compareBy { entity -> entity[TextComponent.mapper] }
){
    private var cam = OrthographicCamera(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    private var vp = FitViewport(cam.viewportWidth,cam.viewportHeight, cam)

    override fun update(deltaTime: Float) {
        vp.update(Gdx.graphics.width,Gdx.graphics.height,false)
        vp.apply()
        batchText.use(vp.camera.combined){
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textComp = entity[TextComponent.mapper]
        require(textComp!= null){"Error 5000: entity=$entity"}
        if (textComp.isText){
            textComp.font.draw(batchText, textComp.textStr, textComp.posTextVec2.x, textComp.posTextVec2.y)
        }else{LOG.error { "Error: 5010. entity=$entity" }  }
    }

}