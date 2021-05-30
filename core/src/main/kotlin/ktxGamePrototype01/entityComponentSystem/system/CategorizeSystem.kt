package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktxGamePrototype01.entityComponentSystem.components.CategorizeComponent

class CategorizeSystem : IteratingSystem(allOf(CategorizeComponent::class).get()){

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        TODO("Not yet implemented")
    }
}