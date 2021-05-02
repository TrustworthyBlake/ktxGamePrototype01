package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.allOf
import ktx.ashley.entity
import ktx.ashley.get
import ktx.ashley.with
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.unitScale

/** First screen of the application. Displayed after the application is created.  */

private val LOG = logger<QuizScreen>()

class QuizScreen(game: Prot01, qzName : String, private val playerUserName : String) : AbstractScreen(game) {
    private var viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val grassTexture = Texture(Gdx.files.internal("graphics/Grass.png"))
    private val holeTexture = Texture(Gdx.files.internal("graphics/Hole.png"))
    private val treeTexture = Texture(Gdx.files.internal("graphics/tree.png"))
    private val blankTexture = Texture(Gdx.files.internal("graphics/blank.png"))
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
    private val quizMap = Gdx.files.internal("maps/map0.txt");
    private var doOnce = 0 // For debugging of saveScore, used in renderer func
    private val tempQuizName = qzName
    val errorList = mutableListOf<String>("Error: No results found")
    var quizInfo: QuizInfo = QuizInfo(batch as SpriteBatch, errorList)
    var playeContr: playerControl = playerControl(batch as SpriteBatch)
    var gameEndFlag = false
    private val playerEntities by lazy {
        engine.getEntitiesFor(allOf(PlayerComponent::class).get())
    }

    override fun show() {
        createPlayerEntity()
        createMapEntities()
    }
    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }
    override fun render(delta: Float) {

        playeContr = playerControl(batch as SpriteBatch)

        // TODO: Currently constantly checking if the variable has changed
        playerEntities.forEach { player ->
            player[QuizComponent.mapper]?.let { quiz ->
                if (quiz.quizIsCompleted) {
                    quizInfo = QuizInfo(batch as SpriteBatch, quiz.quizResultList)
                    gameEndFlag = true
                }
            }
        }

        var tempDelta = delta
        //if(gameEndFlag == true ) tempDelta = 0f
        engine.update(tempDelta)
        if(gameEndFlag == true ) quizInfo.draw()
        //engine.update(delta)
    }

    override fun dispose() {
        engine.removeAllEntities()
    }
    override fun hide() {
        dispose()
        super.hide()
    }

    private fun createPlayerEntity(){
        val prefs: Preferences = Gdx.app.getPreferences("playerData" + playerUserName) // playerName string from app
        val playerHead = prefs.getString("avatarHead")
        val playerBody = prefs.getString("avatarBody")
        if(playerBody != null && playerHead != null) {
            val playerEntityBody = engine.entity {
                var totScore = 0f
                with<TransformComponent> {
                    posVec3.set(4.5f, 10f, -1f)
                }
                with<MovementComponent>()
                with<GraphicComponent> {
                    sprite.run {
                        when(playerHead){
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
                    totScore = playerScore
                    playerName = playerUserName
                }
                with<OrientationComponent>()
                with<TextComponent> {
                    isText = true
                    drawPlayScoreHUD = true
                    font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                    font.data.setScale(4.0f, 4.0f)
                }
                with<QuizComponent> {
                    quizName = tempQuizName
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
        }
    }

    private fun createMapEntities(){
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
                //LOG.debug { line }
            }
        }catch (e: Exception){
            e.printStackTrace()
            LOG.debug { "Reading Failed" }
        }finally{
            LOG.debug { "Done Reading" }
        }
    }

}

