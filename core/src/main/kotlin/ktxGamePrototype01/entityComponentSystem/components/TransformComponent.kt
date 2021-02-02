package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor


class TransformComponent : Component, Pool.Poolable {
    val posVec3 = Vector3()
    val sizeVec2 = Vector2(1f,1f)
    var rotationDeg = 0f

    override fun reset() {
        posVec3.set(Vector3.Zero)
        sizeVec2.set(1f,1f)
        rotationDeg = 0f
    }
    companion object{
        val mapper = mapperFor<TransformComponent>()
    }
}