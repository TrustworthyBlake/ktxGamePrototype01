package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureAtlas
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.utils.Array
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.entityComponentSystem.HelperFunctions
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.offsetPos
import ktxGamePrototype01.unitScale

private val LOG = logger<OpenWorldScreen>()
// Contains the Open World Game Screen
// Takes Prot01, teacherDataList and userName. Inherits from Abstract Screen
class OpenWorldScreen(game : Prot01, private val teacherDataList: List<String>?,
                      private val playerUserName : String) : AbstractScreen(game) {

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
    private var playeContr: playerControl = playerControl(batch as SpriteBatch)

    // When this game screen is shown, called once
    override fun show() {
        createMapEntities()
        createUserEntityFromPlayerData()
        createTeachers(teacherDataList)
    }
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, false)
    }
    override fun render(delta: Float) {
        engine.update(delta)
        // Draw call for ActivateButton
        playeContr.draw()
    }
    override fun dispose() {
        engine.removeAllEntities()
    }
    override fun hide() {
        dispose()
        super.hide()
    }

    // Creates the player entity with the users avatar preferences from playerData+userName.xml
    private fun createUserEntityFromPlayerData(){
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + playerUserName) // playerName string from app
        val playerHead = prefs.getString("avatarHead")
        val playerBody = prefs.getString("avatarBody")
        if(playerBody != null && playerHead != null) {
            val playerEntityBody = engine.entity {
                with<TransformComponent>{
                    // Where the entity is positioned in the game world
                    posVec3.set(29.5f-offsetPos, 15f, -1f)
                }
                with<MovementComponent>()
                with<SpriteComponent>{
                    sprite.run{
                        // Sets the entity's texture based on the string
                        when(playerBody){
                            "colour1" -> setRegion(playerTextureBody1)
                            "colour2" -> setRegion(playerTextureBody2)
                            "colour3" -> setRegion(playerTextureBody3)
                            "colour4" -> setRegion(playerTextureBody4)
                            else -> setRegion(playerTextureBody1)
                        }
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<PlayerComponent> {
                    playerName = playerUserName
                    gameInst = game
                    playerControl = playeContr
                }
                with<OrientationComponent>()
                with<TextComponent> {
                    // Activates HUD to display current player score
                    isText = true
                    drawPlayScoreHUD = true
                    // Makes text clearer
                    font.region.texture.setFilter(Texture.TextureFilter.Linear,
                            Texture.TextureFilter.Linear)
                    // Scales the text up by 4
                    font.data.setScale(4.0f, 4.0f)
                }
            }
            val playerEntityHead = engine.entity {
                with<TransformComponent>{
                    posVec3.set(Vector3.Zero)
                }
                with<SpriteComponent>{
                    sprite.run{
                        // Sets the entity's texture based on the string
                        when(playerHead){
                            "colour1" -> setRegion(playerTextureHead1)
                            "colour2" -> setRegion(playerTextureHead2)
                            "colour3" -> setRegion(playerTextureHead3)
                            "colour4" -> setRegion(playerTextureHead4)
                            else -> setRegion(playerTextureHead1)
                        }
                        setSize(texture.width * unitScale, texture.height * unitScale)
                        setOriginCenter()
                    }
                }
                with<BindEntitiesComponent> {
                    masterEntity = playerEntityBody
                    // Offset by 1 in the y direction so the head is above the body entity
                    posOffset.set(0f, 1f)
                }
                with<OrientationComponent>()
            }

        }else{LOG.debug { "Error: Can not find player data" }}
}

    private fun createTeachers(teacherList : List<String>?) {
        val teacherPosArray = Array<Vector2>()
        var teacherPosX = 26f
        var teacherPosY = 18f   // Starting values slightly above default spawn pos of player
        var count = 0
        var teacherName = ""
        var head = ""
        var body = ""
        var dataTag : String
        var data : String

        LOG.debug { "Teacher list data: $teacherList" }

        if (!teacherList.isNullOrEmpty()) {
            teacherList.forEach {

                // Index 0 is data tag
                dataTag = it.split("-")[0]

                // Index 1 is where the actual data is
                data = it.split("-")[1]

                // Sets the name of teacher, avatar body and head to the correct variables
                when(dataTag){
                    "name" -> teacherName = data
                    "head" -> head = data
                    "body" -> body = data

                    // When completed the teacher data is passed to createTeacherEntity()
                    "complete" -> {

                        // Dynamically adding new position to Array for every teacher
                        teacherPosArray.add(Vector2(teacherPosX, teacherPosY))
                        createTeacherEntity(teacherName, head, body, teacherPosArray[count])
                        count += 1

                        // For placing the teachers in a grid
                        teacherPosX += 6f
                        if (count % 2 == 0) {teacherPosY += 4f; teacherPosX = 26f}
                    }
                    else -> LOG.debug { "Error in teacher list data" }
                    }
            }
        } else {LOG.debug { "Error: Can not find teacher list" }}
    }

    // Creates one teacher entity, takes the teacher's name, head and body as String
    // and the position vector as a Vector2
    private fun createTeacherEntity(teacherName : String, head : String, body: String, teacherPos : Vector2){
        val maxLength = 24
        val textPosOffset = 1.7f
        val helpFun = HelperFunctions()
        // Chops the string
        val (teacherNameChopped , spacer) = helpFun.chopString(teacherName, maxLength)

        val teacherEntityBody = engine.entity {
            with<TransformComponent> {
                // Where the entity is positioned in the game world, uses the pos from array
                posVec3.set(teacherPos.x-offsetPos, teacherPos.y, -1f)
            }
            with<SpriteComponent> {
                sprite.run {
                    // Sets the entity's texture based on the string
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

        val teacherEntityHead = engine.entity {
            with<TransformComponent> {
                // Where the entity is positioned in the game world, uses the pos from array
                // Set to 0 because this entity has BindEntitiesComponent
                posVec3.set(Vector3.Zero)
            }
            with<SpriteComponent> {
                sprite.run {
                    // Sets the entity's texture based on the string
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
                // Makes text clearer
                font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                // Scales the text up by 4
                font.data.setScale(4.0f, 4.0f)
                // Position of text in the game world
                posTextVec2.set(teacherPos.x, teacherPos.y + spacer + textPosOffset)
            }
            with<BindEntitiesComponent> {
                masterEntity = teacherEntityBody
                // Offset by 1 in the y direction so the head is above the body entity
                posOffset.set(0f, 1f)
            }
            with<InteractableComponent>()
            with<QuizQuestComponent> { teacherStr = teacherName }
        }
    }

    // Creates the map entities from map.txt file
    private fun createMapEntities(){
        val quizMap = Gdx.files.internal("maps/mapOpenWorld01.txt")
        val grassTexture = Texture(Gdx.files.internal("graphics/Grass1.png"))
        val treeTexture1 = Texture(Gdx.files.internal("graphics/tree1.png"))
        val treeTexture3 = Texture(Gdx.files.internal("graphics/tree3.png"))
        val treeTexture4 = Texture(Gdx.files.internal("graphics/tree4.png"))
        val rockTexture1 = Texture(Gdx.files.internal("graphics/rock1.png"))
        val rockTexture2 = Texture(Gdx.files.internal("graphics/rock2.png"))
        val rockTexture4 = Texture(Gdx.files.internal("graphics/rock4.png"))
        val rockTexture5 = Texture(Gdx.files.internal("graphics/rock5.png"))
        val rockTexture6 = Texture(Gdx.files.internal("graphics/rock6.png"))
        try{
            var tileArray = arrayOf<CharArray>()
            var charNr = 0
            var lineNr = 0
            var sizeMultiplier = 1f
            val lines:List<String> = (quizMap.readString()).lines().reversed()
            lines.forEach { line ->
                charNr = 0
                line.forEach { char ->
                    val mapEntity = engine.entity {
                        with<TransformComponent> {
                            posVec3.set(charNr.toFloat(), lineNr.toFloat(), 1f)
                        }
                        with<SpriteComponent> {
                            sprite.run {
                                setRegion(grassTexture)
                                setSize(texture.width * unitScale, texture.height * unitScale)
                                setOriginCenter()
                            }
                        }
                        if (char != '0'){
                            val mapNewLayerEntity = engine.entity {
                                with<TransformComponent> {
                                    posVec3.set(charNr.toFloat(), lineNr.toFloat(), -1f)
                                }
                                with<SpriteComponent> {
                                    sprite.run {
                                        when (char) {   // Lowest layer
                                            '1' -> {setRegion(treeTexture1); sizeMultiplier = 3f}
                                            '2' -> {setRegion(treeTexture3); sizeMultiplier = 3f}
                                            '3' -> {setRegion(treeTexture4); sizeMultiplier = 3f}
                                            '4' -> {setRegion(rockTexture1); sizeMultiplier = 2f}
                                            '5' -> {setRegion(rockTexture2); sizeMultiplier = 1.25f}
                                            '7' -> {setRegion(rockTexture5); sizeMultiplier = 1.25f}
                                            '8' -> {setRegion(rockTexture6); sizeMultiplier = 1.25f}
                                            '9' -> {setRegion(rockTexture4); sizeMultiplier = 1f}
                                            else -> {setRegion(blankTexture); sizeMultiplier = 1f}
                                        }
                                        setScale(sizeMultiplier, sizeMultiplier)
                                        setSize((texture.width * unitScale), (texture.height * unitScale))
                                    }
                                }
                                if (char == '9'){
                                    with<InteractableComponent>()
                                }
                            }
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