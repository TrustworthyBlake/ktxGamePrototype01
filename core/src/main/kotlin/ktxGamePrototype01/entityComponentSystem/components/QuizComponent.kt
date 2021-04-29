package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class QuizComponent : Component, Pool.Poolable {
    var quizName = ""
    var playerHasAnswered = false
    var quizIsCompleted = false
    var quizResultList = mutableListOf<String>()

    override fun reset() {
        quizName = ""
        playerHasAnswered = false
        quizIsCompleted = false
        quizResultList = mutableListOf<String>()
    }
    companion object{
        val mapper = mapperFor<QuizComponent>()
    }
}