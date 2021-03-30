package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.math.MathUtils
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktxGamePrototype01.entityComponentSystem.components.*
import kotlin.math.max
import kotlin.math.min


private const val updateRate = 1f / 30f

class MovementSystem : IteratingSystem(allOf(TransformComponent::class, MovementComponent::class).exclude(NukePooledComponent::class).get()) {
    private var accumulator = 0f
    override fun update(deltaTime: Float) {         // Used to prevent unpredictable behavior of game engine, if not used wonky logic can occur during a rendered frame
        accumulator += deltaTime
        while (accumulator >= updateRate){
            accumulator -= updateRate
        }
        super.update(deltaTime)
    }
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transform = entity[TransformComponent.mapper]
        require(transform != null) {"Error: 5007. entity=$entity"}
        val movement = entity[MovementComponent.mapper]
        require(movement != null) {"Error: 5008. entity=$entity"}

        val player = entity[PlayerComponent.mapper]
        if (player != null){
            entity[OrientationComponent.mapper]?.let { direction -> movePlayer(transform, movement, player, direction, deltaTime)}
        }
        else {
            moveEntity(transform, movement, deltaTime)
        }
    }


    private fun movePlayer(transform: TransformComponent, movement: MovementComponent, playerComponent: PlayerComponent, direction: OrientationComponent, deltaTime: Float){
        movement.velocity.x = when(direction.direction){                                    // Horizontal movement velocity
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

        moveEntity(transform, movement, deltaTime)
    }


    private fun moveEntity(transform: TransformComponent, movement: MovementComponent, deltaTime: Float) {
        transform.posVec3.x = MathUtils.clamp(transform.posVec3.x + movement.velocity.x * deltaTime, 0f, 9f - transform.sizeVec2.x)
        transform.posVec3.y = MathUtils.clamp(transform.posVec3.y + movement.velocity.y * deltaTime, 1f, 1f+16f - transform.sizeVec2.y)
    }
}