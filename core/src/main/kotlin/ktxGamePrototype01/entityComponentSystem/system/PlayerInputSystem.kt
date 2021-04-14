package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.ANY_KEY
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktxGamePrototype01.entityComponentSystem.components.OrientationComponent
import ktxGamePrototype01.entityComponentSystem.components.OrientationDirection
import ktxGamePrototype01.entityComponentSystem.components.PlayerComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent

class PlayerInputSystem(
        private val gameViewport: Viewport
        ) : IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class, OrientationComponent::class).get()) {
    private val tempPosVec = Vector2()
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val orientation = entity[OrientationComponent.mapper]
        require(orientation != null) {"Error: 5004. entity=$entity"}
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {"Error: 5005. entity=$entity"}

        tempPosVec.y = 0f
        tempPosVec.y = Gdx.input.y.toFloat()
        gameViewport.unproject(tempPosVec)

        /*val diffDistX = tempPosVec.x - transform.posVec3.x - transform.sizeVec2.x * 0.5f
        val diffDistY = tempPosVec.y - transform.posVec3.y - transform.sizeVec2.y * 0.5f
        orientation.direction = when {
            diffDistX > 0.2f -> OrientationDirection.right
            diffDistX < -0.2f -> OrientationDirection.left
            diffDistY > 0.2f -> OrientationDirection.up
            diffDistY < -0.2f -> OrientationDirection.down
            else -> OrientationDirection.tempOri

        }*/
        if(Gdx.input.isKeyJustPressed(Input.Keys.A)){
            orientation.direction = OrientationDirection.left
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.D)){
            orientation.direction = OrientationDirection.right
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.W)){
            orientation.direction = OrientationDirection.up
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.S)){
            orientation.direction = OrientationDirection.down
        }
    }
}