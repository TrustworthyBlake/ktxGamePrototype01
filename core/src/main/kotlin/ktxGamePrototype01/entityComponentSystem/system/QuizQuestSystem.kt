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
import ktxGamePrototype01.entityComponentSystem.HelperFunctions
import ktxGamePrototype01.entityComponentSystem.components.*
import ktxGamePrototype01.offsetPos
import ktxGamePrototype01.unitScale

private val LOG = logger<QuizQuestSystem>()

// Main logic for generating quest entities in the OpenWorldScreen
class QuizQuestSystem : IteratingSystem(allOf(QuizQuestComponent::class).get()){

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val qQuestComp = entity[QuizQuestComponent.mapper]
        require(qQuestComp != null){"Error: Missing quiz quest component"}
        if (qQuestComp.showAvailableQuizes){            // When an entity has quizQuestComponent and
            createQuestsSignPosts(entity)               //               showAvailableQuizes is true
            qQuestComp.showAvailableQuizes = false      // Set to false so this function is not
        }                                               //           called for every rendered frame
    }

    // Creates the quest entities with their corresponding quiz from a given teacher entity
    private fun createQuestsSignPosts(entity: Entity) {
        val qQuestComp = entity[QuizQuestComponent.mapper]
        require(qQuestComp != null){"Error: Missing quiz quest component"}
        val signPostTexture = Texture(Gdx.files.internal("graphics/signpost.png"))
        val helpFun = HelperFunctions()

        // Array that holds the vector position for 4 entities
        var qPosArray = Array<Vector2>()
        var questPosX = 44f
        var questPosY = 10f
        var count = 0
        var qName : String
        var qNameSplit : String
        val maxLength = 24
        val list = findAllQuizBelongingToTeacher(qQuestComp.teacherStr)

        // If the teacher entity has no quizzes then no quest entities will be created
        if (!list.isNullOrEmpty()){
            list.forEach {
                qPosArray.add(Vector2(questPosX, questPosY))
                qName = it.replace(".txt", "")
                qNameSplit = it.split("-")[0]
                var (quizNameChopped, spacer) = helpFun.chopString(qNameSplit, maxLength)
                val questSingPost = engine.entity {
                    with<TransformComponent> {
                        posVec3.set(qPosArray[count].x - offsetPos, qPosArray[count].y, -1f)
                    }
                    with<SpriteComponent> {
                        sprite.run {
                            setRegion(signPostTexture)
                            setSize(texture.width * unitScale, texture.height * unitScale)
                            setOriginCenter()
                        }
                    }
                    with<InteractableComponent> {
                        isQuest = true
                        nameOfQuiz = qName
                    }
                    with<TextComponent> {
                        isText = true
                        textStr = quizNameChopped
                        posTextVec2.set((qPosArray[count].x), (qPosArray[count].y + spacer + 0.5f))
                        font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                        font.data.setScale(4.0f, 4.0f)
                    }
                }
                count += 1
                // For placing the quest's in a grid
                questPosX += 6f
                when{
                    count % 2 == 0 -> {questPosY += 4f; questPosX = 44f}
                }
            }
        }
    }

    // The function takes the teacher name and returns a list of all quizzes belonging to that teacher
    private fun findAllQuizBelongingToTeacher(tName : String): MutableList<String>{
        val isLocAvailable = Gdx.files.isLocalStorageAvailable
        LOG.debug { "Local is available $isLocAvailable" }
        val directory = Gdx.files.local("assets/quizFiles/")
        val quizNameList = mutableListOf<String>()
        if (directory.isDirectory){
            directory.file().walk().forEach {
                if(it.name.contains(tName, true)){
                    quizNameList.add(it.name)
                    LOG.debug { "File name: ${it.name}" }
                }
            }
        }else{LOG.debug { "Error: Quiz directory does not exist" }}
        return quizNameList
    }
}