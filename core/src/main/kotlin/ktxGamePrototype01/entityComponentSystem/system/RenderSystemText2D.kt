package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktxGamePrototype01.entityComponentSystem.components.GraphicComponent
import ktxGamePrototype01.entityComponentSystem.components.TextComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent

class RenderSystemText2D(
        private val batchText: Batch
) :  SortedIteratingSystem(
        allOf(TextComponent::class).get(),
        compareBy { entity -> entity[TextComponent.mapper] }
){
    //private var vp2 = FitViewport(9f, 16f)

    private val camera = OrthographicCamera(1080f, 1920f)

    override fun update(deltaTime: Float) {
        val vp2 = FitViewport(1080f, 1920f, camera)
        vp2.setScreenBounds(0, 0, Gdx.graphics.width, Gdx.graphics.height)
        vp2.apply()
        batchText.use(vp2.camera.combined){
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val textComp = entity[TextComponent.mapper]
        require(textComp!= null){"Error 5000: entity=$entity"}
        if (textComp.isText){
            //gameViewport = FitViewport(1024f, 1024f)
            //gameViewport.update(Gdx.graphics.width,Gdx.graphics.height,true)
            textComp.font.draw(batchText, textComp.textStr, textComp.posTextVec2.x, textComp.posTextVec2.y)
        }
    }

}