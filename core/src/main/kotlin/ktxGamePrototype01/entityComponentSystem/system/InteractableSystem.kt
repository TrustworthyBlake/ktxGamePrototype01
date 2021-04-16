package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Engine
import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Rectangle
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.screen.FirstScreen
import ktxGamePrototype01.unitScale
import javax.xml.crypto.dsig.Transform

private val LOG = logger<InteractableSystem>()

class InteractableSystem : IteratingSystem(allOf(InteractableComponent::class, TransformComponent::class).exclude(NukePooledComponent::class).get()){
    private val playerHitbox = Rectangle()
    private val interactableHitbox = Rectangle()
    private val playerEntities by lazy{
        engine.getEntitiesFor(allOf(PlayerComponent::class).get())
    }

    private val interactables = mutableListOf<Int>()
    override fun update(deltaTime: Float) {
        super.update(deltaTime)
        if(interactables.isEmpty()) {
            interactables.add(1)
            LOG.debug { "Spawned" }
        }
    }


    override fun processEntity(entity: Entity, deltaTime: Float) {

        val transform = entity[TransformComponent.mapper]
        require(transform != null) { "Entity |entity| must have TransformComponent. entity=$entity"}

        interactableHitbox.set(
                transform.posVec3.x,
                transform.posVec3.y,
                transform.sizeVec2.x,
                transform.sizeVec2.y
        )

        playerEntities.forEach { player ->
            player[TransformComponent.mapper]?.let { playerTransform ->
                playerHitbox.set(
                        playerTransform.posVec3.x,
                        playerTransform.posVec3.y,
                        playerTransform.sizeVec2.x,
                        playerTransform.sizeVec2.y
                )
                if(playerHitbox.overlaps(interactableHitbox)){
                    LOG.debug { "A" }
                    if(playerTransform.posVec3.x < interactableHitbox.x)  playerTransform.posVec3.x = playerTransform.posVec3.x - 0.07f
                    if(playerTransform.posVec3.x > interactableHitbox.x)  playerTransform.posVec3.x = playerTransform.posVec3.x + 0.07f
                    if(playerTransform.posVec3.y < interactableHitbox.y)  playerTransform.posVec3.y = playerTransform.posVec3.y - 0.07f
                    if(playerTransform.posVec3.y > interactableHitbox.y)  playerTransform.posVec3.y = playerTransform.posVec3.y + 0.07f
                    //playerTransform.posVec3.x = playerTransform.posVec3.x - 0.1f
                    //playerTransform.posVec3.y = playerTransform.posVec3.y - 0.1f
                }
            }
        }
    }
}