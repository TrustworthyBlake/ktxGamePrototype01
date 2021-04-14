package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

enum class InteractableType(){}

class InteractableComponent : Component, Pool.Poolable{
    //var type = InteractableType.NONE
    override fun reset(){

    }

    companion object{
        val mapper = mapperFor<InteractableComponent>()
    }
}