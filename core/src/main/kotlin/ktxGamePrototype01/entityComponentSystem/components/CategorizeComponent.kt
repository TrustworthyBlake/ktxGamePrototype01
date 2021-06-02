package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class CategorizeComponent : Component, Pool.Poolable {
    var categorizeName = ""
    var categorizeResultList = mutableListOf<String>()
    var categorizeIsCompleted = false
    var showResultList = false

    override fun reset() {
        categorizeName = ""
        categorizeResultList = mutableListOf<String>()
        categorizeIsCompleted = false
        showResultList = false
    }

    companion object{
        val mapper = mapperFor<CategorizeComponent>()
    }
}