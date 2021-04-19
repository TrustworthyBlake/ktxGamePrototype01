package ktxGamePrototype01.entityComponentSystem.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Pool
import ktx.ashley.mapperFor

class TextComponent : Component, Pool.Poolable, Comparable<TextComponent>{
    var isText = false
    var isQuizAnswer = false
    var textStr = ""
    val posTextVec2 = Vector2()
    var font = BitmapFont()
    var drawPlayScoreHUD = false

    override fun reset() {
        isText = false
        isQuizAnswer = false
        textStr = ""
        posTextVec2.set(Vector2.Zero)
        font = BitmapFont()
        drawPlayScoreHUD = false
    }

    override fun compareTo(other: TextComponent): Int {
        val posDifference = other.posTextVec2.x.compareTo(posTextVec2.x)
        return if (posDifference == 0) other.posTextVec2.y.compareTo(posTextVec2.y) else posDifference
    }
    companion object{
        val mapper = mapperFor<TextComponent>()
    }
}