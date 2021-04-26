package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import ktx.ashley.allOf
import ktx.ashley.exclude
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.NukePooledComponent
import ktxGamePrototype01.entityComponentSystem.components.QuizQuestComponent

private val LOG = logger<QuizQuestSystem>()
class QuizQuestSystem : IteratingSystem(allOf(QuizQuestComponent::class).exclude(NukePooledComponent::class).get()){
    override fun processEntity(entity: Entity?, deltaTime: Float) {
        TODO("Not yet implemented")
    }
}