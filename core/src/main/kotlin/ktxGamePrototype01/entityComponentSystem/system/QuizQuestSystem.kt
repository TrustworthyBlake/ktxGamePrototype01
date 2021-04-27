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
import ktxGamePrototype01.unitScale
import kotlin.with

private val LOG = logger<QuizQuestSystem>()
class QuizQuestSystem : IteratingSystem(allOf(QuizQuestComponent::class).exclude(NukePooledComponent::class).get()){
    private var doOnce = 0
    override fun processEntity(entity: Entity, deltaTime: Float) {
        if (doOnce == 0){
        createQuestsSignPosts(entity)
        doOnce = 1
        }
    }
    private fun createQuestsSignPosts(entity: Entity) {
        val qQuestComp = entity[QuizQuestComponent.mapper]
        require(qQuestComp != null){"Error: Missing quiz quest component"}
        val signPostTexture = Texture(Gdx.files.internal("graphics/skill_icons1.png"))
        val helpFun = HelperFunctions()
        var qPosArray = Array<Vector2>()
        qPosArray.add(Vector2(1f, 11f))
        qPosArray.add(Vector2(7f, 11f))
        qPosArray.add(Vector2(1f, 4f))
        qPosArray.add(Vector2(7f, 4f))
        var count = 0
        var qName = ""
        val maxLength = 26
        val list = findAllQuizBelongingToTeacher(qQuestComp.teacherStr)
        if (!list.isNullOrEmpty()){
            list.forEach {
                qName = it.replace(".txt", "")
                var (quizNameChopped , spacer, centerTextPos) = helpFun.chopString(qName, maxLength)
                val questSingPost = engine.entity {
                    with<TransformComponent> {
                        posVec3.set(qPosArray[count].x, qPosArray[count].y, -1f)
                    }
                    with<GraphicComponent> {
                        sprite.run{
                            setRegion(signPostTexture)
                            setSize(texture.width * unitScale, texture.height * unitScale)
                            setOriginCenter()
                        }
                    }
                    with<InteractableComponent> {  }
                    with<TextComponent> {
                        isText = true
                        textStr = quizNameChopped
                        posTextVec2.set((qPosArray[count].x-centerTextPos), (qPosArray[count].y+spacer+0.5f))
                        font.region.texture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear)
                        font.data.setScale(4.0f, 4.0f)
                    }
                }
                count += 1
            }

        }
    }

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