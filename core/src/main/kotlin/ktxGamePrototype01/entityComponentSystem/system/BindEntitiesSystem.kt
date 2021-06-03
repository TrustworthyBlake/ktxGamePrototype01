package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.BindEntitiesComponent
import ktxGamePrototype01.entityComponentSystem.components.TextComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent

private val LOG = logger<BindEntitiesSystem>()

// The system controls the bound slave entity to its master entity
class BindEntitiesSystem : IteratingSystem(
    allOf(
        BindEntitiesComponent::class,
    ).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {

        val bindComp = entity[BindEntitiesComponent.mapper]
        require(bindComp != null) { "Error: Missing bind entities component" }

        if (!bindComp.isItemEntity) {
            val transformComp = entity[TransformComponent.mapper]
            require(transformComp != null) { "Error: Missing transform component" }
            bindComp.masterEntity[TransformComponent.mapper]?.let { bindTransform ->
                transformComp.posVec3.set(
                        bindTransform.posVec3.x + bindComp.posOffset.x,
                        bindTransform.posVec3.y + bindComp.posOffset.y,
                        bindTransform.posVec3.z
                )
            }
        }
        else {
            val textComp = entity[TextComponent.mapper]
            require(textComp != null) {"Error: Missing text component"}
            bindComp.masterEntity[TransformComponent.mapper]?.let { bindTransform ->
                textComp.posTextVec2.set(
                        bindTransform.posVec3.x + bindComp.posOffset.x,
                        bindTransform.posVec3.y + bindComp.posOffset.y
                    )
                }
        }
    }
}