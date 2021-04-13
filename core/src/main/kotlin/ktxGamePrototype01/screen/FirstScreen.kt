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

/** First screen of the application. Displayed after the application is created.  */

private val LOG = logger<FirstScreen>()

class FirstScreen(game: Prot01) : AbstractScreen(game) {
    private val viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val grassTexture = Texture(Gdx.files.internal("graphics/Grass.png"))
    private val holeTexture = Texture(Gdx.files.internal("graphics/Hole.png"))
    private val treeTexture = Texture(Gdx.files.internal("graphics/tree.png"))

    private val quizMap = Gdx.files.internal("maps/map0.txt");


    private val player = engine.entity{
        with<TransformComponent>{
            posVec3.set(0f, 0f, 0f)
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
    private val tree = engine.entity {
        with<TransformComponent> { posVec3.set(8f, 8f, 0f) }

        with<GraphicComponent> {
            sprite.run {
                setRegion(treeTexture)
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
                    }
                    charNr=charNr+1
                }
                lineNr=lineNr+1
                LOG.debug { line }
            }


        }catch (e: Exception){
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
        if(Gdx.input.isKeyJustPressed(Input.Keys.O)){
            game.addScreen(SecondScreen(game))
        }
        if(Gdx.input.isKeyJustPressed(Input.Keys.P)){
            game.removeScreen(SecondScreen::class.java)
        }
        batch.use(viewport.camera.combined){
        }
        engine.update(delta)
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
           game.setScreen<SecondScreen>()
        }
        if(Gdx.input.isTouched){
        //readQuizFromFile()
        }
    }

    override fun dispose() {
        playerTexture.dispose()
        player.removeAll()
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
        }catch (e: Exception){
            e.printStackTrace()
        }finally{
            println("File read")
        }

    }
    private fun readQuizFromFile(): MutableList<String> {
        val isLocAvailable = Gdx.files.isLocalStorageAvailable
        LOG.debug { "Local is available $isLocAvailable" }
        val isDirectory = Gdx.files.local("assets/quizFiles/").isDirectory
        LOG.debug { "Dir exists $isDirectory" }
        val quizTextFile = Gdx.files.local("assets/quizFiles/test8.txt")
        val tempQuizList = mutableListOf<String>()
        if (quizTextFile.exists()){
            try{
                val lines:List<String> = (quizTextFile.readString()).lines()
                lines.forEach { line ->
                        tempQuizList.add(line)
                    LOG.debug { line }
                    /*LOG.debug { "question: $question, isQuestion: $isQuestion, isAnswer: $isAnswer, " +
                            "statementIsTrue: $statementIsTrue, statementIsFalse: $statementIsFalse" }*/
                }
            }catch (e: Exception){
                e.printStackTrace()
                LOG.debug { "Reading Failed" }
            }finally{
                LOG.debug { "Done Reading" }

            }
        }else{
            LOG.debug { "Error: Cannot find quiz file!" }
        }
        //LOG.debug { "Quiz file is available $quizTextFile" }
        return tempQuizList
    }

    private fun createQuizTextEntities(){
        
    }
}

