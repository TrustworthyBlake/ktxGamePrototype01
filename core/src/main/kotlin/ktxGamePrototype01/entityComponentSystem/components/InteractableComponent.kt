package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class InteractableComponent : Component, Pool.Poolable{
    var correctAnswer = false
    var maxPointsQuestion = 0
    var isTeacher = false
    var isQuest = false

    override fun reset(){
        correctAnswer = false
        maxPointsQuestion = 0
        isTeacher = false
        isQuest = false
    }

    companion object{
        val mapper = mapperFor<InteractableComponent>()
    }
}