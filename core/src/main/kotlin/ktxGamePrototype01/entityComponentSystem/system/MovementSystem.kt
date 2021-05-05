package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktxGamePrototype01.entityComponentSystem.components.*


private const val updateRate = 1f / 30f

// The system controls the position translation of entities
class MovementSystem : IteratingSystem(allOf(TransformComponent::class, MovementComponent::class).exclude(NukePooledComponent::class).get()) {

    override fun update(deltaTime: Float) {
        super.update(updateRate)
    }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {"Error: 5007. entity=$entity"}
        val movement = entity[MovementComponent.mapper]
        require(movement != null) {"Error: 5008. entity=$entity"}

        val player = entity[PlayerComponent.mapper]
        if (player != null){
            entity[OrientationComponent.mapper]?.let { direction -> movePlayerEntity(transform, movement,
                    direction.tempDir, direction, deltaTime)}
        }
        else {
            moveEntity(transform, movement, deltaTime)
        }
    }


    private fun movePlayerEntity(transform: TransformComponent, movement: MovementComponent, mvmt: Vector2,
                                 direction: OrientationComponent, deltaTime: Float){

 /*       movement.velocity.x = when(direction.direction){                                    // Horizontal movement velocity
            OrientationDirection.left -> min(0f, movement.velocity.x - 15f * deltaTime)
            OrientationDirection.right -> max(0f, movement.velocity.x + 15f * deltaTime)
            else -> 0f
        }
        movement.velocity.x = MathUtils.clamp(movement.velocity.x, -6f, 6f)
        movement.velocity.y = when(direction.direction){
            OrientationDirection.down -> min(0f, movement.velocity.y - 15f * deltaTime)
            OrientationDirection.up -> max(0f, movement.velocity.y + 15f * deltaTime)
            else -> 0f
        }
        movement.velocity.y = MathUtils.clamp(movement.velocity.y, -6f, 6f)
*/
        movement.velocity.x = mvmt.x*0.009f
        movement.velocity.y = mvmt.y*0.009f

        moveEntity(transform, movement, deltaTime)
    }


    private fun moveEntity(transform: TransformComponent, movement: MovementComponent, deltaTime: Float) {
        transform.posVec3.x = MathUtils.clamp(transform.posVec3.x + movement.velocity.x * deltaTime, -100f, 100f - transform.sizeVec2.x)    // 0f, 9f
        transform.posVec3.y = MathUtils.clamp(transform.posVec3.y + movement.velocity.y * deltaTime, -100f, 100f - transform.sizeVec2.y)    // 0f, 16f
    }
}