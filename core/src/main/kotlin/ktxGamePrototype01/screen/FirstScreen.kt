package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
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
import java.io.File

/** First screen of the application. Displayed after the application is created.  */

private val LOG = logger<FirstScreen>()

class FirstScreen(game: Prot01) : AbstractScreen(game) {
    private var viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val grassTexture = Texture(Gdx.files.internal("graphics/Grass.png"))
    private val holeTexture = Texture(Gdx.files.internal("graphics/Hole.png"))
    private val treeTexture = Texture(Gdx.files.internal("graphics/tree.png"))
    private val blankTexture = Texture(Gdx.files.internal("graphics/blank.png"))
    private val quizMap = Gdx.files.internal("maps/map0.txt");
    private var doOnce = 0 // For debugging of saveScore, used in renderer func

    private val player = engine.entity{
        var totScore = 0f
        with<TransformComponent>{
            posVec3.set(4.5f, 10f, -1f)
        }
        with<MovementComponent>()
        with<GraphicComponent>{
            sprite.run{
                setRegion(playerTexture)
                setSize(texture.width * unitScale, texture.height * unitScale)
                setOriginCenter()
            }
        }
        with<PlayerComponent> {
            totScore = playerScore
        }
        with<OrientationComponent>()
        with<TextComponent> {
            isText = true
            drawPlayScoreHUD = true
            font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
            font.data.setScale(4.0f, 4.0f)
        }
       with<QuizComponent>{
            quizName = "quiz4"
        }
    }

    override fun show() {
        LOG.debug { "First screen is displayed" }
        createMapEntities()
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
            game.addScreen(SecondScreen(game))
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            game.removeScreen(SecondScreen::class.java)
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
           game.setScreen<SecondScreen>()
        }
        if(Gdx.input.isKeyPressed(Input.Keys.UP)){
            if(doOnce == 0){
                //Put functions to debug here
                doOnce = 1
            }
        }
        engine.update(delta)
    }

    override fun dispose() {
        playerTexture.dispose()
        player.removeAll()
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

