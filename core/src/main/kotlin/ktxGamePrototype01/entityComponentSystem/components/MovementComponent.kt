package ktxGamePrototype01.entityComponentSystem.components
import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class MovementComponent : Component, Pool.Poolable{
    val velocity = Vector2()
    override fun reset() {
        velocity.set(Vector2.Zero)
    }
    companion object{
        val mapper = mapperFor<MovementComponent>()
    }
}