package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class InteractableType{
    CORRECTANSWER,
    WRONGANSWER,
    TEACHER,
    QUEST,
    ITEM,
    CATEGORY,
    DEFAULT
}

class InteractableComponent : Component, Pool.Poolable{
    var correctAnswer = false
    var maxPointsQuestion = 0
    var isTeacher = false
    var isQuest = false
    var isQuestOrAnswer = false
    var nameOfQuiz = ""
    var type = InteractableType.DEFAULT
    var belongsToCategory = 0
    var interactableHitbox = Rectangle()

    override fun reset(){
        correctAnswer = false
        maxPointsQuestion = 0
        isTeacher = false
        isQuest = false
        nameOfQuiz = ""
        isQuestOrAnswer = false
        type = InteractableType.DEFAULT
        belongsToCategory = 0
        interactableHitbox.set(0f,0f,0f,0f)
    }

    companion object{
        val mapper = mapperFor<InteractableComponent>()
    }
}