package ktxGamePrototype01.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.utils.viewport.FitViewport
import ktx.ashley.entity
import ktx.ashley.get
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
        var totScore = 0f
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
        engine.update(delta)
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
    private fun readQuizFromFile(): MutableList<String> {
        val isLocAvailable = Gdx.files.isLocalStorageAvailable
        LOG.debug { "Local is available $isLocAvailable" }
        val isDirectory = Gdx.files.local("assets/quizFiles/").isDirectory
        LOG.debug { "Dir exists $isDirectory" }
        val quizTextFile = Gdx.files.local("assets/quizFiles/test9.txt")        // Change this to quizName parameter later
        val quizList = mutableListOf<String>()
        if (quizTextFile.exists()){
            try{
                val lines:List<String> = (quizTextFile.readString()).lines()
                lines.forEach { line ->
                        quizList.add(line)
                    LOG.debug { line }
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
        return quizList
    }

    private fun createQuizTextEntities() {
        if (!readQuizFromFile().isNullOrEmpty()) {
            val quizList = readQuizFromFile()
            var questAnsw = ""
            var isQuestion = false
            var isCorrect = false
            var maxPoints = 0
            var count = 1




            quizList.forEach() { line ->
                if (line.isNotBlank() && count <= 3) {
                    var tempQuizList: List<String> = line.split("-")
                    questAnsw = tempQuizList[0].drop(1)
                    questAnsw = chopString(questAnsw, 34)
                    isQuestion = tempQuizList[1].toBoolean()
                    LOG.debug { "Should only be isQuestion: $isQuestion" }
                    isCorrect = tempQuizList[2].toBoolean()
                    maxPoints = 0                                               // Needs to be reset
                    if (isQuestion && 4 == tempQuizList.size){
                        maxPoints = tempQuizList[3].toInt()
                        LOG.debug{"Max points = $maxPoints"}
                    }
                    // Todo put variables into entities
                    val textEnti = engine.entity {
                        with<TextComponent> {
                            isText = true
                            textStr = questAnsw
                            when{
                                isQuestion ->{
                                posTextVec2.set(300f, 1780f)
                                }
                                !isQuestion && count >= 2 -> {
                                    posTextVec2.set(200f*count, 200f*count)
                                }
                                else -> {posTextVec2.set(0f, 0f)}
                            }
                            font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                            font.data.setScale(4.0f, 4.0f)
                        }
                    }
                    count += 1
                }
            }
        }
    }
    // Max length should be 34 with text scaling at 4.0f for entire textViewport
    private fun chopString(str : String, maxLength : Int) : String{
        val numChars = str.count()
        var newStr = str
        var spacer = 0
        if(numChars > maxLength) {
            for (i in 0..numChars) {
                if (i.rem(maxLength) == 0) {
                    newStr = StringBuilder(newStr).apply { insert(i+spacer, '\n') }.toString()
                    spacer += 1
                }
            }
        }
        return newStr
    }
}

