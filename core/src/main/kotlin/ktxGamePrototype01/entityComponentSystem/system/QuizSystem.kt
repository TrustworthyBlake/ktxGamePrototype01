package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.unitScale

private val LOG = logger<QuizSystem>()

class QuizSystem : IteratingSystem(allOf(QuizComponent::class).exclude(NukePooledComponent::class).get()) {
    private val holeTexture = Texture(Gdx.files.internal("graphics/Hole.png"))


    private var doOnce = false
    private var indexInArr = 0
    private var i = 0
    private var previousQuestionNr = 1
    override fun processEntity(entity: Entity, deltaTime: Float) {
        val quizComp = entity[QuizComponent.mapper]
        require(quizComp != null)
        if (!doOnce && !quizComp.quizIsCompleted){
            createQuizTextEntities(quizComp.quizName)
            doOnce = true
            quizComp.playerHasAnswered = false
        }
        if(quizComp.playerHasAnswered){
            previousQuestionNr=previousQuestionNr+1
            doOnce = false
        }
        if(quizComp.quizIsCompleted){
            indexInArr = 0
        }
    }

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

    private fun createQuizTextEntities( quizName: String) {
        var qPosArray = Array<Vector2>()
        qPosArray.add(Vector2(1f, 11f))
        qPosArray.add(Vector2(7f, 11f))
        qPosArray.add(Vector2(1f, 4f))
        qPosArray.add(Vector2(7f, 4f))



        if (!readQuizFromFile(quizName).isNullOrEmpty()) {
            val quizList = readQuizFromFile(quizName)
            var questAnsw = ""
            var isQuestion = false
            var isCorrect = false
            var maxPoints = 0
            var count = 0
            var charToNum = 1
            //quizList.forEach() { line ->
            for (indexInArr in i..quizList.size-1) {
                var line = quizList.elementAt(indexInArr)
                if (line.isNotBlank()) {
                    var tempQuizList: List<String> = line.split("-")
                    questAnsw = tempQuizList[0].drop(1)
                    questAnsw = chopString(questAnsw, 34)
                    isQuestion = tempQuizList[1].toBoolean()
                    isCorrect = tempQuizList[2].toBoolean()
                    maxPoints = 0                                               // Needs to be reset
                    if (isQuestion && 4 == tempQuizList.size){
                        maxPoints = tempQuizList[3].toInt()
                    }
                    if(!line.isNullOrEmpty()){
                        charToNum = Character.getNumericValue(line.first())
                    }
                    LOG.debug { "prev = $previousQuestionNr, curr = $charToNum" }
                    if(charToNum != previousQuestionNr){
                        LOG.debug { "should break" }
                        break
                    }

                    val textEnti = engine.entity {
                        with<TextComponent> {
                            isText = true
                            isQuizAnswer = true
                            textStr = questAnsw
                            when{
                                isQuestion ->{
                                    posTextVec2.set(300f, 1780f)
                                }
                                !isQuestion -> {
                                    posTextVec2.set((qPosArray[count].x-1)*120, (qPosArray[count].y+1)*120)}
                                else -> {posTextVec2.set((qPosArray[count].x-1)*120, (qPosArray[count].y+1)*120) }
                            }
                            font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                            font.data.setScale(4.0f, 4.0f)
                        }
                    }
                    if(!isQuestion) {
                        val choiceEnti = engine.entity {
                            with<TransformComponent> {
                                posVec3.set(qPosArray[count].x, qPosArray[count].y, -1f)
                            }
                            with<GraphicComponent> {
                                sprite.run {
                                    setRegion(holeTexture)
                                    setSize(texture.width * unitScale, texture.height * unitScale)
                                    setOriginCenter()
                                }
                            }
                            with<InteractableComponent>{ correctAnswer = isCorrect }
                        }
                    }
                }
                previousQuestionNr = charToNum
                if(!isQuestion)  count += 1
                if(count >= 4)  count = 0
                i = indexInArr+1
            }
        }
    }
    // Max length should be 34 with text scaling at 4.0f for entire textViewport
    private fun chopString(str: String, maxLength: Int) : String{
        val numChars = str.count()
        var newStr = str
        var spacer = 0
        if(numChars > maxLength) {
            for (i in 0..numChars) {
                if (i.rem(maxLength) == 0) {
                    newStr = StringBuilder(newStr).apply { insert(i + spacer, '\n') }.toString()
                    spacer += 1
                }
            }
        }
        return newStr
    }

}