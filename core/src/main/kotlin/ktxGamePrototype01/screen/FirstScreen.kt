package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
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
    private var viewport = FitViewport(9f, 16f)
    private val playerTexture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
    private val grassTexture = Texture(Gdx.files.internal("graphics/Grass.png"))
    private val holeTexture = Texture(Gdx.files.internal("graphics/Hole.png"))
    private val treeTexture = Texture(Gdx.files.internal("graphics/tree.png"))
    private val blankTexture = Texture(Gdx.files.internal("graphics/blank.png"))
    private val quizMap = Gdx.files.internal("maps/map0.txt");

    private val player = engine.entity{
        with<TransformComponent>{
            posVec3.set(0f, 0f, -1f)
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
        with<TransformComponent> { posVec3.set(8f, 8f, -1f)
            }

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
        createQuizTextEntities()
       // val test = InteractableSystem()

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
/*
        batch.use(vp1.camera.combined){
        }
        batchText.use(vp2.camera.combined){
        }*/
        engine.update(delta)
        if(Gdx.input.isKeyJustPressed(Input.Keys.K)){
           game.setScreen<SecondScreen>()
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
        val quizTextFile = Gdx.files.local("assets/quizFiles/zxcvzxc.txt")        // Change this to quizName parameter later
        val quizList = mutableListOf<String>()
        if (quizTextFile.exists()){
            try{
                val lines:List<String> = (quizTextFile.readString()).lines()
                lines.forEach { line ->
                        quizList.add(line)
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
        return quizList
    }

    private fun createQuizTextEntities() {
        if (!readQuizFromFile().isNullOrEmpty()) {
            val quizList = readQuizFromFile()
            var questAnsw = ""
            var isQuestion = false
            var isCorrect = false
            var count = 1
            quizList.forEach() { line ->
                if (line.isNotBlank() && count == 1) {
                    count = 0
                    var tempQuizList: List<String> = line.split("-")
                    questAnsw = tempQuizList[0]
                    isQuestion = tempQuizList[1].toBoolean()
                    LOG.debug { "Should only be isQuestion: $isQuestion" }
                    isCorrect = tempQuizList[2].toBoolean()
                    // Todo put variables into entities
                    val text = engine.entity {
                        with<TextComponent> {
                            textStr = questAnsw
                            isText = true
                            posTextVec2.set(0f, 0f)
                            font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
                            font.data.setScale(2.0f, 2.0f)
                            val calc = -10 * unitScale
                            LOG.debug { "Size of font: $calc" }
                        }
                    }
                }
            }
        }
    }
}

