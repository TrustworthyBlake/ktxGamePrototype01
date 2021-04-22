package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class InteractableType(){}

class InteractableComponent : Component, Pool.Poolable{
    var correctAnswer = false
    var maxPointsQuestion = 0

    //var type = InteractableType.NONE
    override fun reset(){
        correctAnswer = false
        maxPointsQuestion = 0
    }

    companion object{
        val mapper = mapperFor<InteractableComponent>()
    }
}