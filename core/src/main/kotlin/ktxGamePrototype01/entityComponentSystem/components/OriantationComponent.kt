package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class OriantationComponent : Component, Pool.Poolable{
    var direction = FaceingDirection.up
    override fun reset() {
        direction = FaceingDirection.up
    }
    companion object{
        val mapper = mapperFor<OriantationComponent>()
    }
}

enum class FaceingDirection {
    left, right, up, down
}