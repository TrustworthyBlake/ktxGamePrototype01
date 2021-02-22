package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.get
import ktxGamePrototype01.entityComponentSystem.components.NukePooledComponent

class NukePooledSystem : IteratingSystem(allOf(NukePooledComponent::class).get()) {
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val nuke = entity[NukePooledComponent.mapper]
        require(nuke != null) {"Error: 2006. Entity=$entity"}
        nuke.delayTime -= deltaTime
        if(nuke.delayTime <= 0f){
            engine.removeEntity(entity)
        }
    }
}