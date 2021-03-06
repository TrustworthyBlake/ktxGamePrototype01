package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.SortedIteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.graphics.use
import ktx.log.error
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.SpriteComponent
import ktxGamePrototype01.entityComponentSystem.components.PlayerComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent

private val LOG = logger<RenderSystem2D>()

class RenderSystem2D(
        private val batch: Batch, private var gameViewport: Viewport
) : SortedIteratingSystem(
        allOf(TransformComponent::class, SpriteComponent::class).get(),
        compareBy { entity -> entity[TransformComponent.mapper] }
) {

    override fun update(deltaTime: Float) {
        gameViewport.update(Gdx.graphics.width,Gdx.graphics.height, false)
        forceSort()
        // Sets the updated viewport camera in batch
        batch.use(gameViewport.camera.combined){
            super.update(deltaTime)
        }
    }

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformComp = entity[TransformComponent.mapper]
        require(transformComp != null) { "Error: 5001. entity=$entity" }    // Entity is missing its transformation component
        val spriteComp = entity[SpriteComponent.mapper]
        require(spriteComp != null) { "Error: 5002. entity=$entity" }      // Entity is missing its graphics component

        val player = entity[PlayerComponent.mapper]
        if (player != null) {
            entity[TransformComponent.mapper]?.let { trans ->
                // So the viewport follows the player entity
                gameViewport.camera.position.x = trans.posVec3.x
                gameViewport.camera.position.y = trans.posVec3.y
            }
        }
        if(spriteComp.sprite.texture != null) {
            spriteComp.sprite.run{
                rotation = transformComp.rotationDeg
                setBounds(transformComp.posVec3.x, transformComp.posVec3.y, transformComp.sizeVec2.x, transformComp.sizeVec2.y)
                // LibGDX draw call for the batch
                draw(batch)
            }
        }
        else {
            LOG.error { "Error: 5003. entity=$entity" }                          // Entity is missing its texture component
        }
    }
}