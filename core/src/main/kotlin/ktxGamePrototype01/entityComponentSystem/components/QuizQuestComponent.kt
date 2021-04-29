package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class QuizQuestComponent : Component, Pool.Poolable {
    var teacherStr = ""
    var showAvailableQuizes = false

    override fun reset() {
        teacherStr = ""
        showAvailableQuizes = false
    }
    companion object{
        val mapper = mapperFor<QuizQuestComponent>()
    }
}