package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Pool
import com.badlogic.gdx.utils.viewport.Viewport
import ktx.ashley.mapperFor


class TransformComponent : Component, Pool.Poolable, Comparable<TransformComponent> {
    val posVec3 = Vector3()
    val sizeVec2 = Vector2(1f, 1f)
    var rotationDeg = 0f

    override fun reset() {
        posVec3.set(Vector3.Zero)
        sizeVec2.set(1f, 1f)
        rotationDeg = 0f

    }
    override fun compareTo(other: TransformComponent): Int {
        val posDifference = other.posVec3.z.compareTo(posVec3.z)
        return if (posDifference == 0) other.posVec3.y.compareTo(posVec3.y) else posDifference
    }
    companion object{
        val mapper = mapperFor<TransformComponent>()
    }
}