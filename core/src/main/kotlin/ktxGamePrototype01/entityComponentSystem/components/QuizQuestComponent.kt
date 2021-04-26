package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class QuizQuestComponent : Component, Pool.Poolable {
    var teacherName = ""
    var showAvailableQuizes = false

    override fun reset() {
        teacherName = ""
        showAvailableQuizes = false
    }
    companion object{
        val mapper = mapperFor<QuizQuestComponent>()
    }
}