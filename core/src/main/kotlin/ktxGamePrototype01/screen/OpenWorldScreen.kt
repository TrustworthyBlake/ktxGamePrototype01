package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.log.logger
import ktxGamePrototype01.Prot01

private val LOG = logger<OpenWorldScreen>()
class OpenWorldScreen(game : Prot01) : AbstractScreen(game) {
    private var viewport = FitViewport(9f, 16f)

    override fun show() {}
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, false)
    }
    override fun render(delta: Float) {
        engine.update(delta)
    }
    override fun dispose() {}

    // Get player data of sprite
    // Get all teacher player data sprite
    // Dynamically add the teachers to the map
    // Make teachers intractable
    // Quest marker if teacher has added new quiz user haven't completed yet
    // Open up window which lets the user decide which quiz to do or place the different quizes as entities on the map
    // When quiz is chosen, close the open world and open up the quiz screen
    private fun createUserEntityFromFile(){
        //val playerData =
        val prefs: Preferences = Gdx.app.getPreferences("playerData") // + playerName string from app
        val playerHead = prefs.getString("avatarHead")
        val playerBody = prefs.getString("avatarBody")
        if(playerBody != null || playerHead != null) {
            val playerEntitiy = engine.entity {

            }
        }
}

    private fun readTeachersFromFile(){

    }
}