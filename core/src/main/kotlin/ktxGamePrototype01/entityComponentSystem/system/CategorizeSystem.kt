package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Preferences
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.math.Vector2
import ktx.ashley.*
import ktx.log.debug
import ktx.log.logger
import ktxGamePrototype01.entityComponentSystem.HelperFunctions
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.offsetPos
import ktxGamePrototype01.unitScale

private val LOG = logger<CategorizeSystem>()

class CategorizeSystem : IteratingSystem(allOf(CategorizeComponent::class).get()){

    private val gameCompletedSound = Gdx.audio.newSound(Gdx.files.internal("sounds/quizCompletedSound.mp3"));
    private var doneOnce = false
    private var gameCompleted = false

    override fun processEntity(entity: Entity, deltaTime: Float) {

        val catComp = entity[CategorizeComponent.mapper]
        require(catComp != null)

        if (!doneOnce){
        createCategorizeEntities(catComp.categorizeName)
            doneOnce = true
        }



        if (catComp.categorizeIsCompleted){
            savePlayerScore(entity)
            gameCompletedSound.play(1.0f)
            catComp.categorizeIsCompleted = false
        }

    }

    private fun createCategorizeEntities(categorizeName : String) {
        val tempList = mutableListOf<String>()
        tempList.add("question-Question-10")
        tempList.add("category-1CategoryX")
        tempList.add("item-1ItemX1")
        tempList.add("item-1ItemX2")
        tempList.add("item-1ItemX3")
        tempList.add("category-2CategoryY")
        tempList.add("item-2ItemY1")
        tempList.add("item-2ItemY2")
        tempList.add("item-2ItemY3")

        var question: String
        var item: String
        var count = 0
        var dataTag = ""
        var data = mutableListOf<String>()
        var maxScore = 0

        var maxLength = 34

        val questionPosList = mutableListOf<Vector2>()  // Todo: Maybe remove this list
        var questionPosX = 10f                      // Do not change to val, work in progress
        var questionPosY = 21f

        val categoryPosList = mutableListOf<Vector2>()
        var catPosX = 5f
        var catPosY = 17f
        val itemPosList = mutableListOf<Vector2>()
        var itemPosX = 0f
        var itemPosY = 0f

        var allPosLists = mutableListOf<Vector2>()

        val helpFun = HelperFunctions()
        val categorizeTexture = Texture(Gdx.files.internal("graphics/skill_icons16.png"))
        val itemTexture = Texture(Gdx.files.internal("graphics/skill_icons19.png"))

        var belongsToNr = 0
        var tempCharToNum : Char

        var tempVecPos = Vector2(Vector2.Zero)

        val listOfCategorizeData = readCategorizeFromFile(categorizeName)
        LOG.debug { listOfCategorizeData.toString() }
        LOG.debug { "Above this" }

        if (!listOfCategorizeData.isNullOrEmpty()) {
            listOfCategorizeData.forEach {
                dataTag = it.split("-")[0]
                data = it.split("-").toMutableList()
                LOG.debug { "dataTag = $dataTag" }
                //LOG.debug { "data = $data" }

                when(dataTag){
                    "question" -> {
                        maxScore = data[2].toInt()
                        maxLength = 34
                        questionPosList.add(Vector2(questionPosX, questionPosY))
                    }
                    "category" -> {
                        tempCharToNum = data[1].first()
                        belongsToNr = Character.getNumericValue(tempCharToNum)
                        //data.elementAt(1).drop(1).add
                        LOG.debug { "data = ${data[1]}" }
                        LOG.debug { "number = $belongsToNr" }
                        data[1] = data[1].drop(1)
                        LOG.debug { "data = ${data[1]}" }
                        maxLength = 24
                        categoryPosList.add(Vector2(catPosX, catPosY))
                        catPosX += 10
                    }
                    "item" -> {
                        tempCharToNum = data[1].first()
                        belongsToNr = Character.getNumericValue(tempCharToNum)
                        data[1] = data[1].drop(1)
                        maxLength = 24
                        // Todo: make more performance friendly
                        allPosLists.clear()
                        allPosLists = (questionPosList + categoryPosList + itemPosList).toMutableList()
                        val posVector = randomizePositionVector(allPosLists)
                        itemPosList.add(posVector)
                    }
                    else -> LOG.debug{"Error in categorizeList"}
                }

                val (dataChopped, spacer) = helpFun.chopString(data[1], maxLength)

                val textEntity = engine.entity {
                    with<TextComponent> {
                        isText = true
                        textStr = dataChopped
                        when(dataTag){
                            "question" -> {
                                tempVecPos = questionPosList.last();
                                posTextVec2.set(tempVecPos.x, tempVecPos.y + spacer)
                            }
                            "category" -> {
                                tempVecPos = categoryPosList.last()
                                posTextVec2.set(tempVecPos.x, tempVecPos.y + spacer)
                            }
                            "item" -> {
                                tempVecPos = itemPosList.last()
                                posTextVec2.set(tempVecPos.x, tempVecPos.y + spacer)
                            }
                        }
                        font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                        font.data.setScale(4.0f, 4.0f)
                    }
                }
                //LOG.debug { textEntity.components.toString() }
                when(dataTag) {
                    "category" -> {
                        val categoryEntity = engine.entity {
                            with<TransformComponent> {
                                posVec3.set(tempVecPos.x - offsetPos, tempVecPos.y, -1f)
                            }
                            with<SpriteComponent> {
                                sprite.run {
                                    setRegion(categorizeTexture)
                                    setSize(texture.width * unitScale, texture.height * unitScale)
                                    setOriginCenter()
                                }
                            }
                            with<InteractableComponent> {
                                type = InteractableType.CATEGORY
                                belongsToCategory = belongsToNr
                                maxPointsQuestion = maxScore
                            }
                        }
                    }
                    "item" -> {
                        val itemEntity = engine.entity {
                            with<TransformComponent> {
                                posVec3.set(tempVecPos.x - offsetPos, tempVecPos.y, -1f)
                            }
                            with<SpriteComponent> {
                                sprite.run {
                                    setRegion(itemTexture)
                                    setSize(texture.width * unitScale, texture.height * unitScale)
                                    setOriginCenter()
                                }
                            }
                            with<InteractableComponent> {
                                type = InteractableType.ITEM
                                belongsToCategory = belongsToNr
                                maxPointsQuestion = maxScore
                            }
                        }

                        textEntity.addComponent<BindEntitiesComponent>(engine) {
                            masterEntity = itemEntity
                            posOffset.set(0f + offsetPos, 0.6f + spacer)
                            isItemEntity = true
                        }
                    }
                }
                count += 1
            }
        }
    }

    private fun randomizePositionVector(posArray : List<Vector2>) : Vector2 {
        //LOG.debug { posArray.toString() }
        val maxPosValue = 20
        val minPosValue = 0
        var rndmX = (minPosValue..maxPosValue).random().toFloat()
        var rndmY = (minPosValue..maxPosValue).random().toFloat()

        while(posArray.contains(Vector2(rndmX, rndmY))){
            rndmX = (minPosValue..maxPosValue).random().toFloat()
            rndmY = (minPosValue..maxPosValue).random().toFloat()
        }

        return Vector2(rndmX,rndmY)
    }

    // Reads the categorize file from local Android storage, takes the name of the quiz as a string without
    // the file type .txt, returns the quiz as a list of strings
    private fun readCategorizeFromFile(categorizeName : String): MutableList<String> {
        val isLocAvailable = Gdx.files.isLocalStorageAvailable
        LOG.debug { "Local is available $isLocAvailable" }
        val isDirectory = Gdx.files.local("assets/categorizeFiles/").isDirectory
        LOG.debug { "Dir exists $isDirectory" }
        val quizTextFile = Gdx.files.local("assets/categorizeFiles/" + categorizeName + ".txt")        // Change this to quizName parameter later
        val categorizeList = mutableListOf<String>()
        if (quizTextFile.exists()){
            try{
                val lines:List<String> = (quizTextFile.readString()).lines()
                lines.forEach { line ->
                    if(line != "") categorizeList.add(line)
                    LOG.debug { line }
                }
            }catch (e: Exception){
                e.printStackTrace()
                LOG.debug { "Reading Failed" }
            }finally{
                LOG.debug { "Done Reading" }

            }
        }else{
            LOG.debug { "Error: Cannot find categorize file!" }
        }
        return categorizeList
    }

    // Saves the player score to xml in shared_prefs folder
    private fun savePlayerScore(entity: Entity) {
        val player = entity[PlayerComponent.mapper]
        require(player != null) {"Error: Missing player component"}
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