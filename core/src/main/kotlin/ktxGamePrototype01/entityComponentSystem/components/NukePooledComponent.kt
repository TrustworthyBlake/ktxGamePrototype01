package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

// Necessary to avoid unpredictable behavior of game engine
class NukePooledComponent : Component, Pool.Poolable {
    var delayTime = 0f
    override fun reset() {
        delayTime = 0f
    }
    companion object {
        val mapper = mapperFor<NukePooledComponent>()
    }
}