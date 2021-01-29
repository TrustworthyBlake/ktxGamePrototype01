package ktxGamePrototype01

import com.badlogic.gdx.Game

/** [com.badlogic.gdx.ApplicationListener] implementation shared by all platforms.  */
class Prot01 : Game() {
    override fun create() {
        setScreen(FirstScreen())
    }
}