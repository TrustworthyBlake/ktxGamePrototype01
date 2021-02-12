package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

const val maxHp = 100f

class PlayerComponent : Component, Pool.Poolable{
    var currentPlayerHp = maxHp
    var playerMaxHp = maxHp
    var playerScore = 0f

    override fun reset() {
        currentPlayerHp = maxHp
        playerMaxHp = maxHp
        playerScore = 0f
    }
    companion object{
        val mapper = mapperFor<PlayerComponent>()
    }
}