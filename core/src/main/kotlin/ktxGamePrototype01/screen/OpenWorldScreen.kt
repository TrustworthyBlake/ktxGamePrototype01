package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.entityComponentSystem.HelperFunctions
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.unitScale

private val LOG = logger<OpenWorldScreen>()
class OpenWorldScreen(game : Prot01, private val teacherDataList: List<String>?, private val playerUserName : String) : AbstractScreen(game) {       // Todo add list of teachers and name of user
    private var viewport = FitViewport(9f, 16f)
    private val playerTextureHead = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val playerTextureBody = Texture(Gdx.files.internal("graphics/skill_icons19.png"))
    private val playerTextureHead1 = Texture(Gdx.files.internal("graphics/head1.png"))
    private val playerTextureHead2 = Texture(Gdx.files.internal("graphics/head2.png"))
    private val playerTextureHead3 = Texture(Gdx.files.internal("graphics/head3.png"))
    private val playerTextureHead4 = Texture(Gdx.files.internal("graphics/head4.png"))
    private val playerTextureBody1 = Texture(Gdx.files.internal("graphics/body1.png"))
    private val playerTextureBody2 = Texture(Gdx.files.internal("graphics/body2.png"))
    private val playerTextureBody3 = Texture(Gdx.files.internal("graphics/body3.png"))
    private val playerTextureBody4 = Texture(Gdx.files.internal("graphics/body4.png"))
    private val blankTexture = Texture(Gdx.files.internal("graphics/blank.png"))
    var playeContr: playerControl = playerControl(batch as SpriteBatch)

    override fun show() {
        createMapEntities()
        createUserEntityFromPlayerData()
        createTeacherEntities(teacherDataList)
    }
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, false)
    }
    override fun render(delta: Float) {
        engine.update(delta)
        playeContr.draw()
    }
    override fun dispose() {
        engine.removeAllEntities()
    }
    override fun hide() {
        dispose()
        super.hide()
    }

    // Get player data of sprite
    // Get all teacher player data sprite
    // Dynamically add the teachers to the map
    // Make teachers intractable
    // Quest marker if teacher has added new quiz user haven't completed yet
    // Open up window which lets the user decide which quiz to do or place the different quizes as entities on the map
    // When quiz is chosen, close the open world and open up the quiz screen
    private fun createUserEntityFromPlayerData(){
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + playerUserName) // playerName string from app
        val playerHead = prefs.getString("avatarHead")
        val playerBody = prefs.getString("avatarBody")
        if(playerBody != null && playerHead != null) {
            val playerEntityBody = engine.entity {
                with<TransformComponent>{
                    posVec3.set(4.5f, 11f, -1f)
                }
                with<MovementComponent>()
                with<GraphicComponent>{
                    sprite.run{
                        when(playerBody){
                            "colour1" -> setRegion(playerTextureBody1)
                            "colour2" -> setRegion(playerTextureBody2)
                            "colour3" -> setRegion(playerTextureBody3)
                            "colour4" -> setRegion(playerTextureBody4)
                            else -> setRegion(playerTextureBody)
                        }
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<PlayerComponent> {
                    playerName = playerUserName
                    gameInst = game
                }
                with<OrientationComponent>()
                with<TextComponent> {
                    isText = true
                    drawPlayScoreHUD = true
                    font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                    font.data.setScale(4.0f, 4.0f)
                }
            }
            val playerEntityHead = engine.entity {
                with<TransformComponent>{
                    posVec3.set(4.5f, 10f, -1f)
                }
                with<MovementComponent>()
                with<GraphicComponent>{
                    sprite.run{
                        when(playerHead){
                            "colour1" -> setRegion(playerTextureHead1)
                            "colour2" -> setRegion(playerTextureHead2)
                            "colour3" -> setRegion(playerTextureHead3)
                            "colour4" -> setRegion(playerTextureHead4)
                            else -> setRegion(playerTextureHead)
                        }
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<BindEntitiesComponent> {
                    masterEntity = playerEntityBody
                    posOffset.set(0f, 1f)
                }
                with<OrientationComponent>()
            }

        }else{LOG.debug { "Error: Can not find player data" }}
}
    /*private val teachers = mutableListOf<String>(
            "Mr.TeacherMan", "head1", "body1",
            "Mrs.TeacherWoman", "head2", "body2"
    )*/
    private fun createTeacherEntities(teacherList : List<String>?){     //userName : String
        var teacherPosArray = Array<Vector2>()
        teacherPosArray.add(Vector2(1f, 11f))
        teacherPosArray.add(Vector2(7f, 11f))
        teacherPosArray.add(Vector2(1f, 4f))
        teacherPosArray.add(Vector2(7f, 4f))
        var count = 1
        var pos = 0
        var teacherName = ""
        var head = ""
        var body = ""
        val maxLength = 26
        LOG.debug { "Teacher list data: $teacherList" }
        if (!teacherList.isNullOrEmpty()) {
            val helpFun = HelperFunctions()
            for (i in 0 until teacherList.size) {
                var line = teacherList.elementAt(i)
                when (pos) {
                    1 -> {
                        head = line; LOG.debug { "Teachers head: $line" }
                    }
                    2 -> {
                        body = line; LOG.debug { "Teachers body: $line" }
                    }
                    else -> {
                        teacherName = line; LOG.debug { "Teachers name: $line" }
                    }
                }
                var (teacherNameChopped , spacer, centerTextPos) = helpFun.chopString(teacherName, maxLength)
                pos += 1
                if (pos % 3 == 0) {
                    count += 1
                    pos = 0
                    val teacherEntityHead = engine.entity {
                        with<TransformComponent> {
                            posVec3.set(teacherPosArray[count].x, teacherPosArray[count].y + 1f, -1f)
                        }
                        with<GraphicComponent> {
                            sprite.run {
                                when (head) {
                                    "head1" -> setRegion(playerTextureHead1)
                                    "head2" -> setRegion(playerTextureHead2)
                                    "head3" -> setRegion(playerTextureHead3)
                                    "head4" -> setRegion(playerTextureHead4)
                                    else -> setRegion(playerTextureHead)
                                }
                                setSize(texture.width * unitScale, texture.height * unitScale)
                                setOriginCenter()
                            }
                        }
                        with<TextComponent> {
                            isText = true
                            textStr = teacherNameChopped
                            font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                            font.data.setScale(4.0f, 4.0f)
                            posTextVec2.set(teacherPosArray[count].x - centerTextPos, teacherPosArray[count].y + spacer + 1.7f)
                        }
                        with<InteractableComponent> { isTeacher = true }
                        with<QuizQuestComponent> { teacherStr = teacherName }
                    }
                    val teacherEntityBody = engine.entity {
                        with<TransformComponent> {
                            posVec3.set(teacherPosArray[count].x, teacherPosArray[count].y, -1f)
                        }
                        with<GraphicComponent> {
                            sprite.run {
                                when (body) {
                                    "body1" -> setRegion(playerTextureBody1)
                                    "body2" -> setRegion(playerTextureBody2)
                                    "body3" -> setRegion(playerTextureBody3)
                                    "body4" -> setRegion(playerTextureBody4)
                                    else -> setRegion(playerTextureBody)
                                }
                                setSize(texture.width * unitScale, texture.height * unitScale)
                                setOriginCenter()
                            }
                        }
                        with<InteractableComponent> { isTeacher = true }
                        with<QuizQuestComponent> { teacherStr = teacherName }
                    }
                }
            }
        }else{LOG.debug { "Error: Can not find teacher list" }}
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
                    val mapEntity = engine.entity {
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
    private fun createQuizQuestEntities(){
        // Teacher name: who own the quiz
        // Place entity with name of quiz on map when interacting with teacher
        // Remove current placed quests when starting a new interaction with another teacher
        // Start quiz game when activating quest
    }
}