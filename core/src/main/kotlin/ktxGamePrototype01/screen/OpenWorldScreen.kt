package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01

private val LOG = logger<OpenWorldScreen>()
class OpenWorldScreen(game : Prot01) : AbstractScreen(game) {       // Todo add list of teachers and name of user
    private var viewport = FitViewport(9f, 16f)

    override fun show() {
        readTeachersFromFile(teachers)
    }
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
        if(playerBody != null && playerHead != null) {
            val playerEntitiy = engine.entity {

            }
        }
}
    private val teachers = mutableListOf<String>(
            "Mr.TeacherMan", "headString", "bodyString",
            "Mrs.TeacherWoman", "headString", "bodyString"
    )
    private fun readTeachersFromFile(teacherList : MutableList<String>){     //userName : String
        var count = 0
        var pos = 1
        var teacherName = ""
        var head = ""
        var body = ""
        if (!teacherList.isNullOrEmpty()){
            for (i in 0 until teacherList.size){
                var line = teacherList.elementAt(i)
                when{
                    pos % 2 == 0 ->{ head = line; LOG.debug { "Teachers head: $line" }}
                    pos % 3 == 0 ->{body = line; LOG.debug { "Teachers body: $line" }}
                    else -> { teacherName = line; LOG.debug { "Teachers name: $line" }}
                }
                if(i % 3 == 0){

                //LOG.debug { "Teachers: $line" }

                }
                pos += 1
            }
        }
    }
}