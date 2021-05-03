package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class OrientationComponent : Component, Pool.Poolable{
    var direction = OrientationDirection.up
    var tempDir = Vector2(0f,0f)
    override fun reset() {
        direction = OrientationDirection.up
        tempDir.set(Vector2.Zero)
    }
    companion object{
        val mapper = mapperFor<OrientationComponent>()
    }
}

enum class OrientationDirection {
    left, right, up, down, tempOri
}