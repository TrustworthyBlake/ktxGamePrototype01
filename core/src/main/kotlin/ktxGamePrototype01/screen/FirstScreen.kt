package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.with
import ktx.graphics.use
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.Prot01
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.unitScale
import java.io.File
import java.io.InputStream
import java.util.*
import javax.xml.soap.Text

/** First screen of the application. Displayed after the application is created.  */

private val LOG = logger<FirstScreen>()

class FirstScreen(game:Prot01) : AbstractScreen(game) {
    private val viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val grassTexture = Texture(Gdx.files.internal("graphics/grass.png"))
    private val test = Gdx.files.internal("map0.txt")



    private val player = engine.entity{
        with<TransformComponent>{
            posVec3.set(2f,2f,0f)
        }
        with<MovementComponent>()
        with<GraphicComponent>{
            sprite.run{
                setRegion(playerTexture)
                setSize(texture.width * unitScale, texture.height * unitScale)
                setOriginCenter()
            }
        }
        with<PlayerComponent>()
        with<OrientationComponent>()
    }

    private val Thing2 = engine.entity {
        with<TransformComponent> {
            posVec3.set(2f, 1f, 0f)
        }
        with<GraphicComponent> {
            sprite.run {
                setRegion(grassTexture)
                setSize(texture.width * unitScale, texture.height * unitScale)
                setOriginCenter()
            }
        }
    }

    override fun show() {
        LOG.debug { "First screen is displayed" }
        try{
            var tileArray = arrayOf<CharArray>()
            var charNr = 0
            var lineNr = 0
            val fileName = "assets/map0.txt"
            val lines:List<String> = File(fileName).readLines()
            lines.forEach { line ->
                charNr = 0
                line.forEach { char ->
                    val Thing2 = engine.entity {
                        with<TransformComponent> {
                            posVec3.set(charNr.toFloat(), lineNr.toFloat(), 0f)
                        }
                        with<GraphicComponent> {
                            sprite.run {
                                if(char == '1'){setRegion(playerTexture)}
                                if(char == '0') {setRegion(grassTexture)}
                                setSize(texture.width * unitScale, texture.height * unitScale)
                                setOriginCenter()
                            }
                        }
                    }
                    charNr=charNr+1
                }
                lineNr=lineNr+1
                LOG.debug { line }
            }


        }catch(e:Exception){
            e.printStackTrace()
            LOG.debug { "Reading Failed" }
        }finally{
            LOG.debug { "Done Reading" }
        }
    }

    override fun resize(width: Int, height: Int) {
        viewport.update(width, height, true)
    }

    override fun render(delta: Float) {
        viewport.apply()
        batch.use(viewport.camera.combined){
        }
        engine.update(delta)
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
           game.setScreen<SecondScreen>()
        }
    }

    override fun dispose() {
        playerTexture.dispose()
    }

    fun getMap(){
        try{
            val tileArray = arrayOf<CharArray>()
            var lineNr = 1
            val fileName = "map0.txt"
            val lines:List<String> = File(fileName).readLines()
            lines.forEach { line ->
                            tileArray[lineNr] = line.toCharArray()
                            lineNr=lineNr+1
            }
        }catch(e:Exception){
            e.printStackTrace()
        }finally{
            println("File read")
        }

    }
}

