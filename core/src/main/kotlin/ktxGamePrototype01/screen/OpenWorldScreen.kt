package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.unitScale

private val LOG = logger<OpenWorldScreen>()
class OpenWorldScreen(game : Prot01) : AbstractScreen(game) {       // Todo add list of teachers and name of user
    private var viewport = FitViewport(9f, 16f)
    private val playerTextureHead = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val playerTextureBody = Texture(Gdx.files.internal("graphics/skill_icons19.png"))
    private val blankTexture = Texture(Gdx.files.internal("graphics/blank.png"))

    override fun show() {
        createMapEntities()
        createUserEntityFromPlayerData()
        //createTeacherEntities(teachers)
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
    private fun createUserEntityFromPlayerData(){
        //val playerData =

        val prefs: Preferences = Gdx.app.getPreferences("playerDataStudent Testing") // + playerName string from app
        val playerHead = prefs.getString("avatarHead")
        val playerBody = prefs.getString("avatarBody")
        if(playerBody != null && playerHead != null) {
            val playerEntityHead = engine.entity {
                with<TransformComponent>{
                    posVec3.set(4.5f, 10f, -1f)
                }
                with<MovementComponent>()
                with<GraphicComponent>{
                    sprite.run{
                        when(playerHead){
                            "todo1" -> setRegion(playerTextureHead)
                            "todo2" -> setRegion(playerTextureHead)
                            else -> setRegion(playerTextureHead)
                        }
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<PlayerComponent> {}
                with<OrientationComponent>()
                with<TextComponent> {
                    isText = true
                    drawPlayScoreHUD = true
                    font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                    font.data.setScale(4.0f, 4.0f)
                }
            }
            val playerEntityBody = engine.entity {
                with<TransformComponent>{
                    posVec3.set(4.5f, 11f, -1f)
                }
                with<MovementComponent>()
                with<GraphicComponent>{
                    sprite.run{
                        when(playerHead){
                            "todo1" -> setRegion(playerTextureBody)
                            "todo2" -> setRegion(playerTextureBody)
                            else -> setRegion(playerTextureBody)
                        }
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<PlayerComponent> {}
                with<OrientationComponent>()
            }
        }
}
    private val teachers = mutableListOf<String>(
            "Mr.TeacherMan", "head1", "body1",
            "Mrs.TeacherWoman", "head2", "body2"
    )
    private fun createTeacherEntities(teacherList : MutableList<String>){     //userName : String
        var count = 0
        var pos = 0
        var teacherName = ""
        var head = ""
        var body = ""
        if (!teacherList.isNullOrEmpty()){
            for (i in pos until teacherList.size){
                var line = teacherList.elementAt(i)
                when (pos) {
                    1 -> { head = line; LOG.debug { "Teachers head: $line" }}
                    2 -> {body = line; LOG.debug { "Teachers body: $line" }}
                    else -> { teacherName = line; LOG.debug { "Teachers name: $line" }}
                }
                pos += 1
                if(pos % 3 == 0){
                LOG.debug { "break" }
                    //pos = 0
                    break
                }

            }
            val teacherHead = engine.entity {
                with<TransformComponent>{
                    posVec3.set(4.5f, 10f, -1f)
                }
                with<GraphicComponent>{
                    sprite.run{
                        setRegion(blankTexture)
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<InteractableComponent>{}
                with<TextComponent> {
                    isText = true
                    textStr = teacherName
                    font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                    font.data.setScale(4.0f, 4.0f)
                    posTextVec2.set(4.5f, 11.5f)
                }
            }
        }
    }
    private fun createMapEntities(){
        val quizMap = Gdx.files.internal("maps/mapOpenWorld01.txt")
        val grassTexture = Texture(Gdx.files.internal("graphics/Grass.png"))
        val holeTexture = Texture(Gdx.files.internal("graphics/Hole.png"))
        try{
            var tileArray = arrayOf<CharArray>()
            var charNr = 0
            var lineNr = 0
            val lines:List<String> = (quizMap.readString()).lines()
            lines.forEach { line ->
                charNr = 0
                line.forEach { char ->
                    val Thing2 = engine.entity {
                        with<TransformComponent> {
                            posVec3.set(charNr.toFloat(), lineNr.toFloat(), 1f)
                        }
                        with<GraphicComponent> {
                            sprite.run {
                                if(char == '1'){setRegion(holeTexture)}
                                if(char == '0') {setRegion(grassTexture)}
                                setSize(texture.width * unitScale, texture.height * unitScale)
                                setOriginCenter()
                            }
                        }
                        if(char == '1'){
                            with<InteractableComponent>()
                        }
                    }
                    charNr=charNr+1
                }
                lineNr=lineNr+1
            }
        }catch (e: Exception){
            e.printStackTrace()
            LOG.debug { "Reading Failed" }
        }finally{
            LOG.debug { "Done Reading" }
        }
    }
}