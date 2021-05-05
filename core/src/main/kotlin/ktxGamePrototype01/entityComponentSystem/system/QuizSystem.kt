package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.HelperFunctions
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.offsetPos
import ktxGamePrototype01.unitScale

private val LOG = logger<QuizSystem>()

// Handles logic relevant to the QuizScreen game
class QuizSystem : IteratingSystem(allOf(QuizComponent::class).get()) {
    private val holeTexture = Texture(Gdx.files.internal("graphics/Hole.png"))
    var lastTextPositionModifier = 1
    var quizCompletedCheck = false


    private var doOnce = false
    private var updateScoreOnce = true
    private var indexInArr = 0
    private var i = 0
    private var previousQuestionNr = 1
    override fun processEntity(entity: Entity, deltaTime: Float) {

        val quizComp = entity[QuizComponent.mapper]
        require(quizComp != null)
        if (!doOnce && quizCompletedCheck == false){
            createQuizTextEntities(quizComp.quizName)
            doOnce = true
            quizComp.playerHasAnswered = false

        }
        else if(quizComp.playerHasAnswered && quizCompletedCheck == false){
            previousQuestionNr=previousQuestionNr+1
            doOnce = false

        }
        else if(quizCompletedCheck){
            quizComp.quizIsCompleted = true

            while(updateScoreOnce) {
                savePlayerScore(entity)
                updateScoreOnce = false
            }

            indexInArr = 0
            i = 0
        }
    }

    // Reads the quiz file from local Android storage, takes the name of the quiz as a string without
    // the file type .txt, returns the quiz as a list of strings
    private fun readQuizFromFile(quizName : String): MutableList<String> {
        val isLocAvailable = Gdx.files.isLocalStorageAvailable
        LOG.debug { "Local is available $isLocAvailable" }
        val isDirectory = Gdx.files.local("assets/quizFiles/").isDirectory
        LOG.debug { "Dir exists $isDirectory" }
        val quizTextFile = Gdx.files.local("assets/quizFiles/" + quizName + ".txt")        // Change this to quizName parameter later
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

    // Main function of the quiz game system, it creates the quiz entities dynamically based on which part
    // the player has completed of the quiz, it takes a string which is the name of the quiz that is to be played
    private fun createQuizTextEntities( quizName: String) {
        var qPosArray = Array<Vector2>()
        qPosArray.add(Vector2(7f, 16f))
        qPosArray.add(Vector2(13f, 16f))
        qPosArray.add(Vector2(7f, 11f))
        qPosArray.add(Vector2(13f, 11f))
        if (!readQuizFromFile(quizName).isNullOrEmpty()) {

            val quizList = readQuizFromFile(quizName)
            val helpFun = HelperFunctions()
            var questAnsw: String
            var isQuestion = false
            var isCorrect: Boolean
            var maxPoints = 0
            var count = 0
            var charToNum = 1
            var maxLength: Int
            var isNoneLeft = true


            for (indexInArr in i until quizList.size) {

                var line = quizList.elementAt(indexInArr)
                if (line.isNotBlank()) {
                    var tempQuizList: List<String> = line.split("-")
                    questAnsw = tempQuizList[0].drop(1)
                    isQuestion = tempQuizList[1].toBoolean()
                    isCorrect = tempQuizList[2].toBoolean()
                    maxLength = when{
                        isQuestion -> 34
                        else -> 24
                    }
                    var (questAnswChopped , spacer) = helpFun.chopString(questAnsw, maxLength)
                    if (isQuestion && 4 == tempQuizList.size) maxPoints = tempQuizList[3].toInt()
                    charToNum = Character.getNumericValue(line.first())
                    if(charToNum != previousQuestionNr){
                        break
                    }
                    val textEnti = engine.entity {
                        with<TextComponent> {
                            isText = true
                            isQuizAnswer = true
                            textStr = questAnswChopped
                            when{
                                isQuestion ->{
                                    posTextVec2.set(10f, 21f)
                                }
                                !isQuestion -> {
                                    posTextVec2.set(qPosArray[count].x, (qPosArray[count].y+spacer))}
                                else -> {posTextVec2.set(qPosArray[count].x, (qPosArray[count].y+1)) }
                            }
                            font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                            font.data.setScale(4.0f, 4.0f)
                        }
                    }
                    if(!isQuestion) {
                        val choiceEnti = engine.entity {
                            with<TransformComponent> {
                                posVec3.set(qPosArray[count].x - offsetPos, qPosArray[count].y, -1f)
                            }
                            with<SpriteComponent> {
                                sprite.run {
                                    setRegion(holeTexture)
                                    setSize(texture.width * unitScale, texture.height * unitScale)
                                    setOriginCenter()
                                }
                            }
                            with<InteractableComponent>{
                                maxPointsQuestion = maxPoints
                                correctAnswer = isCorrect
                                isQuestOrAnswer = true
                            }
                        }
                    }
                    lastTextPositionModifier = 1

                    isNoneLeft = false
                }



                previousQuestionNr = charToNum
                if(!isQuestion)  count += 1
                if(count >= 4)  count = 0
                i = indexInArr+1
            }

            if(isNoneLeft == true)  quizCompletedCheck = true
            //if(quizList[indexInArr] == quizList[quizList.size-1])
        }

    }

    // Saves the player score to xml in shared_prefs folder
    private fun savePlayerScore(entity: Entity) {
        val player = entity[PlayerComponent.mapper]
        require(player != null)
        LOG.debug { "Adding score = ${player.playerScore}" }
        var score : Float
        val prefs: Preferences = Gdx.app.getPreferences("playerData"+player.playerName)
        score = prefs.getFloat("totalPlayerScore")
        score += player.playerScore
        prefs.putFloat("totalPlayerScore", score)
        prefs.flush()
        LOG.debug { "Saving new total player score = $score" }
    }
}