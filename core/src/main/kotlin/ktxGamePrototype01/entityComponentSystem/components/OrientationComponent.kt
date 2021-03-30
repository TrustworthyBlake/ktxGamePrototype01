package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class OrientationComponent : Component, Pool.Poolable{
    var direction = OrientationDirection.up
    override fun reset() {
        direction = OrientationDirection.up
    }
    companion object{
        val mapper = mapperFor<OrientationComponent>()
    }
}

enum class OrientationDirection {
    left, right, up, down, tempOri
}