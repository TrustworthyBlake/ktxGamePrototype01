package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.screen.playerControl

const val maxHp = 100f

class PlayerComponent : Component, Pool.Poolable{
    var currentPlayerHp = maxHp
    var playerMaxHp = maxHp
    var playerScore = 0f
    var playerName = ""
    lateinit var gameInst : Prot01
    lateinit var playerControl: playerControl

    override fun reset() {
        currentPlayerHp = maxHp
        playerMaxHp = maxHp
        playerScore = 0f
        playerName = ""
    }
    companion object{
        val mapper = mapperFor<PlayerComponent>()
    }
}