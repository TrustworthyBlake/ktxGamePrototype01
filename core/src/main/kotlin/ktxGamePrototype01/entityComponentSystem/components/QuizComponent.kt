package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class QuizComponent : Component, Pool.Poolable {
    var quizName = ""
    var playerHasAnswered = false
    var quizIsCompleted = false

    override fun reset() {
        quizName = ""
        playerHasAnswered = false
        quizIsCompleted = false
    }
    companion object{
        val mapper = mapperFor<QuizComponent>()
    }
}