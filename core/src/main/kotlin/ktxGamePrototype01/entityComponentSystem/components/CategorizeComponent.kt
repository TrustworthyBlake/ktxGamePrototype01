package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class CategorizeComponent : Component, Pool.Poolable {
    var categorizeName = ""

    override fun reset() {
        categorizeName = ""
    }

    companion object{
        val mapper = mapperFor<CategorizeComponent>()
    }
}