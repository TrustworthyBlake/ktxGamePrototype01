package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Input.Keys.ANY_KEY
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.OrientationComponent
import ktxGamePrototype01.entityComponentSystem.components.OrientationDirection
import ktxGamePrototype01.entityComponentSystem.components.PlayerComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent

private val LOG = logger <PlayerInputSystem>()

class PlayerInputSystem(
        private val gameViewport: Viewport
        ) : IteratingSystem(allOf(PlayerComponent::class, TransformComponent::class, OrientationComponent::class).get()) {
    private val tempPosVec = Vector2()
    private var onHoldPosition = Vector2()
    private var onClickPosition = Vector2(0.0f, 0.0f)
    private var finalPositionModifier = Vector2(0.0f, 0.0f)
    private val xx = Vector2()


    override fun processEntity(entity: Entity, deltaTime: Float) {
        val orientation = entity[OrientationComponent.mapper]
        require(orientation != null) {"Error: 5004. entity=$entity"}
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {"Error: 5005. entity=$entity"}



        if(Gdx.input.isTouched) {
            //gameViewport.unproject(tempPosVec)
            //tempPosVec.y = Gdx.input.deltaY.toFloat()
            //tempPosVec.x = Gdx.input.deltaX.toFloat()

            gameViewport.unproject(onHoldPosition)
            onHoldPosition.x = Gdx.input.x.toFloat()
            onHoldPosition.y = Gdx.input.y.toFloat()


            if(Gdx.input.justTouched())  { onClickPosition = Vector2(onHoldPosition.x, onHoldPosition.y) }
            /*
            //      LEGACY MOVEMENT

            if(tempPosVec.x > 3) {
                orientation.direction = OrientationDirection.right

            }
            if(tempPosVec.x < -3) {
                orientation.direction = OrientationDirection.left

            }
            if(tempPosVec.y > 3){
                orientation.direction = OrientationDirection.down

            }
            if (tempPosVec.y < -3){
                orientation.direction = OrientationDirection.up
            }
             */

            val diffX = onHoldPosition.x-onClickPosition.x
            val diffY = onHoldPosition.y-onClickPosition.y
            if(onHoldPosition.x > onClickPosition.x) {
                orientation.direction = OrientationDirection.right
                finalPositionModifier.x = diffX
            }
            if(onHoldPosition.x < onClickPosition.x) {
                orientation.direction = OrientationDirection.left
                finalPositionModifier.x = diffX
            }
            if(onHoldPosition.y > onClickPosition.y){
                orientation.direction = OrientationDirection.down
                finalPositionModifier.y = -diffY
            }
            if (onHoldPosition.y < onClickPosition.y){
                orientation.direction = OrientationDirection.up
                finalPositionModifier.y = -diffY
            }


            //LOG.debug { "pointer pos x.y = $tempPosVec" }
            //LOG.debug { "diffDistX = $diffDistX" }


            orientation.tempDir.x = finalPositionModifier.x
            orientation.tempDir.y = finalPositionModifier.y
        }
        else{
            orientation.direction = OrientationDirection.tempOri
            //onHoldPosition = Vector2(0f, 0f)
            orientation.tempDir = Vector2(0f, 0f)
        }

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