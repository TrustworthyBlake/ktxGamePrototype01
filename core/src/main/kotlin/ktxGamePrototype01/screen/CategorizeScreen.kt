package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.offsetPos
import ktxGamePrototype01.unitScale

private val LOG = logger<CategorizeScreen>()

class CategorizeScreen(game: Prot01, categorizeName : String, private val playerUserName : String) : AbstractScreen(game) {

    private var viewport = FitViewport(9f, 16f)
    var playeContr: playerControl = playerControl(batch as SpriteBatch)
    private val playerTextureHead1 = Texture(Gdx.files.internal("graphics/head1.png"))
    private val playerTextureHead2 = Texture(Gdx.files.internal("graphics/head2.png"))
    private val playerTextureHead3 = Texture(Gdx.files.internal("graphics/head3.png"))
    private val playerTextureHead4 = Texture(Gdx.files.internal("graphics/head4.png"))
    private val playerTextureBody1 = Texture(Gdx.files.internal("graphics/body1.png"))
    private val playerTextureBody2 = Texture(Gdx.files.internal("graphics/body2.png"))
    private val playerTextureBody3 = Texture(Gdx.files.internal("graphics/body3.png"))
    private val playerTextureBody4 = Texture(Gdx.files.internal("graphics/body4.png"))

    override fun show() {
        // Todo call create player
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        // Todo victory screen
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
                    posVec3.set(29.5f- offsetPos, 15f, -1f)
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
                    font.region.texture.setFilter(
                        Texture.TextureFilter.Linear,
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
        val blankTexture = Texture(Gdx.files.internal("graphics/blank.png"))

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