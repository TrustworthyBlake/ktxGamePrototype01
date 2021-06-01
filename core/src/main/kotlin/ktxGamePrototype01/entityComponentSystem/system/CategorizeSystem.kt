package ktxGamePrototype01.entityComponentSystem.system

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.Gdx
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

    private var doneOnce = false

    override fun processEntity(entity: Entity?, deltaTime: Float) {
        if (!doneOnce){
        createCategorizeEntities("sumting")
            doneOnce = true
        }
    }

    enum class EntityType{
        QUESTION,
        CATEGORY,
        ITEM
    }

    private fun createCategorizeEntities(categorizeName : String) {
        val tempList = mutableListOf<String>()
        tempList.add("question-Question-120")
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

        //var type = EntityType.QUESTION

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

        if (!tempList.isNullOrEmpty()) {
            tempList.forEach {
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
                            }
                        }

                        textEntity.addComponent<BindEntitiesComponent>(engine) {
                            masterEntity = itemEntity
                            posOffset.set(0f + offsetPos, 0.6f + spacer) //todo fix
                            isItemEntity = true
                            LOG.debug { "Bind component added" }
                        }
                        LOG.debug { textEntity.components.toString() }
                    }
                }
                count += 1
            }
        }
    }

    private fun randomizePositionVector(posArray : List<Vector2>) : Vector2 {
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
}