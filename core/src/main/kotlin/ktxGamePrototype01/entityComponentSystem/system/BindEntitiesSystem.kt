package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.ashley.get
import ktxGamePrototype01.entityComponentSystem.components.BindEntitiesComponent
import ktxGamePrototype01.entityComponentSystem.components.GraphicComponent
import ktxGamePrototype01.entityComponentSystem.components.NukePooledComponent
import ktxGamePrototype01.entityComponentSystem.components.TransformComponent

// The system controls the bound slave entity to its master entity
class BindEntitiesSystem : IteratingSystem(
    allOf(
        BindEntitiesComponent::class,
        TransformComponent::class, //GraphicComponent::class,
    ).exclude(NukePooledComponent::class).get()
) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val transformComp = entity[TransformComponent.mapper]
        require(transformComp != null){"Error: Missing transform component"}
        val bindComp = entity[BindEntitiesComponent.mapper]
        require(bindComp != null){"Error: Missing bind entities component"}
        bindComp.masterEntity[TransformComponent.mapper]?.let { bindTransform ->
            transformComp.posVec3.set(
                bindTransform.posVec3.x + bindComp.posOffset.x,
                bindTransform.posVec3.y + bindComp.posOffset.y,
                transformComp.posVec3.z
            )
        }
    }
}