package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class BindEntitiesComponent : Component, Pool.Poolable {
    lateinit var masterEntity : Entity
    var posOffset = Vector2()

    override fun reset() {
        posOffset.set(0f,0f)
    }
    companion object{
        val mapper = mapperFor<BindEntitiesComponent>()
    }
}